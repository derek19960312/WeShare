package weshare.groupfour.derek;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;



public class ToolbarBackFragment extends Fragment {
    Toolbar toolbarBack;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toolbar_back, container, false);
        toolbarBack = view.findViewById(R.id.toolbarBack);
        //導覽列上的功能才可以使用
        setHasOptionsMenu(true);


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarBack);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //資料從呼叫該toolbar的地方傳入
        actionBar.setTitle(getActivity().getIntent().getIntExtra("title",R.string.nothing));



        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            getActivity().finish();
        return true;
    }

}
