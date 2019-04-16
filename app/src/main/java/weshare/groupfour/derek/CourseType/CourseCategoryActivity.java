package weshare.groupfour.derek.CourseType;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.InsCourse.InsCourseBrowseActivity;
import weshare.groupfour.derek.R;


public class CourseCategoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_category);

        RecyclerView rvCategory = findViewById(R.id.rvCategory);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rvCategory.setLayoutManager(staggeredGridLayoutManager);

        Gson gson = new Gson();
        CallServlet callServlet = new CallServlet();
        Type listType = new TypeToken<List<CourseTypeVO>>() {
        }.getType();

        try{
            List<CourseTypeVO> courseTypeList = gson.fromJson(callServlet.execute(ServerURL.IP_COURSETYPE,"action=get_all_type").get(),listType);
            if(courseTypeList != null){
                rvCategory.setAdapter(new CourseTypeAdapter(courseTypeList));
            }
        }catch (Exception e) {
            Log.e("連線錯誤",e.toString());
        }
    }

    private class CourseTypeAdapter extends RecyclerView.Adapter<CourseTypeAdapter.ViewHolder>{
        List<CourseTypeVO> CourseTypeVOs;

        public CourseTypeAdapter(List<CourseTypeVO> courseTypeVOS) {
            this.CourseTypeVOs = courseTypeVOS;
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
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            final CourseTypeVO courseTypeVO = CourseTypeVOs.get(position);

            final String data = "courseTypeId="+courseTypeVO.getCourseTypeId()+"&action=find_by_coursetype";

            viewHolder.btnCategory.setText(courseTypeVO.getCourseTypeName());

            viewHolder.btnCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CourseCategoryActivity.this, InsCourseBrowseActivity.class);
                    intent.putExtra("data",data);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return CourseTypeVOs.size();
        }
    }
}
