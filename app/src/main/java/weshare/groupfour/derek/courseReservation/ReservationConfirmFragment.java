package weshare.groupfour.derek.courseReservation;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.insCourse.InsCourseVO;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.util.Tools;

public class ReservationConfirmFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_confirm, container, false);

        InsCourseVO insCourseVO = (InsCourseVO) getActivity().getIntent().getExtras().get("insCourseVO");

        MemberVO TeaMemVO = (MemberVO) getActivity().getIntent().getExtras().get("TeaMemVO");

        SharedPreferences spf = Tools.getSharePreAccount();
        String memId = spf.getString("memId", null);



        final CourseReservationVO crVO = (CourseReservationVO) getArguments().getSerializable("crVO");

        //塞入預覽預約資料
        TextView tvInscName = view.findViewById(R.id.tvInscName);
        TextView tvTeacherName = view.findViewById(R.id.tvTeacherName);
        TextView tvInscLoc = view.findViewById(R.id.tvInscLoc);
        TextView tvDateTime = view.findViewById(R.id.tvDateTime);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvFee = view.findViewById(R.id.tvFee);
        TextView tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        EditText etMore = view.findViewById(R.id.etMore);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        tvInscName.setText(insCourseVO.getCourseId());
        tvTeacherName.setText(TeaMemVO.getMemName());
        tvInscLoc.setText(insCourseVO.getInscLoc());

        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd kk:mm");
        tvDateTime.setText(sdf.format(crVO.getCrvMFD())+"-"+sdf.format((crVO.getCrvEXP())));




        //算總時數
        long diff = (crVO.getCrvEXP().getTime())-(crVO.getCrvMFD().getTime());
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        Double totalTime = new Double(hours);


        int inscPrice = insCourseVO.getInscPrice();
        tvPrice.setText(String.valueOf(inscPrice)+" X "+totalTime+" = "+String.valueOf(inscPrice*totalTime));
        tvFee.setText(String.valueOf(inscPrice)+" X 0.05 = "+String.valueOf(inscPrice*0.05));

        Double totalPrice = inscPrice*totalTime+inscPrice*0.05;
        tvTotalPrice.setText(String.valueOf(totalPrice));

        //製作預約VO
        crVO.setInscId(insCourseVO.getInscId());
        crVO.setMemId(memId);
        crVO.setTeacherId(insCourseVO.getTeacherId());
        crVO.setCrvTotalPrice(totalPrice);
        crVO.setCrvLoc(insCourseVO.getInscLoc());
        crVO.setCrvTotalTime(totalTime);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();

                Map<String,String> requestMap = new HashMap<>();
                requestMap.put("action","make_new_reservation");
                requestMap.put("crVO",gson.toJson(crVO));
                String request = Tools.RequestDataBuilder(requestMap);
                new CallServlet().execute(ServerURL.IP_COURSERESERVATION,request);
                Tools.Toast(getContext(),"已成功預約");
                getActivity().finish();
            }
        });

        return view;
    }
}
