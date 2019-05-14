package weshare.groupfour.derek.courseReservation;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.insCourse.InsCourseTimeVO;
import weshare.groupfour.derek.insCourse.InsCourseVO;
import weshare.groupfour.derek.util.Connect_WebSocket;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Tools;

public class CheckDateFragment extends Fragment {

    private static int year, month, day;
    private static Button btnDate;
    public static RadioGroup rgDate = null;

    private static CourseReservationVO crVO;
    private static View view;
    private static ConstraintLayout clBolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check_date, container, false);






        //時間radioGroup
        rgDate = view.findViewById(R.id.rgDate);


        //日期按鈕
        btnDate = view.findViewById(R.id.btnDate);
        showRightNow();
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerFragment datePickerFragment = new DatePickerFragment();
                FragmentManager fm = getFragmentManager();
                datePickerFragment.show(fm, "datePicker");

            }
        });

        //Bolder
        clBolder = view.findViewById(R.id.clBolder);

        //送出預約按鈕
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //製作訂單VO
                    RadioButton rb = view.findViewById(rgDate.getCheckedRadioButtonId());
                    String[] times = rb.getText().toString().split("-");
                    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd kk:mm");


                    crVO = new CourseReservationVO();
                    //取出timeId
                    crVO.setInscTimeId(rb.getTag().toString());
                    //塞入開始結束時間
                    String MDFTime = btnDate.getText().toString() + " " + times[0];
                    String EXPTime = btnDate.getText().toString() + " " + times[1];
                    try {
                        crVO.setCrvMFD(new Timestamp(sdf.parse(MDFTime).getTime()));
                        crVO.setCrvEXP(new Timestamp(sdf.parse(EXPTime).getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("crVO", crVO);
                    ReservationConfirmFragment rcfragment = new ReservationConfirmFragment();
                    rcfragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    ft.replace(R.id.clReservation, rcfragment, "rcfragment");
                    ft.addToBackStack(null);
                    ft.commit();
                }catch (NullPointerException npe){
                    Tools.Toast(getContext(),"請先挑選時間");
                    clBolder.setBackgroundColor(Color.RED);
                }

            }
        });
        return view;
    }

    private static void showRightNow() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        updateInfo();
    }

    private static void updateInfo() {
        btnDate.setText(new StringBuilder().append(year).append("-")
                //「month + 1」是因為一月的值是0而非1
                .append(parseNum(month + 1)).append("-").append(parseNum(day)));
    }

    private static String parseNum(int day) {
        if (day >= 10)
            return String.valueOf(day);
        else
            return "0" + day;
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker views, int y, int m, int d) {
                    clBolder.setBackgroundColor(Color.BLACK);

                    year = y;
                    month = m;
                    day = d;
                    updateInfo();

                    //取得課程資訊
                    InsCourseVO insCourseVO = (InsCourseVO) getActivity().getIntent().getExtras().get("insCourseVO");
                    java.sql.Date inscDate = new java.sql.Date(new GregorianCalendar(y,m,d).getTimeInMillis());


                    //製作請求指令
                    Map<String,String> requestMap = new HashMap<>();
                    requestMap.put("action","get_time_by_inscId_Date");
                    requestMap.put("inscId",insCourseVO.getInscId());
                    requestMap.put("inscDate",inscDate.toString());

                    try {
                        String request = Tools.RequestDataBuilder(requestMap);
                        String result = new CallServlet(getContext()).execute(ServerURL.IP_INSCOURSE, request).get();
                        Type listType = new TypeToken<List<InsCourseTimeVO>>(){}.getType();
                        List<InsCourseTimeVO> inscTimes = Holder.gson.fromJson(result,listType);

                        rgDate.removeAllViews();

                        TextView tvNoTime = view.findViewById(R.id.tvNoTime);


                        if(inscTimes.size() != 0){

                            tvNoTime.setVisibility(View.GONE);
                            //動態加入時間
                            for(int i=0; i<inscTimes.size(); i++){
                                RadioButton radioButton = new RadioButton(getContext());
                                radioButton.setTextSize(25);
                                final InsCourseTimeVO inscTime = inscTimes.get(i);
                                SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
                                radioButton.setText(sdf.format(inscTime.getInscMFD())+"-"+sdf.format(inscTime.getInscEXP()));
                                //放入timeId
                                radioButton.setTag(inscTime.getInscTimeId());

                                //加入按下推撥事件
                                radioButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Connect_WebSocket.grabCourseWebSocketClient.send(inscTime.getInscId());
                                    }
                                });


                                rgDate.addView(radioButton);
                            }
                        }else{
                            tvNoTime.setVisibility(View.VISIBLE);
                        }


                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }, year, month, day);

            DatePicker dp = dpd.getDatePicker();

            Calendar gc = GregorianCalendar.getInstance();
            //dp.setMinDate(gc.getTimeInMillis());
            gc.add(Calendar.DATE,20);
            dp.setMaxDate(gc.getTimeInMillis());

            return dpd;
        }

    }

}
