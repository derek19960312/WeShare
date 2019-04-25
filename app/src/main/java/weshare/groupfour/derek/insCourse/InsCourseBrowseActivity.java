package weshare.groupfour.derek.insCourse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Tools;

public class InsCourseBrowseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscourse_browse);


        RecyclerView recycleView = findViewById(R.id.rvGoods);
        recycleView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));


        try {
            String data = getIntent().getStringExtra("requestData");

            String result = new CallServlet().execute(ServerURL.IP_COURSE,data).get();

            Type listType = new TypeToken<List<InsCourseVO>>() {}.getType();
            List<InsCourseVO> insCourseVOList = new Gson().fromJson(result,listType);
            if(insCourseVOList != null || insCourseVOList.size() != 0) {



                recycleView.setAdapter(new CourseAdapter(insCourseVOList,this));
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
