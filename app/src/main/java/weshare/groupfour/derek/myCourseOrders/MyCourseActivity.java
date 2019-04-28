package weshare.groupfour.derek.myCourseOrders;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import weshare.groupfour.derek.home.LoginFakeActivity;
import weshare.groupfour.derek.R;

public class MyCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inscourse);
        Intent intent = new Intent(MyCourseActivity.this, LoginFakeActivity.class);
        startActivityForResult(intent,0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.clMyInsCourse,new MyReservationFragment(),"flMyInsCourse");
        ft.commit();


    }

}
