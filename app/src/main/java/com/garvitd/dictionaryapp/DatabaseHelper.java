//This file is useless i created it earlier when i was using a database but now i am using an api.
//I didn't delete so that there would no problems arising due to difference in files in my project and the github repository

/*package com.garvitd.dictionaryapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class DatabaseHelper extends SQLiteOpenHelper {

    private String DB_PATH = null;
    private static String DB_NAME = "eng_dictionary.db";
    private SQLiteDatabase myDatabase;
    private final Context mycontext;
    
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mycontext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.e("Path 1", DB_PATH);
    }

    public void createDatabase() throws IOException {

        boolean dbExist = checkDatabase();

        if (!dbExist) {

            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error Copying Database");
            }

        }

    }

    public boolean checkDatabase() {

        SQLiteDatabase checkDB = null;

        try {

            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        } catch (SQLiteException e) {


        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;


    }

    private void copyDatabase() throws IOException {

        InputStream myInput = mycontext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer)) > 0) {

            myOutput.write(buffer, 0, length);

        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

        Log.i("copyDatabase","Database Copied");
    }







    public void openDatabase() throws SQLException {

        String mypath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if (myDatabase != null) {
            myDatabase.close();
        }

        super.close();


    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {

            this.getReadableDatabase();
            mycontext.deleteDatabase(DB_NAME);
            copyDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Cursor getMeaning(String text)
    {
        SQLiteDatabase myDatabase= this.getReadableDatabase();
        Cursor c= myDatabase.rawQuery("SELECT en_definition,example FROM words WHERE en_word==UPPER('"+text+"')",null);
        return c;
    }

    public Cursor getSuggestions(String text)
    {
        SQLiteDatabase myDatabase= this.getReadableDatabase();
        Cursor c= myDatabase.rawQuery("SELECT _id, en_word FROM words WHERE en_word LIKE '"+text+"%' LIMIT 40",null);
        return c;
    }

    /*public void insertHistory(String text){

        myDatabase.execSQL("INSERT INTO history(word) VALUES (UPPER ('"+text+"'))");

    }

    public Cursor getHistory() {

        Cursor c= myDatabase.rawQuery("select distinct  word, en_definition from history h join words w on h.word==w.en_word order by h._id desc",null);
        return c;
    }

    public void  deleteHistory() {

        myDatabase.execSQL("DELETE  FROM history");
    }

}*/
