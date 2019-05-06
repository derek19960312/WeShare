package weshare.groupfour.derek.goods;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.home.LoginFakeActivity;
import weshare.groupfour.derek.myGoodsOrders.GoodsOrderVO;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Join;
import weshare.groupfour.derek.util.Tools;

public class GoodsCartActivity extends AppCompatActivity {
    TextView tvTotalPrice;
    int totalPrice = 0;
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
            GoodsCartAdapter adapter = new GoodsCartAdapter(new ArrayList<>(myCart.keySet()));
            rvCart.setAdapter(adapter);

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(rvCart);


            for(GoodsVO gvo : myCart.keySet()){
                totalPrice += myCart.get(gvo)*gvo.getGoodPrice();
            }
            tvTotalPrice.setText(String.valueOf(totalPrice));

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

                    final String memId = Tools.getSharePreAccount().getString("memId",null);
                    if(memId != null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(GoodsCartActivity.this);
                        builder.setTitle("確認付款")
                                .setCancelable(false)
                                .setMessage("共：  NT "+totalPrice)
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GoodsOrderVO goodsOrderVO = new GoodsOrderVO();
                                        goodsOrderVO.setMemId(memId);
                                        goodsOrderVO.setGoodTotalPrice(totalPrice);
                                        goodsOrderVO.setGoodDate(new Timestamp(new GregorianCalendar().getTimeInMillis()));

                                        Map<String,String> requestMap = new HashMap<>();
                                        Gson gson = new GsonBuilder()

                                                .enableComplexMapKeySerialization()
                                                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                                .create();
                                        requestMap.put("action","add_new_good_order");
                                        requestMap.put("myCart",gson.toJson(myCart));
                                        requestMap.put("goodsOrderVO",gson.toJson(goodsOrderVO));
                                        String request = Tools.RequestDataBuilder(requestMap);
                                        Log.e("request",request);
                                        String result = null;
                                        try {
                                            result = new CallServlet(GoodsCartActivity.this).execute(ServerURL.IP_GOODSORDER,request).get();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        if (result.equals("Insufficient_account_balance")){
                                            Tools.Toast(GoodsCartActivity.this,"餘額不足，請先儲值");
                                            AlertDialog.Builder builderSuccess = new AlertDialog.Builder(GoodsCartActivity.this);
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

                                        }else{
                                            Tools.Toast(GoodsCartActivity.this,"訂購成功，請靜候賣家出貨");
                                            myCart.clear();
                                            finish();
                                        }

                                    }
                                })
                                .create().show();


                    }else{
                        Intent intent = new Intent(GoodsCartActivity.this,LoginFakeActivity.class);
                        startActivity(intent);
                    }



                }
            });

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("購物車空空如也")
                    .setCancelable(false)
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


    private class GoodsCartAdapter extends RecyclerView.Adapter<GoodsCartAdapter.ViewHolder> implements ItemTouchHelperAdapter{
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
        public boolean onItemMove(int fromPosition, int toPosition) {
            return false;
        }

        @Override
        public void onItemDismiss(int position) {
            AlertDialog.Builder builder  = new AlertDialog.Builder(GoodsCartActivity.this);
            String goodName = goodsVOs.get(position).getGoodName();
            String goodCount = String.valueOf(myCart.get(goodsVOs.get(position)));
            builder.setMessage("確定要刪除 "+goodName+" X "+goodCount);
            builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GoodsVO gvoRemove = goodsVOs.get(position);
                    goodsVOs.remove(position);
                    myCart.remove(gvoRemove);

                    notifyItemRemoved(position);
                    totalPrice = 0;
                    for(GoodsVO gvo : myCart.keySet()){
                        totalPrice += myCart.get(gvo)*gvo.getGoodPrice();
                    }
                    tvTotalPrice.setText(String.valueOf(totalPrice));
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    notifyItemChanged(position);
                }
            }).create().show();


        }

        @Override
        public GoodsCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_goods_cart,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final GoodsCartAdapter.ViewHolder holder, int position) {
            final GoodsVO goodsVO = goodsVOs.get(position);

            if(myCart.size() != 0){

                try {
                    int count = myCart.get(goodsVO)-1;
                    if(count > 8){
                        count = 8;
                    }
                    holder.tvName.setText(goodsVO.getGoodName());
                    holder.tvPrice.setText(String.valueOf(goodsVO.getGoodPrice()));
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
                                totalPrice = total;
                            }else{

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    //收藏
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

                    //載入圖片

                    Join.setPicOn(holder.ivIcon,goodsVO.getGoodId());

                }catch (Exception e){

                }


            }else{
                Toast.makeText(GoodsCartActivity.this , "請先購物", Toast.LENGTH_LONG);
                finish();

            }


        }

        @Override
        public int getItemCount() {
            return goodsVOs.size();
        }


    }


    private class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
        private ItemTouchHelperAdapter adapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            this.adapter = adapter;
        }

        // 預設即為true (寫出來提醒一下)
        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        // 預設即為true (寫出來提醒一下)
        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            // 也可以設ItemTouchHelper.RIGHT(向右滑)，或是 ItemTouchHelper.START | ItemTouchHelper.END (左右滑都可以)
            int swipeFlags = ItemTouchHelper.LEFT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                float width = (float) viewHolder.itemView.getWidth();
                float alpha = 1.0f - Math.abs(dX) / width;
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive);
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            // We only want the active item
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof ItemTouchHelperViewHolder) {
                    ItemTouchHelperViewHolder itemViewHolder =
                            (ItemTouchHelperViewHolder) viewHolder;
                    itemViewHolder.onItemSelected();
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemViewHolder =
                        (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemClear();
                viewHolder.itemView.setTranslationX(0);
            }
        }
    }


}
