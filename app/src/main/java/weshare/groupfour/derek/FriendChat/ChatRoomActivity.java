package weshare.groupfour.derek.FriendChat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.util.Connect_WebSocket;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.RequestDataBuilder;
import weshare.groupfour.derek.util.Tools;

import static weshare.groupfour.derek.util.Connect_WebSocket.getUserName;

public class ChatRoomActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private LocalBroadcastManager broadcastManager;
    private EditText etMessage;
    private String friendId, friendName;
    private RecyclerView rvChat;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private String FriendPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        findViews();
        LinearLayoutManager lm = new LinearLayoutManager(this);

        rvChat.setLayoutManager(lm);
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerChatReceiver();
        // 取得前頁傳來的聊天對象
        friendId = getIntent().getStringExtra("friendId");
        friendName = getIntent().getStringExtra("friendName");
        Connect_WebSocket.connectServerChat(this, getUserName(), ServerURL.WS_CHATROOM);


        //取得聊天對象頭貼
        try {

            RequestDataBuilder rdb = new RequestDataBuilder();
            rdb.build()
                    .setAction("get_member_pic_base64")
                    .setData("memId", friendId);


            FriendPic = new CallServlet(this).execute(ServerURL.IP_MEMBER, rdb.create()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        getHistoryMessage();
    }

    private void findViews() {
        etMessage = findViewById(R.id.etMessage);
        rvChat = findViewById(R.id.rvChat);
    }

    private void getHistoryMessage() {
        ChatMessage chatMessage = new ChatMessage("history", getUserName(), friendId, "");
        String chatMessageJson = new Gson().toJson(chatMessage);
        Connect_WebSocket.chatWebSocketClient.send(chatMessageJson);
    }

    private void registerChatReceiver() {
        IntentFilter chatFilter = new IntentFilter("chat");
        //IntentFilter historyFilter = new IntentFilter("history");
        ChatReceiver chatReceiver = new ChatReceiver();
        broadcastManager.registerReceiver(chatReceiver, chatFilter);
        //broadcastManager.registerReceiver(chatReceiver, historyFilter);
    }

    private class ChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.e("message", message);

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //String[] chatMessageStr = Holder.gson.fromJson(message, String[].class);
            //ChatMessage chatMessage = Holder.gson.fromJson(message, ChatMessage.class);
//            if ("history".equals(chatMessage.getType())) {
//                Type type = new TypeToken<List<String>>(){}.getType();
//                List<String> historyMsg = new Gson().fromJson(chatMessage.getMessage(), type);
            for (int i = 0; i < jsonArray.length(); i++) {

                ChatMessage cm = null;
                try {
                    cm = new Gson().fromJson(jsonArray.get(i).toString(), ChatMessage.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                chatMessages.add(cm);
            }
            rvChat.setAdapter(new ChatMessageAdapter());
            rvChat.getAdapter().notifyDataSetChanged();
            rvChat.scrollToPosition(chatMessages.size() - 1);
//            }

            //           String sender = chatMessage.getSender();

//            if (sender.equals(friendId)) {
//                chatMessages.add(chatMessage);
//
//                rvChat.getAdapter().notifyDataSetChanged();
//                rvChat.scrollToPosition(chatMessages.size()-1);
//            }
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

        // 將輸入的訊息清空
        etMessage.setText(null);

        // 將欲傳送訊息轉成JSON後送出
        ChatMessage chatMessage = new ChatMessage("chat", sender, friendId, message);
        String chatMessageJson = Holder.gson.toJson(chatMessage);
        Connect_WebSocket.chatWebSocketClient.send(chatMessageJson);

        //chatMessages.add(chatMessage);
        //rvChat.setAdapter(new ChatMessageAdapter());
        //rvChat.getAdapter().notifyDataSetChanged();
        //rvChat.scrollToPosition(chatMessages.size()-1);
    }


    private class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {


        class ChatMessageViewHolder extends RecyclerView.ViewHolder {
            TextView tvMessageR, tvMessageL;
            CardView cardRight, cardLeft;
            CircleImageView civFri;

            ChatMessageViewHolder(View itemView) {
                super(itemView);
                tvMessageL = itemView.findViewById(R.id.tvMessageL);
                tvMessageR = itemView.findViewById(R.id.tvMessageR);
                cardRight = itemView.findViewById(R.id.cardRight);
                cardLeft = itemView.findViewById(R.id.cardLeft);
                civFri = itemView.findViewById(R.id.civFri);
            }
        }

        @Override
        public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat_message, parent, false);
            return new ChatMessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatMessageViewHolder holder, int position) {
            ChatMessage ctm = chatMessages.get(position);

            if (ctm.getSender().equals(friendId)) {
                holder.tvMessageL.setText(ctm.getMessage());
                holder.cardRight.setVisibility(View.GONE);
                holder.cardLeft.setVisibility(View.VISIBLE);
                holder.civFri.setImageBitmap(Tools.getBitmapByBase64(FriendPic));

            } else {
                holder.tvMessageR.setText(ctm.getMessage());
                holder.cardRight.setVisibility(View.VISIBLE);
                holder.cardLeft.setVisibility(View.GONE);
                holder.civFri.setVisibility(View.GONE);
            }


        }

        @Override
        public int getItemCount() {
            return chatMessages.size();
        }


    }
}
