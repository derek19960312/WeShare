package weshare.groupfour.derek.goods;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;

public class GoodsLike {
    public void addGoodsLike(String memId, String goodId){
        String reqData = "action=add_to_favorites&memId="+memId+"&goodId="+goodId;
        new CallServlet().execute(ServerURL.IP_GOODSLIKE,reqData);
    }

    public void deleteGoodsLike(String memId, String goodId){
        String reqData = "action=delete_from_favorites&memId="+memId+"&goodId="+goodId;
        new CallServlet().execute(ServerURL.IP_GOODSLIKE,reqData);
    }

    public List<GoodsVO> getMyLikeGoods(String memId){
            String result = null;
            try {
                String requestData = "action=look_my_favorites&memId="+memId;

                result = new CallServlet().execute(ServerURL.IP_GOODSLIKE,requestData).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Type listType = new TypeToken<List<GoodsVO>>(){}.getType();
            Gson gson = new Gson();
            return gson.fromJson(result,listType);
    }

}
