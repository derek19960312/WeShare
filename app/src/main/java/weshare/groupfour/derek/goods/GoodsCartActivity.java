package weshare.groupfour.derek.goods;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Join;
import weshare.groupfour.derek.util.Tools;

public class GoodsCartActivity extends AppCompatActivity {
    TextView tvTotalPrice;
    int totalPrice = 0;
    Map<String,Integer> myCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //購物車是否有東西
        if (Holder.getCart().size() != 0){
            setContentView(R.layout.activity_goods_cart);
            tvTotalPrice = findViewById(R.id.tvTotalPrice);


            Toolbar tbGoodsCart = findViewById(R.id.tbGoodsCart);
            setSupportActionBar(tbGoodsCart);
            RecyclerView rvCart = findViewById(R.id.rvCart);
            rvCart.setLayoutManager(new LinearLayoutManager(this));

            myCart = Holder.getCart();
            List<GoodsVO> goodsVOS = new ArrayList<>();
            Gson gson = new Gson();
            for(String goodsId : myCart.keySet()){
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

            //toolbar 事件
            tbGoodsCart.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    finish();
                    return true;
                }
            });

            //送出訂單
            Button btnBuy = findViewById(R.id.btnBuy);
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tools.Toast(GoodsCartActivity.this,"稍後為您處理訂單");
                }
            });

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("購物車空空如也")
                    .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.cart_close_menu, menu);
        return true;
    }


    private class GoodsCartAdapter extends RecyclerView.Adapter<GoodsCartAdapter.ViewHolder> {
        List<GoodsVO> goodsVOs;

        public GoodsCartAdapter(List goodsVOs) {
            this.goodsVOs = goodsVOs;
        }

        private class ViewHolder extends RecyclerView.ViewHolder{
            ImageView ivIcon;
            TextView tvName, tvPrice;
            Spinner spCount;
            int sumPrice;

            public ViewHolder(View view) {
                super(view);
                ivIcon = view.findViewById(R.id.ivIcon);
                tvName = view.findViewById(R.id.tvName);
                tvPrice = view.findViewById(R.id.tvPrice);
                spCount = view.findViewById(R.id.spCount);
                sumPrice = 0;
            }
        }

        @Override
        public GoodsCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_goods_cart,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final GoodsCartAdapter.ViewHolder holder, int position) {
            final GoodsVO goodsVO = goodsVOs.get(position);

            holder.tvName.setText(goodsVO.getGoodName());
            holder.tvPrice.setText(String.valueOf(goodsVO.getGoodPrice()));

            holder.sumPrice = goodsVO.getGoodPrice()*myCart.get(goodsVO.getGoodId());
            totalPrice += holder.sumPrice;
            tvTotalPrice.setText(String.valueOf(totalPrice));

            holder.spCount.setSelection(myCart.get(goodsVO.getGoodId())-1,true);

            holder.spCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int oldCount = myCart.get(goodsVO.getGoodId());
                    int newCount = ++position;
                    totalPrice += (newCount-oldCount)*goodsVO.getGoodPrice();
                    tvTotalPrice.setText(String.valueOf(totalPrice));
                    myCart.put(goodsVO.getGoodId(),newCount);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //載入圖片
            holder.ivIcon.setImageBitmap(new Join().getGoodsPicBitmap(goodsVO.getGoodId()));


        }

        @Override
        public int getItemCount() {
            return goodsVOs.size();
        }


    }



}
