package weshare.groupfour.derek;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class GoodsMainActivity extends AppCompatActivity {

    NavigationView nvMain;
    DrawerLayout dlMain;
    Toolbar toolbar;
    BottomNavigationView bnvGoodsMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_goods_main);

        toolbar = findViewById(R.id.toolbar);
        dlMain = findViewById(R.id.dlMain);
        nvMain = findViewById(R.id.nvMain);
        bnvGoodsMain = findViewById(R.id.bnvMain);
        bnvGoodsMain.inflateMenu(R.menu.menu_goods_main_btmnav);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,dlMain,toolbar,R.string.drawer_open,R.string.drawer_close);
        dlMain.addDrawerListener(toggle);
        toggle.syncState();
        //側邊攔
        nvMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                dlMain.closeDrawer(GravityCompat.START);
                Intent intent = new Intent();
                switch (menuItem.getItemId()){
                    case R.id.menuCourse:
                        intent.setClass(GoodsMainActivity.this, CourseMainActivity.class);
                        intent.putExtra("title",R.string.insCourseVO);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menuMall:
                        intent.setClass(GoodsMainActivity.this, GoodsMainActivity.class);
                        intent.putExtra("title",R.string.mall);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
        //底部欄
        bnvGoodsMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Intent intent = new Intent();
                int position = menuItem.getItemId();
                switch (position){
                    case R.id.GoodsLike:

                        break;
                    case R.id.GoodsCart:

                        break;
                    case R.id.GoodsOrder:

                        break;
                }

                return false;
            }
        });




        FloatingActionButton fabCalendar = findViewById(R.id.fabCalendar);
        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoodsMainActivity.this,CalendarActivity.class);
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
