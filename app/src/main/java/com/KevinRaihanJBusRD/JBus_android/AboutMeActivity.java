package com.kevinraihanjbusrd.jbus_android;

import static com.kevinraihanjbusrd.jbus_android.LoginActivity.loggedAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kevinraihanjbusrd.jbus_android.model.BaseResponse;
import com.kevinraihanjbusrd.jbus_android.request.BaseApiService;
import com.kevinraihanjbusrd.jbus_android.request.UtilsApi;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText topUpAmount = null;
    private Button topUpButton = null;
    private TextView username = null;
    private TextView email = null;
    private TextView balance = null;
    private LinearLayout registered = null;
    private LinearLayout unregistered = null;
    private TextView registerLink = null;
    private Button manageBusButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        getSupportActionBar().hide();

        mApiService = UtilsApi.getApiService();
        mContext = this;

        username = (TextView)findViewById(R.id.profile_username);
        email = (TextView)findViewById(R.id.profile_email);
        balance = (TextView)findViewById(R.id.profile_balance);
        topUpAmount = findViewById(R.id.topup_amount);
        topUpButton = findViewById(R.id.topup_button);
        registerLink = findViewById(R.id.text_to_register);
        manageBusButton = findViewById(R.id.managebus_button);
        unregistered = findViewById(R.id.unregistered_text);
        registered = findViewById((R.id.registered_text));

        username.setText(loggedAccount.name);
        email.setText(loggedAccount.email);
        balance.setText(String.valueOf(loggedAccount.balance));
        topUpButton.setOnClickListener(x -> {
            handleTopUp();
            Toast.makeText(mContext, "Top up berhasil!", Toast.LENGTH_SHORT).show();
        });
        registerLink.setOnClickListener(x -> {
            moveActivity(mContext, RegisterRenterActivity.class);
        });
        manageBusButton.setOnClickListener(x -> {
            moveActivity(mContext, ManageBusActivity.class);
        });

        if (loggedAccount.company != null) {
            unregistered.setVisibility(View.GONE);
        } else {
            registered.setVisibility(View.GONE);
        }
    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    protected void handleTopUp(){
        String topUpString = topUpAmount.getText().toString();

        if(topUpString.isEmpty()){
            Toast.makeText(mContext, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!topUpString.matches("\\d+")){
            Toast.makeText(mContext, "Isi nominal dengan angka", Toast.LENGTH_SHORT).show();
            return;
        }

        double topUpValue = Double.valueOf(topUpString);

        mApiService.topUp(loggedAccount.id,topUpValue).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Double> res = response.body();
                if(res.success) {
                    finish();
                    overridePendingTransition(0, 0);

                    loggedAccount.balance += res.payload.doubleValue();

                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                Toast.makeText(mContext, "Ada problem pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}