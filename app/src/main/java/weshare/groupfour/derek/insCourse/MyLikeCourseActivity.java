package weshare.groupfour.derek.insCourse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.List;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.home.LoginFakeActivity;

public class MyLikeCourseActivity extends AppCompatActivity {
    RecyclerView rvMyLikeCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylike_course);

        rvMyLikeCourse = findViewById(R.id.rvMyLikeCourse);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        rvMyLikeCourse.setLayoutManager(staggeredGridLayoutManager);

        Intent intent = new Intent(MyLikeCourseActivity.this, LoginFakeActivity.class);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences spf = getSharedPreferences("myAccount", Context.MODE_PRIVATE);
        String memId = spf.getString("memId",null);
        if(memId != null){
            List<InsCourseVO> insCourseVOList = new CourseLike().getMyLikeCourse(memId,this);
            if(insCourseVOList != null && insCourseVOList.size() != 0 ){
                rvMyLikeCourse.setAdapter(new CourseAdapter(insCourseVOList,this));
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


