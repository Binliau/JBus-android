package com.kevinraihanjbusrd.jbus_android;

import static com.kevinraihanjbusrd.jbus_android.LoginActivity.selectedBus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kevinraihanjbusrd.jbus_android.model.BaseResponse;
import com.kevinraihanjbusrd.jbus_android.model.Bus;
import com.kevinraihanjbusrd.jbus_android.model.Schedule;
import com.kevinraihanjbusrd.jbus_android.request.BaseApiService;
import com.kevinraihanjbusrd.jbus_android.request.UtilsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusScheduleActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private HorizontalScrollView pageScroll = null;
    private ListView scheduleList = null;
    private EditText addDate = null;
    private EditText addTime = null;
    private Button addBtn = null;
    private String scheduledTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_schedule);
        getSupportActionBar().hide();
        mContext = this;
        mApiService = UtilsApi.getApiService();

        scheduleList = findViewById(R.id.schedule_list);
        addDate = findViewById(R.id.input_date);
        addTime = findViewById(R.id.input_time);
        addBtn = findViewById(R.id.add_btn);

        setScheduleList();

        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(addDate);
            }
        });

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(addTime);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAddSchedule();
            }
        });

    }

    private void showTimeDialog(final EditText addTime) {
        final Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
                addTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(BusScheduleActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }

    private void showDateDialog(final EditText addDate) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMMM d, yyyy");
                addDate.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(BusScheduleActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setScheduleList() {
        ArrayList<Schedule> setList = new ArrayList<>(selectedBus.schedules);
        BusScheduleArrayAdapter pageList = new BusScheduleArrayAdapter(mContext, setList);
        scheduleList.setAdapter(pageList);
    }

    private void viewToast (Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    private void handleAddSchedule(){
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("MMMM d, yyyy");
        SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm:ss");

        try {
            scheduledTime = outputDateFormat.format(inputDateFormat.parse(addDate.getText().toString())) + " " +
                    outputTimeFormat.format(inputTimeFormat.parse(addTime.getText().toString()));
            mApiService.addSchedule(selectedBus.id, scheduledTime)
                    .enqueue(new Callback<BaseResponse<Bus>>() {
                @Override
                public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response){
                    if(!response.isSuccessful()){
                        viewToast(mContext, "Application error " + response.code());
                        return;
                    }
                    BaseResponse<Bus> res = response.body();
                    if(res.success){
                        selectedBus = res.payload;
                        Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                    viewToast(mContext, "Ada problem pada server");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            viewToast(mContext, "Error formatting timestamp");
        }
    }

    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) / 2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }
}