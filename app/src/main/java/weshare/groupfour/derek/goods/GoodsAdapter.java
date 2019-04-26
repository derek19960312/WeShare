package weshare.groupfour.derek.goods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;
import java.util.Set;

import weshare.groupfour.derek.GetMyLikesThread;
import weshare.groupfour.derek.LoginFakeActivity;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.insCourse.CourseLike;
import weshare.groupfour.derek.insCourse.InsCourseVO;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Join;
import weshare.groupfour.derek.util.Tools;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {

    private List<GoodsVO> goodsVOList;

    public GoodsAdapter(List<GoodsVO> goodsVOList) {
        this.goodsVOList = goodsVOList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice;
        private ImageView ivIcon, ivCart, ivHeart;
        private int heart;
        private Context context;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvPrice = view.findViewById(R.id.tvPrice);
            ivIcon = view.findViewById(R.id.ivIcon);
            ivCart = view.findViewById(R.id.ivCart);
            ivHeart = view.findViewById(R.id.ivHeart);
            context = view.getContext();
            heart = 0;

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_goods_col1, parent, false);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_goods_col2, parent, false);
                break;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final GoodsVO goodsVO = goodsVOList.get(position);
        holder.tvPrice.setText("特價 : " + goodsVO.getGoodPrice());
        holder.tvName.setText(goodsVO.getGoodName());

        holder.ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.context, goodsVO.getGoodName() + "已加入購物車", Toast.LENGTH_LONG).show();
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
        holder.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences spf = Tools.getSharePreAccount();
                String memId = spf.getString("memId",null);
                if(memId != null){
                    switch (holder.heart) {
                        case 0:
                            holder.ivHeart.setImageResource(R.drawable.hearted);
                            new GoodsLike().addGoodsLike(memId,goodsVO.getGoodId(),holder.context);
                            Toast.makeText(holder.context, "已加入收藏", Toast.LENGTH_SHORT).show();
                            holder.heart = 1;
                            break;
                        case 1:
                            holder.ivHeart.setImageResource(R.drawable.heart);
                            new GoodsLike().deleteGoodsLike(memId,goodsVO.getGoodId(),holder.context);
                            Toast.makeText(holder.context, "已取消收藏", Toast.LENGTH_SHORT).show();
                            holder.heart = 0;
                            break;
                    }
                }else{
                    Toast.makeText(holder.context,"請先登入",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(holder.context, LoginFakeActivity.class);
                    holder.context.startActivity(intent);
                }

            }
        });

        //秀出已經加入收藏者
        String memId = Tools.getSharePreAccount().getString("memId",null);
        if(memId != null){
            Set<String> goodsLikes = Tools.getSharePreAccount().getStringSet("goodsLikes",null);
           // List<GoodsVO> GoodsVOListbylike = new GoodsLike().getMyLikeGoods(memId,holder.context);
            if(goodsLikes != null){
                for(String goodsLike : goodsLikes){
                    if(goodsLike.equals(goodsVO.getGoodId())){
                        holder.ivHeart.setImageResource(R.drawable.hearted);
                        holder.heart = 1;
                    }
                }
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(GoodsBrowseActivity.this,goodsVO.getName()+"查看詳情",Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(GoodsBrowseActivity.this, GoodsDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("goodsVO", goodsVO);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
            }
        });


        //載入圖片
        Join.setPicOn(holder.ivIcon,goodsVO.getGoodId());


    }

    @Override
    public int getItemCount() {
        return goodsVOList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(GoodsBrowseActivity.staggeredGridLayoutManager != null){
            return GoodsBrowseActivity.staggeredGridLayoutManager.getSpanCount();
        }else{
            return 1;
        }

    }
}
