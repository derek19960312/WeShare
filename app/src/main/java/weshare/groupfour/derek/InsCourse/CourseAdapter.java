package weshare.groupfour.derek.InsCourse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import weshare.groupfour.derek.LoginFakeActivity;
import weshare.groupfour.derek.MemberVO;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Join;
import weshare.groupfour.derek.util.Tools;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<InsCourseVO> insCourseVOList;

    public CourseAdapter(List<InsCourseVO> insCourseVOList) {
        this.insCourseVOList = insCourseVOList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivTeacherPic,ivLike,ivConnect;
        private TextView tvCourseName, tvTeacherName;
        private int like;
        Context context;

        public ViewHolder(View view) {
            super(view);
            ivTeacherPic = view.findViewById(R.id.ivTeacherPic);
            ivLike = view.findViewById(R.id.ivLike);
            ivConnect = view.findViewById(R.id.ivConnect);
            tvCourseName = view.findViewById(R.id.tvCourseName);
            tvTeacherName = view.findViewById(R.id.tvName);
            context = view.getContext();
            like = 0;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SharedPreferences spf = new Tools().getSharePreAccount();

        final InsCourseVO insCourseVO = insCourseVOList.get(position);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        MemberVO memberVO = null;
        Bitmap bitmap = null;
        final Bundle bundle = new Bundle();

        //取回老師名稱+會員資料
        memberVO = new Join().getMemberbyteacherId(insCourseVO.getTeacherId());
                    //取回圖片
        byte[] memImage = new Join().getMemberPic(memberVO.getMemId());
        memberVO.setMemImage(memImage);
        bitmap = BitmapFactory.decodeByteArray(memImage, 0, memImage.length);

        bundle.putSerializable("insCourseVO", insCourseVO);
        bundle.putSerializable("memberVO", memberVO);

        holder.ivTeacherPic.setImageBitmap(bitmap);
        holder.tvTeacherName.setText(memberVO.getMemName());
        holder.tvCourseName.setText(insCourseVO.getCourseId());
        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memId = spf.getString("memId",null);
                if(memId != null){
                    switch(holder.like){
                        case 0:
                            holder.ivLike.setImageResource(R.drawable.hearted);
                            new CourseLike().addCourseLike(memId,insCourseVO.getInscId());
                            Toast.makeText(holder.context,"已加入收藏", Toast.LENGTH_SHORT).show();
                            holder.like = 1;
                            break;
                        case 1:
                            holder.ivLike.setImageResource(R.drawable.heart);
                            new CourseLike().deleteCourseLike(memId,insCourseVO.getInscId());
                            Toast.makeText(holder.context,"已取消收藏",Toast.LENGTH_SHORT).show();
                            holder.like = 0;
                            break;
                    }
                }else{
                    Toast.makeText(holder.context,"請先登入",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(holder.context, LoginFakeActivity.class);
                    holder.context.startActivity(intent);
                }
            }
        });
        holder.ivConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"分享",Toast.LENGTH_SHORT);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), InsCourseDetailActivity.class);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
        final String memId = spf.getString("memId",null);
        //已經加入收藏的課程
        if(memId != null){
            List<InsCourseVO> insCourseVOListbylike = new CourseLike().getMyLikeCourse(memId);
            if(insCourseVOListbylike != null){
                for(InsCourseVO inscVObylike : insCourseVOListbylike){
                    if(inscVObylike.getInscId().equals(insCourseVO.getInscId())){
                        holder.ivLike.setImageResource(R.drawable.hearted);
                        holder.like = 1;
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return insCourseVOList.size();
    }
}