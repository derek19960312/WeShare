package weshare.groupfour.derek.callServer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.util.Tools;


public class CallServlet_Pic extends AsyncTask<String, Void, Bitmap> {

    ImageView imageView;


    public CallServlet_Pic(ImageView imageView) {
        this.imageView = imageView;
    }


    @Override
    protected Bitmap doInBackground(String... strings) {
        StringBuilder sb = new StringBuilder();
        String strurl = strings[0];

        HttpURLConnection con = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(strurl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            String data;
            if (strings[1] != null) {
                data = strings[1];
                OutputStream out = con.getOutputStream();
                out.write(data.getBytes());
                out.flush();
            }
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bitmap = BitmapFactory.decodeStream(new BufferedInputStream(con.getInputStream()));
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("connection error", e.toString());
        } finally {
            con.disconnect();
        }
        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (isCancelled() || imageView == null) {
            return;
        }
//        //需要監聽器
//        else if (imageView.getTag() != null && (Integer) imageView.getTag() == 0) {
//
//            String memImage = bitMaptoBase64(bitmap);
//            SharedPreferences spf = Tools.getSharePreAccount();
//            spf.edit().putString("memImage", memImage);
//        }


        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.default_image);
        }

    }





    private boolean networkConnected() {
        ConnectivityManager conManager = (ConnectivityManager) new AppCompatActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }



    //觀察者模式
//    public interface ISubject {
//        void RegisterObserver(IObserver pObserver);
//
//        void RemoveObserver(IObserver pObserver);
//
//        void notifyObservers(String pContent);
//    }
//
//    public interface IObserver {
//        void Update(String pMessage);
//    }
//
//    public class NewspaperOffice implements ISubject {
//        List lstObservers; // 使用List來存放觀察者名單
//
//        public NewspaperOffice() {
//            lstObservers = new ArrayList();
//        }
//
//        // 加入觀察者
//        public void RegisterObserver(IObserver pObserver) {
//            lstObservers.add(pObserver);
//        }
//
//        // 移除觀察者
//        public void RemoveObserver(IObserver pObserver) {
//            if (lstObservers.indexOf(pObserver) >= 0)
//                lstObservers.remove(pObserver);
//        }
//
//        // 發送通知給在監聽名單中的觀察者
//        public void notifyObservers(String pContent) {
//            for (IObserver observer : lstObservers) {
//                observer.Update(pContent);
//            }
//        }
//
//        // 訂閱報紙
//        public void SubscribeNewspaper(IObserver pCustomer) {
//            RegisterObserver(pCustomer);
//        }
//
//        // 取消訂閱報紙
//        public void UnsubscribeNewspaper(IObserver pCustomer) {
//            RemoveObserver(pCustomer);
//        }
//
//        // 發送新消息
//        public void SendNewspaper(String pContent) {
//            notifyObservers(pContent);
//        }
//    }
//
//    public class Customer implements IObserver {
//        public String MyName ;
//
//        public String getMyName() {
//            return MyName;
//        }
//
//        public void setMyName(String myName) {
//            MyName = myName;
//        }
//
//        public Customer(String pName) {
//            MyName = pName;
//        }
//
//        // 更新最新消息
//        public void Update(String pMessage) {
//        }
//    }
}


