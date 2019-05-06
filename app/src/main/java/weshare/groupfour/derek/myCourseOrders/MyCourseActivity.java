package weshare.groupfour.derek.myCourseOrders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.MypagerAdapter;
import weshare.groupfour.derek.PageVO;
import weshare.groupfour.derek.FriendChat.State;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.home.LoginFakeActivity;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Connect_WebSocket;
import weshare.groupfour.derek.util.Tools;

public class MyCourseActivity extends AppCompatActivity {
    ViewPager vpMyInsCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inscourse);
        Intent intent = new Intent(MyCourseActivity.this, LoginFakeActivity.class);
        startActivityForResult(intent,0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        vpMyInsCourse = findViewById(R.id.vpMyInsCourse);

        setViewPager();

        TabLayout tbMyInsCourse = findViewById(R.id.tbMyInsCourse);
        tbMyInsCourse.setupWithViewPager(vpMyInsCourse);

        //加入上課驗證聊天室
        String user = Connect_WebSocket.getUserName();

        //註冊
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        //判斷進來的訊息
        IntentFilter lockCourseFilter = new IntentFilter("status");
        ConfirmCourseReceiver confirmCourseReceiver = new ConfirmCourseReceiver();
        broadcastManager.registerReceiver(confirmCourseReceiver, lockCourseFilter);

        Connect_WebSocket.connectServerConfirm(this, user, ServerURL.WS_CONFIRMCOURSE);

    }

    private void setViewPager(){
        List<PageVO> pageVOList = new ArrayList<>();
        pageVOList.add(new PageVO(new MyCourseFragment(),"我預約的"));
        pageVOList.add(new PageVO(new MyTeachFragment(),"我教的"));
        vpMyInsCourse.setAdapter(new MypagerAdapter(getSupportFragmentManager(),pageVOList));
    }
    private class ConfirmCourseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d("success","success");
            Gson gson = new Gson();
            State state = gson.fromJson(message, State.class);
            switch (state.getType()){
                case "success":
                    Tools.Toast(MyCourseActivity.this,"上課驗證成功");
                    setViewPager();
                    break;
                case "fail":
                    Tools.Toast(MyCourseActivity.this,"上課驗證失敗");
                    break;
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
