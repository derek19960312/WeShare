package weshare.groupfour.derek.callServer;

public class ServerURL {

    //GCP
  final public static String IP = "34.80.204.33:8081";

    //迎刃
//  final public static String IP = "18.191.251.39:8081";

// final public static String IP = "localhost:8081";

    //中央宿舍
//  final public static String IP = "192.168.168.123:8081";

    //資策會
// final public static String IP = "10.120.26.19:8081";

    //桃園家
//  final public static String IP = "192.168.1.108:8081";

    //模擬器
// final public static String IP = "10.0.2.2:8081";


    final public static String PROJECT_WEB = "/WeShare_web";
    final public static String PROJECT_ANDROID = "/WeShare_web/android";

    final public static String WS = "ws://";
    final public static String HTTP = "http://";

    final public static String IP_COURSE = HTTP+IP+PROJECT_ANDROID+"/CourseServlet";
    final public static String IP_COURSELIKE = HTTP+IP+PROJECT_ANDROID+"/CourseLikeServlet";
    final public static String IP_COURSERESERVATION = HTTP+IP+PROJECT_ANDROID+"/CourseReservationServlet";
    final public static String IP_COURSETRANSFER = HTTP+IP+PROJECT_ANDROID+"/CourseTransferServlet";
    final public static String IP_COURSETYPE = HTTP+IP+PROJECT_ANDROID+"/CourseTypeServlet";
    final public static String IP_GOODS = HTTP+IP+PROJECT_ANDROID+"/GoodsServlet";
    final public static String IP_GOODSDETAILS = HTTP+IP+PROJECT_ANDROID+"/GoodsDetailsServlet";
    final public static String IP_GOODSLIKE = HTTP+IP+PROJECT_ANDROID+"/GoodsLikeServlet";
    final public static String IP_GOODSMESSAGE = HTTP+IP+PROJECT_ANDROID+"/GoodsMessageServlet";
    final public static String IP_GOODSORDER = HTTP+IP+PROJECT_ANDROID+"/GoodsOrderServlet";
    final public static String IP_INSCOURSE = HTTP+IP+PROJECT_ANDROID+"/InsCourseServlet";
    final public static String IP_MEMBER = HTTP+IP+PROJECT_ANDROID+"/MemberServlet";
    final public static String IP_TEACHER = HTTP+IP+PROJECT_ANDROID+"/TeacherServlet";
    final public static String IP_GET_PIC = HTTP+IP+PROJECT_ANDROID+"/DBGifReader";
    final public static String IP_WITHDRAEALRECORD = HTTP+IP+PROJECT_ANDROID+"/WithdrawalRecordServlet";
    final public static String IP_FRIENDS = HTTP+IP+PROJECT_ANDROID+"/FriendNexusServlet";

    final public static String WS_CHATROOM = WS+IP+PROJECT_WEB+"/FriendWS";
    final public static String WS_GRABCOURSE = WS+IP+PROJECT_WEB+"/GrabCourseWS";

}
