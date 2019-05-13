package weshare.groupfour.derek.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import weshare.groupfour.derek.home.LoginFakeActivity;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Join;
import weshare.groupfour.derek.util.Tools;

public class MyWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        Intent intent = new Intent(MyWalletActivity.this, LoginFakeActivity.class);
        startActivityForResult(intent, 1000);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Button btnDeposit = findViewById(R.id.btnDeposit);
        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWalletActivity.this,DepositActivity.class);
                startActivity(intent);
            }
        });



        TextView tvMyMoney = findViewById(R.id.tvMyMoney);
        String memId = Tools.getSharePreAccount().getString("memId", null);
        if (!memId.isEmpty()) {
            MemberVO memberVO = new Join().getMemberbyMemId(memId, this);
            tvMyMoney.setText(String.valueOf(memberVO.getMemBalance()));

            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("action", "get_my_money_record");
            requestMap.put("memId", memId);
            String request = Tools.RequestDataBuilder(requestMap);


            TextView tvNORecord = findViewById(R.id.tvNORecord);

            if(tvNORecord.getVisibility() == View.VISIBLE){
                tvNORecord.setVisibility(View.GONE);
            }


            try {
                String result = new CallServlet(this).execute(ServerURL.IP_WITHDRAEALRECORD, request).get();
                Type listType = new TypeToken<List<WithdrawalRecordVO>>() {
                }.getType();
                List<WithdrawalRecordVO> withdrawalRecordVOs = Holder.gson.fromJson(result, listType);

                RecyclerView rvMyWallet = findViewById(R.id.rvMyWallet);
                rvMyWallet.setLayoutManager(new LinearLayoutManager(this));
                if(withdrawalRecordVOs != null){
                    if(withdrawalRecordVOs.size() != 0){
                        rvMyWallet.setAdapter(new MyWalletAdapter(withdrawalRecordVOs));
                    }else {

                        tvNORecord.setVisibility(View.VISIBLE);
                    }
                }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    }

    private class MyWalletAdapter extends RecyclerView.Adapter<MyWalletAdapter.ViewHolder> {
        private List<WithdrawalRecordVO> withdrawalRecordVOs;

        public MyWalletAdapter(List<WithdrawalRecordVO> withdrawalRecordVOs) {
            this.withdrawalRecordVOs = withdrawalRecordVOs;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvWrName;
            TextView tvWrTime;
            TextView tvType;
            TextView tvPrice;

            public ViewHolder(View view) {
                super(view);
                tvWrName = view.findViewById(R.id.tvWrName);
                tvWrTime = view.findViewById(R.id.tvWrTime);
                tvType = view.findViewById(R.id.tvType);
                tvPrice = view.findViewById(R.id.tvPrice);

            }
        }

        @Override
        public MyWalletAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_money_record, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyWalletAdapter.ViewHolder holder, int position) {
            WithdrawalRecordVO wrVO = withdrawalRecordVOs.get(position);

            holder.tvWrName.setText(wrVO.getWrnum());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            holder.tvWrTime.setText(sdf.format(wrVO.getWrtime()));

            if(wrVO.getWrmoney() >= 0){
                holder.tvType.setText("儲值");
            }else{
                holder.tvType.setText("消費");
            }

            holder.tvPrice.setText(String.valueOf(Math.abs(wrVO.getWrmoney())));


        }

        @Override
        public int getItemCount() {
            return withdrawalRecordVOs.size();
        }
    }

}
