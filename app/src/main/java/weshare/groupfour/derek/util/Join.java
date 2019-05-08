package weshare.groupfour.derek.util;

import android.content.Context;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.CallServlet_Pic;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.member.MemberVO;

public class Join {


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
        return Holder.gson.fromJson(result,MemberVO.class);
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
        return Holder.gson.fromJson(result,MemberVO.class);
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
