package weshare.groupfour.derek.InsCourse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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

        final List<InsCourseVO> insCourseVOList = new ArrayList<>();


        int j = 0;
        for(int i=0; i<6; i++){
            if(j == 3){
                j=0;
            }else{

                j++;

                insCourseVOList.add(new InsCourseVO(R.drawable.teacher+j,"English","MIMI","英文是值得投資的!!!!"));
            }
        }

        recycleView.setAdapter(new CourseAdapter(insCourseVOList));
    }



    public byte[] compareToByte(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

}
