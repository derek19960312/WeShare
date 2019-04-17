package weshare.groupfour.derek.MyCourse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.CourseReservation.CourseReservationVO;
import weshare.groupfour.derek.MemberVO;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Join;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.ViewHolder>{
    public final static int MEMBER = 0;
    public final static int TEACHER = 1;
    private int fromWhere;
    private List<CourseReservationVO> myCourseRvList;
    public MyCourseAdapter(List<CourseReservationVO> myCourseRvList, int fromWhere) {
        this.myCourseRvList = myCourseRvList;
        this.fromWhere = fromWhere;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView civPic;
        private TextView tvName;
        private TextView tvCourseName;
        private TextView tvCourseTime;
        private TextView tvCoursePlace;
        private TextView tvQrcode;

        public ViewHolder(View view) {
            super(view);
            civPic = view.findViewById(R.id.civPic);
            tvName = view.findViewById(R.id.tvName);
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
        CourseReservationVO myCourseRvVO = (CourseReservationVO) myCourseRvList.get(position);

        switch (fromWhere){
            case TEACHER:

                break;
            case MEMBER:

                Join join = new Join();
                //加入老師名稱
                MemberVO memVO = join.getMemberbyteacherId(myCourseRvVO.getTeacherId());
                myCourseRvVO.setTeacherId(memVO.getMemId());
                //加入老師圖片
                byte[] memImage = join.getMemberPic(memVO.getMemId());
                memVO.setMemImage(memImage);
                Bitmap bitmap = BitmapFactory.decodeByteArray(memImage, 0, memImage.length);


                holder.civPic.setImageBitmap(bitmap);
                holder.tvCourseName.setText(myCourseRvVO.getInscId());
                holder.tvName.setText(myCourseRvVO.getTeacherId());
                holder.tvCoursePlace.setText(myCourseRvVO.getCrvLoc());
                holder.tvCourseTime.setText(myCourseRvVO.getCrvMFD().toString()+" - "+myCourseRvVO.getCrvEXP().toString());


                //可以展開Qrcode
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
                break;
        }





    }

    @Override
    public int getItemCount() {

        return myCourseRvList.size();
    }
}