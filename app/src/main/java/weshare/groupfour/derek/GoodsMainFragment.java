package weshare.groupfour.derek;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import weshare.groupfour.derek.goods.GoodsCartActivity;


public class GoodsMainFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_main, container, false);
        FloatingActionButton fabCart = view.findViewById(R.id.fabCart);
        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GoodsCartActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
