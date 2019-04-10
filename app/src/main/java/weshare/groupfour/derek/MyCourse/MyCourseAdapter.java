package weshare.groupfour.derek.MyCourse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.R;

public class MyCourseAdapter extends RecyclerView.Adapter {
    private List myCourseList;
    public MyCourseAdapter(List myCourseList) {
        this.myCourseList = myCourseList;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView civTeacherPic;
        private TextView tvTeacherName;
        private TextView tvCourseName;
        private TextView tvCourseTime;
        private TextView tvCoursePlace;
        public ViewHolder(View view) {
            super(view);
            civTeacherPic = view.findViewById(R.id.civTeacherPic);
            tvTeacherName = view.findViewById(R.id.tvTeacherName);
            tvCourseName = view.findViewById(R.id.tvCourseName);
            tvCourseTime = view.findViewById(R.id.tvCourseTime);
            tvCoursePlace = view.findViewById(R.id.tvCoursePlace);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_mycourse,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //myCourseList.get(position);

    }

    @Override
    public int getItemCount() {
        return 10;
        // return myCourseList.size();
    }
}