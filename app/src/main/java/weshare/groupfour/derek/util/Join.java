package weshare.groupfour.derek.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.CallServlet_Pic;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.member.MemberVO;

public class Join {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public MemberVO getMemberbyteacherId(String teacherId, Context context){
        String action = "action=get_one_by_teacherId";
        String requestData = action+"&teacherId="+teacherId;

        String result = null;
        try {
            result = new CallServlet(context).execute(ServerURL.IP_MEMBER,requestData).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return gson.fromJson(result,MemberVO.class);
    }
    public MemberVO getMemberbyMemId(String memId, Context context){
        String action = "action=get_one_by_memId";
        String requestData = action+"&memId="+memId;

        String result = null;
        try {
            result = new CallServlet(context).execute(ServerURL.IP_MEMBER,requestData).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return gson.fromJson(result,MemberVO.class);
    }



    public static void setPicOn(ImageView imageView, String id){
        Map<String,String> requestMap = new HashMap<>();
        if(id.contains("GD")){
            requestMap.put("action","get_goods_pic");
            requestMap.put("goodId",id);
        }else{
            requestMap.put("action","get_member_pic");
            requestMap.put("memId",id);
        }
        int imageSize = Holder.getContext().getResources().getDisplayMetrics().widthPixels/3;
        requestMap.put("imageSize",String.valueOf(imageSize));
        String request = Tools.RequestDataBuilder(requestMap);

        new CallServlet_Pic(imageView).execute(ServerURL.IP_GET_PIC,request);

    }



}
