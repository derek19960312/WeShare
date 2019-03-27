package weshare.groupfour.derek;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import weshare.groupfour.derek.Commondity.Commondity_Browse;
import weshare.groupfour.derek.Course.Course_Browse;

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
                        intent.setClass(MainActivity.this,Course_Browse.class);
                        startActivity(intent);
                        break;
                    case R.id.menuMall:
                        intent.setClass(MainActivity.this, Commondity_Browse.class);
                        startActivity(intent);
                        break;
                }
                return false;
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
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        dlMain.closeDrawer(GravityCompat.START);
    }


}






