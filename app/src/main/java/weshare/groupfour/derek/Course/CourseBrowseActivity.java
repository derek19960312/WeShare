package weshare.groupfour.derek.Course;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
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

import weshare.groupfour.derek.R;

public class CourseBrowseActivity extends AppCompatActivity {

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
                //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.teacher+j);

                //roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                //roundedBitmapDrawable.setCircular(true);
                //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.teacher+j);
                j++;
                courseList.add(new Course(R.drawable.teacher+j,"English","MIMI","英文是值得投資的!!!!"));
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
            final Course course = courseList.get(position);



            holder.ivTeacherPic.setImageResource(course.getTeacherPic());
            holder.tvTeacherName.setText(course.getTeacherName());
            holder.tvCourseName.setText(course.getCourseName());
            holder.tvCourseDetail.setText(course.getCourseDetail());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    intent.setClass(CourseBrowseActivity.this,CourseDetailActivity.class);
                    bundle.putSerializable("course",course);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }


    }
}
