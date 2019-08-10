package web.id.azammukhtar.forumkita.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import web.id.azammukhtar.forumkita.Model.Post.DataPost;
import web.id.azammukhtar.forumkita.Model.Post.Post;
import web.id.azammukhtar.forumkita.Model.Response;
import web.id.azammukhtar.forumkita.Model.ResponseLogin;
import web.id.azammukhtar.forumkita.Network.ApiInterface;
import web.id.azammukhtar.forumkita.Network.ApiNetwork;
import web.id.azammukhtar.forumkita.R;
import web.id.azammukhtar.forumkita.Utils.FileUtil;
import web.id.azammukhtar.forumkita.Utils.SessionManager;

import static web.id.azammukhtar.forumkita.UI.MainActivity.POST_KEY;

public class AddActivity extends AppCompatActivity implements IPickResult {
    private static final String TAG = "AddActivity";
    private ImageView imageUploaded;
    private EditText etTitle, etIsi;
    private Button btnAddPhoto;
    private AppCompatButton btnAddThread;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private SessionManager sessionManager;
    private Uri filepath;
    private File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setTitle("Add New Thread");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(this);
        findById();
        buttonClick();
    }

    private void findById(){
        imageUploaded = findViewById(R.id.imageAddImage);
        etIsi = findViewById(R.id.edtAddIsi);
        etTitle = findViewById(R.id.edtAddJudul);
        btnAddPhoto = findViewById(R.id.buttonAddAddPhoto);
        btnAddThread = findViewById(R.id.buttonAddProceed);
        avLoadingIndicatorView = findViewById(R.id.loadingAddProgress);
    }
    private void buttonClick(){
        btnAddThread.setOnClickListener(view -> {
            String judul = etTitle.getText().toString().trim();
            String isi = etIsi.getText().toString().trim();
            if (judul.isEmpty() || isi.isEmpty()) {
                Toast.makeText(this, "Title or Content cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                addThread(judul, isi);
            }
        });

        btnAddPhoto.setOnClickListener(view -> PickImageDialog.build(new PickSetup().setWidth(100).setHeight(100)).show(getSupportFragmentManager()));

    }

    private void addThread(String judul, String isi) {
        RequestBody judulRequest = RequestBody.create(MediaType.parse("text/plain"), judul);
        RequestBody isiRequest = RequestBody.create(MediaType.parse("text/plain"), isi);
        MultipartBody.Part filePart = null;
        if(filepath != null){
            filePart = MultipartBody.Part.createFormData(
                    "image", image.getName(),
                    RequestBody.create(MediaType.parse("image/*"), image));
        }

        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        ApiInterface apiInterface;
        apiInterface = ApiNetwork.getClient().create(ApiInterface.class);
        Call<Response> call = apiInterface.tambahPost("Bearer " + sessionManager.getUserToken(), judulRequest, isiRequest, filePart);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                switch (response.code()) {
                    case 201:
                        Log.d(TAG, "onResponse: Add Thread berhasil " + response);
                        Toast.makeText(AddActivity.this, "Add Thread Success", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        break;
                    case 400:
                        Log.d(TAG, "onResponse: Add Thread gagal " + response);
                        Toast.makeText(AddActivity.this, "Add Thread Fail", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            filepath = r.getUri();
            imageUploaded.setImageURI(filepath);

            try {
                image = FileUtil.from(this, r.getUri());
                image = new Compressor(this).compressToFile(image);
                imageUploaded.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "onPickResult: ", e);
            }


            Log.d(TAG, "onPickResult: filepath " + filepath);

        } else {
            Log.d(TAG, "onPickResult: error image picker " + r.getError().getMessage());
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
