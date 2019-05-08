package weshare.groupfour.derek.util;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import weshare.groupfour.derek.goods.GoodsVO;

public class Holder {
    private static Context applicationContext;
    private static Map<GoodsVO,Integer> cart = new HashMap<>();

    public static Gson gson =  new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .create();

    public static void initial(Context context){
        applicationContext = context;
    }

    public static Context getContext(){
        return applicationContext;
    }

    public static Map<GoodsVO,Integer> getCart(){
        return cart;
    }


}

