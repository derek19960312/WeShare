package weshare.groupfour.derek.goods;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import weshare.groupfour.derek.R;
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
        holder.ivIcon.setImageBitmap(new Join().getGoodsPicBitmap(goodsVO.getGoodId()));
        holder.ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.context, goodsVO.getGoodName() + "已加入購物車", Toast.LENGTH_LONG).show();
                Map<String, Integer> myCart = Holder.getCart();
                String goodsId = goodsVO.getGoodId();
                if (!myCart.containsKey(goodsId)) {
                    myCart.put(goodsId, 1);
                } else {
                    int goodsCount = myCart.get(goodsId);
                    goodsCount++;
                    myCart.put(goodsId, goodsCount);
                }
            }
        });
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
    }

    @Override
    public int getItemCount() {
        return goodsVOList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return GoodsBrowseActivity.staggeredGridLayoutManager.getSpanCount();
    }
}
