package com.andalus.a_zmedicines.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.andalus.a_zmedicines.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.andalus.a_zmedicines.MyClasses.Drug;

public class DrugListAdapter extends RecyclerView.Adapter<DrugListAdapter.DrugViewHolder> {

    private drugOnClickHandler myDrugClickHandler;
    private List<Drug> myDrugs = new ArrayList<>();
    private Context mContext;


    public DrugListAdapter(drugOnClickHandler myDrugClickHandler) {

        this.myDrugClickHandler = myDrugClickHandler;
    }

    @NonNull
    @Override
    public DrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layout = R.layout.drug_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(layout, parent, false);


        return new DrugViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DrugViewHolder holder, int position) {
        Log.i("hassan", "The drugs in adapter size is : " + myDrugs.size());
        Drug drug = myDrugs.get(position);
        holder.drugTitleTextView.setText(drug.getDrug_name());
        holder.drugDescTextView.setText(drug.getDesc());


        Picasso.get().load(drug.getFullImageUrl()).into(holder.drugImageView);
    }

    @Override
    public int getItemCount() {
        return myDrugs.size();
    }


    public interface drugOnClickHandler {
        void onClick(Drug drug);
    }

    class DrugViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView drugImageView;
        private TextView drugTitleTextView;
        private TextView drugDescTextView;


        public DrugViewHolder(@NonNull View itemView) {
            super(itemView);

            drugImageView = itemView.findViewById(R.id.drugListItem_DrugImage_ImageView);
            drugTitleTextView = itemView.findViewById(R.id.drugListItem_DrugName_TextView);
            drugDescTextView = itemView.findViewById(R.id.drugListItem_Desc_TextView);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Drug drug = myDrugs.get(pos);
            myDrugClickHandler.onClick(drug);

        }
    }

    public void setData(List<Drug> drugs) {
        this.myDrugs = drugs;
        Log.i("hassan", "The drugs in adapter setted size is : " + myDrugs.size());
    }

    public void setListener(drugOnClickHandler handler) {
        this.myDrugClickHandler = handler;
    }
}
