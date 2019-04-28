package weshare.groupfour.derek;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import weshare.groupfour.derek.util.Connect_WebSocket;


/*
 * 將websocket server上的所有user以RecyclerView列出。
 * 當server上的user連線或斷線時，ChatWebSocketClient都會發LocalBroadcast，
 * 此頁的BroadcastReceiver會接收到並在RecyclerView呈現。
 */

public class FriendsActivity extends AppCompatActivity {
    private static final String TAG = "FriendsActivity";

    private RecyclerView rvFriends;
    private String user;
    private List<String> friendList;
    private LocalBroadcastManager broadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerFriendStateReceiver();
        // 取得user name
        user = Connect_WebSocket.getUserName();
        // 將標題設定成user name
        setTitle("I am " + user);
        // 初始化聊天清單
        friendList = new LinkedList<>();
        // 初始化RecyclerView
        rvFriends = (RecyclerView) findViewById(R.id.rvFriends);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        rvFriends.setAdapter(new FriendAdapter(this));

        Connect_WebSocket.connectServer(this, user);
    }

    // 攔截user連線或斷線的Broadcast
    private void registerFriendStateReceiver() {
        IntentFilter openFilter = new IntentFilter("open");
        IntentFilter closeFilter = new IntentFilter("close");
        FriendStateReceiver friendStateReceiver = new FriendStateReceiver();
        broadcastManager.registerReceiver(friendStateReceiver, openFilter);
        broadcastManager.registerReceiver(friendStateReceiver, closeFilter);
    }

    // 攔截user連線或斷線的Broadcast，並在RecyclerView呈現
    private class FriendStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            State stateMessage = new Gson().fromJson(message, State.class);
            String type = stateMessage.getType();
            String friend = stateMessage.getUser();
            switch (type) {
                // 有user連線
                case "open":
                    // 如果是自己連線
                    if (friend.equals(user)) {
                        // 取得server上的所有user
                        friendList = new LinkedList<>(stateMessage.getUsers());
                        // 將自己從聊天清單中移除，否則會看到自己在聊天清單上
                        friendList.remove(user);
                    } else {
                        // 如果其他user連線且尚未加入聊天清單，就加上
                        if (!friendList.contains(friend)) {
                            friendList.add(friend);
                        }
                        Connect_WebSocket.showToast(FriendsActivity.this, friend + " is online");
                    }
                    // 重刷聊天清單
                    rvFriends.getAdapter().notifyDataSetChanged();
                    break;
                // 有user斷線
                case "close":
                    // 將斷線的user從聊天清單中移除
                    friendList.remove(friend);
                    rvFriends.getAdapter().notifyDataSetChanged();
                    Connect_WebSocket.showToast(FriendsActivity.this, friend + " is offline");
            }
            Log.d(TAG, message);
        }
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
        Context context;

        FriendAdapter(Context context) {
            this.context = context;
        }

        class FriendViewHolder extends RecyclerView.ViewHolder {
            TextView tvFriendName;

            FriendViewHolder(View itemView) {
                super(itemView);
                tvFriendName = itemView.findViewById(R.id.tvFrinedName);
            }
        }

        @Override
        public int getItemCount() {
            return friendList.size();
        }

        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.card_friends, parent, false);
            return new FriendViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FriendViewHolder holder, int position) {
            final String friend = friendList.get(position);
            holder.tvFriendName.setText(friend);
            // 點選聊天清單上的user即開啟聊天頁面
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FriendsActivity.this, ChatRoomActivity.class);
                    intent.putExtra("friend", friend);
                    startActivity(intent);
                }
            });
        }

    }

    // Activity結束即中斷WebSocket連線
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Connect_WebSocket.disconnectServer();
    }

}
