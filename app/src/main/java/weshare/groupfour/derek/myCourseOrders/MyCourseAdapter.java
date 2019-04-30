package weshare.groupfour.derek.myCourseOrders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Join;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.ViewHolder>{
    public final static int MEMBER = 0;
    public final static int TEACHER = 1;
    private int fromWhere;
    private Context context;
    private List<CourseReservationVO> myCourseRvList;
    public MyCourseAdapter(List<CourseReservationVO> myCourseRvList, int fromWhere, Context context) {
        this.myCourseRvList = myCourseRvList;
        this.fromWhere = fromWhere;
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
        private TextView tvOrderNum;
        private ImageView ivMap;
        private ImageView ivQrcode;



        public ViewHolder(View view) {
            super(view);
            civPic = view.findViewById(R.id.civPic);
            tvName = view.findViewById(R.id.tvName);
            tvCourseName = view.findViewById(R.id.tvCourseName);
            tvCourseMFD = view.findViewById(R.id.tvCourseMFD);
            tvCourseEXP = view.findViewById(R.id.tvCourseEXP);
            tvCoursePlace = view.findViewById(R.id.tvCoursePlace);
            tvQrcode = view.findViewById(R.id.tvQrcode);
            tvOrderNum = view.findViewById(R.id.tvOrderNum);
            ivMap = view.findViewById(R.id.ivMap);
            ivQrcode = view.findViewById(R.id.ivQrcode);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_mycourse,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CourseReservationVO myCourseRvVO = myCourseRvList.get(position);



        holder.tvCourseName.setText("課程名稱："+myCourseRvVO.getInscId());
        holder.tvCoursePlace.setText(myCourseRvVO.getCrvLoc());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd a hh:mm");
        holder.tvCourseMFD.setText(sdf.format(myCourseRvVO.getCrvMFD()));
        holder.tvCourseEXP.setText(sdf.format(myCourseRvVO.getCrvEXP()));
        holder.tvOrderNum.setText("預約編號："+myCourseRvVO.getCrvId());

        holder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Join join = new Join();
        switch (fromWhere){
            case TEACHER:

                //加入學生名稱
                MemberVO memVO = join.getMemberbyMemId(myCourseRvVO.getMemId(),context);
                myCourseRvVO.setTeacherId(memVO.getMemName());
                //加入學生圖片
                join.setPicOn(holder.civPic,memVO.getMemId());

                holder.tvName.setText("學生姓名："+myCourseRvVO.getTeacherId());


                //開啟QRcode掃描器
                holder.ivQrcode.setImageResource(R.drawable.scan_qr_code);
                holder.ivQrcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                break;
            case MEMBER:

                //加入老師名稱
                MemberVO memtVO = join.getMemberbyteacherId(myCourseRvVO.getTeacherId(),context);
                myCourseRvVO.setTeacherId(memtVO.getMemName());
                //加入老師圖片
                join.setPicOn(holder.civPic,memtVO.getMemId());

                holder.tvName.setText("老師姓名："+myCourseRvVO.getTeacherId());

                //可以展開Qrcode
                holder.ivQrcode.setOnClickListener(new View.OnClickListener() {
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
                break;
        }





    }

    @Override
    public int getItemCount() {

        return myCourseRvList.size();
    }
}