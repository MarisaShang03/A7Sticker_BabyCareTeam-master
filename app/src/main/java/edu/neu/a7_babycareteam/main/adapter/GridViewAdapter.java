package edu.neu.a7_babycareteam.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import edu.neu.a7_babycareteam.R;
import edu.neu.a7_babycareteam.model.Sticker;

public class GridViewAdapter<T> extends BaseAdapter {
    private Context mContext;
    private ArrayList<Sticker> mData;
    private int selectorPosition;
    private TextView iconTv;

    public GridViewAdapter(Context context, ArrayList<Sticker> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_grid_sticker, null);
        ConstraintLayout stickerBg = (ConstraintLayout) convertView.findViewById(R.id.iconBg);
        iconTv = convertView.findViewById(R.id.sticker_txt);
        int sentTimes = mData.get(position).getSentTimes();
        iconTv.setText("sent " + sentTimes + " times");

        ImageView iconIv = convertView.findViewById(R.id.sticker_img);
        iconIv.setImageResource(mData.get(position).getStickerId());

        if (selectorPosition == position) {
            stickerBg.setBackgroundResource(R.drawable.sticker_bg);
        }
        return convertView;
    }

    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();
    }

}