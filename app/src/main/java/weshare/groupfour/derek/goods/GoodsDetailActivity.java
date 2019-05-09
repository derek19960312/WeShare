package weshare.groupfour.derek.goods;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.home.LoginFakeActivity;
import weshare.groupfour.derek.myGoodsOrders.GoodsOrderVO;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Join;
import weshare.groupfour.derek.util.Tools;

public class GoodsDetailActivity extends AppCompatActivity {
    ImageView ivPic;
    ImageView ivHeart;
    ImageView ivShare;
    TextView tvDetail;
    TextView tvName;
    TextView tvPrice;
    Button btnBuy;
    Button btnAddCart;
    int heart;
    Map<GoodsVO, Integer> singleBuy;
    int singlePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        findViews();
        GoodsVO goodsVO = (GoodsVO) getIntent().getExtras().getSerializable("goodsVO");
        Join.setPicOn(ivPic, goodsVO.getGoodId());

        singleBuy = new HashMap<>();
        singleBuy.put(goodsVO, 1);
        singlePrice = goodsVO.getGoodPrice();

        tvName.setText("商品名稱：  "+goodsVO.getGoodName());
        tvPrice.setText("價錢：  "+String.valueOf(singlePrice));
        tvDetail.setText("簡介：  "+goodsVO.getGoodInfo());
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.Toast(GoodsDetailActivity.this, "已分享");
            }
        });
        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences spf = Tools.getSharePreAccount();
                String memId = spf.getString("memId", null);
                if (memId != null) {
                    switch (heart) {
                        case 0:
                            ivHeart.setImageResource(R.drawable.hearted);
                            new GoodsLike().addGoodsLike(memId, goodsVO.getGoodId(), GoodsDetailActivity.this);
                            Toast.makeText(GoodsDetailActivity.this, "已加入收藏", Toast.LENGTH_SHORT).show();
                            heart = 1;
                            break;
                        case 1:
                            ivHeart.setImageResource(R.drawable.heart);
                            new GoodsLike().deleteGoodsLike(memId, goodsVO.getGoodId(), GoodsDetailActivity.this);
                            Toast.makeText(GoodsDetailActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                            heart = 0;
                            break;
                    }
                }
            }
        });
        //秀出已經加入收藏者
        String memId = Tools.getSharePreAccount().getString("memId", null);
        if (memId != null) {
            Set<String> goodsLikes = Tools.getSharePreAccount().getStringSet("goodsLikes", null);
            if (goodsLikes != null) {
                for (String goodsLike : goodsLikes) {
                    if (goodsLike.equals(goodsVO.getGoodId())) {
                        ivHeart.setImageResource(R.drawable.hearted);
                        heart = 1;
                    }
                }
            }
        }


        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GoodsDetailActivity.this, goodsVO.getGoodName() + "已加入購物車", Toast.LENGTH_LONG).show();
                Map<GoodsVO, Integer> myCart = Holder.getCart();
                if (!myCart.containsKey(goodsVO)) {
                    myCart.put(goodsVO, 1);
                } else {
                    int goodsCount = myCart.get(goodsVO);
                    goodsCount++;
                    myCart.put(goodsVO, goodsCount);
                }
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String memId = Tools.getSharePreAccount().getString("memId", null);
                if (memId != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoodsDetailActivity.this);
                    builder.setTitle("確認付款")
                            .setCancelable(false)
                            .setMessage("共：  NT " + singlePrice)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    GoodsOrderVO goodsOrderVO = new GoodsOrderVO();
                                    goodsOrderVO.setMemId(memId);
                                    goodsOrderVO.setGoodTotalPrice(singlePrice);
                                    goodsOrderVO.setGoodDate(new Timestamp(new GregorianCalendar().getTimeInMillis()));

                                    Map<String, String> requestMap = new HashMap<>();
                                    Gson gson = new GsonBuilder()
                                            .enableComplexMapKeySerialization()
                                            .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                            .create();

                                    requestMap.put("action", "add_new_good_order");
                                    requestMap.put("myCart", gson.toJson(singleBuy));
                                    requestMap.put("goodsOrderVO", gson.toJson(goodsOrderVO));
                                    String request = Tools.RequestDataBuilder(requestMap);
                                    Log.e("request", request);
                                    String result = null;
                                    try {
                                        result = new CallServlet(GoodsDetailActivity.this).execute(ServerURL.IP_GOODSORDER, request).get();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    if (result.equals("Insufficient_account_balance")) {
                                        Tools.Toast(GoodsDetailActivity.this, "餘額不足，請先儲值");
                                        AlertDialog.Builder builderSuccess = new AlertDialog.Builder(GoodsDetailActivity.this);
                                        builderSuccess
                                                .setMessage("餘額不足是否前往儲值頁面")
                                                .setCancelable(false)
                                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //Intent intent = new Intent(GoodsCartActivity.this,);
                                                        //startActivity(intent);
                                                        //finish();
                                                    }
                                                })
                                                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }).create().show();

                                    } else {
                                        Tools.Toast(GoodsDetailActivity.this, "訂購成功，請靜候賣家出貨");
                                    }

                                }
                            })
                            .create().show();


                } else {
                    Intent intent = new Intent(GoodsDetailActivity.this, LoginFakeActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    private void findViews() {
        ivPic = findViewById(R.id.ivPic);
        ivHeart = findViewById(R.id.ivHeart);
        ivShare = findViewById(R.id.ivShare);
        tvDetail = findViewById(R.id.tvDetail);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        btnAddCart = findViewById(R.id.btnAddCart);
        btnBuy = findViewById(R.id.btnBuy);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }
}


