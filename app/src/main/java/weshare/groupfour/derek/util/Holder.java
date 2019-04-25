package weshare.groupfour.derek.util;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weshare.groupfour.derek.goods.GoodsVO;

public class Holder {
    private static Context applicationContext;
    private static Map<GoodsVO,Integer> cart = new HashMap<>();
    private static ImageView imageView;
    public static void initial(Context context){
        applicationContext = context;
    }

    public static Context getContext(){
        return applicationContext;
    }

    public static Map<GoodsVO,Integer> getCart(){
        return cart;
    }

    public static ImageView getMemImgV(){
        imageView = new ImageView(applicationContext);
        imageView.setTag(new Integer(0));
        return imageView;
    }
}

