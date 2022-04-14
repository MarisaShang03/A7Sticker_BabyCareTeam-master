package edu.neu.a7_babycareteam.history.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.a7_babycareteam.R;
import edu.neu.a7_babycareteam.main.ItemClickListener;
import edu.neu.a7_babycareteam.model.Message;


public class HistoryMsgListViewAdapter extends RecyclerView.Adapter<HistoryMsgListViewAdapter.RecyclerViewHolder> {

    private final ArrayList<Message> itemList;
    private ItemClickListener listener;
    private int selectorPosition = 0;

    public HistoryMsgListViewAdapter(ArrayList<Message> itemList) {
        this.itemList = itemList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_history_item, parent, false);
        return new RecyclerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Message msg = itemList.get(position);
        holder.msgTv.setText(msg.getSenderName() + " send a sticker " + msg.getStickerName() + " at " + msg.getSendTime());
        holder.stickIv.setImageResource(msg.getStickerID());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView msgTv;
        public ImageView stickIv;

        public RecyclerViewHolder(View itemView, final ItemClickListener listener) {
            super(itemView);
            msgTv = itemView.findViewById(R.id.msgTv);
            stickIv = (ImageView) itemView.findViewById(R.id.stickerIv);

        }
    }
}