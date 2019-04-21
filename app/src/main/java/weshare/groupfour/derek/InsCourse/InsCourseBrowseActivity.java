package weshare.groupfour.derek.InsCourse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Tools;

public class InsCourseBrowseActivity extends AppCompatActivity {
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscourse_browse);
        RecyclerView recycleView = findViewById(R.id.rvGoods);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recycleView.setLayoutManager(staggeredGridLayoutManager);


        try {
            String data = getIntent().getStringExtra("data");

            String result = new CallServlet().execute(ServerURL.IP_COURSETYPE,data).get();

            Type listType = new TypeToken<List<InsCourseVO>>() {}.getType();
            List<InsCourseVO> insCourseVOList = new Gson().fromJson(result,listType);
            if(insCourseVOList.size() != 0) {
                recycleView.setAdapter(new CourseAdapter(insCourseVOList));
            }else {
                Tools.Toast(this,"查無資料");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
