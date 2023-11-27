package com.kevinraihanjbusrd.jbus_android;

import static com.kevinraihanjbusrd.jbus_android.LoginActivity.loggedAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.kevinraihanjbusrd.jbus_android.model.Bus;
import com.kevinraihanjbusrd.jbus_android.request.BaseApiService;
import com.kevinraihanjbusrd.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBusActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private ListView busListView = null;
    private TextView noBus;
    private List<Bus> busList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        busListView = findViewById(R.id.manageBusView);

        ManageBusArrayAdapter numbersArrayAdapter = new ManageBusArrayAdapter(this, Bus.sampleBusList(20));
        ListView numbersListView = findViewById(R.id.manageBusView);
        numbersListView.setAdapter(numbersArrayAdapter);

        getSupportActionBar().setTitle("Manage Bus");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater actionBar = getMenuInflater();
        actionBar.inflate(R.menu.manage_bus_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_bus) {
            moreActivity(this, AddBusActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void moreActivity (Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    protected void getAllMyBus(){
        mApiService.getMyBus(loggedAccount.id).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                busList = response.body();
                if(!busList.isEmpty()){

                    ArrayList<Bus> setList = new ArrayList<>(busList);

                    ManageBusArrayAdapter pageList = new ManageBusArrayAdapter(mContext, setList);
                    busListView.setAdapter(pageList);
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Ada problem pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}