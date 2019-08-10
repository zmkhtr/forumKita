package web.id.azammukhtar.forumkita.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import retrofit2.Response;
import web.id.azammukhtar.forumkita.Model.ResponseLogin;
import web.id.azammukhtar.forumkita.Network.ApiInterface;
import web.id.azammukhtar.forumkita.Network.ApiNetwork;
import web.id.azammukhtar.forumkita.R;
import web.id.azammukhtar.forumkita.Utils.FileUtil;
import web.id.azammukhtar.forumkita.Utils.SessionManager;

public class RegisterActivity extends AppCompatActivity implements IPickResult {
    private static final String TAG = "RegisterActivity";

    private EditText etFullname, etEmail, etPassword;
    private ImageView imageUploaded;
    private Button btnRegister, btnAddPhoto;
    private AVLoadingIndicatorView loadingIndicatorView;
    private ApiInterface apiInterface;
    private SessionManager sessionManager;
    private TextView textToLogin;
    private Uri filepath;
    private File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        findId();
        buttonClick();
    }

    private void findId() {
        etEmail = findViewById(R.id.edtRegisterEmail);
        etPassword = findViewById(R.id.edtRegisterPassword);
        etFullname = findViewById(R.id.edtRegisterFullName);
        btnRegister = findViewById(R.id.buttonRegisterProceed);
        btnAddPhoto = findViewById(R.id.buttonRegisterAddPhoto);
        imageUploaded = findViewById(R.id.imageRegisterAvatar);
        loadingIndicatorView = findViewById(R.id.loadingRegisterProgress);
        apiInterface = ApiNetwork.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(this);
        textToLogin = findViewById(R.id.textRegisterToLogin);
    }

    private void buttonClick() {
        btnRegister.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String fullName = etFullname.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                Toast.makeText(this, "Fullname or Email or Password cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                register(fullName, email, password);
            }
        });
        textToLogin.setOnClickListener(view -> onBackPressed());

        btnAddPhoto.setOnClickListener(view -> PickImageDialog.build(new PickSetup().setWidth(100).setHeight(100)).show(getSupportFragmentManager()));
    }

    private void register(String fullName, String email, String password) {
        RequestBody emailRequest = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passwordRequest = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody fullNameRequest = RequestBody.create(MediaType.parse("text/plain"), fullName);

        MultipartBody.Part filePart = null;
        if(filepath != null){
            filePart = MultipartBody.Part.createFormData(
                    "image", image.getName(),
                    RequestBody.create(MediaType.parse("image/*"), image));
        }

        loadingIndicatorView.setVisibility(View.VISIBLE);
        Call<ResponseLogin> call = apiInterface.register(emailRequest, passwordRequest, fullNameRequest, filePart);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                Log.d(TAG, "onResponse: " + response);
                switch (response.code()) {
                    case 201:
                        Log.d(TAG, "onResponse: register berhasil " + response);
                        Log.d(TAG, "onResponse: token = " + response.body().getToken());
                        Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        break;
                    case 400:
                        Log.d(TAG, "onResponse: register gagal " + response);
                        Toast.makeText(RegisterActivity.this, "Register Fail", Toast.LENGTH_SHORT).show();
                        break;
                }
                loadingIndicatorView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                loadingIndicatorView.setVisibility(View.INVISIBLE);
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
