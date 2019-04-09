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

        //RoundedBitmapDrawable roundedBitmapDrawable;
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

    public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

        private List<InsCourseVO> insCourseVOList;

        public CourseAdapter(List<InsCourseVO> insCourseVOList) {
            this.insCourseVOList = insCourseVOList;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_course_browser,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder( ViewHolder holder, int position) {

            final InsCourseVO insCourseVO = insCourseVOList.get(position);



            holder.ivTeacherPic.setImageResource(insCourseVO.getTeacherPic());
            holder.tvTeacherName.setText(insCourseVO.getTeacherName());
            holder.tvCourseName.setText(insCourseVO.getCourseName());
            holder.tvCourseDetail.setText(insCourseVO.getCourseDetail());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    intent.setClass(InsCourseBrowseActivity.this, InsCourseDetailActivity.class);
                    bundle.putSerializable("insCourseVO", insCourseVO);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return insCourseVOList.size();
        }


    }

    public byte[] compareToByte(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

}
