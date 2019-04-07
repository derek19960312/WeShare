package weshare.groupfour.derek;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MyInsCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inscourse);

        ViewPager vpMyInsCourse = findViewById(R.id.vpMyInsCourse);
        vpMyInsCourse.setAdapter(new MypagerAdapter(getSupportFragmentManager()));

        TabLayout tbMyInsCourse = findViewById(R.id.tbMyInsCourse);
        tbMyInsCourse.setupWithViewPager(vpMyInsCourse);


    }
    private class MypagerAdapter extends FragmentPagerAdapter {
        List<Page> pageList;
        public MypagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(new MyCourseFragment(),"我預約的"));
            pageList.add(new Page(new MyTeachFragment(),"我教的"));

        }

        @Override
        public Fragment getItem(int i) {
            return pageList.get(i).getFragment();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return pageList.get(position).getTitle();
        }
    }

}
