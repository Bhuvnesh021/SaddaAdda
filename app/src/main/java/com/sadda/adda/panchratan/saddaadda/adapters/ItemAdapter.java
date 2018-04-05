package com.sadda.adda.panchratan.saddaadda.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sadda.adda.panchratan.saddaadda.R;
import com.sadda.adda.panchratan.saddaadda.objects.Item;
import com.sadda.adda.panchratan.saddaadda.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 22-07-2017.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private static final String TAG = "ItemAdapter";
    private List<Item> itemArrayList;

    public ItemAdapter(List<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_itemName, txt_quantity, txt_price,txt_added_names, txt_comment_user_name, txt_comment_date_time;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_itemName = (TextView) itemView.findViewById(R.id.txt_cv_item_name);
            txt_quantity = (TextView) itemView.findViewById(R.id.txt_cv_item_quantity);
            txt_price = (TextView) itemView.findViewById(R.id.txt_cv_item_price);
            txt_added_names = (TextView) itemView.findViewById(R.id.txt_cv_added_names);
            txt_comment_user_name = (TextView) itemView.findViewById(R.id.txt_comment_user_name);
            txt_comment_date_time = (TextView) itemView.findViewById(R.id.txt_comment_date_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = itemArrayList.get(position);
//        Log.i(TAG, "onBindViewHolder: Data Received: userName: " + item.getUserName() + "\n comment: " + item.getComment() + " \n dateAndTime: " + item.getTimeAndDate());
        holder.txt_itemName.setText(item.getItemName());
        holder.txt_quantity.setText(item.getQuantity());
//        String formatedTime = Utils.convertGMTToIST(item.getTimeAndDate());
        holder.txt_price.setText(item.getPrice());
        holder.txt_added_names.setText(item.getNames());
        holder.txt_comment_user_name.setText(item.getUserName());
        String formatedDate = Utils.convertGMTToIST(item.getDate());
        holder.txt_comment_date_time.setText(formatedDate);
    }


    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}
