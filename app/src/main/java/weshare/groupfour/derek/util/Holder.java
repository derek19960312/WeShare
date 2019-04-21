package weshare.groupfour.derek.util;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Holder {
    private static Context applicationContext;
    private static Map<String,Integer> cart = new HashMap<>();
    public static void initial(Context context){
        applicationContext = context;
    }

    public static Context getContext(){
        return applicationContext;
    }

    public static Map<String,Integer> getCart(){
        return cart;
    }
}

