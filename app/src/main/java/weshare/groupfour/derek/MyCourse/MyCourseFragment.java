package weshare.groupfour.derek.MyCourse;

import android.content.Context;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.CourseReservation.CourseReservationVO;
import weshare.groupfour.derek.LoginFakeActivity;
import weshare.groupfour.derek.R;


public class MyCourseFragment extends Fragment {
    RecyclerView rvMyCourse;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_course, container, false);

        Log.e("onCreateView","onCreateView");

        rvMyCourse = view.findViewById(R.id.rvMyCourse);
        rvMyCourse.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        SharedPreferences spf = getActivity().getSharedPreferences("myAccount", Context.MODE_PRIVATE);
        String memId = spf.getString("memId", null);
        if (memId != null) {
            String resquestData = "action=find_my_reservation&param=" + memId;
            try {
                String result = new CallServlet().execute(ServerURL.IP_COURSERESERVATION, resquestData).get();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                Type listType = new TypeToken<List<CourseReservationVO>>() {
                }.getType();
                List<CourseReservationVO> myCourseRvList = gson.fromJson(result, listType);
                if (myCourseRvList.size() != 0) {
                    rvMyCourse.setAdapter(new MyCourseAdapter(myCourseRvList, MyCourseAdapter.MEMBER));
                } else {
                    view.findViewById(R.id.tvNoData).setVisibility(View.VISIBLE);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(getActivity(), LoginFakeActivity.class);
            getActivity().startActivity(intent);
        }
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences spf = getActivity().getSharedPreferences("myAccount", Context.MODE_PRIVATE);
        String memId = spf.getString("memId", null);
        if (memId != null) {
            String resquestData = "action=find_my_reservation&param=" + memId;
            try {
                String result = new CallServlet().execute(ServerURL.IP_COURSERESERVATION, resquestData).get();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                Type listType = new TypeToken<List<CourseReservationVO>>() {
                }.getType();
                List<CourseReservationVO> myCourseRvList = gson.fromJson(result, listType);
                if (myCourseRvList.size() != 0) {
                    rvMyCourse.setAdapter(new MyCourseAdapter(myCourseRvList, MyCourseAdapter.MEMBER));
                } else {
                    view.findViewById(R.id.tvNoData).setVisibility(View.VISIBLE);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("onAttach","onAttach");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause","onPause");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("onPause","onPause");
    }
}
