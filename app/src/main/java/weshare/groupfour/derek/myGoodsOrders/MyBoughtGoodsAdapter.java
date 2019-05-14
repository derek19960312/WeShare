package weshare.groupfour.derek.myGoodsOrders;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.R;


public class MyBoughtGoodsAdapter extends RecyclerView.Adapter<MyBoughtGoodsAdapter.ViewHolder> {
    public final static int BUYER = 0;
    public final static int SELLER = 1;
    private int fromWhere;
    private Context context;


    public List<GoodsOrderVO> myGoodsOrderRvList = null;

    public MyBoughtGoodsAdapter(List<GoodsOrderVO> myGoodsOrderRvList, int fromWhere, Context context) {
        this.myGoodsOrderRvList = myGoodsOrderRvList;
        this.fromWhere = fromWhere;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvOrderDate;
        private TextView tvOrderStatus;
        private TextView tvOrderPrice;
        private TextView tvOrderType;
        private TextView tvOrderNumber;
        private RecyclerView rvOrderDetail;

        public ViewHolder(View view) {
            super(view);
            tvOrderDate = view.findViewById(R.id.tvOrderDate);
            tvOrderStatus = view.findViewById(R.id.tvOrderStatus);
            tvOrderPrice = view.findViewById(R.id.tvOrderPrice);
            tvOrderType = view.findViewById(R.id.tvOrderType);
            tvOrderNumber = view.findViewById(R.id.tvOrderNumber);
            rvOrderDetail = view.findViewById(R.id.rvOrderDetail);
        }
    }

    @Override
    public MyBoughtGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_good_order_buy, parent, false);
        return new MyBoughtGoodsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyBoughtGoodsAdapter.ViewHolder holder, int position) {
        GoodsOrderVO myGoodsOrder = myGoodsOrderRvList.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        holder.tvOrderDate.setText(String.valueOf(sdf.format(myGoodsOrder.getGoodDate())));

        switch (myGoodsOrder.getGoodOrdStatus()) {
            case 0:
                holder.tvOrderStatus.setText("訂單取消");
                break;
            case 1:
                holder.tvOrderStatus.setText("未出貨");
                break;
            case 2:
                holder.tvOrderStatus.setText("出貨中");
                break;
            case 3:
                holder.tvOrderStatus.setText("訂單完成");
                break;
        }


        holder.tvOrderPrice.setText(String.valueOf(myGoodsOrder.getGoodTotalPrice()));
        holder.tvOrderNumber.setText(myGoodsOrder.getGoodOrderId());

        switch ((int) (Math.random() * 2)) {
            case 0:
                holder.tvOrderType.setText("宅配");
                break;
            case 1:
                holder.tvOrderType.setText("貨到付款");
                break;

        }


        //強迫關閉
        if (holder.rvOrderDetail.getVisibility() != View.GONE) {
            holder.rvOrderDetail.setVisibility(View.GONE);
        }

        holder.rvOrderDetail.setLayoutManager(new LinearLayoutManager(context));


        holder.rvOrderDetail.setAdapter(new MyGoodsDetailsAdapter(new ArrayList(myGoodsOrder.getGoodsDetailsVOs()), context));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.rvOrderDetail.getVisibility()) {
                    case View.VISIBLE:
                        holder.rvOrderDetail.setVisibility(View.GONE);
                        break;
                    case View.GONE:
                        holder.rvOrderDetail.setVisibility(View.VISIBLE);
                        break;
                }


            }
        });


    }

    @Override
    public int getItemCount() {

        return myGoodsOrderRvList.size();
    }
}



