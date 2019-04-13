package weshare.groupfour.derek.InsCourse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import weshare.groupfour.derek.R;

public class MyLikeCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylike_course);

        RecyclerView rvMyLikeCourse = findViewById(R.id.rvMyLikeCourse);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        rvMyLikeCourse.setLayoutManager(staggeredGridLayoutManager);


//        final List<InsCourseVO> insCourseVOList = new ArrayList<>();
//
//
//        int j = 0;
//        for(int i=0; i<6; i++){
//            if(j == 3){
//                j=0;
//            }else{
//
//                j++;
//
//                insCourseVOList.add(new InsCourseVO(R.drawable.teacher+j,"English","MIMI","英文是值得投資的!!!!"));
//            }
//        }
//        rvMyLikeCourse.setAdapter(new CourseAdapter(insCourseVOList));


    }
}
