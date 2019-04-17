package weshare.groupfour.derek.InsCourse;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;

public class CourseLike {
    public void addCourseLike(String memId, String inscId){
        String reqData = "action=add_to_favorites&memId="+memId+"&inscId="+inscId;
        new CallServlet().execute(ServerURL.IP_COURSELIKE,reqData);
    }

    public void deleteCourseLike(String memId, String inscId){
        String reqData = "action=delete_from_favorites&memId="+memId+"&inscId="+inscId;
        new CallServlet().execute(ServerURL.IP_COURSELIKE,reqData);
    }

    public List<InsCourseVO> getMyLikeCourse(String memId){
            String result = null;
            try {
                String requestData = "action=look_my_favorites&memId="+memId;
                result = new CallServlet().execute(ServerURL.IP_COURSELIKE,requestData).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Type listType = new TypeToken<List<InsCourseVO>>(){}.getType();
            Gson gson = new Gson();
            return gson.fromJson(result,listType);
    }

}
