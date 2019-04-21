package weshare.groupfour.derek;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import weshare.groupfour.derek.Goods.GoodsBrowseActivity;
import weshare.groupfour.derek.Goods.GoodsCartActivity;


public class ToolbarBackFragment extends Fragment {
    Toolbar toolbarBack;
    public static boolean MEMBER = false;
    public static boolean SEARCH = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toolbar_back, container, false);
        toolbarBack = view.findViewById(R.id.toolbarBack);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarBack);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //資料從呼叫該toolbar的地方傳入
        actionBar.setTitle(getActivity().getIntent().getStringExtra("title"));
        //init();
        setSearch();
        setMember();


        return view;
    }

    private void init() {
        //導覽列上的功能才可以使用
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarBack);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //資料從呼叫該toolbar的地方傳入
        actionBar.setTitle(getActivity().getIntent().getIntExtra("title", R.string.nothing));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                Intent intent = new Intent(getActivity(), GoodsCartActivity.class);
                startActivity(intent);
                return true;
            case R.id.styleChange:
                GoodsBrowseActivity.changeStyle();
                return true;
            default:
                getActivity().finish();
                return true;
        }
    }

    public void setSearch() {

    }

    public void setMember() {

    }

}
