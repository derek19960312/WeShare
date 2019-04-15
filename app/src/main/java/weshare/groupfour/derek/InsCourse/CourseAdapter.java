package weshare.groupfour.derek.InsCourse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        MemberVO memberVO = null;
        Bitmap bitmap = null;
        try {
            String memStr = new CallServlet().execute(ServerURL.IP_SEARCH_MEMBER,"action=get_one_by_android&teacherId="+insCourseVO.getTeacherId()).get();
            memberVO = gson.fromJson(memStr,MemberVO.class);

            String memBase64 = new CallServlet().execute(ServerURL.IP_GET_PIC,"action=get_member_pic&memId="+memberVO.getMemId()).get();

            byte[] bmemImage = Base64.decode(memBase64,Base64.DEFAULT);

            memberVO.setMemImage(bmemImage);
            bitmap = BitmapFactory.decodeByteArray(bmemImage,0,bmemImage.length);
            Log.e("erro",String.valueOf(bitmap == null));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        holder.ivTeacherPic.setImageBitmap(bitmap);
        holder.tvTeacherName.setText(memberVO.getMemName());
        holder.tvCourseName.setText(insCourseVO.getCourseId());


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