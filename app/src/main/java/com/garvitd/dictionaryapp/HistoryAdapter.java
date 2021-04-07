package com.garvitd.dictionaryapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public HistoryAdapter(Context context, Cursor cursor){
        mContext=context;
        mCursor=cursor;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        public TextView nameText;

        public HistoryViewHolder( View itemView) {
            super(itemView);

            nameText= itemView.findViewById(R.id.textview_name_item);
        }
    }

    @Override
    public HistoryViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.suggestions,parent,false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder( HistoryViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME));

        holder.nameText.setText(name);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();

    }

    public void swapCursor(Cursor newCursor){
        if (mCursor != null){
            mCursor.close();
        }
        mCursor=newCursor;
        if(newCursor != null){
            notifyDataSetChanged();
        }
    }


}
