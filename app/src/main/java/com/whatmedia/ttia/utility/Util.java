package com.whatmedia.ttia.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.whatmedia.ttia.R;
import com.whatmedia.ttia.page.main.flights.notify.MyFlightsNotifyContract;
import com.whatmedia.ttia.response.GetFlightsInfoResponse;
import com.whatmedia.ttia.response.GetMyFlightsResponse;
import com.whatmedia.ttia.response.data.ClockData;
import com.whatmedia.ttia.response.GetClockDataResponse;
import com.whatmedia.ttia.response.data.ClockTimeData;
import com.whatmedia.ttia.response.data.FlightsInfoData;
import com.whatmedia.ttia.services.FlightClockBroadcast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * Created by neo_mac on 2017/8/6.
 */

public class Util {
    private final static String TAG = Util.class.getSimpleName();

    public final static String TAG_FORMAT_YMD = "yyyy/MM/dd";
    public final static String TAG_FORMAT_MD = "MM/dd";
    public final static String TAG_FORMAT_HM = "HH:mm";
    public final static String TAG_FORMAT_HMS = "HH:mm:ss";
    public final static String TAG_FORMAT_HH = "HH";
    public final static String TAG_FORMAT_mm = "mm";
    public final static String TAG_DAY = "day";
    public final static String TAG_HOUR = "hour";
    public final static String TAG_MIN = "min";
    public final static String TAG_SEC = "sec";

    /**
     * Get now data
     *
     * @return
     */
    public static String getNowDate() {
        DateFormat df = new SimpleDateFormat(TAG_FORMAT_YMD);
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    /**
     * Get now date by format
     *
     * @param format
     * @return
     */
    public static String getNowDate(String format) {
        DateFormat df = new SimpleDateFormat(format);
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    /**
     * Get count date
     *
     * @param count
     * @return
     */
    public static String getCountDate(int count) {
        DateFormat df = new SimpleDateFormat(TAG_FORMAT_MD);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, count);
        String date = df.format(calendar.getTime());
        return date;
    }

    /**
     * Get count date customize format
     *
     * @param count
     * @param format
     * @return
     */
    public static String getCountDate(int count, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, count);
        String date = df.format(calendar.getTime());
        return date;
    }

