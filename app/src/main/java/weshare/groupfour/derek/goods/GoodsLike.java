package weshare.groupfour.derek.goods;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Tools;

public class GoodsLike {
    public void addGoodsLike(String memId, String goodId, Context context){
        String reqData = "action=add_to_favorites&memId="+memId+"&goodId="+goodId;
        new CallServlet(context).execute(ServerURL.IP_GOODSLIKE,reqData);
        SharedPreferences spf = Tools.getSharePreAccount();
        Set<String> goodsLikes = spf.getStringSet("goodsLikes",null);
        goodsLikes.add(goodId);
        spf.edit().putStringSet("goodsLikes",goodsLikes).apply();
    }

    public void deleteGoodsLike(String memId, String goodId, Context context){
        String reqData = "action=delete_from_favorites&memId="+memId+"&goodId="+goodId;
        new CallServlet(context).execute(ServerURL.IP_GOODSLIKE,reqData);
        SharedPreferences spf = Tools.getSharePreAccount();
        Set<String> goodsLikes = spf.getStringSet("goodsLikes",null);
        goodsLikes.remove(goodId);
        spf.edit().putStringSet("goodsLikes",goodsLikes).apply();
    }

    public List<GoodsVO> getMyLikeGoods(String memId , Context context){
            String result = null;
            try {
                String requestData = "action=look_my_favorites&memId="+memId;

                result = new CallServlet(context).execute(ServerURL.IP_GOODSLIKE,requestData).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Type listType = new TypeToken<List<GoodsVO>>(){}.getType();
            return Holder.gson.fromJson(result,listType);
    }

}
