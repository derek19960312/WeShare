package weshare.groupfour.derek.MyCourse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.R;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.ViewHolder>{
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
        private TextView tvQrcode;

        public ViewHolder(View view) {
            super(view);
            civTeacherPic = view.findViewById(R.id.civTeacherPic);
            tvTeacherName = view.findViewById(R.id.tvTeacherName);
            tvCourseName = view.findViewById(R.id.tvCourseName);
            tvCourseTime = view.findViewById(R.id.tvCourseTime);
            tvCoursePlace = view.findViewById(R.id.tvCoursePlace);
            tvQrcode = view.findViewById(R.id.tvQrcode);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_mycourse,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MyCourseVO myCourseVO = (MyCourseVO)myCourseList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (holder.tvQrcode.getVisibility()){
                    case View.VISIBLE:
                        holder.tvQrcode.setVisibility(View.GONE);
                        break;
                    case View.GONE:
                        holder.tvQrcode.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return myCourseList.size();
    }
}