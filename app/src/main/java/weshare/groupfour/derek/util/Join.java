package weshare.groupfour.derek.util;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.MemberVO;

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

    public byte[] getMemberPic(String memId){
        String action = "action=get_member_pic";
        String requestData = action+"&memId="+memId;
        String result;
        byte[] b = null;
        try {
            result = new CallServlet().execute(ServerURL.IP_GET_PIC,requestData).get();
            if(result != null){
                b = Base64.decode(result,Base64.DEFAULT);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return b;
    }


}
