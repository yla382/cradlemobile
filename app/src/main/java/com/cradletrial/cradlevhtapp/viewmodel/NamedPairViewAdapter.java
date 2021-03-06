package com.cradletrial.cradlevhtapp.viewmodel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.model.Settings;
import com.cradletrial.cradlevhtapp.utilitiles.DateUtil;
import com.cradletrial.cradlevhtapp.view.ui.settings.SettingNamedPairsActivity;

import java.util.List;

/**
 * Show a single named pair (settings) in the list
 * source: https://developer.android.com/guide/topics/ui/layout/recyclerview#java
 */
public class NamedPairViewAdapter extends RecyclerView.Adapter<NamedPairViewAdapter.MyViewHolder> {
    private List<Settings.NamedPair> data;
    private RecyclerView recyclerView;

    public interface OnClickElement {
        void onClick(Settings.NamedPair pair, int position);
        // Return true if click handled
        boolean onLongClick(Settings.NamedPair pair);
    }
    private OnClickElement onClickElementListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View itemView;
        public MyViewHolder(View v) {
            super(v);
            itemView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NamedPairViewAdapter(List<Settings.NamedPair> data) {
        this.data = data;
    }

    // Store ref to recycler
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NamedPairViewAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listelement_named_pair, parent, false);

        // on click
        v.setOnClickListener( (view)-> onClick(view));
        v.setOnLongClickListener( (view) -> onLongClick(view));

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    public void setOnClickElementListener(OnClickElement obs) {
        onClickElementListener = obs;
    }

    private void onClick(View view) {
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        Settings.NamedPair pair = data.get(itemPosition);
        if (onClickElementListener != null) {
            onClickElementListener.onClick(pair, itemPosition);
        }
    }
    private boolean onLongClick(View view) {
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        Settings.NamedPair pair = data.get(itemPosition);
        if (onClickElementListener != null) {
            return onClickElementListener.onLongClick(pair);
        }
        return false;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        View v = holder.itemView;
        Settings.NamedPair pair = data.get(position);

        // header
        TextView tvHeader = v.findViewById(R.id.tvName);
        tvHeader.setText(pair.name);

        // value
        TextView tvValue = v.findViewById(R.id.tvValue);
        tvValue.setText(pair.value);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}