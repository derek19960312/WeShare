package weshare.groupfour.derek.myCourseOrders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import weshare.groupfour.derek.MypagerAdapter;
import weshare.groupfour.derek.PageVO;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.home.LoginFakeActivity;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.util.Connect_WebSocket;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Join;
import weshare.groupfour.derek.util.RequestDataBuilder;
import weshare.groupfour.derek.util.Tools;

public class MyCourseActivity extends AppCompatActivity {
    ViewPager vpMyInsCourse;
    Set<String> nearbyme;
    GetMyLocation getMyLocation;
    String user;
    //搖搖用參數
    SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inscourse);
        //登入驗證
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Intent intent = new Intent(MyCourseActivity.this, LoginFakeActivity.class);
        startActivityForResult(intent, 0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //成功驗證登入
        if (requestCode == 0) {
            vpMyInsCourse = findViewById(R.id.vpMyInsCourse);

            setViewPager();

            TabLayout tbMyInsCourse = findViewById(R.id.tbMyInsCourse);
            tbMyInsCourse.setupWithViewPager(vpMyInsCourse);


            //註冊廣播接收
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
            //判斷進來的訊息
            IntentFilter CourseComFilter = new IntentFilter("check");
            IntentFilter NearByFilter = new IntentFilter("nearby");

            ConfirmCourseReceiver confirmCourseReceiver = new ConfirmCourseReceiver();
            NearByReceiver nearByReceiver = new NearByReceiver();

            broadcastManager.registerReceiver(confirmCourseReceiver, CourseComFilter);
            broadcastManager.registerReceiver(nearByReceiver, NearByFilter);


            //驗證上課聊天室
            Connect_WebSocket.connectServerConfirm(this, user, ServerURL.WS_CONFIRMCOURSE);
            Connect_WebSocket.connectServerWhoArround(this, user, ServerURL.WS_AROUNDS);

            //更新最新位置
            getMyLocation = new GetMyLocation(this, this);
            getMyLocation.startLocationUpdates();
        }


    }

    private void setViewPager() {
        List<PageVO> pageVOList = new ArrayList<>();
        pageVOList.add(new PageVO(new MyCourseFragment(), "我預約的"));
        pageVOList.add(new PageVO(new MyTeachFragment(), "我教的"));
        vpMyInsCourse.setAdapter(new MypagerAdapter(getSupportFragmentManager(), pageVOList));
    }

    List<CourseReservationVO> myNearByCourseRv;

    private class NearByReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Type setType = new TypeToken<Set<MyLocationVO>>() {
            }.getType();
            Set<MyLocationVO> myLocationVOS = Holder.gson.fromJson(message, setType);
            nearbyme = new HashSet<>();
            for (MyLocationVO myLocationVO : myLocationVOS) {
                if (!myLocationVO.getMemberId().equals(Connect_WebSocket.getUserName()) && distance(myLocationVO) < 500f) {
                    if (nearbyme.contains(myLocationVO.getMemberId())) {

                    } else {
                        nearbyme.add(myLocationVO.getMemberId());
                        Log.e("CourseReservationVO", myLocationVO.getMemberId());
                        refreshMyNearBYCourse();
                    }
                }
            }
        }
    }

    private void refreshMyNearBYCourse() {

        myNearByCourseRv = new ArrayList<>();
        if (MyTeachFragment.myTeachRvList != null) {
            for (CourseReservationVO crVO : MyTeachFragment.myTeachRvList) {
                if (crVO.getClassStatus() != 1 && nearbyme.contains(crVO.getMemId())) {
                    myNearByCourseRv.add(crVO);
                }
            }
        }
        if (MyCourseFragment.myCourseRvList != null) {
            for (CourseReservationVO crVO : MyCourseFragment.myCourseRvList) {
                MemberVO MemVO = new Join().getMemberbyteacherId(crVO.getTeacherId(), MyCourseActivity.this);
                if (crVO.getClassStatus() != 1 && nearbyme.contains(MemVO.getMemId())) {
                    myNearByCourseRv.add(crVO);
                }
            }
        }
    }

    private class ConfirmCourseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            switch (message) {
                case "success":
                    Tools.Toast(MyCourseActivity.this, "上課驗證成功");
                    setViewPager();
                    break;
                case "fail":
                    Tools.Toast(MyCourseActivity.this, "上課驗證失敗");
                    break;
            }


        }

    }

    Location location;

    // 計算距離
    public float distance(MyLocationVO myLocationVO) {
        float[] results = new float[1];
        // 計算自己位置與使用者輸入地點，此2點間的距離(公尺)，結果會存入results[0]
        Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                myLocationVO.getLat(), myLocationVO.getLng(), results);
        return results[0];

    }

    @Override
    protected void onResume() {
        super.onResume();

        user = Connect_WebSocket.getUserName();
        //開啟搖搖功能


        //搖搖
        // 註冊監聽器，若是行動裝置有對應的感應器則回傳true，反之為false
        boolean enable = sm.registerListener(listener,
                // 註冊的感應器
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        if (!enable) {
            sm.unregisterListener(listener);
        }


    }


    public void connectWS(Location location) {
        this.location = location;
        MyLocationVO mylVO = new MyLocationVO();
        mylVO.setMemberId(user);
        mylVO.setLat(location.getLatitude());
        mylVO.setLng(location.getLongitude());
        Connect_WebSocket.whoAroundsWebSocketClient.send(Holder.gson.toJson(mylVO));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //關閉搖搖註冊
        sm.unregisterListener(listener);
    }

    // Activity結束即中斷WebSocket連線
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Connect_WebSocket.disconnectServerConfirm();
        Connect_WebSocket.disconnectServerWhoArround();
        //停止更新最新位置
        getMyLocation.stopLocationUpdates();
    }


    private static final int FORCE_THRESHOLD = 350;
    private static final int TIME_THRESHOLD = 100;
    private static final int SHAKE_TIMEOUT = 300;
    private static final int SHAKE_DURATION = 1000;
    private static final int SHAKE_COUNT = 5;

    private float lastX = -1.0f, lastY = -1.0f, lastZ = -1.0f;
    private long lastTime;
    private int shakeCount;
    private long lastShake;
    private long lastForce;

    //搖搖感應
    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float[] values = event.values.clone();
                long now = System.currentTimeMillis();

                if ((now - lastForce) > SHAKE_TIMEOUT) {
                    shakeCount = 0;
                }

                if ((now - lastTime) > TIME_THRESHOLD) {
                    long diff = now - lastTime;
                    float speed = Math.abs(values[0] + values[1] + values[2] - lastX - lastY - lastZ) / diff * 10000;
                    if (speed > FORCE_THRESHOLD) {
                        if ((++shakeCount >= SHAKE_COUNT) && (now - lastShake > SHAKE_DURATION)) {
                            lastShake = now;
                            shakeCount = 0;
                            // shake事件確定後，要做的事在這執行

                            if (myNearByCourseRv != null && myNearByCourseRv.size() == 0) {
                                Tools.Toast(MyCourseActivity.this, "沒有可驗證課程");
                            } else {
                                List<String> list = new ArrayList<>();
                                for (CourseReservationVO crv : myNearByCourseRv) {
                                    list.add(crv.getCrvId());
                                }
                                Object[] objs = list.toArray();
                                String[] stringArray = Arrays.copyOf(objs, objs.length, String[].class);
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyCourseActivity.this);
                                builder.setTitle("請選擇要驗證的課")
                                        .setItems(stringArray, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                RequestDataBuilder rdb = new RequestDataBuilder();
                                                rdb.build()
                                                        .setAction("confirm_for_course_shake")
                                                        .setData("crvId", myNearByCourseRv.get(which).getCrvId())
                                                        .setData("memId", user);
                                                try {
                                                    String status = new CallServlet(MyCourseActivity.this).execute(ServerURL.IP_COURSERESERVATION, rdb.create()).get();
                                                    switch (status) {
                                                        case "success":
                                                            Connect_WebSocket.confirmCourseWebSocketClient.send(Holder.gson.toJson(myNearByCourseRv.get(which)));
                                                            break;
                                                        case "wait":
                                                            Tools.Toast(MyCourseActivity.this, "等待驗證");
                                                            break;
                                                        case "hadCome":
                                                            Tools.Toast(MyCourseActivity.this, "請勿重複驗證");
                                                            break;
                                                    }
                                                } catch (ExecutionException e) {
                                                    e.printStackTrace();
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).create().show();
                            }


                        }
                        lastForce = now;
                    }
                    lastTime = now;
                    lastX = values[0];
                    lastY = values[1];
                    lastZ = values[2];
                }

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


}
