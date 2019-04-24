package weshare.groupfour.derek.myCourseOrders;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.MypagerAdapter;
import weshare.groupfour.derek.PageVO;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Tools;


public class MyReservationFragment extends Fragment {


    public MyReservationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_reservation, container, false);
        ViewPager vpMyInsCourse = view.findViewById(R.id.vpMyInsCourse);
        SharedPreferences spf = Tools.getSharePreAccount();
        String memId = spf.getString("memId", null);

        if (memId != null) {
            List<PageVO> pageVOList = new ArrayList<>();
            pageVOList.add(new PageVO(new MyCourseFragment(),"我預約的"));
            pageVOList.add(new PageVO(new MyTeachFragment(),"我教的"));
            vpMyInsCourse.setAdapter(new MypagerAdapter(getChildFragmentManager(),pageVOList));
        }

        TabLayout tbMyInsCourse = view.findViewById(R.id.tbMyInsCourse);
        tbMyInsCourse.setupWithViewPager(vpMyInsCourse);
        return view;
    }

}
