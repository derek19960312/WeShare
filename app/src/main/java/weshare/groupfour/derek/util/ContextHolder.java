package weshare.groupfour.derek.util;

import android.app.Application;
import android.content.Context;

public class ContextHolder{
    private static Context applicationContext;

    public static void initial(Context context){
        applicationContext = context;
    }
    public static Context getContext(){
        return applicationContext;
    }
}

