package com.whatmedia.ttia.page.main.flights.result;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whatmedia.ttia.R;
import com.whatmedia.ttia.interfaces.IOnItemClickListener;
import com.whatmedia.ttia.response.data.FlightsInfoData;
import com.whatmedia.ttia.utility.Preferences;
import com.whatmedia.ttia.utility.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by neo on 2017/8/4.
 */

public class FlightsSearchResultRecyclerViewAdapter extends RecyclerView.Adapter<FlightsSearchResultRecyclerViewAdapter.ViewHolder> {
    private List<FlightsInfoData> mItems;
    private Context mContext;
    private IOnItemClickListener mListener;
    private String mLocale;
    private boolean isScreen34Mode;
    private RelativeLayout.LayoutParams mParamsFrame;
    private RelativeLayout.LayoutParams mParamsBackground;

    public FlightsSearchResultRecyclerViewAdapter(Context context, List<FlightsInfoData> data) {
        mContext = context;
        mItems = data;
        mLocale = Preferences.getLocaleSetting(context);
        isScreen34Mode = Preferences.checkScreenIs34Mode(mContext);
        initParams();
    }

    public FlightsSearchResultRecyclerViewAdapter(Context context) {
        mContext = context;
        mLocale = Preferences.getLocaleSetting(context);
        isScreen34Mode = Preferences.checkScreenIs34Mode(mContext);
        initParams();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flights_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mItems == null)
            return;
        FlightsInfoData item = mItems.get(position);
        if (item == null)
            return;

        if (!isScreen34Mode) {
            holder.mLayoutFrame.setLayoutParams(mParamsFrame);
            holder.mLayoutBackground.setLayoutParams(mParamsBackground);
        }

        holder.mTextViewTime.setText(!TextUtils.isEmpty(item.getExpressTime()) ? Util.getTransformTimeFormat(Util.TAG_FORMAT_HM, item.getExpressTime().trim()) : "");
        holder.mTextViewFlightCode.setText(!TextUtils.isEmpty(item.getFlightCode()) ? item.getFlightCode().trim() : "");
        switch (mLocale) {
            case "zh_TW":
                holder.mTextViewLocation.setText(!TextUtils.isEmpty(item.getContactsLocationChinese()) ? item.getContactsLocationChinese().trim() : "");
                break;
            case "zh_CN":
                holder.mTextViewLocation.setText(!TextUtils.isEmpty(item.getContactsLocationChinese()) ? item.getContactsLocationChinese().trim() : "");
                break;
            case "en":
                holder.mTextViewLocation.setText(!TextUtils.isEmpty(item.getContactsLocationEng()) ? item.getContactsLocationEng().trim() : "");
                break;
            case "ja":
                holder.mTextViewLocation.setText(!TextUtils.isEmpty(item.getContactsLocationEng()) ? item.getContactsLocationEng().trim() : "");
                break;
            default:
                holder.mTextViewLocation.setText(!TextUtils.isEmpty(item.getContactsLocationChinese()) ? item.getContactsLocationChinese().trim() : "");
                break;
        }
        if (TextUtils.equals(item.getKinds(), FlightsInfoData.TAG_KIND_ARRIVE))
            holder.mTextViewGate.setText(!TextUtils.isEmpty(item.getLuggageCarousel()) ? item.getLuggageCarousel().trim() : "");
        else {
            holder.mTextViewGate.setText(!TextUtils.isEmpty(item.getGate()) ? item.getGate().trim() : "");
        }
        if (!TextUtils.isEmpty(item.getTerminals())) {

            StringBuilder builder = new StringBuilder();
            switch (mLocale) {
                case "zh_TW":
                case "zh_CN":
                    builder.append(item.getTerminals()).append(mContext.getString(R.string.flights_search_result_terminal_text));
                    break;
                case "en":
                case "ja":
                    builder.append(mContext.getString(R.string.flights_search_result_terminal_text)).append(item.getTerminals());
                    break;
                default:
                    builder.append(mContext.getString(R.string.flights_search_result_terminal_text)).append(item.getTerminals());
                    break;
            }
//            builder.append(item.getTerminals()).append(mContext.getString(R.string.flights_search_result_terminal_text));
            holder.mTextViewTerminal.setText(builder);
        } else
            holder.mTextViewTerminal.setText("");

        if (!TextUtils.isEmpty(item.getFlightStatus())) {
            if (checkFlightState(item.getFlightStatus()))
                holder.mTextViewState.setTextColor(ContextCompat.getColor(mContext, R.color.colorText));
            else
                holder.mTextViewState.setTextColor(ContextCompat.getColor(mContext, R.color.colorTextSpecial));
            holder.mTextViewState.setText(FlightsInfoData.checkFlightShowText(mContext, item.getFlightStatus()));
        } else
            holder.mTextViewState.setText("");

        if (!TextUtils.isEmpty(item.getAirlineCode())) {
            int id = Util.getDrawableByString(mContext, "airline_" + item.getAirlineCode().toLowerCase());
            if (id != 0)
                Picasso.with(mContext).load(id).into(holder.mImageViewLogo);
            holder.mImageViewLogo.setVisibility(View.VISIBLE);
        } else {
            holder.mImageViewLogo.setVisibility(View.INVISIBLE);
        }

        holder.mLayoutFrame.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public void setData(List<FlightsInfoData> data) {
        mItems = data;
        notifyDataSetChanged();
    }

    public void setClickListener(IOnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_background)
        ImageView mLayoutBackground;
        @BindView(R.id.layout_frame)
        RelativeLayout mLayoutFrame;
        @BindView(R.id.textView_time)
        TextView mTextViewTime;
        @BindView(R.id.imageView_logo)
        ImageView mImageViewLogo;
        @BindView(R.id.textView_flight_code)
        TextView mTextViewFlightCode;
        @BindView(R.id.textView_location)
        TextView mTextViewLocation;
        @BindView(R.id.textView_terminal)
        TextView mTextViewTerminal;
        @BindView(R.id.textView_gate)
        TextView mTextViewGate;
        @BindView(R.id.layout_terminal)
        RelativeLayout mLayoutTerminal;
        @BindView(R.id.textView_state)
        TextView mTextViewState;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.layout_frame)
        public void onClick(View view) {
            if (mListener != null)
                mListener.onClick(view);
        }

    }

    /**
     * Check flight is "on time" or "arrived" or "departure"
     *
     * @param data
     * @return
     */
    private boolean checkFlightState(String data) {
        if (data.contains(FlightsInfoData.TAG_ON_TIME) || data.contains(FlightsInfoData.TAG_ARRIVED) || data.contains(FlightsInfoData.TAG_DEPARTED))
            return true;
        return false;
    }

    public void initParams() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        //宽度 dm.widthPixels
        //高度 dm.heightPixels

        mParamsFrame = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mContext.getResources().getDimensionPixelOffset(R.dimen.dp_pixel_70));
        mParamsFrame.setMargins(mContext.getResources().getDimensionPixelOffset(R.dimen.dp_pixel_10), 0
                , mContext.getResources().getDimensionPixelOffset(R.dimen.dp_pixel_10), 0);

        mParamsBackground = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mContext.getResources().getDimensionPixelOffset(R.dimen.dp_pixel_70));
        mParamsBackground.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }
}
