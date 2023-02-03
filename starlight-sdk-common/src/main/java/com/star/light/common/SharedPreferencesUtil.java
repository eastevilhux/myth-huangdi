package com.star.light.common;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtil {

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "kantucai_sp";


    public static String getString(Context context,String key,String defVlaue){
        Object obj = getData(context,key,defVlaue);
        if(obj != null){
            return String.valueOf(obj);
        }else{
            return defVlaue;
        }
    }

    public static String getString(Context context,String key){
        return getString(context,key,"");
    }

    public static int getInt(Context context,String key,int defValue){
        Object obj = getData(context,key,defValue);
        if(obj != null){
            try {
                return Integer.parseInt(obj.toString());
            }catch (Exception e){
                return defValue;
            }
        }else{
            return defValue;
        }
    }

    public static boolean getBoolean(Context context,String key,boolean defValue){
        Object obj = getData(context,key,defValue);
        if(obj != null){
            try{
                return Boolean.parseBoolean(obj.toString());
            }catch (Exception e){
                return defValue;
            }
        }else{
            return defValue;
        }
    }

    public static boolean getBoolean(Context context,String key){
        return getBoolean(context,key,false);
    }

    public static int getInt(Context context,String key){
        return getInt(context,key,0);
    }


    public static void putData(Context context , String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }
        editor.commit();
    }

    private static Object getData(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }

        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }

        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }

        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }

        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }


    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }


    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
}
