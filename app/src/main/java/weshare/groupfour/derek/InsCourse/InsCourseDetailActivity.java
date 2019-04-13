package weshare.groupfour.derek.InsCourse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import weshare.groupfour.derek.R;

public class InsCourseDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscourse_detail);

        RecyclerView rvCourseDetail = findViewById(R.id.rvCourseDetail);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvCourseDetail.setLayoutManager(layoutManager);
        rvCourseDetail.setAdapter(new CourseDetailAdapter());

    }

    private class CourseDetailAdapter extends RecyclerView.Adapter{
        Intent intent = getIntent();
        InsCourseVO insCourseVO = (InsCourseVO) intent.getExtras().getSerializable("insCourseVO");


        class CourseDetailViewHoder extends RecyclerView.ViewHolder{
            ImageView ivTeacherPic;
            TextView tvCourseName;
            TextView tvTeacherName;
            TextView tvCourseDetail;
            public CourseDetailViewHoder(View view) {
                super(view);
                ivTeacherPic = view.findViewById(R.id.ivTeacherPic_D);
                tvCourseName = view.findViewById(R.id.tvCourseName_D);
                tvTeacherName = view.findViewById(R.id.tvTeacherName_D);
                tvCourseDetail = view.findViewById(R.id.tvCourseDetail_D);
            }
        }

        class CourseReservationViewHolder extends RecyclerView.ViewHolder{
            public CourseReservationViewHolder(View view) {
                super(view);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType){
                case 0:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_course_detail,parent,false);
                    return new CourseDetailViewHoder(view);
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_course_reservation,parent,false);
                    return new CourseReservationViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            switch (position){
//                case 0:
//                    ((CourseDetailViewHoder)holder).ivTeacherPic.setImageResource(insCourseVO.getTeacherPic());
//                    ((CourseDetailViewHoder)holder).tvTeacherName.setText(insCourseVO.getTeacherName());
//                    ((CourseDetailViewHoder)holder).tvCourseName.setText(insCourseVO.getCourseName());
//                    ((CourseDetailViewHoder)holder).tvCourseDetail.setText(insCourseVO.getCourseDetail());
//                    break;
//                case 1:
//                    break;
//            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }



}
