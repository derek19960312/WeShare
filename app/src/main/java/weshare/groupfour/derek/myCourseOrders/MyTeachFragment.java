package weshare.groupfour.derek.myCourseOrders;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.util.Connect_WebSocket;
import weshare.groupfour.derek.util.RequestDataBuilder;
import weshare.groupfour.derek.util.Tools;


public class MyTeachFragment extends Fragment {



    TextView tvNoData;
    public static List<CourseReservationVO> myTeachRvList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_teach, container, false);

        RecyclerView rvMyTeachCourse = view.findViewById(R.id.rvMyTeachCourse);
        rvMyTeachCourse.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        SharedPreferences spf = Tools.getSharePreAccount();
        String teacherId = spf.getString("teacherId", null);
        if (teacherId != null) {
        String resquestData = "action=find_my_reservation&param=" + teacherId;
        try {
            String result = new CallServlet(getContext()).execute(ServerURL.IP_COURSERESERVATION, resquestData).get();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            Type listType = new TypeToken<List<CourseReservationVO>>() {
            }.getType();
            myTeachRvList = gson.fromJson(result, listType);

            if (myTeachRvList !=null && myTeachRvList.size() != 0) {
                rvMyTeachCourse.setAdapter(new MyCourseAdapter(myTeachRvList, MyCourseAdapter.TEACHER,getContext(),this));
            } else {
                view.findViewById(R.id.tvNoData).setVisibility(View.VISIBLE);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }else{
           tvNoData = view.findViewById(R.id.tvNoData);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText("您並非老師身分");
       }
        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                Toast.makeText(getContext(), "You can't celled the scanning", Toast.LENGTH_SHORT).show();

            } else {
                Connect_WebSocket.confirmCourseWebSocketClient.send(result.getContents());

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}