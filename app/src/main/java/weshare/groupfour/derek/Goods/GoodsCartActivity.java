package weshare.groupfour.derek.Goods;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.Goods.GoodsVO;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Tools;

public class GoodsCartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_cart);

        Toolbar tbGoodsCart = findViewById(R.id.tbGoodsCart);
        RecyclerView rvCart = findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(this));


        List<String> cart_goodsId = Holder.getCart();
        List<GoodsVO> goodsVOS = new ArrayList<>();
        Gson gson = new Gson();
        for(String goodsId : cart_goodsId){
            Map<String,String> request = new HashMap<>();
            request.put("action","get_one_by_Id");
            request.put("goodsId",goodsId);
            String requestData = Tools.RequestDataBuilder(request);
            try {
                String result = new CallServlet().execute(ServerURL.IP_GOODS,requestData).get();
                GoodsVO goodsVO = gson.fromJson(result,GoodsVO.class);
                goodsVOS.add(goodsVO);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        rvCart.setAdapter(new GoodsCartAdapter(goodsVOS));


        //設定寬高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.2f;
        win.setAttributes(lp);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.cart_close_menu, menu);
        return true;
    }


    private class GoodsCartAdapter extends RecyclerView.Adapter<GoodsCartAdapter.ViewHolder> {
        List goodsVOs;

        public GoodsCartAdapter(List goodsVOs) {
            this.goodsVOs = goodsVOs;
        }

        private class ViewHolder extends RecyclerView.ViewHolder{


            public ViewHolder(View item) {
                super(item);
            }
        }

        @Override
        public GoodsCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_goods_cart,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GoodsCartAdapter.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return goodsVOs.size();
        }


    }
}
