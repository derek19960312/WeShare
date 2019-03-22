package weshare.groupfour.derek;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import weshare.groupfour.derek.Commondity.Commondity;
import weshare.groupfour.derek.Commondity.Commondity_Browse;
import weshare.groupfour.derek.Course.Course_Browse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout dlMain = findViewById(R.id.dlMain);
        NavigationView nvMain = findViewById(R.id.nvMain);

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
}
