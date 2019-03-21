package weshare.groupfour.derek.Course;

import android.graphics.drawable.Drawable;

public class Course {
    private Drawable ivTeacherPic;
    private String tvCourseName,tvTeacherName,tvCourseDetail;

    public Course() {
    }

    public Course(Drawable ivTeacherPic, String tvCourseName, String tvTeacherName, String tvCourseDetail) {
        this.ivTeacherPic = ivTeacherPic;
        this.tvCourseName = tvCourseName;
        this.tvTeacherName = tvTeacherName;
        this.tvCourseDetail = tvCourseDetail;
    }

    public Drawable getIvTeacherPic() {
        return ivTeacherPic;
    }

    public void setIvTeacherPic(Drawable ivTeacherPic) {
        this.ivTeacherPic = ivTeacherPic;
    }

    public String getTvCourseName() {
        return tvCourseName;
    }

    public void setTvCourseName(String tvCourseName) {
        this.tvCourseName = tvCourseName;
    }

    public String getTvTeacherName() {
        return tvTeacherName;
    }

    public void setTvTeacherName(String tvTeacherName) {
        this.tvTeacherName = tvTeacherName;
    }

    public String getTvCourseDetail() {
        return tvCourseDetail;
    }

    public void setTvCourseDetail(String tvCourseDetail) {
        this.tvCourseDetail = tvCourseDetail;
    }
}
