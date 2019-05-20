package weshare.groupfour.derek.myCourseOrders;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.util.Connect_WebSocket;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Join;
import weshare.groupfour.derek.util.RequestDataBuilder;
import weshare.groupfour.derek.util.Tools;

public class MyCourseMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private final static String TAG = "MyCourseMapActivity";
    private GoogleMap mMap;
    private UiSettings uiSettings;
    Map<Marker, CourseReservationVO> markerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    @SuppressLint("MissingPermission")
    private void setUpMap() {
        //交通資訊
        mMap.setTrafficEnabled(true);
        //顯示自己位置圖層
        mMap.setMyLocationEnabled(true);
        uiSettings = mMap.getUiSettings();
        //縮放按鈕
        uiSettings.setZoomControlsEnabled(true);
        //地圖捲動手勢
        uiSettings.setScrollGesturesEnabled(true);
        //自己位置按鈕
        uiSettings.setMyLocationButtonEnabled(true);
        //地圖縮放手勢
        //uiSettings.setZoomGesturesEnabled(true);

        // 如果不套用自訂InfoWindowAdapter會自動套用預設訊息視窗
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        MyMarkerListener listener = new MyMarkerListener();
        // 註冊OnMarkerClickListener，當標記被點擊時會自動呼叫該Listener的方法
        //mMap.setOnMarkerClickListener(listener);
        // 註冊OnInfoWindowClickListener，當標記訊息視窗被點擊時會自動呼叫該Listener的方法
        mMap.setOnInfoWindowClickListener(listener);
        // 註冊OnMarkerDragListener，當標記被拖曳時會自動呼叫該Listener的方法
        //mMap.setOnMarkerDragListener(listener);

        setMarker();
    }

    // 將地名或地址轉成位置後在地圖打上對應標記
    private void setMarker() {
        // 增加新標記前，先清除舊有標記
        mMap.clear();
        markerMap = new HashMap<>();
        for (CourseReservationVO crVO : MyCourseActivity.myNearByCourseRvLearn) {
            MemberVO memVO = new Join().getMemberbyteacherId(crVO.getTeacherId(), this);
            String teacherName = memVO.getMemName();

            MyLocationVO myLocationVO = MyCourseActivity.nearbyme.get(memVO.getMemId());
            LatLng latLng = new LatLng(myLocationVO.getLat(), myLocationVO.getLng());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("老師： " + teacherName)
                    .snippet("點擊驗證"));
            markerMap.put(marker, crVO);
            marker.showInfoWindow();
        }
        for (CourseReservationVO crVO : MyCourseActivity.myNearByCourseTeach) {
            MemberVO memVO = new Join().getMemberbyMemId(crVO.getMemId(), this);
            String MemName = memVO.getMemName();

            MyLocationVO myLocationVO = MyCourseActivity.nearbyme.get(memVO.getMemId());
            LatLng latLng = new LatLng(myLocationVO.getLat(), myLocationVO.getLng());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("學生： " + MemName)
                    .snippet("點擊驗證")
            );

            markerMap.put(marker, crVO);
             marker.showInfoWindow();
        }

        Location location = GetMyLocation.location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        // 將鏡頭焦點設定在使用者輸入的地點上
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // 自訂InfoWindowAdapter，當點擊標記時會跳出自訂風格的訊息視窗
    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View infoWindow;

        MyInfoWindowAdapter() {
            infoWindow = LayoutInflater.from(MyCourseMapActivity.this)
                    .inflate(R.layout.custom_infowindow, null);
        }

        @Override
        // 回傳設計好的訊息視窗樣式
        // 回傳null會自動呼叫getInfoContents(Marker)
        public View getInfoWindow(Marker marker) {

            // 顯示標題
            String title = marker.getTitle();
            TextView tvTitle = infoWindow.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            // 顯示描述
            String snippet = marker.getSnippet();
            TextView tvSnippet = infoWindow.findViewById(R.id.tvSnippet);
            tvSnippet.setText(snippet);

            return infoWindow;
        }

        @Override
        // 當getInfoWindow(Marker)回傳null時才會呼叫此方法
        // 此方法如果再回傳null，代表套用預設視窗樣式
        public View getInfoContents(Marker marker) {
            return null;
        }
    }


    private class MyMarkerListener implements GoogleMap.OnMarkerClickListener,
            GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }

        @Override
        // 點擊標記的訊息視窗
        public void onInfoWindowClick(Marker marker) {
            CourseReservationVO crVO = markerMap.get(marker);
            RequestDataBuilder rdb = new RequestDataBuilder();
            rdb.build()
                    .setAction("confirm_for_course_shake")
                    .setData("crvId", crVO.getCrvId())
                    .setData("memId", Connect_WebSocket.getUserName());
            try {
                String status = new CallServlet(MyCourseMapActivity.this).execute(ServerURL.IP_COURSERESERVATION, rdb.create()).get();
                switch (status) {
                    case "success":
                        Connect_WebSocket.confirmCourseWebSocketClient.send(Holder.gson.toJson(crVO));
                        Log.d("send","00000000000000000000000000000000000000");
                        finish();
                        break;
                    case "wait":
                        Tools.Toast(MyCourseMapActivity.this, "等待驗證");
                        finish();
                        break;
                    case "hadCome":
                        Tools.Toast(MyCourseMapActivity.this, "請勿重複驗證");
                        finish();
                        break;
                    case "not_yet":
                        Tools.Toast(MyCourseMapActivity.this, "尚未到可驗證時間");
                        finish();
                        break;
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        @Override
        // 開始拖曳標記
        public void onMarkerDragStart(Marker marker) {
        }

        @Override
        // 拖曳標記過程中會不斷呼叫此方法
        public void onMarkerDrag(Marker marker) {
            // 以TextView顯示標記的緯經度
        }

        @Override
        // 結束拖曳標記
        public void onMarkerDragEnd(Marker marker) {
        }
    }


}
