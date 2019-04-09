package weshare.groupfour.derek.InsCourse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.R;

public class MyLikeCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylike_course);

        RecyclerView rvMyLikeCourse = findViewById(R.id.rvMyLikeCourse);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        rvMyLikeCourse.setLayoutManager(staggeredGridLayoutManager);

        List<InsCourseVO> courseVOList = new ArrayList<>();

        rvMyLikeCourse.setAdapter(new InsCourseBrowseActivity().new CourseAdapter(courseVOList));


    }
}
