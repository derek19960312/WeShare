package weshare.groupfour.derek.util;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Holder {
    private static Context applicationContext;
    private static List<String> cart = new ArrayList<>();
    public static void initial(Context context){
        applicationContext = context;
    }

    public static Context getContext(){
        return applicationContext;
    }

    public static List<String> getCart(){
        return cart;
    }
}

