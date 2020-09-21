package com.demo.nspl.restaurantlite.SessionManager;

import android.content.SharedPreferences;

public class SharedPrefClass {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static String PREF_NAME = "MyAndroidApp";
    private static int PRIVATE_MODE = 1;

    private static String KEY_UNAME = "KEY_UNAME";

//    @SuppressLint("WrongConstant")
//    public SharedPrefClass(Context context) {
//        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
//        editor = pref.edit();
//        editor.apply();
//    }

    public void setKeyUname(String pname) {
        editor.putString(KEY_UNAME, pname);
        editor.commit();
    }

    public String getKeyUname() {
        return pref.getString(KEY_UNAME, "");
    }

}
