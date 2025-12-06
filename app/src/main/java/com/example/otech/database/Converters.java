package com.example.otech.database;

import androidx.room.TypeConverter;

import com.example.otech.model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class Converters {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<String> fromStringList(String value) {
        if (value == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }

    @TypeConverter
    public static ArrayList<CartItem> fromCartItemList(String value) {
        if (value == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<CartItem>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromCartItemArrayList(ArrayList<CartItem> list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static java.util.Map<String, Integer> fromStringIntMap(String value) {
        if (value == null) {
            return new java.util.HashMap<>();
        }
        Type mapType = new TypeToken<java.util.Map<String, Integer>>() {}.getType();
        return gson.fromJson(value, mapType);
    }

    @TypeConverter
    public static String fromMap(java.util.Map<String, Integer> map) {
        if (map == null) {
            return null;
        }
        return gson.toJson(map);
    }
}
