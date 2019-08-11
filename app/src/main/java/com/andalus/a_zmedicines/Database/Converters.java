package com.andalus.a_zmedicines.Database;


import android.arch.persistence.room.TypeConverter;
import android.util.Log;


import java.util.ArrayList;

/**
 * This class is used to save the interactions array
 * as a string in the database
 */
public class Converters {
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        String[] x = value.split(",");
        ArrayList<String> result = new ArrayList<>();
        if (x != null) {
            for (String n : x) {
                result.add(n);
            }
        }

        return result;
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        String x = "";
        if (list != null) {
            for (String x1 : list) {
                x = x + "," + x1;
            }
            x = x.substring(1);
            Log.i("hassan", "The sub string is : " + x);
        }

        return x;
    }
}

