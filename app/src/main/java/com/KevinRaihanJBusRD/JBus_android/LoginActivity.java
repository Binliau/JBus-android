package com.kevinraihanjbusrd.jbus_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kevinraihanjbusrd.jbus_android.model.Account;
import com.kevinraihanjbusrd.jbus_android.model.BaseResponse;
import com.kevinraihanjbusrd.jbus_android.request.BaseApiService;
import com.kevinraihanjbusrd.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static Account loggedAccount;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText email, password;
    private TextView registerNow = null;
    private Button loginButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mContext = this;
        mApiService = UtilsApi.getApiService();
// sesuaikan dengan ID yang kalian buat di layout
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerNow = findViewById(R.id.register_now);
        loginButton = findViewById(R.id.login_button);


        registerNow.setOnClickListener(v -> {
            moveActivity(mContext, RegisterActivity.class);
        });
        loginButton.setOnClickListener(v -> {
            handleLogin();
        });
    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    protected void handleLogin() {
        // handling empty field
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();

        if (emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.login(emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();
// if success finish this activity (back to login activity)
                if (res.success) {
                    Toast.makeText(mContext, "Login Berhasil", Toast.LENGTH_SHORT).show();
                    loggedAccount = res.payload;
                    moveActivity(mContext, MainActivity.class);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}