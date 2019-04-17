package weshare.groupfour.derek.InsCourse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.LoginFakeActivity;
import weshare.groupfour.derek.R;

public class MyLikeCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylike_course);

        RecyclerView rvMyLikeCourse = findViewById(R.id.rvMyLikeCourse);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        rvMyLikeCourse.setLayoutManager(staggeredGridLayoutManager);

        SharedPreferences spf = getSharedPreferences("myAccount", Context.MODE_PRIVATE);
        String memId = spf.getString("memId",null);
        if(memId != null){
            List<InsCourseVO> insCourseVOList = new CourseLike().getMyLikeCourse(memId);
            if(insCourseVOList != null){
                rvMyLikeCourse.setAdapter(new CourseAdapter(insCourseVOList));
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("查無收藏清單")
                        .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create().show();
            }
        }else{
            Toast.makeText(MyLikeCourseActivity.this,"請先登入",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyLikeCourseActivity.this, LoginFakeActivity.class);
            startActivity(intent);
        }
    }
}
