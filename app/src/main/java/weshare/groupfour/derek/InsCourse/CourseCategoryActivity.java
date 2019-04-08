package weshare.groupfour.derek.InsCourse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.CallServlet;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.ServerURL;

public class CourseCategoryActivity extends AppCompatActivity {

    String URL = ServerURL.IP_COURSECATEGORY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_category);

        RecyclerView rvCategory = findViewById(R.id.rvCategory);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rvCategory.setLayoutManager(staggeredGridLayoutManager);

        final List<CourseTypeVO> courseTypeVOS = new ArrayList<>();

        CallServlet callServlet = new CallServlet();
        callServlet.execute(URL,"");
        try{
            Thread.sleep(500);
        }catch (Exception e) {
            Toast.makeText( this, "連線錯誤", Toast.LENGTH_SHORT).show();
        }

        if(callServlet.getList().size()!=0){
            try {
                JSONArray jsonArray = new JSONArray(callServlet.getList().get(0));
                for(int i = 0 ; i<jsonArray.length(); i++){
                    CourseTypeVO cusVO = new CourseTypeVO();
                    cusVO.setCourseTypeName(((JSONObject)jsonArray.get(i)).getString("courseTypeName"));
                    courseTypeVOS.add(cusVO);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "錯誤", Toast.LENGTH_SHORT).show();
        }






        rvCategory.setAdapter(new CourseTypeAdapter(courseTypeVOS));
    }

    private class CourseTypeAdapter extends RecyclerView.Adapter<CourseTypeAdapter.ViewHolder>{
        List<CourseTypeVO> courseTypeVOS;

        public CourseTypeAdapter(List<CourseTypeVO> courseTypeVOS) {
            this.courseTypeVOS = courseTypeVOS;
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            private Button btnCategory;

            public ViewHolder(View view) {
                super(view);
                btnCategory = view.findViewById(R.id.btnCategory);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_course_category,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final CourseTypeVO courseTypeVO = courseTypeVOS.get(position);
            viewHolder.btnCategory.setText(courseTypeVO.getCourseTypeName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return courseTypeVOS.size();
        }


    }
}
