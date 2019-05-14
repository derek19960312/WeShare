package weshare.groupfour.derek.FriendChat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
    private ImageView ivCamera, ivPicture;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_IMAGE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private static final int REQ_PERMISSIONS_STORAGE = 101;
    private Uri contentUri, croppedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        findViews();
        requestPermission_Storage();

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
        ivCamera = findViewById(R.id.ivCamara);
        ivPicture = findViewById(R.id.ivPicture);
    }

    private void getHistoryMessage() {
        ChatMessage chatMessage = new ChatMessage("history", getUserName(), friendId, "", "history");
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

            ChatMessage cm = Holder.gson.fromJson(message, ChatMessage.class);
            if (cm != null) {
                chatMessages.add(cm);
            }

            if (chatMessages != null && chatMessages.size() != 0) {
                rvChat.setAdapter(new ChatMessageAdapter());
                rvChat.getAdapter().notifyDataSetChanged();
                rvChat.scrollToPosition(chatMessages.size() - 1);
            }


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
        ChatMessage chatMessage = new ChatMessage("chat", sender, friendId, message, "text");
        String chatMessageJson = Holder.gson.toJson(chatMessage);
        Connect_WebSocket.chatWebSocketClient.send(chatMessageJson);

        //chatMessages.add(chatMessage);
        //rvChat.setAdapter(new ChatMessageAdapter());
        //rvChat.getAdapter().notifyDataSetChanged();
        //rvChat.scrollToPosition(chatMessages.size()-1);
    }

    Intent intent;

    public void cilckCamera(View view) {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture.jpg");
        // targeting Android 7.0 (API level 24) and higher,
        // storing images using a FileProvider.
        // passing a file:// URI across a package boundary causes a FileUriExposedException.
        contentUri = FileProvider.getUriForFile(
                this, getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        startActivityForResult(intent, REQ_TAKE_PICTURE);

    }

    public void cilckPicture(View view) {

        intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            int newSize = 400;
            switch (requestCode) {
                // 手機拍照App拍照完成後可以取得照片圖檔
                case REQ_TAKE_PICTURE:
                    Log.d(TAG, "REQ_TAKE_PICTURE: " + contentUri.toString());
                    crop(contentUri);
                    break;
                case REQ_PICK_IMAGE:
                    Uri uri = intent.getData();
                    crop(uri);
                    break;
                case REQ_CROP_PICTURE:
                    Log.d(TAG, "REQ_CROP_PICTURE: " + croppedImageUri.toString());
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(croppedImageUri));
                        Bitmap downsizedImage = Tools.downSize(bitmap, newSize);
                        String sender = getUserName();
//                        showImage(sender, downsizedImage, false);
                        // 將欲傳送的對話訊息轉成JSON後送出
                        String message = Base64.encodeToString(bitmapToPNG(downsizedImage), Base64.DEFAULT);
                        Log.e("toopanIN", message);
                        ChatMessage chatMessage = new ChatMessage("chat", sender, friendId, message, "image");
                        String chatMessageJson = new Gson().toJson(chatMessage);
                        Connect_WebSocket.chatWebSocketClient.send(chatMessageJson);
                        Log.d(TAG, "output: " + chatMessageJson);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        croppedImageUri = Uri.fromFile(file);
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // the recipient of this Intent can read soruceImageUri's data
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // set image source Uri and type
            cropIntent.setDataAndType(sourceImageUri, "image/*");
            // send crop message
            cropIntent.putExtra("crop", "true");
            // aspect ratio of the cropped area, 0 means user define
            cropIntent.putExtra("aspectX", 0); // this sets the max width
            cropIntent.putExtra("aspectY", 0); // this sets the max height
            // output with and height, 0 keeps original size
            cropIntent.putExtra("outputX", 0);
            cropIntent.putExtra("outputY", 0);
            // whether keep original aspect ratio
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            // whether return data by the intent
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQ_CROP_PICTURE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast.makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] bitmapToPNG(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 轉成PNG不會失真，所以quality參數值會被忽略
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void requestPermission_Storage() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int result = ContextCompat.checkSelfPermission(this, permissions[0]);
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    REQ_PERMISSIONS_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ivCamera.setEnabled(true);
                    ivPicture.setEnabled(true);
                } else {
                    ivCamera.setEnabled(false);
                    ivPicture.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }


    private class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


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


        class ChatImageViewHolder extends RecyclerView.ViewHolder {

            ImageView ivMessageR, ivMessageL;
            CardView cardRight, cardLeft;
            CircleImageView civFri;

            ChatImageViewHolder(View itemView) {
                super(itemView);
                ivMessageL = itemView.findViewById(R.id.ivMessageL);
                ivMessageR = itemView.findViewById(R.id.ivMessageR);
                cardRight = itemView.findViewById(R.id.cardRight);
                cardLeft = itemView.findViewById(R.id.cardLeft);
                civFri = itemView.findViewById(R.id.civFri);
            }

        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = null;

            switch (viewType) {
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat_img, parent, false);
                    return new ChatImageViewHolder(view);
                case 0:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat_message, parent, false);
                    return new ChatMessageViewHolder(view);
            }
            return null;

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ChatMessage ctm = chatMessages.get(position);
            switch (ctm.gettOrm()) {
                case "image":
                    if (ctm.getSender().equals(friendId)) {
                        ((ChatImageViewHolder) holder).ivMessageL.setImageBitmap(Tools.getBitmapByBase64(ctm.getMessage()));
                        ((ChatImageViewHolder) holder).cardRight.setVisibility(View.GONE);
                        ((ChatImageViewHolder) holder).cardLeft.setVisibility(View.VISIBLE);
                        ((ChatImageViewHolder) holder).civFri.setImageBitmap(Tools.getBitmapByBase64(FriendPic));

                    } else {
                        Log.e("toopanIN", ctm.getMessage());
                        ((ChatImageViewHolder) holder).ivMessageR.setImageBitmap(Tools.getBitmapByBase64(ctm.getMessage()));
                        ((ChatImageViewHolder) holder).cardRight.setVisibility(View.VISIBLE);
                        ((ChatImageViewHolder) holder).cardLeft.setVisibility(View.GONE);
                        ((ChatImageViewHolder) holder).civFri.setVisibility(View.GONE);
                    }
                    break;
                case "text":
                    if (ctm.getSender().equals(friendId)) {
                        ((ChatMessageViewHolder) holder).tvMessageL.setText(ctm.getMessage());
                        ((ChatMessageViewHolder) holder).cardRight.setVisibility(View.GONE);
                        ((ChatMessageViewHolder) holder).cardLeft.setVisibility(View.VISIBLE);
                        ((ChatMessageViewHolder) holder).civFri.setImageBitmap(Tools.getBitmapByBase64(FriendPic));

                    } else {
                        ((ChatMessageViewHolder) holder).tvMessageR.setText(ctm.getMessage());
                        ((ChatMessageViewHolder) holder).cardRight.setVisibility(View.VISIBLE);
                        ((ChatMessageViewHolder) holder).cardLeft.setVisibility(View.GONE);
                        ((ChatMessageViewHolder) holder).civFri.setVisibility(View.GONE);
                    }
                    break;
            }


        }

        @Override
        public int getItemCount() {
            return chatMessages.size();
        }

        @Override
        public int getItemViewType(int position) {

            switch (chatMessages.get(position).gettOrm()) {
                case "image":
                    return 1;
                case "text":
                    return 0;
            }

            return super.getItemViewType(position);
        }
    }
}
