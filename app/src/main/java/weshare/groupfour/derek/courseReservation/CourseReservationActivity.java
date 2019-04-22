package weshare.groupfour.derek.courseReservation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import weshare.groupfour.derek.R;
import weshare.groupfour.derek.insCourse.InsCourseVO;


public class CourseReservationActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_reservation);

        InsCourseVO insCourseVO = (InsCourseVO) getIntent().getExtras().get("insCourseVO");

        Toolbar tbReservation = findViewById(R.id.tbReservation);
        setSupportActionBar(tbReservation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        tbReservation.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                finish();
                return true;
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.close_menu, menu);
        return true;
    }
}
