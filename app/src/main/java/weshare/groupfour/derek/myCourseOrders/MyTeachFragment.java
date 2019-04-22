package weshare.groupfour.derek.myCourseOrders;


import android.content.Context;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.R;


public class MyTeachFragment extends Fragment {
    TextView tvNoData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_teach, container, false);

        RecyclerView rvMyTeachCourse = view.findViewById(R.id.rvMyTeachCourse);
        rvMyTeachCourse.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        SharedPreferences spf = getActivity().getSharedPreferences("myAccount", Context.MODE_PRIVATE);
        String teacherId = spf.getString("teacherId", null);
        if (teacherId != null) {
        String resquestData = "action=find_my_reservation&param=" + teacherId;
        try {
            String result = new CallServlet().execute(ServerURL.IP_COURSERESERVATION, resquestData).get();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            Type listType = new TypeToken<List<CourseReservationVO>>() {
            }.getType();
            List<CourseReservationVO> myCourseRvList = gson.fromJson(result, listType);
            Log.e("myCourseRvList", String.valueOf(myCourseRvList.size()));
            if (myCourseRvList.size() != 0) {
                rvMyTeachCourse.setAdapter(new MyCourseAdapter(myCourseRvList, MyCourseAdapter.TEACHER));
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

}