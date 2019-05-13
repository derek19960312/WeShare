package weshare.groupfour.derek.myCourseOrders;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Tools;


public class MyTeachFragment extends Fragment {

    FloatingActionButton fabScanner;

    TextView tvNoData;
    public static List<CourseReservationVO> myTeachRvList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_teach, container, false);
        fabScanner = view.findViewById(R.id.fabScanner);
        RecyclerView rvMyTeachCourse = view.findViewById(R.id.rvMyTeachCourse);
        rvMyTeachCourse.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        SharedPreferences spf = Tools.getSharePreAccount();
        String teacherId = spf.getString("teacherId", null);
        if (teacherId != null) {
            String resquestData = "action=find_my_reservation&param=" + teacherId;
            try {
                String result = new CallServlet(getContext()).execute(ServerURL.IP_COURSERESERVATION, resquestData).get();
                Type listType = new TypeToken<List<CourseReservationVO>>() {
                }.getType();
                myTeachRvList = Holder.gson.fromJson(result, listType);

                if (myTeachRvList != null && myTeachRvList.size() != 0) {
                    rvMyTeachCourse.setAdapter(new MyCourseAdapter(myTeachRvList, MyCourseAdapter.TEACHER, getContext(), this));
                } else {
                    view.findViewById(R.id.tvNoData).setVisibility(View.VISIBLE);
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            tvNoData = view.findViewById(R.id.tvNoData);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText("您並非老師身分");
        }

        //開啟掃描器
        fabScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(MyTeachFragment.this);

                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");       //底部提示的文字
                integrator.setCameraId(0);          //前面或後面的相機
                integrator.setBeepEnabled(true);    //掃描成功後發出 BB 聲
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });



        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();

            } else {
                Log.e("result.get", result.getContents().substring(0, 2));
                if (result.getContents().substring(0, 2).trim().equals("CR")) {
                    for (CourseReservationVO crVO : myTeachRvList) {
                        if (result.getContents().equals(crVO.getCrvId())) {
                            Connect_WebSocket.confirmCourseWebSocketClient.send(Holder.gson.toJson(crVO));
                            break;
                        }
                    }
                } else {
                    Tools.Toast(getContext(), "無法驗證非本公司的QRCODE");
                }


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }


}