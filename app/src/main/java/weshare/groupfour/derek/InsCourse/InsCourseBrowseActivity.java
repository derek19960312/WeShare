package weshare.groupfour.derek.InsCourse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscourse__browse);
        RecyclerView recycleView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

        final List<InsCourse> insCourseList = new ArrayList<>();

        //RoundedBitmapDrawable roundedBitmapDrawable;
        int j = 0;
        for(int i=0; i<6; i++){
            if(j == 3){
                j=0;
            }else{
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.teacher+j);

//                roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
//                roundedBitmapDrawable.setCircular(true);
                j++;



                insCourseList.add(new InsCourse(R.drawable.teacher+j,"English","MIMI","英文是值得投資的!!!!"));
            }
        }

        recycleView.setAdapter(new CourseAdapter(insCourseList));
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

        private List<InsCourse> insCourseList;

        public CourseAdapter(List<InsCourse> insCourseList) {
            this.insCourseList = insCourseList;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inscourse_card,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder( ViewHolder holder, int position) {

            final InsCourse insCourse = insCourseList.get(position);



            holder.ivTeacherPic.setImageResource(insCourse.getTeacherPic());
            holder.tvTeacherName.setText(insCourse.getTeacherName());
            holder.tvCourseName.setText(insCourse.getCourseName());
            holder.tvCourseDetail.setText(insCourse.getCourseDetail());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    intent.setClass(InsCourseBrowseActivity.this, InsCourseDetailActivity.class);
                    bundle.putSerializable("insCourse", insCourse);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return insCourseList.size();
        }


    }

    public byte[] compareToByte(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

}
