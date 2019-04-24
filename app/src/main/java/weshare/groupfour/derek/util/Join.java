package weshare.groupfour.derek.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

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

    public MemberVO getMemberbyteacherId(String teacherId){
        String action = "action=get_one_by_teacherId";
        String requestData = action+"&teacherId="+teacherId;

        String result = null;
        try {
            result = new CallServlet().execute(ServerURL.IP_MEMBER,requestData).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return gson.fromJson(result,MemberVO.class);
    }
    public MemberVO getMemberbyMemId(String memId){
        String action = "action=get_one_by_memId";
        String requestData = action+"&memId="+memId;

        String result = null;
        try {
            result = new CallServlet().execute(ServerURL.IP_MEMBER,requestData).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return gson.fromJson(result,MemberVO.class);
    }

    public byte[] getMemberPic(Context context,String memId){
        byte[] bPic = null;
        String base64 = getMemberPicB64(context,memId);
        if(base64 != null){
            bPic = Base64.decode(base64,Base64.DEFAULT);
        }
        return bPic;
    }

    public String getMemberPicB64(Context context,String memId){
        String action = "action=get_member_pic";
        int imageSize = Holder.getContext().getResources().getDisplayMetrics().widthPixels/3;
        String requestData = action+"&memId="+memId+"&imageSize="+imageSize;
        String base64 = null;
        try {
            base64 = new CallServlet_Pic(context).execute(ServerURL.IP_GET_PIC,requestData).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return base64;
    }


    public String getGoodsPicB64(Context context,String goodId){
        String action = "action=get_goods_pic";
        int imageSize = Holder.getContext().getResources().getDisplayMetrics().widthPixels/3;
        String requestData = action+"&goodId="+goodId+"&imageSize="+imageSize;
        String base64 = null;
        try {
            base64 = new CallServlet_Pic(context).execute(ServerURL.IP_GET_PIC,requestData).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return base64;
    }




}
