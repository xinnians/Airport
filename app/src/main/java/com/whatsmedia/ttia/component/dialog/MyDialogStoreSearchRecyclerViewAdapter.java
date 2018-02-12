package com.whatsmedia.ttia.component.dialog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whatsmedia.ttia.R;
import com.whatsmedia.ttia.interfaces.IOnItemClickListener;
import com.whatsmedia.ttia.newresponse.data.StoreConditionCodeData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by neo_mac on 2017/8/5.
 */

public class MyDialogStoreSearchRecyclerViewAdapter extends RecyclerView.Adapter<MyDialogStoreSearchRecyclerViewAdapter.ViewHolder> {
    private List<StoreConditionCodeData> mItems;
    private List<StoreConditionCodeData> mAreaCodeList;
    private List<StoreConditionCodeData> mFloorCodeList;
    private List<StoreConditionCodeData> mRestaurantCodeList;
    private List<StoreConditionCodeData> mStoreCodeList;

    private IOnItemClickListener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_dialog_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mItems != null) {
            StoreConditionCodeData item = mItems.get(position);
            if (item == null)
                return;

            holder.mTextViewItem.setText(item.getName());
            holder.mTextViewItem.setTag(item);
        }
//        else if (mAreaCodeList != null) {
//            StoreConditionCodeData item = mAreaCodeList.get(position);
//            if (item == null)
//                return;
//
//            holder.mTextViewItem.setText(item.getName());
//            holder.mTextViewItem.setTag(item);
//        } else if (mFloorCodeList != null) {
//            StoreConditionCodeData item = mFloorCodeList.get(position);
//            if (item == null)
//                return;
//
//            holder.mTextViewItem.setText(item.getName());
//            holder.mTextViewItem.setTag(item);
//        } else if (mRestaurantCodeList != null) {
//            StoreConditionCodeData item = mRestaurantCodeList.get(position);
//            if (item == null)
//                return;
//
//            holder.mTextViewItem.setText(item.getName());
//            holder.mTextViewItem.setTag(item);
//        } else if (mStoreCodeList != null) {
//            StoreConditionCodeData item = mStoreCodeList.get(position);
//            if (item == null)
//                return;
//
//            holder.mTextViewItem.setText(item.getName());
//            holder.mTextViewItem.setTag(item);
//        }
    }

    @Override
    public int getItemCount() {
//        int count = 0;
//        if (mItems != null)
//            count = mItems.size();
//        else if (mAreaCodeList != null)
//            count = mAreaCodeList.size();
//        else if (mFloorCodeList != null)
//            count = mFloorCodeList.size();
//        else if (mRestaurantCodeList != null)
//            count = mRestaurantCodeList.size();
//        else if (mStoreCodeList != null)
//            count = mStoreCodeList.size();
//        return count;
        return mItems != null ? mItems.size() : 0;
    }


    public void setTerminalCodeData(List<StoreConditionCodeData> list) {
        mItems = list;
        notifyDataSetChanged();
    }

    public void setAreaCodeData(List<StoreConditionCodeData> list) {
        mAreaCodeList = list;
        notifyDataSetChanged();
    }

    public void setFloorCodeData(List<StoreConditionCodeData> list) {
        mFloorCodeList = list;
        notifyDataSetChanged();
    }

    public void setRestaurantCodeData(List<StoreConditionCodeData> list) {
        mRestaurantCodeList = list;
        notifyDataSetChanged();
    }

    public void setStoreCodeData(List<StoreConditionCodeData> list) {
        mStoreCodeList = list;
        notifyDataSetChanged();
    }

    public void setClickListener(IOnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_item)
        TextView mTextViewItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.textView_item)
        public void onClick(View view) {
            if (mListener != null)
                mListener.onClick(view);
        }
    }
}