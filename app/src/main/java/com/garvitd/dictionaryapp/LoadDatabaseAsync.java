package com.garvitd.dictionaryapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;



import java.io.IOException;

public class LoadDatabaseAsync  extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private AlertDialog alertDialog;
    private DatabaseHelper mydbhelper;

    public LoadDatabaseAsync(Context context) {

        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        AlertDialog.Builder d = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View dialogView = layoutInflater.inflate(R.layout.alert_dialog, null);
        d.setTitle("Loading Database");
        d.setView(dialogView);
        alertDialog = d.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        mydbhelper = new DatabaseHelper(context);

        try {
            mydbhelper.createDatabase();
        } catch (IOException e) {
            throw new Error("Database was not Created");
        }

        mydbhelper.close();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        alertDialog.dismiss();
        word_search.openDatabase();
    }
}
