package web.id.azammukhtar.forumkita.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import web.id.azammukhtar.forumkita.Model.ResponseLogin;
import web.id.azammukhtar.forumkita.Network.ApiInterface;
import web.id.azammukhtar.forumkita.Network.ApiNetwork;
import web.id.azammukhtar.forumkita.R;
import web.id.azammukhtar.forumkita.Utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText etEmail, etPassword;
    private TextView textToRegister;
    private AppCompatButton btnLogin;
    private AVLoadingIndicatorView loadingIndicatorView;
    private ApiInterface apiInterface;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        findId();
        buttonClick();
    }

    private void findId() {
        etEmail = findViewById(R.id.textLoginEmail);
        etPassword = findViewById(R.id.textLoginPassword);
        btnLogin = findViewById(R.id.btnLoginProceed);
        loadingIndicatorView = findViewById(R.id.loadingLoginProgress);
        apiInterface = ApiNetwork.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(this);
        textToRegister = findViewById(R.id.textLoginToRegister);
    }

    private void buttonClick() {
        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email or Password cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                doLogin(email, password);
            }
        });

        textToRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void doLogin(String email, String password)
    {
        loadingIndicatorView.setVisibility(View.VISIBLE);
        Call<ResponseLogin> call = apiInterface.login(email, password);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseLogin = response.body();
                Log.d(TAG, "onResponse: " + responseLogin);
                switch (response.code()) {
                    case 201:
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        sessionManager.setLogin(true, response.body().getToken());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        break;
                    case 400:
                        Log.d(TAG, "onResponse: login gagal " + response);
                        Toast.makeText(LoginActivity.this, "Login Fail, make sure you use the correct email and password", Toast.LENGTH_SHORT).show();
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
}
