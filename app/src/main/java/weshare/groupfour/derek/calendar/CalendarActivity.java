package weshare.groupfour.derek.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.myCourseOrders.MyReservationFragment;
import weshare.groupfour.derek.util.Tools;


public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar tbCalender = findViewById(R.id.tbCalender);
        tbCalender.setTitle(R.string.calender);
        setSupportActionBar(tbCalender);
        //toolbar 事件
        tbCalender.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                finish();
                return true;
            }
        });


        //撈取我的課程資料
        SharedPreferences spf = Tools.getSharePreAccount();
        Map<String, String> requestMap;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        Type listType = new TypeToken<List<CourseReservationVO>>() {
        }.getType();
        //我是老師
        String teacherId = spf.getString("teacherId", null);
        requestMap = new HashMap<>();
        requestMap.put("action", "find_my_reservation");
        requestMap.put("param", teacherId);
        String requestData = Tools.RequestDataBuilder(requestMap);
        String result = null;
        try {
            result = new CallServlet(this).execute(ServerURL.IP_COURSERESERVATION, requestData).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final List<CourseReservationVO> mycrvTec = gson.fromJson(result, listType);

        //我是學生
        String memId = spf.getString("memId", null);
        requestMap = new HashMap<>();
        requestMap.put("action", "find_my_reservation");
        requestMap.put("param", memId);
        requestData = Tools.RequestDataBuilder(requestMap);
        try {
            result = new CallServlet(this).execute(ServerURL.IP_COURSERESERVATION, requestData).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final List<CourseReservationVO> mycrvMem = gson.fromJson(result, listType);

        final RecyclerView rvClendar = findViewById(R.id.rvClendar);
        rvClendar.setLayoutManager(new LinearLayoutManager(this));

        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                List<CourseReservationVO> myCourseRvList = new ArrayList<>();

                long clicked = new GregorianCalendar(year, month, dayOfMonth).getTimeInMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("yy,MM,dd");
                for(CourseReservationVO crVO : mycrvTec){
                    long crMFD = 0;
                    try {
                        crMFD = sdf.parse(sdf.format(crVO.getCrvMFD())).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(clicked == crMFD){
                        crVO.setIdFlag(1);
                        myCourseRvList.add(crVO);
                    }
                }
                for(CourseReservationVO crVO : mycrvMem){
                    long crMFD = 0;
                    try {
                        crMFD = sdf.parse(sdf.format(crVO.getCrvMFD())).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(crMFD == clicked){
                        crVO.setIdFlag(0);
                        myCourseRvList.add(crVO);
                    }
                }

                rvClendar.setAdapter(new ClendarAdapter(myCourseRvList,CalendarActivity.this));


            }
        });










    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.close_menu, menu);
        return true;
    }


}
