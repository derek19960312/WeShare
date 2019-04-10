package weshare.groupfour.derek.MyCourse;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.PageVO;
import weshare.groupfour.derek.R;

public class MyCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inscourse);

        ViewPager vpMyInsCourse = findViewById(R.id.vpMyInsCourse);
        vpMyInsCourse.setAdapter(new MypagerAdapter(getSupportFragmentManager()));

        TabLayout tbMyInsCourse = findViewById(R.id.tbMyInsCourse);
        tbMyInsCourse.setupWithViewPager(vpMyInsCourse);

        //onActivityResult(RESULT_OK,,getIntent());
    }
    private class MypagerAdapter extends FragmentPagerAdapter {
        List<PageVO> pageVOList;
        public MypagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageVOList = new ArrayList<>();
            pageVOList.add(new PageVO(new MyCourseFragment(),"我預約的"));
            pageVOList.add(new PageVO(new MyTeachFragment(),"我教的"));

        }

        @Override
        public Fragment getItem(int i) {
            return pageVOList.get(i).getFragment();
        }

        @Override
        public int getCount() {
            return pageVOList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return pageVOList.get(position).getTitle();
        }
    }

}
