package weshare.groupfour.derek.insCourse;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.util.Tools;

public class CourseLike {
    public void addCourseLike(String memId, String inscId, Context context){
        String reqData = "action=add_to_favorites&memId="+memId+"&inscId="+inscId;
        new CallServlet(context).execute(ServerURL.IP_COURSELIKE,reqData);
        SharedPreferences spf = Tools.getSharePreAccount();
        Set<String> inscLikes = spf.getStringSet("inscLikes",null);
        inscLikes.add(inscId);
        spf.edit().putStringSet("inscLikes",inscLikes).apply();
    }

    public void deleteCourseLike(String memId, String inscId, Context context){
        String reqData = "action=delete_from_favorites&memId="+memId+"&inscId="+inscId;
        new CallServlet(context).execute(ServerURL.IP_COURSELIKE,reqData);
        SharedPreferences spf = Tools.getSharePreAccount();
        Set<String> inscLikes = spf.getStringSet("inscLikes",null);
        inscLikes.remove(inscId);
        spf.edit().putStringSet("inscLikes",inscLikes).apply();
    }

    public List<InsCourseVO> getMyLikeCourse(String memId, Context context){
            String result = null;
            try {
                String requestData = "action=look_my_favorites&memId="+memId;
                result = new CallServlet(context).execute(ServerURL.IP_COURSELIKE,requestData).get();
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
