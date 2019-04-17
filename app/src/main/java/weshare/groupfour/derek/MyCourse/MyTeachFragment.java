package weshare.groupfour.derek.MyCourse;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.CourseReservation.CourseReservationVO;
import weshare.groupfour.derek.R;


public class MyTeachFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_teach, container, false);
        RecyclerView rvMyTeachCourse = view.findViewById(R.id.rvMyTeachCourse);
        rvMyTeachCourse.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        List<CourseReservationVO> myCourseList = new ArrayList();


        rvMyTeachCourse.setAdapter(new MyCourseAdapter(myCourseList,MyCourseAdapter.TEACHER));

        return view;
    }

}
