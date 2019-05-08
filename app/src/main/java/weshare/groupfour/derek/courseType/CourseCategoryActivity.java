package weshare.groupfour.derek.courseType;

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

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.insCourse.InsCourseBrowseActivity;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Tools;


public class CourseCategoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_category);

        RecyclerView rvCategory = findViewById(R.id.rvCategory);
        rvCategory.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));


        Type listType = new TypeToken<List<CourseTypeVO>>() {}.getType();

        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("action","get_all_type");
        String requestData= new Tools().RequestDataBuilder(requestMap);
        try{
            String result = new CallServlet(this).execute(ServerURL.IP_COURSETYPE,requestData).get();
            List<CourseTypeVO> courseTypeList = Holder.gson.fromJson(result,listType);

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
            Map<String,String> requestMap = new HashMap<>();
            requestMap.put("action","find_by_coursetype");
            requestMap.put("courseTypeId",courseTypeVO.getCourseTypeId().toString());
            final String requestData = new Tools().RequestDataBuilder(requestMap);


            viewHolder.btnCategory.setText(courseTypeVO.getCourseTypeName());

            viewHolder.btnCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CourseCategoryActivity.this, InsCourseBrowseActivity.class);
                    intent.putExtra("requestData",requestData);
                    startActivity(intent);
                    //CourseCategoryActivity.this.overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_out_right);
                }
            });
        }

        @Override
        public int getItemCount() {
            return CourseTypeVOs.size();
        }
    }
}
