package com.kevinraihanjbusrd.jbus_android;

import static com.kevinraihanjbusrd.jbus_android.LoginActivity.loggedAccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kevinraihanjbusrd.jbus_android.model.Account;
import com.kevinraihanjbusrd.jbus_android.model.BaseResponse;
import com.kevinraihanjbusrd.jbus_android.model.Renter;
import com.kevinraihanjbusrd.jbus_android.request.BaseApiService;
import com.kevinraihanjbusrd.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRenterActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText companyName, address, phoneNumber;
    private Button registerRenterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);
        getSupportActionBar().hide();
        mContext = this;
        mApiService = UtilsApi.getApiService();
        companyName = findViewById(R.id.register_company);
        address = findViewById(R.id.register_address);
        phoneNumber = findViewById(R.id.register_phone);
        registerRenterButton = findViewById(R.id.register_renter_button);

        registerRenterButton.setOnClickListener(v -> {
            handleRegisterRenter();
        });
    }

    protected void handleRegisterRenter() {
        // handling empty field
        String companyNameS = companyName.getText().toString();
        String addressS = address.getText().toString();
        String phoneNumberS = phoneNumber.getText().toString();
        if (companyNameS.isEmpty() || addressS.isEmpty() || phoneNumberS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.registerRenter(loggedAccount.id, companyNameS, addressS, phoneNumberS).enqueue(new Callback<BaseResponse<Renter>>() {
            @Override
            public void onResponse(Call<BaseResponse<Renter>> call, Response<BaseResponse<Renter>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Renter> res = response.body();
// if success finish this activity (back to login activity)
                if(res.success){
                    loggedAccount.company = res.payload;
                    finish();
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Renter>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}