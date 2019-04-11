//package weshare.groupfour.derek;
//
//
//import android.app.SearchManager;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.BottomNavigationView;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.NavigationView;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.View;
//
//public class CourseMainActivity_deprecate extends AppCompatActivity{
//    NavigationView nvMain;
//    DrawerLayout dlMain;
//    Toolbar toolbar;
//    BottomNavigationView bnvCourseMain;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_course_goods_main);
//
//        toolbar = findViewById(R.id.toolbar);
//        dlMain = findViewById(R.id.dlMain);
//        nvMain = findViewById(R.id.nvMain);
//        bnvCourseMain = findViewById(R.id.bnvMain);
//        bnvCourseMain.inflateMenu(R.menu.menu_course_main);
//
//
//
//
//
//        FloatingActionButton fabCalendar = findViewById(R.id.fabCalendar);
//        fabCalendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CourseMainActivity_deprecate.this,CalendarActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu,menu);
//        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        return true;
//    }
//
//
//
//
//}
//
//
//
//
//
//
//
