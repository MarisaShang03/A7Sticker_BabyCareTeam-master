package edu.neu.a7_babycareteam.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import edu.neu.a7_babycareteam.R;
import edu.neu.a7_babycareteam.main.ItemClickListener;
import edu.neu.a7_babycareteam.model.FriendItem;


public class FriendsListViewAdapter extends RecyclerView.Adapter<FriendsListViewAdapter.RecyclerViewHolder> {

    private final ArrayList<FriendItem> itemList;
    private ItemClickListener listener;
    private int selectorPosition;
    private RecyclerView mRv;

    public FriendsListViewAdapter(ArrayList<FriendItem> itemList,  int selectorPosition, RecyclerView rv) {
        this.itemList = itemList;
        this.mRv = rv;
        this.selectorPosition = selectorPosition;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_friend_item, parent, false);
        return new RecyclerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        FriendItem currentItem = itemList.get(position);
        holder.friendNameTv.setText(currentItem.getFriendName());
        holder.ivSelect.setSelected(itemList.get(position).isSelected());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewHolder viewHolder = (RecyclerViewHolder) mRv.findViewHolderForLayoutPosition(selectorPosition);
                if (viewHolder != null) {
                    viewHolder.ivSelect.setSelected(false);
                } else {
                    notifyItemChanged(selectorPosition);
                }
                itemList.get(selectorPosition).setSelected(false);
                selectorPosition = position;
                itemList.get(selectorPosition).setSelected(true);
                holder.ivSelect.setSelected(true);

                if (listener != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        String friendName = (String) holder.friendNameTv.getText();
                        listener.onItemClick(v.getContext(), friendName, position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView friendNameTv;
        public ImageView ivSelect;

        public RecyclerViewHolder(View itemView, final ItemClickListener listener) {
            super(itemView);
            friendNameTv = itemView.findViewById(R.id.friend_name);
            ivSelect = (ImageView) itemView.findViewById(R.id.iv_item_select);

        }
    }
}
