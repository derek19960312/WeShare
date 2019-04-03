package weshare.groupfour.derek.LogSignin;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.R;


public class LogSigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logsignin);

        ViewPager vpLogSignin = findViewById(R.id.vpLogSignin);
        vpLogSignin.setAdapter(new MypagerAdapter(getSupportFragmentManager()));

        TabLayout tbLogSignin = findViewById(R.id.tbLogSignin);
        tbLogSignin.setupWithViewPager(vpLogSignin);


        ImageView ivClose = findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private class MypagerAdapter extends FragmentPagerAdapter{
        List<Page> pageList;
        public MypagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(new LoginFragment(),"登入"));
            pageList.add(new Page(new SigninFragment(),"註冊"));

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

