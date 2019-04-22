package weshare.groupfour.derek.goods;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Tools;

public class GoodsBrowseActivity extends AppCompatActivity {
    static StaggeredGridLayoutManager staggeredGridLayoutManager;
    static RecyclerView rvGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_browse);
        rvGoods = findViewById(R.id.rvGoods);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvGoods.setLayoutManager(staggeredGridLayoutManager);
        rvGoods.getBaseline();

        Gson gson = new Gson();
        Map<String, String> request = new HashMap<>();
        request.put("action", "get_all");
        String requestData = Tools.RequestDataBuilder(request);
        String result = null;
        try {
            result = new CallServlet().execute(ServerURL.IP_GOODS, requestData).get();
            Log.e("result", result);
            Type listType = new TypeToken<List<GoodsVO>>() {
            }.getType();
            List<GoodsVO> goodsVOList = gson.fromJson(result, listType);
            if (goodsVOList != null) {
                rvGoods.setAdapter(new GoodsAdapter(goodsVOList));
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        getMenuInflater().inflate(R.menu.style_change_menu, menu);
        return true;
    }

    public static void changeStyle(){
        switch (staggeredGridLayoutManager.getSpanCount()) {
            case 1:
                staggeredGridLayoutManager.setSpanCount(2);
                break;
            case 2:
                staggeredGridLayoutManager.setSpanCount(1);
                break;
        }
        rvGoods.getBaseline();
    }

}
