package weshare.groupfour.derek.insCourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.courseReservation.CourseReservationActivity;
import weshare.groupfour.derek.home.LoginFakeActivity;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.member.TeacherVO;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Join;
import weshare.groupfour.derek.util.Tools;

public class InsCourseDetailActivity extends AppCompatActivity {
    InsCourseVO MinsCourseVO;
    MemberVO TeaMemVO;
    int like;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscourse_detail);

        RecyclerView rvCourse = findViewById(R.id.rvCourse);
        rvCourse.setLayoutManager(new LinearLayoutManager(this));
        ImageView ivTeacherPic = findViewById(R.id.ivTeacherPic);
        TextView tvTeacherName = findViewById(R.id.tvName);
        //上層傳來
        Intent intent = getIntent();
        MinsCourseVO = (InsCourseVO) intent.getExtras().getSerializable("insCourseVO");
        TeaMemVO = (MemberVO) intent.getExtras().getSerializable("TeaMemVO");

        //分享按鈕
        ImageView ivConnect = findViewById(R.id.ivConnect);
        ivConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.Toast(InsCourseDetailActivity.this,"已分享");
            }
        });


        //評分星星
        RatingBar rbCourse = findViewById(R.id.rbCourse);
        Map<String, String> request = new HashMap<>();
        request.put("action", "get_star_count");
        request.put("param", MinsCourseVO.getTeacherId());
        String requestData = Tools.RequestDataBuilder(request);
        String result = null;
        try {
            result = new CallServlet(this).execute(ServerURL.IP_COURSERESERVATION, requestData).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rbCourse.setRating((float) Double.parseDouble(result));


        //撈老師資料
        Map<String, String> requestT = new HashMap<>();
        requestT.put("action", "find_by_teacherId");
        requestT.put("teacherId", MinsCourseVO.getTeacherId());
        String requestDataT = Tools.RequestDataBuilder(requestT);
        String resultT = null;
        try {
            resultT = new CallServlet(this).execute(ServerURL.IP_TEACHER, requestDataT).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TeacherVO teacherVO = Holder.gson.fromJson(resultT, TeacherVO.class);
        TextView tvDrgee = findViewById(R.id.tvDegree);
        TextView tvAbouts = findViewById(R.id.tvAbouts);
        if (teacherVO != null) {
            tvDrgee.setText("學歷：  "+teacherVO.getTeacherEdu());
            tvAbouts.setText("關於老師："+teacherVO.getTeacherText());
        }

        //去撈課程
        Map<String, String> requestC = new HashMap<>();
        requestC.put("action", "find_by_teacher");
        requestC.put("teacherId", MinsCourseVO.getTeacherId());
        String requestDataC = Tools.RequestDataBuilder(requestC);
        String resultC = null;
        try {
            resultC = new CallServlet(this).execute(ServerURL.IP_INSCOURSE, requestDataC).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Type listType = new TypeToken<List<InsCourseVO>>() {
        }.getType();
        List<InsCourseVO> insCourseVOS = Holder.gson.fromJson(resultC, listType);
        if (insCourseVOS != null) {
            rvCourse.setAdapter(new InscAdpter(insCourseVOS));
        }

        //detail2收合
        final LinearLayout detail2 = findViewById(R.id.detail2);
        CardView cvCourse = findViewById(R.id.cvCourse);
        cvCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Visibility = detail2.getVisibility();
                switch (Visibility) {
                    case View.VISIBLE:
                        detail2.setVisibility(View.GONE);
                        break;
                    case View.GONE:
                        detail2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        final ImageView ivLike = findViewById(R.id.ivLike);
        //收藏判斷
        if(new CourseLike().isLikedCourse(MinsCourseVO.getInscId())){
            ivLike.setImageResource(R.drawable.hearted);
            like = 1;
        }
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memId = Tools.getSharePreAccount().getString("memId", null);
                if (memId != null) {
                    switch (like) {
                        case 0:
                            ivLike.setImageResource(R.drawable.hearted);
                            new CourseLike().addCourseLike(memId, MinsCourseVO.getInscId(), InsCourseDetailActivity.this);
                            Toast.makeText(InsCourseDetailActivity.this, "已加入收藏", Toast.LENGTH_SHORT).show();
                            like = 1;
                            break;
                        case 1:
                            ivLike.setImageResource(R.drawable.heart);
                            new CourseLike().deleteCourseLike(memId, MinsCourseVO.getInscId(), InsCourseDetailActivity.this);
                            Toast.makeText(InsCourseDetailActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                            like = 0;
                            break;
                    }
                } else {
                    Intent intent = new Intent(InsCourseDetailActivity.this, LoginFakeActivity.class);
                    startActivity(intent);
                }
            }
        });


        //塞資料
        Join.setPicOn(ivTeacherPic,TeaMemVO.getMemId());
        tvTeacherName.setText(TeaMemVO.getMemName());


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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sensei_no_course, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final InscAdpter.ViewHolder holder, int position) {
            final InsCourseVO insCourseVO = insCourseVOS.get(position);
            if (insCourseVO.getInscId().equals(MinsCourseVO.getInscId())) {
                holder.touchToShow.setVisibility(View.VISIBLE);
            }
            holder.tvCourseName.setText(insCourseVO.getCourseId());
            holder.tvCourseName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Visibility = holder.touchToShow.getVisibility();
                    switch (Visibility) {
                        case View.VISIBLE:
                            holder.touchToShow.setVisibility(View.GONE);
                            break;
                        case View.GONE:
                            holder.touchToShow.setVisibility(View.VISIBLE);
                            break;
                    }

                }
            });
            switch (insCourseVO.getInscType()) {
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

            //送出預約
            holder.btnCrv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(InsCourseDetailActivity.this, CourseReservationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("insCourseVO",insCourseVO);
                    bundle.putSerializable("TeaMemVO",TeaMemVO);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return insCourseVOS.size();
        }
    }
}
