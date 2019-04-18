package weshare.groupfour.derek;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.CourseType.CourseCategoryActivity;
import weshare.groupfour.derek.Goods.GoodsBrowseActivity;
import weshare.groupfour.derek.InsCourse.MyLikeCourseActivity;
import weshare.groupfour.derek.MyCourse.MyCourseActivity;


public class MainActivity extends AppCompatActivity {
    NavigationView nvMain;
    DrawerLayout dlMain;
    Toolbar toolbar;
    BottomNavigationView bnvCourseMain;
    ViewPager vpMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        dlMain = findViewById(R.id.dlMain);
        nvMain = findViewById(R.id.nvMain);
        bnvCourseMain = findViewById(R.id.bnvMain);

        vpMain = findViewById(R.id.vpMain);
        List<PageVO> pageVOList = new ArrayList<>();
        pageVOList.add(new PageVO(new CourseMainFragment(), "課程"));
        pageVOList.add(new PageVO(new GoodsMainFragment(), "教材商城"));
        vpMain.setAdapter(new MypagerAdapter(getSupportFragmentManager(), pageVOList));


        //側邊欄開關
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dlMain, toolbar, R.string.drawer_open, R.string.drawer_close);
        dlMain.addDrawerListener(toggle);
        toggle.syncState();

        //側邊藍
        nvMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                dlMain.closeDrawer(GravityCompat.START);
                Intent intent = new Intent();
                switch (menuItem.getItemId()) {
                    case R.id.myLikeInsCourse:
                        intent.setClass(MainActivity.this, MyLikeCourseActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.CourseCategort:
                        intent.setClass(MainActivity.this, CourseCategoryActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.myInsCourse:
                        intent.setClass(MainActivity.this, MyCourseActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.MyWallet:
                        intent.setClass(MainActivity.this, MyWalletActivity.class);
                        startActivity(intent);
                        return true;
//                    case R.id.GoodsLike:
//                        intent.setClass(MainActivity.this, MyLikeCourseActivity.class);
//                        startActivity(intent);
//                        return true;

                    //目前先接商品瀏覽
                    case R.id.GoodsCart:
                        intent.setClass(MainActivity.this, GoodsBrowseActivity.class);
                        startActivity(intent);
                        return true;
//                    case R.id.GoodsOrder:
//                        intent.setClass(MainActivity.this, MyLikeCourseActivity.class);
//                        startActivity(intent);
//                        return true;
                }
                return false;
            }
        });

        //加入側邊攔HEADER
        addNavigationHeader();


        //底層欄
        bnvCourseMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuCourse:
                        vpMain.setCurrentItem(0);
                        return true;
                    case R.id.menuMall:
                        vpMain.setCurrentItem(1);
                        return true;
                }
                return false;
            }

        });


        //滑動時
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                bnvCourseMain.getMenu().getItem(position).setChecked(true);
                nvMain.getMenu().clear();
                switch (position) {
                    case 0:
                        nvMain.inflateMenu(R.menu.menu_course_main);
                        break;
                    case 1:
                        nvMain.inflateMenu(R.menu.menu_goods_main);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    Button btnLogin;
    SharedPreferences spf;

    public void addNavigationHeader() {
        //側邊攔HEADDER
        View view = nvMain.getHeaderView(0);
        CircleImageView civMemImage = view.findViewById(R.id.civMemImage);
        TextView tvMemId = view.findViewById(R.id.tvMemId);
        btnLogin = view.findViewById(R.id.btnLogin);
        spf = getSharedPreferences("myAccount", Context.MODE_PRIVATE);

        String memId = spf.getString("memId", null);
        if (memId != null) {
            tvMemId.setText(memId);
            String sMempic = spf.getString("memImage", null);
            byte[] bmemImage = Base64.decode(sMempic, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bmemImage, 0, bmemImage.length);
            civMemImage.setImageBitmap(bitmap);
            btnLogin.setText("登出");
        } else {
            tvMemId.setText("尚未登入");
            civMemImage.setImageResource(R.drawable.teacher);
            btnLogin.setText("登入");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        addNavigationHeader();
    }

    public void onLogin(View v) {

        switch ((String) btnLogin.getText()) {
            case "登入":
//                LoginDialog_deprecate dialog = new LoginDialog_deprecate();
//                dialog.show(getSupportFragmentManager(),"alert");
                Intent intent = new Intent(MainActivity.this, LoginFakeActivity.class);
                startActivity(intent);
                dlMain.closeDrawer(GravityCompat.START);
            case "登出":
                spf.edit().clear().commit();
                dlMain.closeDrawer(GravityCompat.START);
                addNavigationHeader();
        }

    }


}






