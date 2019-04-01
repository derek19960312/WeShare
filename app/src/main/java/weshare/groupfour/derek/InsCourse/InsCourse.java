package weshare.groupfour.derek.InsCourse;

public class InsCourse implements java.io.Serializable {
    private int TeacherPic;
    private String CourseName, TeacherName, CourseDetail;

    public InsCourse() {
    }

    public InsCourse(int TeacherPic, String CourseName, String tvTeacherName, String CourseDetail) {
        this.TeacherPic = TeacherPic;
        this.CourseName = CourseName;
        this.TeacherName = tvTeacherName;
        this.CourseDetail = CourseDetail;
    }

    public int getTeacherPic() {
        return TeacherPic;
    }

    public void setTeacherPic(int teacherPic) {
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
