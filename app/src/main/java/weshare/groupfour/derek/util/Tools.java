package weshare.groupfour.derek.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;

public class Tools {

    public static SharedPreferences getSharePreAccount(){
        return ContextHolder.getContext().getSharedPreferences("myAccount", Context.MODE_PRIVATE);
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
        Toast.makeText(context,content, Toast.LENGTH_SHORT);
    }

}
