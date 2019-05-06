package weshare.groupfour.derek;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class mapNavActivity_test extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings uiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_nav);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navMap);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        mMap = mMap;
        //  setUpMap();
    }

    private void asyncGetDirection() {
        if (mMap == null) {
            return;
        }

        mMap.clear();
//        final Dialog dialog = ProgressDialog.show(this, "計算路徑", "計算中");
//        new DirectionApiHelper(this).getDirection(fromEdit.getEditableText().toString(), toEdit.getEditableText().toString(), new OnEasyApiCallbackListener() {
//            @Override
//            public void onDone(EasyResponseObject response) {
//                dialog.dismiss();
//                try {
//                    Direction direction = new Direction();
//                    EasyResponseObjectParser.startParsing(response.getBody(), direction);
//                    drawPath(direction);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private void drawPath(Direction direction) {
//        if (direction.routes.size() == 0 || direction.routes.get(0).legs.size() == 0) {
//            return;
//        }
//        Direction.Leg leg = direction.routes.get(0).legs.get(0);
//
//        //
//        PolylineOptions polylineOptions = new PolylineOptions();
//        for (int i = 0; i < leg.steps.size(); i++) {
//            Direction.Step step = leg.steps.get(i);
//            polylineOptions.add(new LatLng(step.start_location.lat, step.start_location.lng));
//
//            if (i == 0) {
//                LatLng latLng = new LatLng(step.start_location.lat, step.start_location.lng);
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title("起點");
//                mMap.addMarker(markerOptions);
//            }
//            if (i == leg.steps.size() - 1) {
//                LatLng latLng = new LatLng(step.end_location.lat, step.end_location.lng);
//                polylineOptions.add(latLng);
//
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title("終點");
//                mMap.addMarker(markerOptions);
//            }
//        }
//        polylineOptions.color(0xfffd8364);
//        polylineOptions.width(polylineOptions.getWidth() * 2.5f);
//        //
//        mMap.addPolyline(polylineOptions);
//        //
//        if (leg.steps.size() > 0) {
//            Direction.Step step = leg.steps.get(0);
//            moveCamera(new LatLng(step.start_location.lat, step.start_location.lng));
//        }
//    }
//
//    private void moveCamera(LatLng latLng) {
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
//        mMap.moveCamera(cameraUpdate);
//        cameraUpdate = CameraUpdateFactory.zoomTo(14);
//        mMap.moveCamera(cameraUpdate);
//    }

//}

    }
}