package weshare.groupfour.derek;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class CheckDateFragment extends Fragment {
    private static int year, month, day, hour;
    static Button btnDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_date, container, false);

        //日期按鈕
        btnDate = view.findViewById(R.id.btnDate);
        showRightNow();
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                FragmentManager fm = getFragmentManager();
                datePickerFragment.show(fm, "datePicker");
            }
        });

        //動態時間
        RecyclerView rvTime = view.findViewById(R.id.rvTime);
        rvTime.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        rvTime.setAdapter(new TimeAdapter());
        return view;
    }

    private static void showRightNow() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        updateInfo();
    }

    private static void updateInfo() {
        btnDate.setText(new StringBuilder().append(year).append("-")
                //「month + 1」是因為一月的值是0而非1
                .append(parseNum(month + 1)).append("-").append(parseNum(day)));
    }

    private static String parseNum(int day) {
        if (day >= 10)
            return String.valueOf(day);
        else
            return "0" + String.valueOf(day);
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int y, int m, int d) {
                    year = y;
                    month = m;
                    day = d;
                    updateInfo();

                }
            }, year, month, day);
            DatePicker dp = dpd.getDatePicker();


            return dpd;
        }


    }


    private class TimeAdapter extends RecyclerView.Adapter {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
