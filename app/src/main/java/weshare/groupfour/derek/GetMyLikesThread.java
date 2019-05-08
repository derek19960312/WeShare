package weshare.groupfour.derek;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import weshare.groupfour.derek.goods.GoodsLike;
import weshare.groupfour.derek.goods.GoodsVO;
import weshare.groupfour.derek.insCourse.CourseLike;
import weshare.groupfour.derek.insCourse.InsCourseVO;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Tools;

public class GetMyLikesThread extends Thread {

    public void run(){

        SharedPreferences spf = Tools.getSharePreAccount();
        String memId = spf.getString("memId",null);

        List<InsCourseVO> insCourseVOListbylike = new CourseLike().getMyLikeCourse(memId,Holder.getContext());
        Set<String> inscLikes = new HashSet<>();
        for (InsCourseVO inscVO :insCourseVOListbylike){
            inscLikes.add(inscVO.getInscId());
        }
        spf.edit().putStringSet("inscLikes",inscLikes).apply();


        List<GoodsVO> GoodsVOListbylike = new GoodsLike().getMyLikeGoods(memId, Holder.getContext());
        Set<String> goodsLikes = new HashSet<>();
        for (GoodsVO gvo :GoodsVOListbylike){
            goodsLikes.add(gvo.getGoodId());
        }
        spf.edit().putStringSet("goodsLikes",goodsLikes).apply();
    }

}
