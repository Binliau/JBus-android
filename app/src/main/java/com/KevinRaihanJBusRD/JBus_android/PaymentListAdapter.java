package com.kevinraihanjbusrd.jbus_android;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kevinraihanjbusrd.jbus_android.model.Bus;
import com.kevinraihanjbusrd.jbus_android.model.Payment;
import com.kevinraihanjbusrd.jbus_android.request.BaseApiService;
import com.kevinraihanjbusrd.jbus_android.request.UtilsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentListAdapter extends ArrayAdapter<Payment> {
    private BaseApiService mApiService;
    private Bus currentBus;
    private String busSeats;
    public PaymentListAdapter(@NonNull Context context, List<Payment> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;
        mApiService = UtilsApi.getApiService();
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.payment_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Payment currentNumberPosition = getItem(position);

        assert currentNumberPosition != null;
        TextView deptArr = currentItemView.findViewById(R.id.terminal);

        TextView busName = currentItemView.findViewById(R.id.bus_name);
        mApiService.getBusById(currentNumberPosition.getBusId()).enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                currentBus = response.body();
                busName.setText(currentBus.name);
                deptArr.setText(currentBus.departure.stationName + " - " + currentBus.arrival.stationName);
            }
            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
            }
        }
        );
        //busName.setText(LoginActivity.currentBus.name);

        //deptArr.setText(currentBus.departure.stationName+" - "+currentBus.arrival.stationName);
        TextView departureDate = currentItemView.findViewById(R.id.date);
        departureDate.setText(currentNumberPosition.getDepartureTime());

        TextView bookingStatus = currentItemView.findViewById(R.id.status_display);
        bookingStatus.setText(currentNumberPosition.status.toString());

        return currentItemView;
    }
}