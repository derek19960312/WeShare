package weshare.groupfour.derek;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.CourseType.CourseCategoryActivity;
import weshare.groupfour.derek.Goods.GoodsBrowseActivity;
import weshare.groupfour.derek.InsCourse.MyLikeCourseActivity;
import weshare.groupfour.derek.MyCourse.MyCourseActivity;


public class MainActivity extends AppCompatActivity{
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
        pageVOList.add(new PageVO(new CourseMainFragment(),"課程"));
        pageVOList.add(new PageVO(new GoodsMainFragment(),"教材商城"));
        vpMain.setAdapter(new MypagerAdapter(getSupportFragmentManager(),pageVOList));



        //側邊欄開關
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,dlMain,toolbar,R.string.drawer_open,R.string.drawer_close);
        dlMain.addDrawerListener(toggle);
        toggle.syncState();

        //側邊欄
        nvMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                dlMain.closeDrawer(GravityCompat.START);
                Intent intent = new Intent();
                switch (menuItem.getItemId()){
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
        //底層欄
        bnvCourseMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menuCourse:
                        vpMain.setCurrentItem(0);
                        nvMain.inflateMenu(R.menu.menu_course_main);
                        return true;
                    case R.id.menuMall:
                        vpMain.setCurrentItem(1);
                        nvMain.inflateMenu(R.menu.menu_goods_main);
                        return true;
                }
                return false;
            }

        });

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                bnvCourseMain.getMenu().getItem(position).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        }




    public void onLogin(View v){
        LoginDialog dialog = new LoginDialog();
        dialog.show(getSupportFragmentManager(),"alert");
        dlMain.closeDrawer(GravityCompat.START);

    }

}






