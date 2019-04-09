package weshare.groupfour.derek.Goods;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import weshare.groupfour.derek.R;

public class GoodsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ImageView ivPic = findViewById(R.id.ivPic),ivHeart = findViewById(R.id.ivHeart),ivShare = findViewById(R.id.ivShare);
        TextView tvDetail = findViewById(R.id.tvDetail),tvName = findViewById(R.id.tvName);

        GoodsVO goodsVO = (GoodsVO)getIntent().getExtras().getSerializable("goodsVO");
        ivPic.setImageResource(goodsVO.getIcon());
        tvName.setText(goodsVO.getName());
        tvDetail.setText(goodsVO.getPrice());
    }
}
