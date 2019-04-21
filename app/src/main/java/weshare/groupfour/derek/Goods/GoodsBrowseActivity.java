package weshare.groupfour.derek.Goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Tools;

public class GoodsBrowseActivity extends AppCompatActivity {
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_browse);
        RecyclerView rvGoods = findViewById(R.id.rvGoods);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvGoods.setLayoutManager(staggeredGridLayoutManager);

        ImageView ivChange = findViewById(R.id.ivChange);
        ivChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (staggeredGridLayoutManager.getSpanCount()) {
                    case 1:
                        staggeredGridLayoutManager.setSpanCount(2);
                        break;
                    case 2:
                        staggeredGridLayoutManager.setSpanCount(1);
                        break;
                }
            }
        });


        ImageView ivMyCart = findViewById(R.id.ivMyCart);
        ivMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GoodsBrowseActivity.this, "查看我的購物車", Toast.LENGTH_LONG).show();
            }
        });


        Gson gson = new Gson();
        Map<String, String> request = new HashMap<>();
        request.put("action", "get_all");
        String requestData = Tools.RequestDataBuilder(request);
        String result = null;
        try {
            result = new CallServlet().execute(ServerURL.IP_GOODS, requestData).get();
            Log.e("result",result);
            Type listType = new TypeToken<List<GoodsVO>>() {
            }.getType();
            List<GoodsVO> goodsVOList = gson.fromJson(result, listType);
            if(goodsVOList != null){
                rvGoods.setAdapter(new GoodsAdapter(goodsVOList));
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
            private List<GoodsVO> goodsVOList;

            public GoodsAdapter(List<GoodsVO> goodsVOList) {
                this.goodsVOList = goodsVOList;
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                private TextView tvName, tvPrice;
                private ImageView ivIcon, ivCart, ivHeart;
                private int heart;

                public ViewHolder(View view) {
                    super(view);
                    tvName = view.findViewById(R.id.tvName);
                    tvPrice = view.findViewById(R.id.tvPrice);
                    ivIcon = view.findViewById(R.id.ivIcon);
                    ivCart = view.findViewById(R.id.ivCart);
                    ivHeart = view.findViewById(R.id.ivHeart);
                    heart = 0;
                }
            }


            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_goods, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, int position) {
                final GoodsVO goodsVO = goodsVOList.get(position);
                holder.tvPrice.setText("特價 : " + goodsVO.getGoodPrice());
                holder.tvName.setText(goodsVO.getGoodName());
                //holder.ivIcon.setImageResource(goodsVO.);
                holder.ivCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(GoodsBrowseActivity.this, goodsVO.getGoodName()+"已加入購物車", Toast.LENGTH_LONG).show();
                        Holder.getCart().add(goodsVO.getGoodId());
                    }
                });
                holder.ivHeart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (holder.heart) {
                            case 0:
                                holder.ivHeart.setImageResource(R.drawable.hearted);
                                Toast.makeText(GoodsBrowseActivity.this, "已加入收藏", Toast.LENGTH_SHORT).show();
                                holder.heart = 1;
                                break;
                            case 1:
                                holder.ivHeart.setImageResource(R.drawable.heart);
                                Toast.makeText(GoodsBrowseActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                                holder.heart = 0;
                                break;
                        }
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(GoodsBrowseActivity.this,goodsVO.getName()+"查看詳情",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(GoodsBrowseActivity.this, GoodsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("goodsVO", goodsVO);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return goodsVOList.size();
            }
        }

    }
