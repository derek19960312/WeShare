package weshare.groupfour.derek.InsCourse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.util.Tools;
import weshare.groupfour.derek.util.Tools;
import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.MemberVO;
import weshare.groupfour.derek.R;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<InsCourseVO> insCourseVOList;

    public CourseAdapter(List<InsCourseVO> insCourseVOList) {
        this.insCourseVOList = insCourseVOList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivTeacherPic,ivLike,ivConnect;
        private TextView tvCourseName, tvTeacherName;
        private int heart;

        public ViewHolder(View view) {
            super(view);
            ivTeacherPic = view.findViewById(R.id.ivTeacherPic);
            ivLike = view.findViewById(R.id.ivLike);
            ivConnect = view.findViewById(R.id.ivConnect);
            tvCourseName = view.findViewById(R.id.tvCourseName);
            tvTeacherName = view.findViewById(R.id.tvTeacherName);
            heart = 0;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final InsCourseVO insCourseVO = insCourseVOList.get(position);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        MemberVO memberVO = null;
        Bitmap bitmap = null;
        final Bundle bundle = new Bundle();


        try {
            //取回會員資料
            String memStr = new CallServlet().execute(ServerURL.IP_SEARCH_MEMBER, "action=get_one_by_android&teacherId=" + insCourseVO.getTeacherId()).get();
            if (memStr != null) {
                memberVO = gson.fromJson(memStr, MemberVO.class);
                //取回圖片
                    byte[] memImage = new Tools().getPicbymemId(memberVO.getMemId());
                    memberVO.setMemImage(memImage);
                    bitmap = BitmapFactory.decodeByteArray(memImage, 0, memImage.length);

            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bundle.putSerializable("insCourseVO", insCourseVO);
        bundle.putSerializable("memberVO", memberVO);

        holder.ivTeacherPic.setImageBitmap(bitmap);
        holder.tvTeacherName.setText(memberVO.getMemName());
        holder.tvCourseName.setText(insCourseVO.getCourseId());
        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(holder.heart){
                    case 0:
                        holder.ivLike.setImageResource(R.drawable.hearted);
                        //Toast.makeText(GoodsBrowseActivity.this,"已加入收藏",Toast.LENGTH_SHORT).show();
                        holder.heart = 1;
                        break;
                    case 1:
                        holder.ivLike.setImageResource(R.drawable.heart);
                        //Toast.makeText(GoodsBrowseActivity.this,"已取消收藏",Toast.LENGTH_SHORT).show();
                        holder.heart = 0;
                        break;
                }
            }
        });
        holder.ivConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(new Fragment().getActivity(),"分享",Toast.LENGTH_SHORT);
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

    }

    @Override
    public int getItemCount() {
        return insCourseVOList.size();
    }
}