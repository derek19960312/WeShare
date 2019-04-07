package weshare.groupfour.derek;

import java.io.Serializable;

public class CourseTypeVO implements Serializable {
    private String CourseTypeId;
    private String CourseTypeName;

    public CourseTypeVO() {
    }

    public CourseTypeVO(String courseTypeName) {
        CourseTypeName = courseTypeName;
    }

    public String getCourseTypeId() {
        return CourseTypeId;
    }

    public void setCourseTypeId(String courseTypeId) {
        CourseTypeId = courseTypeId;
    }

    public String getCourseTypeName() {
        return CourseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        CourseTypeName = courseTypeName;
    }


}
