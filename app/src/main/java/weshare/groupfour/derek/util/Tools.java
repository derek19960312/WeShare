package weshare.groupfour.derek.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Tools {

    public static SharedPreferences getSharePreAccount(){
        return Holder.getContext().getSharedPreferences("myAccount", Context.MODE_PRIVATE);
    }

    public static String RequestDataBuilder(Map<String,String> request){
        StringBuilder sb = new StringBuilder();
        if(request.size() != 0){
            Set<Map.Entry<String,String>> sme = request.entrySet();
            Iterator<Map.Entry<String,String>> it = sme.iterator();
            while(it.hasNext()){
                Map.Entry<String, String> me = it.next();
                sb.append("&"+me.getKey()+"="+me.getValue());
            }
        }
        return sb.toString();
    }


    public static void Toast(Context context,String content){
        Toast.makeText(context,content, Toast.LENGTH_SHORT).show();
    }


    public static Bitmap getBitmapByBase64(String base64){
        byte[] bPic = null;
        if(base64 != null){
            bPic = Base64.decode(base64,Base64.DEFAULT);
        }
        return BitmapFactory.decodeByteArray(bPic,0,bPic.length);
    }

    public static String getBase64ByBitMap(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
    }


    // check if the device connect to the network
    public static boolean networkConnected(Activity activity) {

        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

}
