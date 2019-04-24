package weshare.groupfour.derek.courseReservation;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.insCourse.InsCourseTimeVO;
import weshare.groupfour.derek.insCourse.InsCourseVO;
import weshare.groupfour.derek.util.Tools;

public class CheckDateFragment extends Fragment {
    private static int year, month, day, hour;
    private static Button btnDate;
    private static RadioGroup rgDate = null;
    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    private static CourseReservationVO crVO;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_check_date, container, false);

        //時間radioGroup
        rgDate = view.findViewById(R.id.rgDate);


        //日期按鈕
        btnDate = view.findViewById(R.id.btnDate);
        showRightNow();
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                FragmentManager fm = getFragmentManager();
                datePickerFragment.show(fm, "datePicker");
            }
        });

        //送出預約按鈕
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //製作訂單VO
                RadioButton rb = view.findViewById(rgDate.getCheckedRadioButtonId());
                String[] times = rb.getText().toString().split("-");
                Log.e("111",times[0]+" "+times[1]);
                SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd kk:mm");


                //塞入開始結束時間
                crVO = new CourseReservationVO();
                String MDFTime = btnDate.getText().toString()+" "+times[0];
                String EXPTime = btnDate.getText().toString()+" "+times[1];
                try {
                    crVO.setCrvMFD(new Timestamp(sdf.parse(MDFTime).getTime()));
                    crVO.setCrvEXP(new Timestamp(sdf.parse(EXPTime).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("crVO",crVO);
                ReservationConfirmFragment rcfragment = new ReservationConfirmFragment();
                rcfragment.setArguments(bundle);
                FragmentTransaction ft = CourseReservationActivity.fm.beginTransaction();

                ft.replace(R.id.clReservation,rcfragment,"rcfragment");
                ft.commit();


            }
        });
        return view;
    }

    private static void showRightNow() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
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
            return "0" + String.valueOf(day);
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int y, int m, int d) {
                    year = y;
                    month = m;
                    day = d;
                    updateInfo();
                    //取得課程資訊
                    InsCourseVO insCourseVO = (InsCourseVO) getActivity().getIntent().getExtras().get("insCourseVO");
                    java.sql.Date inscDate = new java.sql.Date(y-1900,m,d);


                    //製作請求指令
                    Map<String,String> requestMap = new HashMap<>();
                    requestMap.put("action","get_time_by_inscId_Date");
                    requestMap.put("inscId",insCourseVO.getInscId());
                    requestMap.put("inscDate",inscDate.toString());

                    try {
                        String request = Tools.RequestDataBuilder(requestMap);
                        String result = new CallServlet().execute(ServerURL.IP_INSCOURSE, request).get();
                        Type listType = new TypeToken<List<InsCourseTimeVO>>(){}.getType();
                        List<InsCourseTimeVO> inscTimes = gson.fromJson(result,listType);

                        rgDate.removeAllViews();
                        //動態加入時間
                        for(int i=0; i<inscTimes.size(); i++){
                            RadioButton radioButton = new RadioButton(getContext());
                            radioButton.setTextSize(25);
                            InsCourseTimeVO inscTime = inscTimes.get(i);
                            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
                            radioButton.setText(sdf.format(inscTime.getInscMFD())+"-"+sdf.format(inscTime.getInscEXP()));
                            rgDate.addView(radioButton);
                        }


                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }, year, month, day);

            DatePicker dp = dpd.getDatePicker();



            return dpd;
        }

    }

}
