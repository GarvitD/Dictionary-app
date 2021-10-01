package com.garvitd.dictionaryapp;


import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

     Context context;
     List<HistoryModel> historyModelList;
     DatabaseReference databaseReferenceHistoryAdapter;
     FirebaseAuth mAuth;

    public HistoryAdapter(Context context, List<HistoryModel> historyModelList) {
        this.context = context;
        this.historyModelList = historyModelList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.suggestions,parent,false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        
        HistoryModel historyModel = historyModelList.get(position);
        holder.textViewHistory.setText(historyModel.historyWord);
        holder.historyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entered_text = holder.textViewHistory.getText().toString();
                Intent intent= new Intent(context,word_meaning.class);
                intent.putExtra(Intent.EXTRA_TEXT,entered_text);
                context.startActivity(intent);
            }
        });
        holder.delete_history_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth= FirebaseAuth.getInstance();
                first_screen first_screenInstance = new first_screen();
                databaseReferenceHistoryAdapter = FirebaseDatabase.getInstance().getReference("history").child(first_screenInstance.checkId(mAuth.getCurrentUser().getEmail()));
                Query query = databaseReferenceHistoryAdapter.orderByChild("historyWord").equalTo(historyModel.historyWord);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            dataSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return historyModelList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView textViewHistory;
        View historyItem;
        ImageView delete_history_item;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHistory = itemView.findViewById(R.id.textview_history_item);
            historyItem = itemView.findViewById(R.id.historyItem);
            delete_history_item=itemView.findViewById(R.id.delete_history_tem);
        }
    }
}
