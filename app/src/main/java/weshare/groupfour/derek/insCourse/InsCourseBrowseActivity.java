package weshare.groupfour.derek.insCourse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Tools;

public class InsCourseBrowseActivity extends AppCompatActivity {
    RecyclerView recycleView;
    TextView tvNoCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscourse_browse);


        recycleView = findViewById(R.id.rvGoods);

        tvNoCourse = findViewById(R.id.tvNoCourse);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //讀取資料
        recycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        try {
            String data = getIntent().getStringExtra("requestData");

            String result = new CallServlet(this).execute(ServerURL.IP_COURSE, data).get();
            Type listType = new TypeToken<List<InsCourseVO>>() {
            }.getType();
            List<InsCourseVO> insCourseVOList = Holder.gson.fromJson(result, listType);

            if (insCourseVOList != null || insCourseVOList.size() != 0) {
                //判斷是否為該會員所開課程
                String teacherId = Tools.getSharePreAccount().getString("teacherId", null);
                List<InsCourseVO> inscVO_except_mine = new ArrayList<>();
                if (teacherId != null) {
                    for (int i = 0; i < insCourseVOList.size(); i++) {
                        if (!insCourseVOList.get(i).getTeacherId().equals(teacherId)) {
                            inscVO_except_mine.add(insCourseVOList.get(i));
                        }
                    }
                    if (inscVO_except_mine.size() != 0) {
                        recycleView.setAdapter(new CourseAdapter(inscVO_except_mine, this));
                        tvNoCourse.setVisibility(View.GONE);
                    } else {
                        tvNoCourse.setVisibility(View.VISIBLE);
                    }


                } else {
                    recycleView.setAdapter(new CourseAdapter(insCourseVOList, this));
                    tvNoCourse.setVisibility(View.GONE);
                }

            } else {
                tvNoCourse.setVisibility(View.VISIBLE);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
