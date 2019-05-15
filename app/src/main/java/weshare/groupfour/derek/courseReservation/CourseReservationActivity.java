package weshare.groupfour.derek.courseReservation;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.insCourse.InsCourseTimeVO;
import weshare.groupfour.derek.insCourse.InsCourseVO;
import weshare.groupfour.derek.util.Connect_WebSocket;
import weshare.groupfour.derek.util.Holder;


public class CourseReservationActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_reservation);



        Toolbar tbReservation = findViewById(R.id.tbReservation);
        setSupportActionBar(tbReservation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        tbReservation.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                finish();
                return true;
            }
        });


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CheckDateFragment cdf = new CheckDateFragment();
        Fragment rcfragment = fm.findFragmentByTag("rcfragment");

        if(rcfragment == null){
            ft.add(R.id.clReservation,cdf,"DatePickFragment");
            ft.commit();
        }

        //連接搶課大聊天室
        InsCourseVO insCourseVO = (InsCourseVO) getIntent().getExtras().getSerializable("insCourseVO");
        String user = Connect_WebSocket.getUserName();

        //註冊
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        //判斷進來的訊息
        IntentFilter lockCourseFilter = new IntentFilter("inscTimeId");
        GrabCourseReceiver grabCourseReceiver = new GrabCourseReceiver();
        broadcastManager.registerReceiver(grabCourseReceiver, lockCourseFilter);

        Connect_WebSocket.connectServerGrab(this, user, ServerURL.WS_GRABCOURSE);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.close_menu, menu);
        return true;
    }


    private class GrabCourseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String inscTimeIdIn = intent.getStringExtra("message");


            RadioGroup rgDate = CheckDateFragment.rgDate;
            for(int i=0; i<rgDate.getChildCount(); i++){
                RadioButton rb = (RadioButton) rgDate.getChildAt(i);
                String inscTimeId = rb.getTag().toString();
                if(inscTimeIdIn.equals(inscTimeId)){
                    rb.setClickable(false);
                    rb.setTextColor(Color.GRAY);
                }else{
                    rb.setClickable(true);
                    rb.setTextColor(Color.BLACK);
                }

            }

        }

    }


    // Activity結束即中斷WebSocket連線
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Connect_WebSocket.disconnectServerGrab();
    }

}
