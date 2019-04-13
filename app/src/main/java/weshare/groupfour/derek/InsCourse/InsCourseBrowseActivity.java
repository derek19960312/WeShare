package weshare.groupfour.derek.InsCourse;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.R;

public class InsCourseBrowseActivity extends AppCompatActivity {
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscourse_browse);
        RecyclerView recycleView = findViewById(R.id.recyclerView);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recycleView.setLayoutManager(staggeredGridLayoutManager);


        try {
            List<InsCourseVO> insCourseVOList = new Gson().fromJson(new CallServlet().execute(ServerURL.IP_SEARCH_INSCOURSE,data).get());


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        int j = 0;
//        for(int i=0; i<6; i++){
//            if(j == 3){
//                j=0;
//            }else{
//                j++;
//                insCourseVOList.add(new InsCourseVO(R.drawable.teacher+j,"English","MIMI","英文是值得投資的!!!!"));
//            }
//        }

        recycleView.setAdapter(new CourseAdapter(insCourseVOList));
    }



    public byte[] compareToByte(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

}
