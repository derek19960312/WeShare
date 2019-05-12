package weshare.groupfour.derek.myGoodsOrders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Join;

class MyGoodsDetailsAdapter extends RecyclerView.Adapter<MyGoodsDetailsAdapter.ViewHolder> {

    private Context context;

    public List<GoodsDetailsVO> GoodsDetailsVOs = null;

    public MyGoodsDetailsAdapter(List<GoodsDetailsVO> GoodsDetailsVOs, Context context) {
        this.GoodsDetailsVOs = GoodsDetailsVOs;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivGoodPic;
        private TextView tvGoodName;
        private TextView tvGoodAmount;
        private TextView tvGoodPrice;
        private TextView tvGoodTotal;


        public ViewHolder(View view) {
            super(view);
            ivGoodPic = view.findViewById(R.id.ivGoodPic);
            tvGoodName = view.findViewById(R.id.tvGoodName);
            tvGoodAmount = view.findViewById(R.id.tvGoodAmount);
            tvGoodPrice = view.findViewById(R.id.tvGoodPrice);
            tvGoodTotal = view.findViewById(R.id.tvGoodTotal);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_good_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GoodsDetailsVO goodsDetailsVO = GoodsDetailsVOs.get(position);

        holder.tvGoodName.setText(goodsDetailsVO.getGoodsVO().getGoodName());
        holder.tvGoodPrice.setText(String.valueOf(goodsDetailsVO.getGoodsVO().getGoodPrice()));
        holder.tvGoodAmount.setText(String.valueOf(goodsDetailsVO.getGoodAmount()));
        holder.tvGoodTotal.setText(String.valueOf(goodsDetailsVO.getGoodAmount()*goodsDetailsVO.getGoodsVO().getGoodPrice()));

        Join.setPicOn(holder.ivGoodPic,goodsDetailsVO.getGoodsVO().getGoodId());

    }

    @Override
    public int getItemCount() {

        return GoodsDetailsVOs.size();
    }
}
