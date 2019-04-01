package weshare.groupfour.derek.Course;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import weshare.groupfour.derek.R;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ImageView ivTeacherPic = findViewById(R.id.ivTeacherPic);
        TextView tvCourseName = findViewById(R.id.tvCourseName);
        TextView tvTeacherName = findViewById(R.id.tvTeacherName);
        TextView tvCourseDetail = findViewById(R.id.tvCourseDetail);
        Intent intent = getIntent();
        Course course = (Course) intent.getExtras().getSerializable("course");

        ivTeacherPic.setImageResource(course.getTeacherPic());
        tvTeacherName.setText(course.getTeacherName());
        tvCourseName.setText(course.getCourseName());
        tvCourseDetail.setText(course.getCourseDetail());

    }
}
