package weshare.groupfour.derek.MyCourse;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.MypagerAdapter;
import weshare.groupfour.derek.PageVO;
import weshare.groupfour.derek.R;

public class MyCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inscourse);

        ViewPager vpMyInsCourse = findViewById(R.id.vpMyInsCourse);
        List<PageVO> pageVOList = new ArrayList<>();
        pageVOList.add(new PageVO(new MyCourseFragment(),"我預約的"));
        //pageVOList.add(new PageVO(new MyTeachFragment(),"我教的"));
        vpMyInsCourse.setAdapter(new MypagerAdapter(getSupportFragmentManager(),pageVOList));

        TabLayout tbMyInsCourse = findViewById(R.id.tbMyInsCourse);
        tbMyInsCourse.setupWithViewPager(vpMyInsCourse);

        //onActivityResult(RESULT_OK,,getIntent());
    }


}
