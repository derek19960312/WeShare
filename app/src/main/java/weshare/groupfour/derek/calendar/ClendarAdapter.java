package weshare.groupfour.derek.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.MapsActivity;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.qrCode.Contents;
import weshare.groupfour.derek.qrCode.QRCodeEncoder;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Join;

public class ClendarAdapter extends RecyclerView.Adapter<ClendarAdapter.ViewHolder>{

    private List<CourseReservationVO> myCourseRvList;
    private Context context;
    private Activity activity;

    public ClendarAdapter(List<CourseReservationVO> myCourseRvList, Context context, Activity activity) {
        this.myCourseRvList = myCourseRvList;
        this.context = context;
        this.activity = activity;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView civPic;
        private TextView tvName;
        private TextView tvCourseName;
        private TextView tvCourseMFD;
        private TextView tvCourseEXP;
        private TextView tvCoursePlace;
        private ImageView Qrcode;
        private LinearLayout llCalendar;
        private ImageView ivQrcode;
        private ImageView ivMap;

        public ViewHolder(View view) {
            super(view);
            civPic = view.findViewById(R.id.civPic);
            tvName = view.findViewById(R.id.tvName);
            tvCourseName = view.findViewById(R.id.tvCourseName);
            tvCourseMFD = view.findViewById(R.id.tvCourseMFD);
            tvCourseEXP = view.findViewById(R.id.tvCourseEXP);
            tvCoursePlace = view.findViewById(R.id.tvCoursePlace);
            Qrcode = view.findViewById(R.id.Qrcode);
            llCalendar = view.findViewById(R.id.llCalendar);
            ivMap = view.findViewById(R.id.ivMap);
            ivQrcode = view.findViewById(R.id.ivQrcode);
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


        holder.tvCourseName.setText("課程名稱："+myCourseRvVO.getInscId());
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
                //可打開Qrcode掃描器
                holder.ivQrcode.setImageResource(R.drawable.scan_qr_code);
                holder.ivQrcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        IntentIntegrator integrator = new IntentIntegrator(activity);

                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        integrator.setPrompt("Scan");       //底部提示的文字
                        integrator.setCameraId(0);          //前面或後面的相機
                        integrator.setBeepEnabled(false);    //掃描成功後發出 BB 聲
                        integrator.setBarcodeImageEnabled(false);
                        integrator.initiateScan();
                    }
                });

                break;

            case 1:
                //以學生身分
                //加入老師名稱
                MemberVO memtVO = join.getMemberbyteacherId(myCourseRvVO.getTeacherId(),context);
                //加入老師圖片
                join.setPicOn(holder.civPic,memtVO.getMemId());

                holder.tvName.setText("老師姓名："+memtVO.getMemName());

                String QrcodeData = Holder.gson.toJson(myCourseRvVO);
                //設定QRCODE
                QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(myCourseRvVO.getCrvId(), null,
                        Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                        200);
                Bitmap bitmap = null;
                try {
                    bitmap = qrCodeEncoder.encodeAsBitmap();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                holder.Qrcode.setImageBitmap(bitmap);

                //可以展開Qrcode
                holder.ivQrcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (holder.Qrcode.getVisibility()){
                            case View.VISIBLE:
                                holder.Qrcode.setVisibility(View.GONE);
                                break;
                            case View.GONE:
                                holder.Qrcode.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                });
                break;
        }


        //可打開詳情
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

            }
        });
        //可開啟地圖
        holder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("locationName",myCourseRvVO.getCrvLoc());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {

        return myCourseRvList.size();
    }
}