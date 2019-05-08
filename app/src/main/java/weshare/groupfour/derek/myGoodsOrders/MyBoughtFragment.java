package weshare.groupfour.derek.myGoodsOrders;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.util.Holder;


public class MyBoughtFragment extends Fragment {
    TextView tvNoData;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bought, container, false);

        RecyclerView rvMyBoughtGood = view.findViewById(R.id.rvMyBoughtGood);
        rvMyBoughtGood.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        SharedPreferences spf = getActivity().getSharedPreferences("myAccount", Context.MODE_PRIVATE);
        String memId = spf.getString("memId", null);
        String resquestData = "action=find_my_good_by_memId&memId=" + memId;
        try {
            String result = new CallServlet(getContext()).execute(ServerURL.IP_GOODSORDER, resquestData).get();
            Type listType = new TypeToken<List<GoodsOrderVO>>() {
            }.getType();
            List<GoodsOrderVO> myGoodsOrderRvList = Holder.gson.fromJson(result, listType);
            if (myGoodsOrderRvList != null && myGoodsOrderRvList.size() != 0) {
                rvMyBoughtGood.setAdapter(new MyGoodsAdapter(myGoodsOrderRvList, MyGoodsAdapter.BUYER,getContext()));
            } else {
                tvNoData = view.findViewById(R.id.tvNoData);
                tvNoData.setVisibility(View.VISIBLE);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }

}
