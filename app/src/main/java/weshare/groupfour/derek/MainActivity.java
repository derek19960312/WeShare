package weshare.groupfour.derek;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent();

        Button btnCourse = findViewById(R.id.btnCourse);
        Button btnGoods = findViewById(R.id.btnGoods);
        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this, CourseMainActivity.class);
                startActivity(intent);
            }
        });
        btnGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this, GoodsMainActivity.class);
                startActivity(intent);
            }
        });


    }

}






