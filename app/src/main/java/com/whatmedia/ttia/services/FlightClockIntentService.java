package com.whatmedia.ttia.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.whatmedia.ttia.R;
import com.whatmedia.ttia.page.main.MainActivity;
import com.whatmedia.ttia.page.main.flights.notify.MyFlightsNotifyContract;
import com.whatmedia.ttia.response.data.ClockData;
import com.whatmedia.ttia.response.GetClockDataResponse;
import com.whatmedia.ttia.response.data.FlightsInfoData;
import com.whatmedia.ttia.utility.Preferences;
import com.whatmedia.ttia.utility.Util;

import java.util.List;

/**
 * Created by neo_mac on 2017/8/8.
 */

public class FlightClockIntentService extends IntentService {
    private final static String TAG = FlightClockIntentService.class.getSimpleName();
    private Gson mGson;

    public FlightClockIntentService() {
        super(FlightClockIntentService.class.getSimpleName());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FlightClockIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");
        if (intent.getExtras() != null && intent.getExtras().getInt(MyFlightsNotifyContract.TAG_NOTIFY_ID) != 0
                && intent.getExtras().getString(MyFlightsNotifyContract.TAG_NOTIFY_Flight_DATA) != null) {
            int id = intent.getExtras().getInt(MyFlightsNotifyContract.TAG_NOTIFY_ID);


            FlightsInfoData flightsInfoData = getFlightsInfo(intent.getExtras().getString(MyFlightsNotifyContract.TAG_NOTIFY_Flight_DATA));

            //Notification message
            Notification.Builder builder = new Notification.Builder(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                builder.setContentTitle(getString(R.string.title_flight_notify))
                        .setContentText(getString(R.string.my_flights_notify_message,
                                !TextUtils.isEmpty(flightsInfoData.getAirLineCName()) ? flightsInfoData.getAirLineCName().trim() : "",
                                !TextUtils.isEmpty(flightsInfoData.getFlightCode()) ? flightsInfoData.getFlightCode().trim() : "",
                                !TextUtils.isEmpty(flightsInfoData.getCExpectedTime()) ? flightsInfoData.getCExpectedTime().trim() : "",
                                !TextUtils.isEmpty(flightsInfoData.getContactsLocation()) ? flightsInfoData.getContactsLocation().trim() : ""))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setLargeIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                        .setSmallIcon(R.mipmap.icon)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setAutoCancel(true);
            } else {
                builder.setContentTitle(getString(R.string.title_flight_notify))
                        .setContentText(getString(R.string.my_flights_notify_message,
                                !TextUtils.isEmpty(flightsInfoData.getAirLineCName()) ? flightsInfoData.getAirLineCName().trim() : "",
                                !TextUtils.isEmpty(flightsInfoData.getFlightCode()) ? flightsInfoData.getFlightCode().trim() : "",
                                !TextUtils.isEmpty(flightsInfoData.getCExpectedTime()) ? flightsInfoData.getCExpectedTime().trim() : "",
                                !TextUtils.isEmpty(flightsInfoData.getContactsLocation()) ? flightsInfoData.getContactsLocation().trim() : ""))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setAutoCancel(true);
            }

            Util.wakeUpScreen(getApplicationContext(), 6000, TAG);

            //Click intent
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            Notification notificationCompat = builder.build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(id, notificationCompat);

//            deleteReceiveNotification(id);
        } else {
            Log.e(TAG, "Service bundle is error");
        }
    }

    /**
     * 收到推播之後把推播刪除
     */
    private void deleteReceiveNotification(int receiveId) {
        List<ClockData> datas = GetClockDataResponse.newInstance(Preferences.getClockData(getApplicationContext()));
        for (ClockData item : datas) {
            if (item.getId() == receiveId) {
                datas.remove(item);
                break;
            }
        }
        if (mGson == null)
            mGson = new Gson();
        String json = mGson.toJson(datas);
        Preferences.saveClockData(getApplicationContext(), json);
    }

    /**
     * get Flights Info
     *
     * @param flightsData
     * @return
     */
    private FlightsInfoData getFlightsInfo(String flightsData) {
        if (mGson == null)
            mGson = new Gson();
        FlightsInfoData flightsInfoData = mGson.fromJson(flightsData, FlightsInfoData.class);
        return flightsInfoData;
    }
}
