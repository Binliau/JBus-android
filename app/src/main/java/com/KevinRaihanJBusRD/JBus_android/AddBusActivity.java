package com.kevinraihanjbusrd.jbus_android;

import static com.kevinraihanjbusrd.jbus_android.LoginActivity.loggedAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.kevinraihanjbusrd.jbus_android.model.BaseResponse;
import com.kevinraihanjbusrd.jbus_android.model.Bus;
import com.kevinraihanjbusrd.jbus_android.model.BusType;
import com.kevinraihanjbusrd.jbus_android.model.Facility;
import com.kevinraihanjbusrd.jbus_android.model.Station;
import com.kevinraihanjbusrd.jbus_android.request.BaseApiService;
import com.kevinraihanjbusrd.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText busName, busCapacity, busPrice;
    private BusType[] busType = BusType.values();
    private BusType selectedBusType;
    private Spinner busTypeDropdown;
    private List<Station> stationList = new ArrayList<>();
    private int selectedDepartureStation;
    private int selectedArrivalStation;
    private Spinner departureSpinnerDropdown;
    private Spinner arrivalSpinnerDropdown;
    private TableLayout facilitiesSection;
    private CheckBox AC, WIFI, Toilet, LCD_TV, Lunch, Large_Baggage, CoolBox, Electric_Socket;
    private List<Facility> facilities = new ArrayList<>();
    private Button addBusButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);
        this.getSupportActionBar().hide();

        mApiService = UtilsApi.getApiService();
        mContext = this;

        busName = findViewById(R.id.busName);
        busCapacity = findViewById(R.id.busCapacity);
        busPrice = findViewById(R.id.busPrice);

        busTypeDropdown = findViewById(R.id.bus_type_dropdown);
        departureSpinnerDropdown = findViewById(R.id.departure_dropdown);
        arrivalSpinnerDropdown = findViewById(R.id.arrival_dropdown);

        addBusButton = findViewById(R.id.add_bus_button);

        ArrayAdapter adapterBusType = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, busType);
        adapterBusType.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeDropdown.setAdapter(adapterBusType);
        busTypeDropdown.setOnItemSelectedListener(setBusType());

        setStationList();

        addBusButton.setOnClickListener(x -> {
            handleFacilitiesCheckbox();
            handleAddBus();
        });
    }

    private void viewToast (Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    protected AdapterView.OnItemSelectedListener setBusType(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBusType = busType[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    protected AdapterView.OnItemSelectedListener setDepartureStation(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Station dept = (Station) adapterView.getSelectedItem();
                selectedDepartureStation = dept.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    protected AdapterView.OnItemSelectedListener setArrivalStation(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Station arr = (Station) adapterView.getSelectedItem();
                selectedArrivalStation = arr.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    protected void setStationList(){
        mApiService.getAllStation().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(!response.isSuccessful()){
                    viewToast(mContext, "Application error " + response.code());
                    return;
                }

                stationList = response.body();

                ArrayAdapter adapterDepartureStation = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, stationList);
                adapterDepartureStation.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                departureSpinnerDropdown.setAdapter(adapterDepartureStation);
                departureSpinnerDropdown.setOnItemSelectedListener(setDepartureStation());

                ArrayAdapter adapterArrivalStation = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, stationList);
                adapterArrivalStation.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                arrivalSpinnerDropdown.setAdapter(adapterArrivalStation);
                arrivalSpinnerDropdown.setOnItemSelectedListener(setArrivalStation());
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                viewToast(mContext, "Ada problem pada server");
            }
        });
    }

    protected void handleFacilitiesCheckbox(){
        AC = findViewById(R.id.checkbox_AC);
        WIFI = findViewById(R.id.checkbox_wifi);
        Toilet = findViewById(R.id.checkbox_toilet);
        LCD_TV = findViewById(R.id.checkbox_lcd_tv);
        Lunch = findViewById(R.id.checkbox_lunch);
        Large_Baggage = findViewById(R.id.checkbox_large_baggage);
        CoolBox = findViewById(R.id.checkbox_coolbox);
        Electric_Socket = findViewById(R.id.checkbox_electric_socket);

        if(AC.isChecked()){
            facilities.add(Facility.AC);
        }

        if(WIFI.isChecked()){
            facilities.add(Facility.WIFI);
        }

        if(Toilet.isChecked()){
            facilities.add(Facility.TOILET);
        }

        if(LCD_TV.isChecked()){
            facilities.add(Facility.LCD_TV);
        }

        if(Lunch.isChecked()){
            facilities.add(Facility.LUNCH);
        }

        if(Large_Baggage.isChecked()){
            facilities.add(Facility.LARGE_BAGGAGE);
        }

        if(CoolBox.isChecked()){
            facilities.add(Facility.COOL_BOX);
        }

        if(Electric_Socket.isChecked()){
            facilities.add(Facility.ELECTRIC_SOCKET);
        }
    }

    protected void handleAddBus(){
        String busNameValue = busName.getText().toString();
        String busCapacityValue = busCapacity.getText().toString();
        String busPriceValue = busPrice.getText().toString();

        if(busNameValue.isEmpty() || busCapacityValue.isEmpty() || busPriceValue.isEmpty()){
            viewToast(mContext, "Field Tidak Boleh Kosong");
            return;
        }

        if(!busCapacityValue.matches("\\d+")){
            viewToast(mContext, "Field Capacity harus berupa angka");
        }

        if(!busPriceValue.matches("\\d+")){
            viewToast(mContext, "Field Harga harus berupa angka");
        }

        int capacity = Integer.valueOf(busCapacityValue);
        int price = Integer.valueOf(busPriceValue);
        mApiService.create(loggedAccount.id, busNameValue, capacity, facilities, selectedBusType, price, selectedDepartureStation, selectedArrivalStation)
                .enqueue(new Callback<BaseResponse<Bus>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                        if(!response.isSuccessful()){
                            viewToast(mContext, "Application error " + response.code());
                            return;
                        }

                        BaseResponse<Bus> res = response.body();
                        if(res.success) finish();
                        viewToast(mContext, res.message);
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                        viewToast(mContext, "Ada problem pada server");
                    }
                });
    }

    public void onCheckboxClicked(View view) {
    }
}