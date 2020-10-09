package com.skripsi.greenlab;

import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Modul {
    public static String getDate(String string2) {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat(string2).format(calendar.getTime());
    }
    public static int strToInt(String string2) {
        try {
            int n = Integer.parseInt(string2);
            return n;
        }
        catch (Exception exception) {
            return 0;
        }
    }
    public static String removeMinus(String string2) {
        try {
            String string3 = string2.replace("-", "");
            return string3;
        }
        catch (Exception exception) {
            return "";
        }
    }
    public static String setRight(String string2) {
        int n = string2.length();
        String string3 = "";
        for (int i = 0; i < 32 - n; ++i) {
            if (31 - n == i) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(string3);
                stringBuilder.append(string2);
                string3 = stringBuilder.toString();
                continue;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string3);
            stringBuilder.append(" ");
            string3 = stringBuilder.toString();
        }
        return string3;
    }
    public static int getCount(Cursor cursor) {
        try {
            int n = cursor.getCount();
            return n;
        }
        catch (Exception exception) {
            return 0;
        }
    }

    public static String getString(Cursor cursor, String string2) {
        try {
            String string3 = cursor.getString(cursor.getColumnIndex(string2));
            return string3;
        }
        catch (Exception exception) {
            return "";
        }
    }
    public static String dateToNormal(String string2) {
        try {
            String string3 = string2.substring(4);
            String string4 = string3.substring(2);
            String string5 = string3.substring(0, 2);
            String string6 = string4.substring(0, 2);
            String string7 = string2.substring(0, 4);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string6);
            stringBuilder.append("/");
            stringBuilder.append(string5);
            stringBuilder.append("/");
            stringBuilder.append(string7);
            String string8 = stringBuilder.toString();
            return string8;
        }
        catch (Exception exception) {
            return "ini tanggal";
        }
    }

    public static String setDatePickerNormal(int n, int n2, int n3) {
        String string2;
        String string3;
        if (n2 < 10) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("0");
            stringBuilder.append(Modul.intToStr(n2));
            string3 = stringBuilder.toString();
        } else {
            string3 = Modul.intToStr(n2);
        }
        if (n3 < 10) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("0");
            stringBuilder.append(Modul.intToStr(n3));
            string2 = stringBuilder.toString();
        } else {
            string2 = Modul.intToStr(n3);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("/");
        stringBuilder.append(string3);
        stringBuilder.append("/");
        stringBuilder.append(Modul.intToStr(n));
        return stringBuilder.toString();
    }

    public static String intToStr(int n) {
        try {
            String string2 = String.valueOf((int)n);
            return string2;
        }
        catch (Exception exception) {
            return "";
        }
    }

    public static String convertDate(String string2) {
        String[] arrstring = string2.split("/");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(arrstring[2]);
        stringBuilder.append(arrstring[1]);
        stringBuilder.append(arrstring[0]);
        return stringBuilder.toString();
    }
}
