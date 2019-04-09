package weshare.groupfour.derek;

import android.support.v4.app.Fragment;

public class PageVO {
    private Fragment fragment;
    private String title;

    public PageVO(Fragment fragment, String title) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