    /**
     * Get different time with now time
     *
     * @param time
     * @return
     */
    public static HashMap<String, Long> getDifferentTimeWithNowTime(String time) {
        DateFormat df = new SimpleDateFormat(TAG_FORMAT_HM);
        long hours = 0;
        long minutes = 0;
        long diff = 0;
        try {
            Date d1 = df.parse(time);
            Date d2 = df.parse(getNowDate(TAG_FORMAT_HM));
            diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        HashMap<String, Long> diffTime = new HashMap<>();
        diffTime.put(TAG_HOUR, hours);
        diffTime.put(TAG_MIN, minutes);
        diffTime.put(TAG_SEC, diff / 1000);
        return diffTime;
    }

    /**
     * Get different time
     *
     * @param timeSource
     * @param timeTarget
     * @return
     */
    public static String getDifferentTime(String timeSource, String timeTarget) {
        DateFormat df = new SimpleDateFormat(TAG_FORMAT_HM);
        DateFormat dfSource = new SimpleDateFormat(TAG_FORMAT_HH);

        Date targetDate = null;
        Date sourceDate = null;
        try {
            targetDate = df.parse(timeTarget);
            sourceDate = df.parse(timeSource);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String hourSource = dfSource.format(sourceDate);
        dfSource = new SimpleDateFormat(TAG_FORMAT_mm);
        String minSource = dfSource.format(sourceDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(targetDate);
        calendar.add(Calendar.HOUR, -Integer.parseInt(hourSource));
        calendar.add(Calendar.MINUTE, -Integer.parseInt(minSource));
        Date resultD = calendar.getTime();
        String resultT = df.format(resultD);
        Log.d("TAG", resultT);
        return resultT;
    }

    public static String getTransformTimeFormat(String formatTarget, String time) {
        DateFormat df = new SimpleDateFormat(formatTarget);
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = df.format(date.getTime());
        return result;
    }

    /**
     * Get Now time
     *
     * @return
     */
    public static float getNowTime() {
        return System.currentTimeMillis();
    }

    /**
     * Xml to Json String
     *
     * @param xmlSource
     * @return
     */
    public static String xmlToSJsonString(String xmlSource) {
        XmlToJson xmlToJson = new XmlToJson.Builder(xmlSource).build();
        String formatted = xmlToJson.toFormattedString();
        return formatted;
    }

    /**
     * Get drawable by string
     *
     * @param context
     * @param name
     * @return
     */
    public static int getDrawableByString(Context context, String name) {
        int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return id;
    }

    /**
     * hide soft keyboard
     *
     * @param view
     */
    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Show time picker
     *
     * @param context
     * @param listener
     */
    public static void showTimePicker(Context context, TimePickerDialog.OnTimeSetListener listener) {
        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        TimePickerDialog timePicker = new TimePickerDialog(context, listener, calendar.get(calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE), true);
        timePicker.show();
    }

    /**
     * time's format yyyy-MM-ddTtt:tt:tt
     * <p>
     * use this methods will return yyyy-MM-dd
     *
     * @param time
     */
    public static String justShowDate(String time) {
        String[] result = time.split("T");
        return !TextUtils.isEmpty(result[0]) && result != null && result.length > 0 ? result[0] : time;
    }

    /**
     * Set Alert clock
     *
     * @param context
     * @param data
     */
    public static void setAlertClock(Context context, ClockData data) {
        if (data != null && data.getFlightsData() != null) {
            for (FlightsInfoData item : data.getFlightsData()) {
                if (item.getNotificationTime() != null && item.getNotificationId() != 0) {
                    int sec = (int) item.getNotificationTime().getSec();
                    Integer id = item.getNotificationId();
                    Calendar cal1 = Calendar.getInstance();
                    cal1.add(Calendar.SECOND, sec);
                    Log.d(TAG, "ID : " + id + " 配置鬧終於" + sec + "秒後: " + cal1);
                    Gson gson = new Gson();
                    String flightData = gson.toJson(item, FlightsInfoData.class);

                    Intent intent = new Intent(context, FlightClockBroadcast.class);
                    intent.putExtra(MyFlightsNotifyContract.TAG_NOTIFY_Flight_DATA, flightData);
                    intent.putExtra(MyFlightsNotifyContract.TAG_NOTIFY_ID, id);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, cal1.getTimeInMillis(), pendingIntent);
                } else {
                    Log.e(TAG, "FlightsInfoData notification info in error");
                }
            }
        } else {
            Log.d(TAG, "ClockData is error or data.getFlightsData() is error");
        }
    }

    /**
     * Cancel alert clock
     *
     * @param context
     * @param flightsInfoDataList
     */
    public static void cancelAlertClock(Context context, List<FlightsInfoData> flightsInfoDataList) {
        Intent intent = new Intent(context, FlightClockBroadcast.class);
        for (FlightsInfoData item : flightsInfoDataList) {
            int id = item.getNotificationId();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pendingIntent);

            Log.d(TAG, "cancel alert clock : " + id);
        }
    }

    /**
     * Get Marquee Sub Message
     *
     * @param context
     * @return
     */
    public static String getMarqueeSubMessage(Context context) {
        List<FlightsInfoData> datas = GetFlightsInfoResponse.newInstance(Preferences.getMyFlightsData(context));

        StringBuilder marqueeSubMessage = new StringBuilder();

        if (datas != null) {
            for (FlightsInfoData item : datas) {
                if (marqueeSubMessage.length() > 0) {
                    marqueeSubMessage.append(", ");
                }
//                marqueeSubMessage.append(!TextUtils.isEmpty(item.getCExpectedTime()) ? item.getCExpectedTime() : "")
//                        .append(" ")
//                        .append(!TextUtils.isEmpty(item.getCTName()) ? item.getCTName() : "")
//                        .append(" ")
//                        .append(!TextUtils.isEmpty(item.getFlightCode()) ? item.getFlightCode() : "")
//                        .append(" ")
//                        .append(!TextUtils.isEmpty(item.getGate()) ? item.getGate() : "")
//                        .append(" ")
//                        .append(!TextUtils.isEmpty(item.getFlightStatus()) ? item.getFlightStatus() : "");

                marqueeSubMessage.append(!TextUtils.isEmpty(item.getFlightCode()) ? item.getFlightCode().trim() : "")
                        .append(" ")
                        .append(!TextUtils.isEmpty(item.getAirLineCName()) ? item.getAirLineCName().trim() : "")
                        .append(" ")
                        .append(!TextUtils.isEmpty(item.getKinds()) ? item.getKinds().equals(FlightsInfoData.TAG_KIND_ARRIVE)?context.getString(R.string.marquee_arrive):context.getString(R.string.marquee_dexxxxx) : "")
                        .append(!TextUtils.isEmpty(item.getContactsLocationChinese()) ? item.getContactsLocationChinese().trim() : "")
                        .append(" ")
                        .append(context.getString(R.string.marquee_ecpected_time))
                        .append(!TextUtils.isEmpty(item.getCExpectedTime()) ? item.getCExpectedTime().trim() : "")
                        .append(" ")
                        .append(context.getString(R.string.marquee_status))
                        .append(!TextUtils.isEmpty(item.getFlightStatus()) ? item.getFlightStatus().trim() : "")
                        .append(" ")
                        .append(context.getString(R.string.marquee_ecpress_time))
                        .append(!TextUtils.isEmpty(item.getCExpressTime()) ? item.getCExpressTime().trim() : "")
                        .append(" ")
                        .append(context.getString(R.string.marquee_terminal))
                        .append(!TextUtils.isEmpty(item.getTerminals()) ? item.getTerminals().trim() : "")
                        .append(" ")
                        .append(context.getString(R.string.marquee_gate))
                        .append(!TextUtils.isEmpty(item.getGate()) ? item.getGate().trim() : "")
                        .append(" ")
                        .append(!TextUtils.isEmpty(item.getKinds()) ? item.getKinds().equals(FlightsInfoData.TAG_KIND_ARRIVE)?context.getString(R.string.marquee_luggage):context.getString(R.string.marquee_gt) : "")
                        .append(!TextUtils.isEmpty(item.getKinds()) ? item.getKinds().equals(FlightsInfoData.TAG_KIND_ARRIVE)?
                                !TextUtils.isEmpty(item.getLuggageCarousel()) ? item.getLuggageCarousel().trim() : "":
                                !TextUtils.isEmpty(item.getCounter()) ? item.getCounter().trim() : "":"");


            }
        }
        if (marqueeSubMessage.length() == 0) {
            marqueeSubMessage.append(context.getString(R.string.marquee_default_end_message));
        }
        Log.e("Ian",marqueeSubMessage.toString());
        return marqueeSubMessage.toString();
    }

    /**
     * 合成Bitmap
     *
     * @param bitmapList
     * @return
     * @Param space --每張圖之間的間隔
     */
    public static Bitmap combineBitmap(Bitmap[] bitmapList, int space) {
        if (bitmapList == null || bitmapList.length <= 0) {
            return null;
        }

        int width = bitmapList[0].getWidth();
        int height = 0;
        for (int i = 0; i < bitmapList.length; i++) {
            height += bitmapList[i].getHeight();
            height += space;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmapList[0], 0, 0, null);
        int tempHeight = bitmapList[0].getHeight() + space;
        for (int i = 1; i < bitmapList.length; i++) {
            canvas.drawBitmap(bitmapList[i], 0, tempHeight, null);
            tempHeight += bitmapList[i].getHeight();
            tempHeight += space;
        }
        return bitmap;
    }

    /**
     *  Bitmap放大的方法  height(想放大的高度) width(想放大的寬度)
     * @param bitmap
     * @param height
     * @param width
     * @return
     */
    public static Bitmap setBitmapScale(Bitmap bitmap,int height,int width) {

        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();

        if(bitmapHeight <= 0 || bitmapWidth <= 0){
            return bitmap;
        }

        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width<=0?bitmapWidth:width) / bitmapWidth;
        float scaleHeight = ((float) height<=0?bitmapHeight:height) / bitmapHeight;

        matrix.postScale(scaleWidth, scaleHeight); //長寬比例

        Log.e("Ian","bitmap.getWidth():"+bitmapWidth+", bitmap.getHeight():"+bitmapHeight+", height:"+height+", width:"+width);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        return resizeBmp;
    }

