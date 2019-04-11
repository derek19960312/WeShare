package weshare.groupfour.derek;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class MypagerAdapter extends FragmentPagerAdapter {
    List<PageVO> pageVOList;
    public MypagerAdapter(FragmentManager fragmentManager, List<PageVO> pageVOList) {
        super(fragmentManager);
        this.pageVOList = pageVOList;
    }

    @Override
    public Fragment getItem(int i) {
        return pageVOList.get(i).getFragment();
    }

    @Override
    public int getCount() {
        return pageVOList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return pageVOList.get(position).getTitle();
    }
}
