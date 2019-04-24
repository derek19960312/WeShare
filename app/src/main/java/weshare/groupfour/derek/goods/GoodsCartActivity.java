package weshare.groupfour.derek.goods;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Tools;

public class GoodsCartActivity extends AppCompatActivity {
    TextView tvTotalPrice;

    Map<GoodsVO,Integer> myCart;
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
            rvCart.setAdapter(new GoodsCartAdapter(new ArrayList<GoodsVO>(myCart.keySet())));


            int total = 0;
            for(GoodsVO gvo : myCart.keySet()){
                total += myCart.get(gvo)*gvo.getGoodPrice();
            }
            tvTotalPrice.setText(String.valueOf(total));

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
        getMenuInflater().inflate(R.menu.close_menu, menu);
        return true;
    }


    private class GoodsCartAdapter extends RecyclerView.Adapter<GoodsCartAdapter.ViewHolder> {
        List<GoodsVO> goodsVOs;

        public GoodsCartAdapter(List<GoodsVO> goodsVOs) {
            this.goodsVOs = goodsVOs;
        }

        private class ViewHolder extends RecyclerView.ViewHolder{
            ImageView ivIcon, ivHeart;
            TextView tvName, tvPrice;
            Spinner spCount;
            int heart;
            Context context;

            public ViewHolder(View view) {
                super(view);
                ivIcon = view.findViewById(R.id.ivIcon);
                tvName = view.findViewById(R.id.tvName);
                tvPrice = view.findViewById(R.id.tvPrice);
                spCount = view.findViewById(R.id.spCount);
                ivHeart = view.findViewById(R.id.ivHeart);
                heart = 0;
                context = view.getContext();
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
            int count = myCart.get(goodsVO)-1;
            if(count > 8){
                count = 8;
            }
            holder.spCount.setSelection(count,true);

            holder.spCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position < 9 ){
                        myCart.put(goodsVO,++position);
                        int total = 0;
                        for(GoodsVO gvo : myCart.keySet()){
                            total += myCart.get(gvo)*gvo.getGoodPrice();
                        }
                        tvTotalPrice.setText(String.valueOf(total));
                    }else{

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //載入圖片
            holder.ivIcon.setImageBitmap(Tools.getBitmapByBase64(goodsVO.getGoodImg()));

            //收藏
            holder.ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences spf = Tools.getSharePreAccount();
                    String memId = spf.getString("memId",null);
                    switch (holder.heart) {
                        case 0:
                            holder.ivHeart.setImageResource(R.drawable.hearted);
                            new GoodsLike().addGoodsLike(memId,goodsVO.getGoodId());
                            Toast.makeText(holder.context, "已加入收藏", Toast.LENGTH_SHORT).show();
                            holder.heart = 1;
                            break;
                        case 1:
                            holder.ivHeart.setImageResource(R.drawable.heart);
                            new GoodsLike().deleteGoodsLike(memId,goodsVO.getGoodId());
                            Toast.makeText(holder.context, "已取消收藏", Toast.LENGTH_SHORT).show();
                            holder.heart = 0;
                            break;
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return goodsVOs.size();
        }


    }



}
