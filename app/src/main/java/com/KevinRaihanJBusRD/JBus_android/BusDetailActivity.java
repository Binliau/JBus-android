package com.kevinraihanjbusrd.jbus_android;

import static com.kevinraihanjbusrd.jbus_android.LoginActivity.selectedBus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kevinraihanjbusrd.jbus_android.model.Schedule;
import com.kevinraihanjbusrd.jbus_android.request.BaseApiService;
import com.kevinraihanjbusrd.jbus_android.request.UtilsApi;

import org.w3c.dom.Text;

public class BusDetailActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private TextView busName;
    private TextView busType;
    private TextView capacity;
    private TextView facilities;
    private TextView departure;
    private TextView arrival;
    private TextView price;
    private Spinner dateSpinner;
    private Button makeBookBtn;
    public static Schedule selectedSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
        getSupportActionBar();

        mApiService = UtilsApi.getApiService();
        mContext = this;

        busName = findViewById(R.id.display_bus_name);
        busType = findViewById(R.id.display_bus_type);
        capacity = findViewById(R.id.display_capacity);
        facilities = findViewById(R.id.display_facilities);
        departure = findViewById(R.id.display_departure);
        arrival = findViewById(R.id.display_arrival);
        price = findViewById(R.id.display_price);
        makeBookBtn = findViewById(R.id.makebook_button);
        dateSpinner = (Spinner) findViewById(R.id.date_dropdown);

        busName.setText(""+selectedBus.name);
        busType.setText(""+selectedBus.busType);
        capacity.setText(""+selectedBus.capacity);
        if(selectedBus.facilities != null) {
            facilities.setText(""+selectedBus.facilities);
        } else {
            facilities.setText(" NONE");
        }
        departure.setText(""+selectedBus.departure.stationName);
        arrival.setText(""+selectedBus.arrival.stationName);
        price.setText(""+selectedBus.price.price);

        ArrayAdapter adapterSchedule = new ArrayAdapter<>(mContext, R.layout.spinner_entry_layout, selectedBus.schedules);
        adapterSchedule.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapterSchedule);
        dateSpinner.setOnItemSelectedListener(setSchedule());

        makeBookBtn.setOnClickListener(x -> {
            moveActivity(mContext, MakeBookingActivity.class);
        });


    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    protected AdapterView.OnItemSelectedListener setSchedule(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSchedule = (Schedule) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }
}

