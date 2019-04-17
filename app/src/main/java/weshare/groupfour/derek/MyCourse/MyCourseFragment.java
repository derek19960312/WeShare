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

import weshare.groupfour.derek.R;


public class MyCourseFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_course, container, false);

        RecyclerView rvMyCourse = view.findViewById(R.id.rvMyCourse);
        rvMyCourse.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        List myCourseList = new ArrayList();
        myCourseList.add(new MyCourseVO(R.drawable.teacher));
        myCourseList.add(new MyCourseVO(R.drawable.teacher3));
        myCourseList.add(new MyCourseVO(R.drawable.teacher2));
        rvMyCourse.setAdapter(new MyCourseAdapter(myCourseList));

        return view;

    }


}
