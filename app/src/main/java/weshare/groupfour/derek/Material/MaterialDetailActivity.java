package weshare.groupfour.derek.Material;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import weshare.groupfour.derek.R;

public class MaterialDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material__detail);
        ImageView ivPic = findViewById(R.id.ivPic),ivHeart = findViewById(R.id.ivHeart),ivShare = findViewById(R.id.ivShare);
        TextView tvDetail = findViewById(R.id.tvDetail),tvName = findViewById(R.id.tvName);

        Material material = (Material)getIntent().getExtras().getSerializable("material");
        ivPic.setImageResource(material.getIcon());
        tvName.setText(material.getName());
        tvDetail.setText(material.getPrice());
    }
}
