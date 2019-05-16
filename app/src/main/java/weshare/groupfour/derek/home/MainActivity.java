package weshare.groupfour.derek.home;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.FriendChat.FriendsActivity;
import weshare.groupfour.derek.MypagerAdapter;
import weshare.groupfour.derek.PageVO;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.courseType.CourseCategoryActivity;
import weshare.groupfour.derek.goods.GoodsBrowseActivity;
import weshare.groupfour.derek.goods.MyLikeGoodsActivity;
import weshare.groupfour.derek.insCourse.MyLikeCourseActivity;
import weshare.groupfour.derek.member.MyWalletActivity;
import weshare.groupfour.derek.myCourseOrders.MyCourseActivity;
import weshare.groupfour.derek.myGoodsOrders.MyGoodsActivity;
import weshare.groupfour.derek.util.Tools;


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

        Tools.Toast(MainActivity.this, ServerURL.IP);

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

        //TOOLBAR事件監聽
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent();
                intent.putExtra("title",menuItem.getTitle());
                switch (menuItem.getItemId()) {
                    case R.id.MyWallet:
                        intent.setClass(MainActivity.this, MyWalletActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.Friend:
                        intent.setClass(MainActivity.this, FriendsActivity.class);
                        startActivity(intent);
                        return true;
//                    case R.id.ChatRoom:
//                        intent.setClass(MainActivity.this, ChatRoomActivity.class);
//                        startActivity(intent);
//                        return true;
                }
                return false;
            }
        });


        //側邊欄
        nvMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                dlMain.closeDrawer(GravityCompat.START);
                Intent intent = new Intent();
                intent.putExtra("title",menuItem.getTitle());
                switch (menuItem.getItemId()) {
                    //課程側邊欄選單
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

                    //商品側邊欄選單
                    case R.id.GoodsLike:
                        intent.setClass(MainActivity.this, MyLikeGoodsActivity.class);
                        startActivity(intent);
                        return true;

                    //目前先接商品瀏覽
                    case R.id.InsCourseBrowser :
                        intent.setClass(MainActivity.this, GoodsBrowseActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.GoodsOrder:
                        intent.setClass(MainActivity.this, MyGoodsActivity.class);
                        startActivity(intent);
                        return true;
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



        //ViewPager滑動時
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



    }//onCreate

    Button btnLogin;
    SharedPreferences spf;

    public void addNavigationHeader() {
        //抓取側邊攔的HEADDER
        View view = nvMain.getHeaderView(0);
        //抓取View上的元件
        CircleImageView civMemImage = view.findViewById(R.id.civMemImage);
        TextView tvMemId = view.findViewById(R.id.tvMemId);
        TextView tvMemName = view.findViewById(R.id.tvMemName);
        btnLogin = view.findViewById(R.id.btnLogin);
        //從sharePreferences取出登入資料
        spf = getSharedPreferences("myAccount", Context.MODE_PRIVATE);
        String memId = spf.getString("memId", null);
        String memName = spf.getString("memName", null);

        if (memId != null) {
            //有登入資訊
            tvMemId.setText(memId);
            tvMemName.setText(memName);

            //取出圖片並轉成Bitmap
            String sMempic = spf.getString("memImage", null);
            byte[] bmemImage = Base64.decode(sMempic, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bmemImage, 0, bmemImage.length);
            civMemImage.setImageBitmap(bitmap);
            btnLogin.setText("登出");
        } else {
            //無登入資訊
            tvMemId.setText("");
            tvMemName.setText("尚未登入");
            civMemImage.setImageResource(R.mipmap.ic_launcher_round);
            btnLogin.setText("登入");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新載入Headder
        addNavigationHeader();

    }

    public void onLogin(View v) {
        switch ((String) btnLogin.getText()) {
            case "登入":
           //目前暫時無法解決  Fragment 生命週期問題
//                LoginDialog_deprecate dialog = new LoginDialog_deprecate();
//                dialog.show(getSupportFragmentManager(),"alert");
                Intent intent = new Intent(MainActivity.this, LoginFakeActivity.class);
                startActivity(intent);
                dlMain.closeDrawer(GravityCompat.START);
            case "登出":
                spf.edit().clear().apply();
                addNavigationHeader();
                dlMain.closeDrawer(GravityCompat.START);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.member_menu, menu);
        return true;
    }





    //請求存取位置相關資源

    private static final int MY_REQUEST_CODE = 1;

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();
    }

    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,


        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE:
                String text = "";
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        text += permissions[i] + "\n";
                    }
                }
                if (!text.isEmpty()) {
                    text += getString(R.string.text_NotGranted);
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



}






