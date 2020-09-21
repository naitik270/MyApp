package com.demo.nspl.restaurantlite.Global;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import io.requery.android.database.sqlite.SQLiteDatabase;
import io.requery.android.database.sqlite.SQLiteOpenHelper;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Database_Name;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASEVERSION = 1;
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static DatabaseHelper mInstance;
    private SQLiteDatabase mDatabase;

    private DatabaseHelper(Context context) {
        super(context, Database_Name, null, DATABASEVERSION);
        this.context = context;
    }

    /**
     * Get default instance of the class to keep it a singleton
     *
     * @param context the application context
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }


    /**
     * Create Tables.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("Check", "onCreate Tables call");
        // CreateTables in fTouchRetail Database.
        new CreateTables().create_tables(db, context);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.e("Check", "oldVersion: " + oldVersion);
        Log.e("Check", "newVersion: " + newVersion);
        // Example for upgrading database.
//        if (oldVersion < 2) {
//           Log.e("Check", "newVersion < 2: " + newVersion) ;
//           db.execSQL("ALTER TABLE [tbl_Terms] ADD [NewColumn2] INTEGER");
//        }
//
//        if (oldVersion < 3) {
//            Log.e("Check", "newVersion < 3: " + newVersion) ;
//            db.execSQL("ALTER TABLE [tbl_Terms] ADD [NewColumn3] INTEGER");
//        }



    }


    /**
     * Open Database.
     */
    public synchronized SQLiteDatabase openDatabase() {
        if (mInstance != null && mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = this.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * Close Database.
     */
    public synchronized void closeDatabase() {
        if (mInstance != null && mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            this.close();
        }
    }
}
