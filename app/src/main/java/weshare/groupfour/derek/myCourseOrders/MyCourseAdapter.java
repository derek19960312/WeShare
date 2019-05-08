package weshare.groupfour.derek.myCourseOrders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.MapsActivity;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.qrCode.Contents;
import weshare.groupfour.derek.qrCode.QRCodeEncoder;
import weshare.groupfour.derek.util.Join;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.ViewHolder> {
    public final static int MEMBER = 0;
    public final static int TEACHER = 1;
    private int fromWhere;
    private Context context;
    private List<CourseReservationVO> myCourseRvList;
    private Fragment fragment;

    public MyCourseAdapter(List<CourseReservationVO> myCourseRvList, int fromWhere, Context context, Fragment fragment) {
        this.myCourseRvList = myCourseRvList;
        this.fromWhere = fromWhere;
        this.context = context;
        this.fragment = fragment;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civPic;
        private TextView tvName;
        private TextView tvCourseName;
        private TextView tvCourseMFD;
        private TextView tvCourseEXP;
        private TextView tvCoursePlace;
        private TextView tvOrderNum;
        private TextView tvAlready;
        private ImageView ivMap;
        private ImageView ivQrcode;
        private ImageView Qrcode;
        private CardView courseCard;

        public ViewHolder(View view) {
            super(view);
            civPic = view.findViewById(R.id.civPic);
            tvName = view.findViewById(R.id.tvName);
            tvCourseName = view.findViewById(R.id.tvCourseName);
            tvCourseMFD = view.findViewById(R.id.tvCourseMFD);
            tvCourseEXP = view.findViewById(R.id.tvCourseEXP);
            tvCoursePlace = view.findViewById(R.id.tvCoursePlace);
            tvOrderNum = view.findViewById(R.id.tvOrderNum);
            ivMap = view.findViewById(R.id.ivMap);
            ivQrcode = view.findViewById(R.id.ivQrcode);
            Qrcode = view.findViewById(R.id.sQrcode);
            courseCard = view.findViewById(R.id.courseCard);
            tvAlready = view.findViewById(R.id.tvAlready);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_mycourse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CourseReservationVO myCourseRvVO = myCourseRvList.get(position);


        holder.tvCourseName.setText("課程名稱：" + myCourseRvVO.getInscId());
        holder.tvCoursePlace.setText(myCourseRvVO.getCrvLoc());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd a hh:mm");
        holder.tvCourseMFD.setText(sdf.format(myCourseRvVO.getCrvMFD()));
        holder.tvCourseEXP.setText(sdf.format(myCourseRvVO.getCrvEXP()));
        holder.tvOrderNum.setText("預約編號：" + myCourseRvVO.getCrvId());

        holder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("locationName", myCourseRvVO.getCrvLoc());
                context.startActivity(intent);
            }
        });

        Join join = new Join();
        switch (fromWhere) {
            case TEACHER:

                //加入學生名稱
                MemberVO memVO = join.getMemberbyMemId(myCourseRvVO.getMemId(), context);
                //加入學生圖片
                join.setPicOn(holder.civPic, memVO.getMemId());

                holder.tvName.setText("學生姓名：" + memVO.getMemName());


                //開啟QRcode掃描器
                holder.ivQrcode.setImageResource(R.drawable.scan_qr_code);
                holder.ivQrcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(fragment);

                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        integrator.setPrompt("Scan");       //底部提示的文字
                        integrator.setCameraId(0);          //前面或後面的相機
                        integrator.setBeepEnabled(false);    //掃描成功後發出 BB 聲
                        integrator.setBarcodeImageEnabled(false);
                        integrator.initiateScan();


                    }
                });

                break;
            case MEMBER:

                //加入老師名稱
                MemberVO memtVO = join.getMemberbyteacherId(myCourseRvVO.getTeacherId(), context);
                //加入老師圖片
                join.setPicOn(holder.civPic, memtVO.getMemId());

                holder.tvName.setText("老師姓名：" + memtVO.getMemName());


                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .create();

                String QrcodeData = gson.toJson(myCourseRvVO);
                //設定QRCODE
                QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(QrcodeData, null,
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
                    public void onClick(View view) {
                        switch (holder.Qrcode.getVisibility()) {
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

        if (myCourseRvVO.getClassStatus() == 1) {
            holder.ivMap.setClickable(false);
            holder.courseCard.setAlpha(0.5f);
            holder.tvAlready.setVisibility(View.VISIBLE);
            holder.ivQrcode.setClickable(false);
        }

    }

    @Override
    public int getItemCount() {

        return myCourseRvList.size();
    }


}