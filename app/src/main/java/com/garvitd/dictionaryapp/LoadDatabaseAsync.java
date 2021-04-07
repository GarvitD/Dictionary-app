//This file is useless i created it earlier when i was using a database but now i am using an api.
//I didn't delete so that there would no problems arising due to difference in files in my project and the github repository

/*package com.garvitd.dictionaryapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;



import java.io.IOException;

public class LoadDatabaseAsync  extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private AlertDialog alertDialog;
    private DatabaseHelper myDbhelper;

    public LoadDatabaseAsync(Context context) {

        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        //TODO Auto-generated method stub

        super.onPreExecute();

        AlertDialog.Builder d = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        LayoutInflater Inflater = LayoutInflater.from(context);
        View dialogView = Inflater.inflate(R.layout.alert_dialog, null);
        d.setTitle("Just a sec..");
        d.setView(dialogView);
        alertDialog = d.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        myDbhelper = new DatabaseHelper(context);

        try {
            myDbhelper.createDatabase();
        } catch (IOException e) {
            throw new Error("Database was not Created");
        }

        myDbhelper.close();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        alertDialog.dismiss();
        first_screen.openDatabase();
    }
}*/
