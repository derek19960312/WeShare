package weshare.groupfour.derek.InsCourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import weshare.groupfour.derek.R;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<InsCourseVO> insCourseVOList;

    public CourseAdapter(List<InsCourseVO> insCourseVOList) {
        this.insCourseVOList = insCourseVOList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivTeacherPic;
        private TextView tvCourseName,tvTeacherName;


        public ViewHolder(View view) {
            super(view);
            ivTeacherPic = view.findViewById(R.id.ivTeacherPic);
            tvCourseName = view.findViewById(R.id.tvCourseName);
            tvTeacherName = view.findViewById(R.id.tvTeacherName);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_course,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        final InsCourseVO insCourseVO = insCourseVOList.get(position);



        holder.ivTeacherPic.setImageResource(insCourseVO.getTeacherPic());
        holder.tvTeacherName.setText(insCourseVO.getTeacherName());
        holder.tvCourseName.setText(insCourseVO.getCourseName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(view.getContext(), InsCourseDetailActivity.class);
                bundle.putSerializable("insCourseVO", insCourseVO);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return insCourseVOList.size();
    }


}