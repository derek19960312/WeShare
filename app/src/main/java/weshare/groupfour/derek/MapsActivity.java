package weshare.groupfour.derek;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity  extends AppCompatActivity implements OnMapReadyCallback  {
    private final static String TAG = "GeocoderActivity";
    private GoogleMap mMap;
    private UiSettings uiSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


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
        //uiSettings.setScrollGesturesEnabled(true);
        //自己位置按鈕
        uiSettings.setMyLocationButtonEnabled(true);
        //地圖縮放手勢
        //uiSettings.setZoomGesturesEnabled(true);
        String locationName = getIntent().getStringExtra("locationName");
        locationNameToMarker(locationName);
    }

    // 將地名或地址轉成位置後在地圖打上對應標記
    private void locationNameToMarker(String locationName) {
        // 增加新標記前，先清除舊有標記
        mMap.clear();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            // 解譯地名/地址後可能產生多筆位置資訊，所以回傳List<Address>
            // 將maxResults設為1，限定只回傳1筆
            addressList = geocoder.getFromLocationName(locationName, maxResults);
//            geocoder.getFromLocation()

            // 如果無法連結到提供服務的伺服器，會拋出IOException
        } catch (IOException ie) {
            Log.e(TAG, ie.toString());
        }

            // 因為當初限定只回傳1筆，所以只要取得第1個Address物件即可
            Address address = addressList.get(0);

            // Address物件可以取出緯經度並轉成LatLng物件
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

    private boolean isMapReady() {
        if (mMap == null) {
            Toast.makeText(this, "地圖尚未準備完成", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private LatLng getMyDirection(){
        double lat, lng;
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lat = location.getLatitude();  // 取得經度
        lng = location.getLongitude(); // 取得緯度
        LatLng HOME = new LatLng(lat, lng);
        return HOME;
    }


    // 開啟Google地圖應用程式來完成導航要求
    private void direct(double fromLat, double fromLng, double toLat, double toLng) {
        // 設定欲前往的Uri，saddr-出發地緯經度；daddr-目的地緯經度
        String uriStr = String.format(Locale.TAIWAN,
                "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                fromLat, fromLng, toLat, toLng);
        Intent intent = new Intent();
        // 指定交由Google地圖應用程式接手
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        // ACTION_VIEW-呈現資料給使用者觀看
        intent.setAction(Intent.ACTION_VIEW);
        // 將Uri資訊附加到Intent物件上
        intent.setData(Uri.parse(uriStr));
        startActivity(intent);
    }

}
