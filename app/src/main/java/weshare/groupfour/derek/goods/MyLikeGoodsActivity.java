package weshare.groupfour.derek.goods;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.List;

import weshare.groupfour.derek.home.LoginFakeActivity;
import weshare.groupfour.derek.R;

public class MyLikeGoodsActivity extends AppCompatActivity {
    RecyclerView rvMyLikeGoods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylike_goods);
        rvMyLikeGoods = findViewById(R.id.rvMyLikeGoods);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        rvMyLikeGoods.setLayoutManager(staggeredGridLayoutManager);

        Intent intent = new Intent(MyLikeGoodsActivity.this, LoginFakeActivity.class);
        startActivityForResult(intent,2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences spf = getSharedPreferences("myAccount", Context.MODE_PRIVATE);
        String memId = spf.getString("memId",null);
        if(memId != null){
            List<GoodsVO> GoodsVOList = new GoodsLike().getMyLikeGoods(memId,this);
            if(GoodsVOList != null && GoodsVOList.size() != 0 ){
                rvMyLikeGoods.setAdapter(new GoodsAdapter(GoodsVOList));
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("查無收藏清單")
                        .setCancelable(false)
                        .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create().show();
            }
        }
    }
}
