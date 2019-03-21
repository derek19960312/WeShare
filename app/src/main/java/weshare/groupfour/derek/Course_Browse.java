package weshare.groupfour.derek;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Course_Browse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course__browse);
        RecyclerView recycleView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

        final List<Course> courseList = new ArrayList<>();

        RoundedBitmapDrawable roundedBitmapDrawable;
        int j = 0;
        for(int i=0; i<6; i++){
            if(j == 3){
                j=0;
            }else{
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.teacher+j);
                j++;
                roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                roundedBitmapDrawable.setCircular(true);
                courseList.add(new Course(roundedBitmapDrawable,"English","MIMI","英文是值得投資的!!!!"));
            }
        }

        recycleView.setAdapter(new CourseAdapter(courseList));
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

        private List<Course> courseList;

        public CourseAdapter(List<Course> courseList) {
            this.courseList = courseList;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivTeacherPic;
            private TextView tvCourseName,tvTeacherName,tvCourseDetail;


            public ViewHolder(View view) {
                super(view);
                ivTeacherPic = view.findViewById(R.id.ivTeacherPic);
                tvCourseName = view.findViewById(R.id.tvCourseName);
                tvTeacherName = view.findViewById(R.id.tvTeacherName);
                tvCourseDetail = view.findViewById(R.id.tvCourseDetail);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder( ViewHolder holder, int position) {
            Course course = courseList.get(position);



            holder.ivTeacherPic.setImageDrawable(course.getIvTeacherPic());
            holder.tvTeacherName.setText(course.getTvTeacherName());
            holder.tvCourseName.setText(course.getTvCourseName());
            holder.tvCourseDetail.setText(course.getTvCourseDetail());
        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }


    }
}
