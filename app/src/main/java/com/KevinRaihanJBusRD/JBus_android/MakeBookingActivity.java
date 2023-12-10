package com.kevinraihanjbusrd.jbus_android;

import static com.kevinraihanjbusrd.jbus_android.BusDetailActivity.selectedSchedule;
import static com.kevinraihanjbusrd.jbus_android.LoginActivity.loggedAccount;
import static com.kevinraihanjbusrd.jbus_android.LoginActivity.selectedBus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kevinraihanjbusrd.jbus_android.model.BaseResponse;
import com.kevinraihanjbusrd.jbus_android.model.Payment;
import com.kevinraihanjbusrd.jbus_android.request.BaseApiService;
import com.kevinraihanjbusrd.jbus_android.request.UtilsApi;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeBookingActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private TextView busTitle;
    private TextView departureDate;
    private TextView availableSeatsDisplay;
    private TextView totalPriceDisplay;
    private TableLayout seatLayout;
    private ImageButton seats[];
    private Button orderButton;
    private int size;
    private double pricePerSeat;
    private double totalPrice = 0;
    private int availableSeats = 0;
    private List<String> selectedSeat = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_booking);
        getSupportActionBar().hide();

        mApiService = UtilsApi.getApiService();
        mContext = this;

        busTitle = findViewById(R.id.bus_title);
        departureDate = findViewById(R.id.departure_date);
        availableSeatsDisplay = findViewById(R.id.available_seats);
        totalPriceDisplay = findViewById((R.id.total_price));
        orderButton = findViewById(R.id.order_button);
        seatLayout = findViewById(R.id.seat_layout);
        seatLayout.removeAllViews();

        createSeats();
        busTitle.setText(selectedBus.name);
        departureDate.setText(""+selectedSchedule);
        availableSeatsDisplay.setText(""+availableSeats);
        
        orderButton.setOnClickListener( x -> {
            handleMakeBooking();
            recreate();
        });
    }

    private void handleMakeBooking() {
        int buyerId = loggedAccount.id;
        int renterId = loggedAccount.company.id;
        int busId = selectedBus.id;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
        String departureDate = dateFormat.format(selectedSchedule.departureSchedule);

        mApiService.makeBooking(buyerId, renterId, busId, selectedSeat, departureDate)
                .enqueue(new Callback<BaseResponse<Payment>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                        if (!response.isSuccessful()) {
                            try {
                                String errorBody = response.errorBody().string();
                                showToast("Application error: " + errorBody);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                        BaseResponse<Payment> res = response.body();
                        if (res.success) {
                            showToast(res.message);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                        showToast("Ada problem pada server: " + t.getMessage());
                        t.printStackTrace();
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


    private void createSeats() {
        pricePerSeat = selectedBus.price.price;
        size = selectedBus.capacity;
        String[] seatList = selectedSchedule.seatAvailability.keySet().toArray(new String[0]);
        Iterator<Boolean> val = selectedSchedule.seatAvailability.values().iterator();
        seats = new ImageButton[size];
        int maxSeatsPerRow = 4;

        for (int i = 0; i < size; i++) {
            // Create a new row for every maxSeatsPerRow seats
            if (i % maxSeatsPerRow == 0) {
                seatLayout.addView(new TableRow(mContext));
            }

            seats[i] = new ImageButton(mContext);
            seats[i].setImageDrawable(getResources().getDrawable(R.drawable.seat_vector));
            seats[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            seats[i].setScaleX(1.1F);
            seats[i].setScaleY(1.1F);

            int row = i / maxSeatsPerRow; // Calculate the row number

            if (val.next()) {
                availableSeats++;
                changeSeatColor(i, Color.parseColor("#00FF00"));
                int j = i;
                seats[i].setOnClickListener(l -> {
                    if (!selectedSeat.contains(seatList[j])) {
                        changeSeatColor(j, Color.parseColor("#4F4FD8"));
                        selectedSeat.add(seatList[j]);
                        totalPrice += pricePerSeat;
                        totalPriceDisplay.setText("Rp."+totalPrice);
                    } else {
                        changeSeatColor(j, Color.parseColor("#00FF00"));
                        selectedSeat.remove(seatList[j]);
                        totalPrice -= pricePerSeat;
                        totalPriceDisplay.setText("Rp."+totalPrice);
                    }
                });
            } else {
                changeSeatColor(i, Color.parseColor("#FF0000"));
            }

            // Add the seat to the current row
            TableRow currentRow = (TableRow) seatLayout.getChildAt(row);
            currentRow.addView(seats[i]);
        }
    }

    private void changeSeatColor(int seatIndex, int color) {
        seats[seatIndex].setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}


