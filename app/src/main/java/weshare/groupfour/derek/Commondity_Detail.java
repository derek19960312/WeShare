package weshare.groupfour.derek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Commondity_Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commondity__detail);
        ImageView ivPic = findViewById(R.id.ivPic),ivHeart = findViewById(R.id.ivHeart),ivShare = findViewById(R.id.ivShare);
        TextView tvDetail = findViewById(R.id.tvDetail),tvName = findViewById(R.id.tvName);

        Commondity commondity = (Commondity)getIntent().getExtras().getSerializable("commondity");
        ivPic.setImageResource(commondity.getIcon());
        tvName.setText(commondity.getName());
        tvDetail.setText(commondity.getPrice());
    }
}
