package weshare.groupfour.derek.calendar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.RequestDataBuilder;
import weshare.groupfour.derek.util.Tools;


public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

//        Toolbar tbCalender = findViewById(R.id.tbCalender);
//        tbCalender.setTitle(R.string.calender);
//        setSupportActionBar(tbCalender);
//        //toolbar 事件
//        tbCalender.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                finish();
//                return true;
//            }
//        });

        //本日無預約
        TextView tvNoReservation = findViewById(R.id.tvNoReservation);


        //撈取我的課程資料
        SharedPreferences spf = Tools.getSharePreAccount();
        Map<String, String> requestMap;
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

        final List<CourseReservationVO> mycrvTec = Holder.gson.fromJson(result, listType);

        //我是學生
        String memId = spf.getString("memId", null);
        requestData = new RequestDataBuilder().build()
                .setAction("find_my_reservation")
                .setData("param", memId)
                .create();

        try {
            result = new CallServlet(this).execute(ServerURL.IP_COURSERESERVATION, requestData).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final List<CourseReservationVO> mycrvMem = Holder.gson.fromJson(result, listType);

        List<EventDay> events = new ArrayList<>();
        for (CourseReservationVO crvo : mycrvMem) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTimeInMillis(crvo.getCrvMFD().getTime());
            events.add(new EventDay(calendar, R.drawable.circle_dot));
        }
        for (CourseReservationVO crvo : mycrvTec) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTimeInMillis(crvo.getCrvMFD().getTime());
            events.add(new EventDay(calendar, R.drawable.circle_dot));
        }

        CalendarView calendarView = findViewById(R.id.calendarView);
        try {
            calendarView.setDate(new Date());
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
        calendarView.setEvents(events);


        final RecyclerView rvClendar = findViewById(R.id.rvClendar);
        rvClendar.setLayoutManager(new LinearLayoutManager(this));

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                Calendar calendar = eventDay.getCalendar();
                List<CourseReservationVO> myCourseRvList = new ArrayList<>();

                long clicked = calendar.getTimeInMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("yy,MM,dd");
                for (CourseReservationVO crVO : mycrvTec) {
                        long crMFD = 0;
                        try {
                            crMFD = sdf.parse(sdf.format(crVO.getCrvMFD())).getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (clicked == crMFD) {
                            crVO.setIdFlag(0);
                            myCourseRvList.add(crVO);
                        }
                    }
                    for (CourseReservationVO crVO : mycrvMem) {
                        long crMFD = 0;
                        try {
                            crMFD = sdf.parse(sdf.format(crVO.getCrvMFD())).getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (crMFD == clicked) {
                            crVO.setIdFlag(1);
                            myCourseRvList.add(crVO);
                        }
                    }

                    rvClendar.setAdapter(new ClendarAdapter(myCourseRvList, CalendarActivity.this, CalendarActivity.this));


                }
            });


        }

//        @Override
//        public boolean onCreateOptionsMenu (Menu menu){
//            // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
//            getMenuInflater().inflate(R.menu.close_menu, menu);
//            return true;
//        }


    }
