package web.id.azammukhtar.forumkita.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import web.id.azammukhtar.forumkita.Model.Post.DataPost;
import web.id.azammukhtar.forumkita.Model.Response;
import web.id.azammukhtar.forumkita.Network.ApiInterface;
import web.id.azammukhtar.forumkita.Network.ApiNetwork;
import web.id.azammukhtar.forumkita.R;
import web.id.azammukhtar.forumkita.Utils.SessionManager;

import static web.id.azammukhtar.forumkita.UI.MainActivity.POST_KEY;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";
    private EditText etJudul, etIsi;
    private AppCompatButton btnEdit, btnDelete;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private DataPost dataPost;
    private ApiInterface apiInterface;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        findById();
        setDataPost();
        setOnClick();
        getSupportActionBar().setTitle("Edit Thread");
    }

    private void findById(){
        etJudul = findViewById(R.id.edtEditJudul);
        etIsi = findViewById(R.id.edtEditIsi);
        btnEdit = findViewById(R.id.buttonEditProceed);
        btnDelete = findViewById(R.id.buttonEditDelete);
        avLoadingIndicatorView = findViewById(R.id.loadingEditProgress);
        dataPost = getIntent().getParcelableExtra(POST_KEY);
        apiInterface = ApiNetwork.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(this);
    }

    private void setDataPost(){
        etJudul.setText(dataPost.getJudul());
        etIsi.setText(dataPost.getIsi());
    }

    private void setOnClick(){
        String judul = etJudul.getText().toString();
        String isi = etIsi.getText().toString();
        btnEdit.setOnClickListener(view -> {
            if (judul.isEmpty() || isi.isEmpty()){
                Toast.makeText(EditActivity.this, "Title or content cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                doEdit(judul, isi);
            }
        });
        btnDelete.setOnClickListener(view -> doDelete(dataPost.getId()));
    }

    private void doDelete(int id) {
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        Call<Response> call = apiInterface.deletePost("Bearer " + sessionManager.getUserToken(),id);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                switch (response.code()) {
                    case 201:
                        Log.d(TAG, "onResponse: Delete berhasil " + response);
                        Toast.makeText(EditActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        break;
                    case 400:
                        Log.d(TAG, "onResponse: Delete gagal " + response);
                        Toast.makeText(EditActivity.this, "Delete Fail", Toast.LENGTH_SHORT).show();
                        break;
                }
                avLoadingIndicatorView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                avLoadingIndicatorView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void doEdit(String judul, String isi) {
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        Call<Response> call = apiInterface.editPost("Bearer " + sessionManager.getUserToken(),dataPost.getId(),judul,isi);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                switch (response.code()) {
                    case 201:
                        Log.d(TAG, "onResponse: Edit berhasil " + response);
                        Toast.makeText(EditActivity.this, "Edit Success", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        break;
                    case 400:
                        Log.d(TAG, "onResponse: Edit gagal " + response);
                        Toast.makeText(EditActivity.this, "Edit Fail", Toast.LENGTH_SHORT).show();
                        break;
                }
                avLoadingIndicatorView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                avLoadingIndicatorView.setVisibility(View.INVISIBLE);
            }
        });
    }
}
