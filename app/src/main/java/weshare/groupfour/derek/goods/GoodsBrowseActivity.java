package weshare.groupfour.derek.goods;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.RequestDataBuilder;

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

        RequestDataBuilder rdb = new RequestDataBuilder();
        rdb.build()
                .setAction("get_all");

        String result;
        try {
            result = new CallServlet(this).execute(ServerURL.IP_GOODS, rdb.create()).get();
            Log.e("result", result);
            Type listType = new TypeToken<List<GoodsVO>>() {
            }.getType();
            List<GoodsVO> goodsVOList = Holder.gson.fromJson(result, listType);
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

    public static void changeStyle(MenuItem item){
        switch (staggeredGridLayoutManager.getSpanCount()) {
            case 1:
                item.setIcon(R.drawable.list);
                staggeredGridLayoutManager.setSpanCount(2);
                break;
            case 2:
                item.setIcon(R.drawable.grid);
                staggeredGridLayoutManager.setSpanCount(1);
                break;
        }
    }

}
