package weshare.groupfour.derek.calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.util.Join;

public class ClendarAdapter extends RecyclerView.Adapter<ClendarAdapter.ViewHolder>{

    private List<CourseReservationVO> myCourseRvList;
    private Context context;

    public ClendarAdapter(List<CourseReservationVO> myCourseRvList, Context context) {
        this.myCourseRvList = myCourseRvList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView civPic;
        private TextView tvName;
        private TextView tvCourseName;
        private TextView tvCourseMFD;
        private TextView tvCourseEXP;
        private TextView tvCoursePlace;
        private TextView tvQrcode;
        private LinearLayout llCalendar;

        public ViewHolder(View view) {
            super(view);
            civPic = view.findViewById(R.id.civPic);
            tvName = view.findViewById(R.id.tvName);
            tvCourseName = view.findViewById(R.id.tvCourseName);
            tvCourseMFD = view.findViewById(R.id.tvCourseMFD);
            tvCourseEXP = view.findViewById(R.id.tvCourseEXP);
            tvCoursePlace = view.findViewById(R.id.tvCoursePlace);
            tvQrcode = view.findViewById(R.id.tvQrcode);
            llCalendar = view.findViewById(R.id.llCalendar);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_myclendar,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CourseReservationVO myCourseRvVO = myCourseRvList.get(position);


        holder.tvCourseName.setText(myCourseRvVO.getInscId());
        holder.tvCoursePlace.setText(myCourseRvVO.getCrvLoc());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd a h點");
        holder.tvCourseMFD.setText(sdf.format(myCourseRvVO.getCrvMFD()));
        holder.tvCourseEXP.setText(sdf.format(myCourseRvVO.getCrvEXP()));



        Join join = new Join();
        switch (myCourseRvVO.getIdFlag()){
            case 0:
                //以老師身分
                //加入學生名稱
                MemberVO memVO = join.getMemberbyMemId(myCourseRvVO.getMemId(),context);
                //加入學生圖片
                join.setPicOn(holder.civPic,memVO.getMemId());
                holder.tvName.setText("學生姓名："+memVO.getMemName());
                break;
            case 1:
                //以學生身分
                //加入老師名稱
                MemberVO memtVO = join.getMemberbyteacherId(myCourseRvVO.getTeacherId(),context);
                //加入老師圖片
                join.setPicOn(holder.civPic,memtVO.getMemId());

                holder.tvName.setText("老師姓名："+memtVO.getMemName());


                //可以展開Qrcode
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (holder.llCalendar.getVisibility()){
                            case View.VISIBLE:
                                holder.llCalendar.setVisibility(View.GONE);
                                break;
                            case View.GONE:
                                holder.llCalendar.setVisibility(View.VISIBLE);
                                break;
                        }
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
                break;
        }





    }

    @Override
    public int getItemCount() {

        return myCourseRvList.size();
    }
}