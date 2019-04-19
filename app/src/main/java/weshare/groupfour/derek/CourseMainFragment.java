package weshare.groupfour.derek;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;


public class CourseMainFragment extends Fragment {


    public CourseMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_main, container, false);


        FloatingActionButton fabCalendar = view.findViewById(R.id.fabCalendar);
        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });



//        //廣告強
//        Banner mBanner;
//        ArrayList<String> images = null;
//        ArrayList<String> imageTitle = null;
//
//        //设置图片资源:url或本地资源
//        images = new ArrayList<>();
//        images.add("https://hahow-production.imgix.net/5c4147b8ae76e00020978734?w=300&auto=format&s=e331df40e5185b15f536e5063a4a3cf8");
//        images.add("https://hahow-production.imgix.net/5cb562b3903de500207f75e3?w=300&auto=format&s=6f75af155401ae5c6b163f805d233f04");
//        images.add("https://hahow-production.imgix.net/5c8e69c88d1cc80020953710?w=300&auto=format&s=751c391c505726f819943272548b154f");
//        images.add("https://hahow-production.imgix.net/5c7f8e02a52fc8002453c271?w=300&auto=format&s=b28b2fd6944b3323138e384a7fb9f709");
//        images.add("https://hahow-production.imgix.net/5c66341091da6d00207669fb?w=300&auto=format&s=7707bbbb31908b52f81f822ed448cd43");
//        images.add("https://hahow-production.imgix.net/5c49489a28c84d00201c809f?w=300&auto=format&s=977389aa41aa49892b5dc08d061375ef");
//        //设置图片标题:自动对应
//        imageTitle = new ArrayList<>();
//        imageTitle.add("提升工程師的科技力！AWS 雲端網站建置");
//        imageTitle.add("鮪魚的獨門閱讀新法");
//        imageTitle.add("用 Python 理財：打造自己的 AI 股票理專");
//        imageTitle.add("TALK SMART！打造更有深度的英語口說");
//        imageTitle.add("火頭工做麵包：用科學方法學做健康麵包");
//        imageTitle.add("Kotlin Android 高效開發：全新語言雲端世代");
//        mBanner = view.findViewById(R.id.banner);
//        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
//        //可选样式如下:
//        //1. Banner.CIRCLE_INDICATOR    显示圆形指示器
//        //2. Banner.NUM_INDICATOR   显示数字指示器
//        //3. Banner.NUM_INDICATOR_TITLE 显示数字指示器和标题
//        //4. Banner.CIRCLE_INDICATOR_TITLE  显示圆形指示器和标题
//        //设置banner样式
//        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
//        //设置图片加载器
//        mBanner.setImageLoader(new ImageLoader() {
//            @Override
//            public void displayImage(Context context, Object path, ImageView imageView) {
//                Glide.with(context)
//                        .load((String) path)
//                        .into(imageView);
//            }
//        });
//        //设置标题集合（当banner样式有显示title时）
//        mBanner.setBannerTitles(imageTitle);
//        //设置轮播样式（没有标题默认为右边,有标题时默认左边）
//        //可选样式:
//        //Banner.LEFT   指示器居左
//        //Banner.CENTER 指示器居中
//        //Banner.RIGHT  指示器居右
//        mBanner.setIndicatorGravity(BannerConfig.CENTER);
//        //设置是否允许手动滑动轮播图
//        mBanner.setViewPagerIsScroll(true);
//        //设置是否自动轮播（不设置则默认自动）
//        mBanner.isAutoPlay(true);
//        //设置轮播图片间隔时间（不设置默认为2000）
//        mBanner.setDelayTime(1500);
//        //设置图片资源:可选图片网址/资源文件，默认用Glide加载,也可自定义图片的加载框架
//        //所有设置参数方法都放在此方法之前执行
//        mBanner.setIndicatorGravity(BannerConfig.CENTER);
//        mBanner.setImages(images)
//                .setOnBannerListener(new OnBannerListener() {
//                    @Override
//                    public void OnBannerClick(int position) {
//                        Toast.makeText(getActivity(), "你點了第" + (position + 1) + "張圖", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .start();


        return view;
    }

}
