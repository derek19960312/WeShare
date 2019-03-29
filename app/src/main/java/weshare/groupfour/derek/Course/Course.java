package weshare.groupfour.derek.Course;

import android.graphics.drawable.Drawable;

public class Course implements java.io.Serializable {
    private byte[] TeacherPic;
    private String CourseName, TeacherName, CourseDetail;

    public Course() {
    }

    public Course(byte[] TeacherPic, String CourseName, String tvTeacherName, String CourseDetail) {
        this.TeacherPic = TeacherPic;
        this.CourseName = CourseName;
        this.TeacherName = tvTeacherName;
        this.CourseDetail = CourseDetail;
    }

    public byte[] getTeacherPic() {
        return TeacherPic;
    }

    public void setTeacherPic(byte[] teacherPic) {
        this.TeacherPic = teacherPic;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        this.CourseName = courseName;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        this.TeacherName = teacherName;
    }

    public String getCourseDetail() {
        return CourseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.CourseDetail = courseDetail;
    }
}
