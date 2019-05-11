package weshare.groupfour.derek.myGoodsOrders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.MypagerAdapter;
import weshare.groupfour.derek.PageVO;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.home.LoginFakeActivity;
import weshare.groupfour.derek.util.Tools;

public class MyGoodsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        Intent intent = new Intent(MyGoodsActivity.this, LoginFakeActivity.class);
        startActivityForResult(intent,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ViewPager vpMyGood = findViewById(R.id.vpMyGood);
        SharedPreferences spf = Tools.getSharePreAccount();
        String memId = spf.getString("memId", null);

        if (memId != null) {
            List<PageVO> pageVOList = new ArrayList<>();
            pageVOList.add(new PageVO(new MyBoughtFragment(),"我訂的"));
            pageVOList.add(new PageVO(new MySoldFragment(),"我賣的"));
            vpMyGood.setAdapter(new MypagerAdapter(getSupportFragmentManager(),pageVOList));
        }

        TabLayout tbMyGood = findViewById(R.id.tbMyGood);
        tbMyGood.setupWithViewPager(vpMyGood);


    }



}
