package com.kevinraihanjbusrd.jbus_android;

import static com.kevinraihanjbusrd.jbus_android.LoginActivity.loggedAccount;
import static com.kevinraihanjbusrd.jbus_android.LoginActivity.selectedBus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kevinraihanjbusrd.jbus_android.model.Bus;
import com.kevinraihanjbusrd.jbus_android.model.Payment;
import com.kevinraihanjbusrd.jbus_android.request.BaseApiService;
import com.kevinraihanjbusrd.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private List<Payment> paymentList;
    private LinearLayout noPayment;
    private LinearLayout paymentLayout;
    private ListView paymentListView;
    public static Payment selectedPayment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        this.getSupportActionBar().hide();

        mApiService = UtilsApi.getApiService();
        mContext = this;
        noPayment = findViewById(R.id.no_payment);
        paymentLayout = findViewById(R.id.payment_list_layout);
        noPayment = findViewById(R.id.no_payment);
        paymentListView = findViewById(R.id.payment_list_view);
        paymentLayout.setVisibility(View.GONE);

        getMyPayment();

        paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paymentListView, View view, int i, long l) {
                selectedPayment = (Payment) paymentListView.getItemAtPosition(i);

                if(selectedPayment != null) {
                    moveActivity(mContext, MainActivity.class);
                }
            }
        });


    }
    protected void getMyPayment() {
        mApiService.getMyPayment(loggedAccount.id).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                paymentList = response.body();
                if (!paymentList.isEmpty()) {
                    noPayment.setVisibility(View.GONE);
                    paymentLayout.setVisibility(View.VISIBLE);
                    ArrayList<Payment> setList = new ArrayList<>(paymentList);

                    PaymentListAdapter pageList = new PaymentListAdapter(mContext, setList);
                    paymentListView.setAdapter(pageList);
                }
            }
            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Ada problem pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
}