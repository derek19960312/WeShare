package weshare.groupfour.derek.myCourseOrders;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

import java.io.IOException;
import java.util.List;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.util.Join;

public class MyCourseMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private final static String TAG = "MyCourseMapActivity";
    private GoogleMap mMap;
    private UiSettings uiSettings;
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
        mMap.setOnMarkerClickListener(listener);
        // 註冊OnInfoWindowClickListener，當標記訊息視窗被點擊時會自動呼叫該Listener的方法
        mMap.setOnInfoWindowClickListener(listener);
        // 註冊OnMarkerDragListener，當標記被拖曳時會自動呼叫該Listener的方法
        mMap.setOnMarkerDragListener(listener);


    }
    // 將地名或地址轉成位置後在地圖打上對應標記
    private void locationNameToMarker() {
        // 增加新標記前，先清除舊有標記
        mMap.clear();

        for(CourseReservationVO crVO : MyCourseActivity.myNearByCourseRvLearn){
            MemberVO memVO = new Join().getMemberbyteacherId(crVO.getTeacherId(),this);
            String teacherName = memVO.getMemName();

            MyLocationVO myLocationVO = MyCourseActivity.nearbyme.get(memVO.getMemId());
            LatLng latLng = new LatLng(myLocationVO.getLat(), myLocationVO.getLng());

            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("老師： "+teacherName)
                    .snippet("上課時間："));

        }

        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        // 將地址取出當作標記的描述文字
        String snippet = address.getAddressLine(0);

        String code = address.getPostalCode();


        // 將地名或地址轉成位置後在地圖打上對應標記
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(code + locationName)
                .snippet(snippet));

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
            infoWindow = LayoutInflater.from(MarkerActivity.this)
                    .inflate(R.layout.custom_infowindow, null);
        }

        @Override
        // 回傳設計好的訊息視窗樣式
        // 回傳null會自動呼叫getInfoContents(Marker)
        public View getInfoWindow(Marker marker) {
            int logoId;
            // 使用equals()方法檢查2個標記是否相同，千萬別用「==」檢查
            if (marker.equals(marker_yangmingshan)) {
                logoId = R.drawable.logo_yangmingshan;
            } else if (marker.equals(marker_taroko)) {
                logoId = R.drawable.logo_taroko;
            } else if (marker.equals(marker_yushan)) {
                logoId = R.drawable.logo_yushan;
            } else if (marker.equals(marker_kenting)) {
                logoId = R.drawable.logo_kenting;
            } else {
                // 呼叫setImageResource(int)傳遞0則不會顯示任何圖形
                logoId = 0;
            }

            // 顯示圖示
            ImageView ivLogo = infoWindow.findViewById(R.id.ivLogo);
            ivLogo.setImageResource(logoId);

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
}
