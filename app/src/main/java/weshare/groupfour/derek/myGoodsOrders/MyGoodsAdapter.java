package weshare.groupfour.derek.myGoodsOrders;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.goods.GoodsVO;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Join;
import weshare.groupfour.derek.util.Tools;


public class MyGoodsAdapter extends RecyclerView.Adapter<MyGoodsAdapter.ViewHolder> {
    public final static int BUYER = 0;
    public final static int SELLER = 1;
    private int fromWhere;
    private Context context;


    public List<GoodsOrderVO> myGoodsOrderRvList = null;

    public MyGoodsAdapter(List<GoodsOrderVO> myGoodsOrderRvList, int fromWhere, Context context) {
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
    public MyGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_good_order, parent, false);
        return new MyGoodsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyGoodsAdapter.ViewHolder holder, int position) {
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
        if(holder.rvOrderDetail.getVisibility() != View.GONE ){
            holder.rvOrderDetail.setVisibility(View.GONE);
        }

        holder.rvOrderDetail.setLayoutManager(new LinearLayoutManager(context));

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("action", "find_good_detail_by_orderId");
        requestMap.put("goodOrderId", myGoodsOrder.getGoodOrderId());
        String request = Tools.RequestDataBuilder(requestMap);
        try {
            String result = new CallServlet(context).execute(ServerURL.IP_GOODSDETAILS, request).get();
            Type listType = new TypeToken<List<GoodsVO>>() {
            }.getType();
            List<GoodsVO> GoodsVOs = Holder.gson.fromJson(result, listType);
            if (GoodsVOs != null && GoodsVOs.size() != 0) {
                holder.rvOrderDetail.setAdapter(new MyGoodsDetailsAdapter(GoodsVOs, context));
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(holder.rvOrderDetail.getVisibility()){
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

class MyGoodsDetailsAdapter extends RecyclerView.Adapter<MyGoodsDetailsAdapter.ViewHolder> {

    private Context context;

    public List<GoodsVO> GoodsVOs = null;

    public MyGoodsDetailsAdapter(List<GoodsVO> GoodsVOs, Context context) {
        this.GoodsVOs = GoodsVOs;
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
    public MyGoodsDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_good_detail, parent, false);
        return new MyGoodsDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyGoodsDetailsAdapter.ViewHolder holder, int position) {
        GoodsVO goodsVO = GoodsVOs.get(position);

        holder.tvGoodName.setText(goodsVO.getGoodName());
        holder.tvGoodPrice.setText(String.valueOf(goodsVO.getGoodPrice()));
        holder.tvGoodAmount.setText(String.valueOf(goodsVO.getGoodStatus()));
        holder.tvGoodTotal.setText(String.valueOf(goodsVO.getGoodStatus()*goodsVO.getGoodPrice()));

        Join.setPicOn(holder.ivGoodPic,goodsVO.getGoodId());

    }

    @Override
    public int getItemCount() {

        return GoodsVOs.size();
    }
}