    /**
     * 取得 deviceId
     * @param context
     * @return
     */
    public static String getDeviceId(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * Add Notification
     *
     * @param context
     * @param hourOfDay
     * @param minute
     * @return
     */
    public static List<ClockData> addNotification(Context context, int hourOfDay, int minute) {
        String sourceTime = hourOfDay + ":" + minute;
        List<ClockData> clockDataList = GetClockDataResponse.newInstance(Preferences.getClockData(context));
        Gson gson = new Gson();
        List<FlightsInfoData> myFlightDataList = GetMyFlightsResponse.newInstance(Preferences.getMyFlightsData(context));
        ClockData clockData = new ClockData();

        if (myFlightDataList != null) {

            for (FlightsInfoData item : myFlightDataList) {
                if (item.getNotificationId() == 0) {
                    ClockTimeData clockTimeData = new ClockTimeData();
                    HashMap<String, Long> diffTime = Util.getDifferentTimeWithNowTime(Util.getDifferentTime(sourceTime, item.getExpectedTime()));
                    clockTimeData.setHour(diffTime.get(Util.TAG_HOUR));
                    clockTimeData.setMin(diffTime.get(Util.TAG_MIN));
                    clockTimeData.setSec(diffTime.get(Util.TAG_SEC));
                    if (clockTimeData.getSec() > 0) {

                        item.setNotificationId(new Random().nextInt(9000) + 65);
                        item.setNotificationTime(clockTimeData);
                    }
                }
            }
            clockData.setFlightsData(myFlightDataList);

        } else {
            Log.e(TAG, "myFlightDataList = null");
        }

        clockData.setNotify(true);
        String timeString = context.getString(R.string.my_flights_notify, hourOfDay, minute);
        ClockTimeData clockTimeData = new ClockTimeData();
        clockTimeData.setHour(hourOfDay);
        clockTimeData.setMin(minute);
        clockData.setTime(clockTimeData);
        clockData.setTimeString(timeString);
        clockData.setId(new Random().nextInt(9000) + 65);
        clockDataList.add(clockData);

        String json = gson.toJson(clockDataList);
        Preferences.saveClockData(context, json);
        return clockDataList;
    }

    /**
     * Modify notification time
     * 先讓此Item刪除 然後重新新增
     *
     * @param context
     * @param hourOfDay
     * @param minute
     * @param selectId
     * @param clockDataList
     * @return
     */
    public static List<ClockData> modifyNotification(Context context, int hourOfDay, int minute, int selectId, List<ClockData> clockDataList) {
        for (ClockData item : clockDataList) {
            if (item.getId() == selectId) {
                item.setIsCheck(true);
                deleteNotification(context, clockDataList);
                break;
            }
        }

        return addNotification(context, hourOfDay, minute);
    }

    /**
     * Delete notification
     *
     * @param context
     * @param selectList
     * @return
     */
    public static List<ClockData> deleteNotification(Context context, List<ClockData> selectList) {

        List<ClockData> cacheData = new ArrayList<>(selectList);

        for (ClockData item : selectList) {
            if (item.getIsCheck()) {
                cacheData.remove(item);
                if (item.getFlightsData() != null)
                    Util.cancelAlertClock(context, item.getFlightsData());
                else {
                    Log.e(TAG, "subItem.getFlightsData() is null");
                }
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(cacheData);
        Preferences.saveClockData(context, json);
        return cacheData;
    }

    /**
     * Delete notification
     *
     * @param context
     * @param clockList
     * @return
     */
    public static List<ClockData> deleteAllNotification(Context context, List<ClockData> clockList) {

        List<ClockData> cacheData = new ArrayList<>(clockList);

        for (ClockData item : clockList) {
            cacheData.remove(item);
            if (item.getFlightsData() != null)
                Util.cancelAlertClock(context, item.getFlightsData());
            else {
                Log.e(TAG, "subItem.getFlightsData() is null");

            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(cacheData);
        Preferences.saveClockData(context, json);
        return cacheData;
    }

    public static void resetNotification(Context context, List<FlightsInfoData> myFlightData) {
        if (myFlightData != null && myFlightData.size() > 0) {
            List<ClockData> clockList = GetClockDataResponse.newInstance(Preferences.getClockData(context));

            deleteAllNotification(context, clockList);
            for (ClockData item : clockList) {
                List<ClockData> changeList = addNotification(context, (int) item.getTime().getHour(), (int) item.getTime().getMin());
                for (ClockData subItem : changeList) {
                    setAlertClock(context, subItem);
                }
            }
        }
    }
}
