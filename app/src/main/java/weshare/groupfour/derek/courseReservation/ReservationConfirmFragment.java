package weshare.groupfour.derek.courseReservation;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import android.os.Bundle;

import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.goods.GoodsCartActivity;
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

        Toolbar tbReservation = getActivity().findViewById(R.id.tbReservation);
        tbReservation.setTitle("訂單確認");


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

        SimpleDateFormat sdfMFD = new SimpleDateFormat("yyyy/MM/dd kk:mm");
        SimpleDateFormat sdfEXP = new SimpleDateFormat("kk:mm");
        tvDateTime.setText(sdfMFD.format(crVO.getCrvMFD())+" ~ "+sdfEXP.format((crVO.getCrvEXP())));




        //算總時數
        long diff = (crVO.getCrvEXP().getTime())-(crVO.getCrvMFD().getTime());
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        Double totalTime = new Double(hours);


        int inscPrice = insCourseVO.getInscPrice();
        tvPrice.setText(inscPrice+" X "+totalTime+" = "+inscPrice*totalTime);
        tvFee.setText(inscPrice+" X 0.1 = "+inscPrice*0.1);

        Double totalPrice = inscPrice*totalTime+inscPrice*0.1;
        tvTotalPrice.setText(String.valueOf(totalPrice));

        //製作預約VO
        crVO.setInscId(insCourseVO.getInscId());
        crVO.setMemId(memId);
        crVO.setTeacherId(insCourseVO.getTeacherId());
        crVO.setCrvTotalPrice(totalPrice);
        crVO.setCrvLoc(insCourseVO.getInscLoc());
        crVO.setCrvTotalTime(totalTime);
        crVO.setTeamId(null);
        crVO.setCrvStatus(1);
        crVO.setClassStatus(0);
        crVO.setTranStatus(0);
        crVO.setCrvRate(null);



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
                String result = null;
                try {
                    result = new CallServlet(getContext()).execute(ServerURL.IP_COURSERESERVATION,request).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(result.isEmpty()){
                    Tools.Toast(getContext(),"已成功預約");
                    getActivity().finish();

                }else if(result.equals("Insufficient_account_balance")){
                    Tools.Toast(getContext(),"餘額不足，請先儲值");
                    AlertDialog.Builder builderSuccess = new AlertDialog.Builder(getContext());
                    builderSuccess.setMessage("餘額不足是否前往儲值頁面")
                            .setCancelable(false)
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Intent intent = new Intent(GoodsCartActivity.this,);
                                    //startActivity(intent);
                                    //finish();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();

                }else if(result.equals("can't_buy_your_course")){

                    Tools.Toast(getContext(),"無法購買自己開授的課程");
                    getActivity().finish();

                }else if(result.equals("course_not_exsist")){
                    Tools.Toast(getContext(),"不好意思，課程已被訂走，請您重新選擇");
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                }
            }
        });

        return view;
    }
}
