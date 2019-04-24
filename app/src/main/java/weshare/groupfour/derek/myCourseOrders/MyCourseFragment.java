package weshare.groupfour.derek.myCourseOrders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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


public class MyCourseFragment extends Fragment {
    TextView tvNoData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_course, container, false);

        RecyclerView rvMyCourse = view.findViewById(R.id.rvMyCourse);
        rvMyCourse.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        SharedPreferences spf = getActivity().getSharedPreferences("myAccount", Context.MODE_PRIVATE);
        String memId = spf.getString("memId", null);
        String resquestData = "action=find_my_reservation&param=" + memId;
        try {
            String result = new CallServlet().execute(ServerURL.IP_COURSERESERVATION, resquestData).get();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            Type listType = new TypeToken<List<CourseReservationVO>>() {
            }.getType();
            List<CourseReservationVO> myCourseRvList = gson.fromJson(result, listType);
            if (myCourseRvList != null & myCourseRvList.size() != 0) {
                rvMyCourse.setAdapter(new MyCourseAdapter(myCourseRvList, MyCourseAdapter.MEMBER,getContext()));
            } else {
                tvNoData = view.findViewById(R.id.tvNoData);
                tvNoData.setVisibility(View.VISIBLE);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
