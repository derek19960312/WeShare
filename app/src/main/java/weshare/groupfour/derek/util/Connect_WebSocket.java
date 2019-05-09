package weshare.groupfour.derek.util;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

import weshare.groupfour.derek.ws.ChatWebSocketClient;
import weshare.groupfour.derek.ws.ConfirmCourseWebSocketClient;
import weshare.groupfour.derek.ws.GrabCourseWebSocketClient;
import weshare.groupfour.derek.ws.WhoAroundsWebSocketClient;

public class Connect_WebSocket {
    private final static String TAG = "Util";

    public static ChatWebSocketClient chatWebSocketClient;
    public static GrabCourseWebSocketClient grabCourseWebSocketClient;
    public static ConfirmCourseWebSocketClient confirmCourseWebSocketClient;
    public static WhoAroundsWebSocketClient whoAroundsWebSocketClient;

    // 建立ChatWebSocket連線
    public static void connectServerChat(Context context, String userName, String ws) {
        URI uri = null;
        try {
            uri = new URI(ws + "/"+userName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (chatWebSocketClient == null) {
            chatWebSocketClient = new ChatWebSocketClient(uri, context);
            chatWebSocketClient.connect();
        }
    }
    // 中斷ChatWebSocket連線
    public static void disconnectServerChat() {
        if (chatWebSocketClient != null) {
            chatWebSocketClient.close();
            chatWebSocketClient = null;
        }
    }

    // 建立GrabWebSocket連線
    public static void connectServerGrab(Context context, String userName, String ws) {
        URI uri = null;
        try {
            uri = new URI(ws + "/"+userName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (grabCourseWebSocketClient == null) {
            grabCourseWebSocketClient = new GrabCourseWebSocketClient(uri, context);
            grabCourseWebSocketClient.connect();
        }
    }
    // 中斷GrabWebSocket連線
    public static void disconnectServerGrab() {
        if (grabCourseWebSocketClient != null) {
            grabCourseWebSocketClient.close();
            grabCourseWebSocketClient = null;
        }
    }

    // 建立ConfirmWebSocket連線
    public static void connectServerConfirm(Context context, String userName, String ws) {
        URI uri = null;
        try {


            uri = new URI(ws + "/"+userName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (confirmCourseWebSocketClient == null) {
            confirmCourseWebSocketClient = new ConfirmCourseWebSocketClient(uri, context);
            confirmCourseWebSocketClient.connect();
        }
    }
    // 中斷ConfirmWebSocket連線
    public static void disconnectServerConfirm() {
        if (confirmCourseWebSocketClient != null) {
            confirmCourseWebSocketClient.close();
            confirmCourseWebSocketClient = null;
        }
    }
    // 建立WhoArroundWebSocket連線
    public static void connectServerWhoArround(Context context, String userName, String ws) {
        URI uri = null;
        try {

            uri = new URI(ws + "/"+userName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (whoAroundsWebSocketClient == null) {
            whoAroundsWebSocketClient = new WhoAroundsWebSocketClient(uri, context);
            whoAroundsWebSocketClient.connect();
        }
    }
    // 中斷WhoArroundWebSocket連線
    public static void disconnectServerWhoArround() {
        if (whoAroundsWebSocketClient != null) {
            whoAroundsWebSocketClient.close();
            whoAroundsWebSocketClient = null;
        }
    }









    public static String getUserName() {
        String userName = Tools.getSharePreAccount().getString("memId","XXX");
        return userName;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
    }


}
