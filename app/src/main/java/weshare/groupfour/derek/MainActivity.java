package weshare.groupfour.derek;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import weshare.groupfour.derek.Material.MaterialBrowseActivity;
import weshare.groupfour.derek.InsCourse.InsCourseBrowseActivity;

public class MainActivity extends AppCompatActivity{
    NavigationView nvMain;
    DrawerLayout dlMain;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        dlMain = findViewById(R.id.dlMain);
        nvMain = findViewById(R.id.nvMain);




        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,dlMain,toolbar,R.string.drawer_open,R.string.drawer_close);
        dlMain.addDrawerListener(toggle);
        toggle.syncState();

        nvMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                dlMain.closeDrawer(GravityCompat.START);
                Intent intent = new Intent();
                switch (menuItem.getItemId()){
                    case R.id.menuCourse:
                        intent.setClass(MainActivity.this, InsCourseBrowseActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menuMall:
                        intent.setClass(MainActivity.this, MaterialBrowseActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.myInsCourse:
                        intent.setClass(MainActivity.this, MyInsCourseActivity.class);
                        intent.putExtra("title",R.string.myInsCourse);
                        startActivity(intent);
                        break;
                    case R.id.myLikeInsCourse:
                        intent.setClass(MainActivity.this, MyLikeInsCourseActivity.class);
                        intent.putExtra("title",R.string.myLikeInsCourse);
                        startActivity(intent);
                        break;
                    case R.id.CourseCategort:
                        intent.setClass(MainActivity.this, CourseCategoryActivity.class);
                        intent.putExtra("title",R.string.CourseCategort);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        FloatingActionButton fabCalendar = findViewById(R.id.fabCalendar);
        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                CalendarFragment newFragment = new CalendarFragment();

                if (true) {
                    // The device is using a large layout, so show the fragment as a dialog
                    newFragment.show(fragmentManager, "dialog");
                } else {
                    // The device is smaller, so show the fragment fullscreen
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    // For a little polish, specify a transition animation
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    // To make it fullscreen, use the 'content' root view as the container
                    // for the fragment, which is always the root view for the activity
                    transaction.add(android.R.id.content, newFragment)
                            .addToBackStack(null).commit();
                }
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






