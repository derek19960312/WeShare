package weshare.groupfour.derek.InsCourse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.R;


public class MyCourseFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_course, container, false);

        RecyclerView rvMyCourse = view.findViewById(R.id.rvMyCourse);
        rvMyCourse.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        List myCourseList = new ArrayList();
        rvMyCourse.setAdapter(new MyCourseAdapter(myCourseList));

        return view;

    }

    private class MyCourseAdapter extends RecyclerView.Adapter {
        private List myCourseList;
        public MyCourseAdapter(List myCourseList) {
            this.myCourseList = myCourseList;
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            private CircleImageView civTeacherPic;
            private TextView tvTeacherName;
            private TextView tvCourseName;
            private TextView tvCourseTime;
            private TextView tvCoursePlace;
            public ViewHolder(View view) {
                super(view);
                civTeacherPic = view.findViewById(R.id.civTeacherPic);
                tvTeacherName = view.findViewById(R.id.tvTeacherName);
                tvCourseName = view.findViewById(R.id.tvCourseName);
                tvCourseTime = view.findViewById(R.id.tvCourseTime);
                tvCoursePlace = view.findViewById(R.id.tvCoursePlace);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.card_myinscourse,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            myCourseList.get(position);

        }

        @Override
        public int getItemCount() {
            return myCourseList.size();
        }
    }
}
