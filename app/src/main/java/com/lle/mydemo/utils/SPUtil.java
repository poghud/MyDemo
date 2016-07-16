package com.lle.mydemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("unused")
public class SPUtil {
    @SuppressWarnings("FieldCanBeLocal")
    private static String SPName = "xxx_cache";
    private static SharedPreferences mSp;

    private static SharedPreferences getSP(Context context) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        }
        return mSp;
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSP(context).getBoolean(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSP(context).edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        return getSP(context).getString(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        getSP(context).edit().putString(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSP(context).getInt(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        getSP(context).edit().putInt(key, value).commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        return getSP(context).getLong(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        getSP(context).edit().putLong(key, value).commit();
    }

}
