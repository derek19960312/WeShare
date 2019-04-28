package weshare.groupfour.derek;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import weshare.groupfour.derek.util.Connect_WebSocket;
import weshare.groupfour.derek.util.Tools;

import static weshare.groupfour.derek.util.Connect_WebSocket.getUserName;

public class ChatRoomActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private LocalBroadcastManager broadcastManager;
    private TextView tvMessage;
    private EditText etMessage;
    private ScrollView scrollView;
    private String friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        findViews();
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerChatReceiver();
        // 取得前頁傳來的聊天對象
        friend = getIntent().getStringExtra("friend");
        //setTitle("friend: " + friend);
        Connect_WebSocket.connectServer(this, getUserName() );
//        getHistoryMessage();
    }

    private void findViews() {
        tvMessage = findViewById(R.id.tvMessage);
        etMessage = findViewById(R.id.etMessage);
        scrollView = findViewById(R.id.scrollView);
    }

    private void getHistoryMessage() {
        ChatMessage chatMessage = new ChatMessage("history", getUserName(), friend, "");
        String chatMessageJson = new Gson().toJson(chatMessage);
        Connect_WebSocket.chatWebSocketClient.send(chatMessageJson);
    }

    private void registerChatReceiver() {
        IntentFilter chatFilter = new IntentFilter("chat");
//        IntentFilter historyFilter = new IntentFilter("history");
        ChatReceiver chatReceiver = new ChatReceiver();
        broadcastManager.registerReceiver(chatReceiver, chatFilter);
//        broadcastManager.registerReceiver(chatReceiver, historyFilter);
    }

    // 接收到聊天訊息會在TextView呈現
    private class ChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);

//            if ("history".equals(chatMessage.getType())) {
//                Type type = new TypeToken<List<String>>(){}.getType();
//                List<String> historyMsg = new Gson().fromJson(chatMessage.getMessage(), type);
//                for (String str : historyMsg) {
//                    ChatMessage cm = new Gson().fromJson(str, ChatMessage.class);
//                    tvMessage.append(cm.getSender() + ": " + cm.getMessage() + "\n");
//                }
//            }

            String sender = chatMessage.getSender();
            // 接收到聊天訊息，若發送者與目前聊天對象相同，就將訊息顯示在TextView
            if (sender.equals(friend)) {
                tvMessage.append(sender + ": " + chatMessage.getMessage() + "\n");
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
            Log.d(TAG, message);
        }
    }

    // user點擊發送訊息按鈕
    public void clickSend(View view) {
        String message = etMessage.getText().toString();
        if (message.trim().isEmpty()) {
            Connect_WebSocket.showToast(this, "請輸入訊息");
            return;
        }
        String sender = getUserName();
        // 將欲傳送訊息先顯示在TextView上
        tvMessage.append(sender + ": " + message + "\n");
        // 將輸入的訊息清空
        etMessage.setText(null);
        // 捲動至最新訊息
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        // 將欲傳送訊息轉成JSON後送出
        ChatMessage chatMessage = new ChatMessage("chat", sender, friend, message);
        String chatMessageJson = new Gson().toJson(chatMessage);
        Connect_WebSocket.chatWebSocketClient.send(chatMessageJson);
        Log.d(TAG, "output: " + chatMessageJson);
    }
}