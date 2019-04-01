package weshare.groupfour.derek.InsCourse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import weshare.groupfour.derek.R;

public class InsCourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscourse_detail);
        ImageView ivTeacherPic = findViewById(R.id.ivTeacherPic);
        TextView tvCourseName = findViewById(R.id.tvCourseName);
        TextView tvTeacherName = findViewById(R.id.tvTeacherName);
        TextView tvCourseDetail = findViewById(R.id.tvCourseDetail);
        Intent intent = getIntent();
        InsCourse insCourse = (InsCourse) intent.getExtras().getSerializable("insCourse");

        ivTeacherPic.setImageResource(insCourse.getTeacherPic());
        tvTeacherName.setText(insCourse.getTeacherName());
        tvCourseName.setText(insCourse.getCourseName());
        tvCourseDetail.setText(insCourse.getCourseDetail());

    }
}
