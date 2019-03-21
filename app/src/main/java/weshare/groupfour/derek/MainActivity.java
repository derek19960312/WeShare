package weshare.groupfour.derek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import weshare.groupfour.derek.Commondity.Commondity_Browse;
import weshare.groupfour.derek.Course.Course_Browse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews() {
        Button btn_commondity = findViewById(R.id.btn_commondity);
        btn_commondity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Commondity_Browse.class);
                startActivity(intent);
            }
        });

        Button btn_course = findViewById(R.id.btn_course);
        btn_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Course_Browse.class);
                startActivity(intent);
            }
        });
    }
}
