package weshare.groupfour.derek;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import weshare.groupfour.derek.CourseType.CourseCategoryActivity;
import weshare.groupfour.derek.InsCourse.MyLikeCourseActivity;
import weshare.groupfour.derek.MyCourse.MyCourseActivity;

public class CourseMainActivity extends AppCompatActivity{
    NavigationView nvMain;
    DrawerLayout dlMain;
    Toolbar toolbar;
    BottomNavigationView bnvCourseMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_goods_main);

        toolbar = findViewById(R.id.toolbar);
        dlMain = findViewById(R.id.dlMain);
        nvMain = findViewById(R.id.nvMain);
        bnvCourseMain = findViewById(R.id.bnvMain);
        bnvCourseMain.inflateMenu(R.menu.menu_course_main_btmnav);


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,dlMain,toolbar,R.string.drawer_open,R.string.drawer_close);
        dlMain.addDrawerListener(toggle);
        toggle.syncState();
        //側邊欄
        nvMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                dlMain.closeDrawer(GravityCompat.START);
                Intent intent = new Intent();
                switch (menuItem.getItemId()){
                    case R.id.menuCourse:
                        intent.setClass(CourseMainActivity.this, CourseMainActivity.class);
                        intent.putExtra("title",R.string.insCourseVO);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menuMall:
                        intent.setClass(CourseMainActivity.this, GoodsMainActivity.class);
                        intent.putExtra("title",R.string.mall);
                        startActivity(intent);
                        finish();
                        break;

                }
                return false;
            }
        });
        //底層欄
        bnvCourseMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent = new Intent();
                bnvCourseMain.setSelectedItemId(menuItem.getItemId());
                switch (menuItem.getItemId()){
                    case R.id.myLikeInsCourse:
                        intent.setClass(CourseMainActivity.this, MyLikeCourseActivity.class);
                        intent.putExtra("title",R.string.myLikeInsCourse);
                        startActivityForResult(intent,1);
                        break;
                    case R.id.CourseCategort:
                        intent.setClass(CourseMainActivity.this, CourseCategoryActivity.class);
                        intent.putExtra("title",R.string.CourseCategort);
                        startActivityForResult(intent,2);
                        break;
                    case R.id.myInsCourse:
                        intent.setClass(CourseMainActivity.this, MyCourseActivity.class);
                        intent.putExtra("title",R.string.myInsCourse);
                        startActivityForResult(intent,3);
                        break;
                }
                return false;
            }

        });


        FloatingActionButton fabCalendar = findViewById(R.id.fabCalendar);
        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseMainActivity.this,CalendarActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    public void onLogin(View v){
        LoginDialog dialog = new LoginDialog();
        dialog.show(getSupportFragmentManager(),"alert");
        dlMain.closeDrawer(GravityCompat.START);

    }


}







