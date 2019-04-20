package weshare.groupfour.derek.InsCourse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.Member.TeacherVO;
import weshare.groupfour.derek.MemberVO;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Tools;

public class InsCourseDetailActivity extends AppCompatActivity {
    InsCourseVO MinsCourseVO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscourse_detail);

        RecyclerView rvCourse = findViewById(R.id.rvCourse);
        rvCourse.setLayoutManager(new LinearLayoutManager(this));
        ImageView ivTeacherPic = findViewById(R.id.ivTeacherPic);
        TextView tvTeacherName = findViewById(R.id.tvName);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        //上層傳來
        Intent intent = getIntent();
        MinsCourseVO = (InsCourseVO) intent.getExtras().getSerializable("insCourseVO");
        MemberVO memberVO = (MemberVO) intent.getExtras().getSerializable("memberVO");

        //撈老師資料
        Map<String,String> requestT = new HashMap<>();
        requestT.put("action","find_by_teacherId");
        requestT.put("teacherId",MinsCourseVO.getTeacherId());
        String requestDataT = Tools.RequestDataBuilder(requestT);
        String resultT = null;
        try {
            resultT = new CallServlet().execute(ServerURL.IP_COURSE,requestDataT).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TeacherVO teacherVO = gson.fromJson(resultT,TeacherVO.class);
        TextView tvDrgee = findViewById(R.id.tvDegree);
        TextView tvAbouts = findViewById(R.id.tvAbouts);
        if(teacherVO != null){
            tvDrgee.setText(teacherVO.getTeacherEdu());
            tvAbouts.setText(teacherVO.getTeacherText());
        }

        //去撈課程
        Map<String,String> requestC = new HashMap<>();
        requestC.put("action","find_by_teacher");
        requestC.put("teacherId",MinsCourseVO.getTeacherId());
        String requestDataC = Tools.RequestDataBuilder(requestC);
        String resultC = null;
        try {
            resultC = new CallServlet().execute(ServerURL.IP_INSCOURSE,requestDataC).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Type listType = new TypeToken<List<InsCourseVO>>() {}.getType();
        List<InsCourseVO> insCourseVOS = gson.fromJson(resultC,listType);
        if(insCourseVOS != null){
            rvCourse.setAdapter(new InscAdpter(insCourseVOS));
        }

        //detail2收合
        final LinearLayout detail2 = findViewById(R.id.detail2);
        CardView cvCourse = findViewById(R.id.cvCourse);
        cvCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Visibility = detail2.getVisibility();
                switch(Visibility){
                    case View.VISIBLE:
                        detail2.setVisibility(View.GONE);
                        break;
                    case View.GONE:
                        detail2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


        //收藏判斷



        //塞資料
        Bitmap bitmap = BitmapFactory.decodeByteArray(memberVO.getMemImage(), 0, memberVO.getMemImage().length);
        ivTeacherPic.setImageBitmap(bitmap);
        tvTeacherName.setText(memberVO.getMemName());





    }

    private class InscAdpter extends RecyclerView.Adapter<InscAdpter.ViewHolder> {
        private List<InsCourseVO> insCourseVOS;


        public InscAdpter(List<InsCourseVO> insCourseVOS) {
            this.insCourseVOS = insCourseVOS;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvCourseName, tvCourseType, tvCourseLang, tvCourseLoc, tvCourseDetail;
            LinearLayout touchToShow;
            Button btnCrv;


            public ViewHolder(View view) {
                super(view);
                tvCourseName = view.findViewById(R.id.tvCourseName);
                tvCourseType = view.findViewById(R.id.tvCourseType);
                tvCourseLang = view.findViewById(R.id.tvCourseLang);
                tvCourseLoc = view.findViewById(R.id.tvCourseLoc);
                tvCourseDetail = view.findViewById(R.id.tvCourseDetail);
                touchToShow = view.findViewById(R.id.touchToShow);
                btnCrv = view.findViewById(R.id.btnCrv);
            }
        }

        @Override
        public InscAdpter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sensei_no_course,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final InscAdpter.ViewHolder holder, int position) {
            InsCourseVO insCourseVO = insCourseVOS.get(position);
            if(insCourseVO.getInscId().equals(MinsCourseVO.getInscId())){
                holder.touchToShow.setVisibility(View.VISIBLE);
            }
            holder.tvCourseName.setText(insCourseVO.getCourseId());
            holder.tvCourseName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Visibility = holder.touchToShow.getVisibility();
                    switch(Visibility){
                        case View.VISIBLE:
                            holder.touchToShow.setVisibility(View.GONE);
                            break;
                        case View.GONE:
                            holder.touchToShow.setVisibility(View.VISIBLE);
                            break;
                    }

                }
            });
            switch(insCourseVO.getInscType()){
                case 0:
                    holder.tvCourseType.setText(R.string.Personal);
                    break;
                case 1:
                    holder.tvCourseType.setText(R.string.Group);
                    break;
            }
            holder.tvCourseLang.setText(insCourseVO.getInscLang());
            holder.tvCourseLoc.setText(insCourseVO.getInscLoc());
            holder.tvCourseDetail.setText(insCourseVO.getInscCourser());
            holder.btnCrv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return insCourseVOS.size();
        }
    }


}
