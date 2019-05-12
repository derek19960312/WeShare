package weshare.groupfour.derek.myGoodsOrders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.util.RequestDataBuilder;


public class MySoldGoodsAdapter extends RecyclerView.Adapter<MySoldGoodsAdapter.ViewHolder> {
    public final static int BUYER = 0;
    public final static int SELLER = 1;
    private int fromWhere;
    private Context context;


    public List<GoodsOrderVO> myGoodsOrderRvList = null;

    public MySoldGoodsAdapter(List<GoodsOrderVO> myGoodsOrderRvList, int fromWhere, Context context) {
        this.myGoodsOrderRvList = myGoodsOrderRvList;
        this.fromWhere = fromWhere;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvOrderDate;
        private TextView tvOrderType;
        private TextView tvOrderNumber;
        private RecyclerView rvOrderDetail;
        private TextView tvBuyer;
        private TextView tvBuyerPhone;
        private TextView tvBuyerAdd;
        private TextView tvSomeThing;
        private Button btnSend;
        private CardView cardMe;



        public ViewHolder(View view) {
            super(view);
            tvOrderDate = view.findViewById(R.id.tvOrderDate);
            tvOrderType = view.findViewById(R.id.tvOrderType);
            tvOrderNumber = view.findViewById(R.id.tvOrderNumber);
            rvOrderDetail = view.findViewById(R.id.rvOrderDetail);
            tvBuyer = view.findViewById(R.id.tvBuyer);
            tvBuyerPhone = view.findViewById(R.id.tvBuyerPhone);
            tvBuyerAdd = view.findViewById(R.id.tvBuyerAdd);
            tvSomeThing = view.findViewById(R.id.tvSomeThing);
            btnSend = view.findViewById(R.id.btnSend);
            cardMe = view.findViewById(R.id.cardMe);

        }
    }

    @Override
    public MySoldGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_good_order_sold, parent, false);
        return new MySoldGoodsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MySoldGoodsAdapter.ViewHolder holder, int position) {
        GoodsOrderVO myGoodsOrder = myGoodsOrderRvList.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        holder.tvOrderDate.setText(String.valueOf(sdf.format(myGoodsOrder.getGoodDate())));

        holder.tvSomeThing.setVisibility(View.GONE);
        //訂單狀態
        if(myGoodsOrder.getGoodOrdStatus() != 1){
            holder.cardMe.setAlpha(0.5f);
            holder.btnSend.setClickable(false);
            holder.tvSomeThing.setVisibility(View.VISIBLE);
            switch (myGoodsOrder.getGoodOrdStatus()){
                case 0:
                    holder.tvSomeThing.setText("訂單取消");
                    break;
                case 2:
                    holder.tvSomeThing.setText("已出貨");
                    break;
                case 3:
                    holder.tvSomeThing.setText("訂單完成");
                    break;
            }
        }else{
            String[] status  = { "取消", "確認出貨"};
            //註冊按鈕
            holder.btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setItems(status, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    break;
                                case 1:
                                    RequestDataBuilder rdb = new RequestDataBuilder();
                                    rdb.build()
                                            .setAction("update_order_status")
                                            .setData("goodOrderId",myGoodsOrder.getGoodOrderId())
                                            .setData("orderStatus","2");
                                    new CallServlet(context).execute(ServerURL.IP_GOODSORDER,rdb.create());

                                    myGoodsOrder.setGoodOrdStatus(2);
                                    notifyDataSetChanged();
                                    break;
                            }
                        }
                    }).create().show();
                }
            });
        }


        holder.tvBuyer.setText(myGoodsOrder.getBuyerName());
        holder.tvBuyerAdd.setText(myGoodsOrder.getBuyerAddress());
        holder.tvBuyerPhone.setText(myGoodsOrder.getBuyerPhone());
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
        holder.rvOrderDetail.setAdapter(new MyGoodsDetailsAdapter(new ArrayList(myGoodsOrder.getGoodsDetailsVOs()), context));


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



