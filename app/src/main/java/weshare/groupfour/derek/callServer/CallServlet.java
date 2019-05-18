package weshare.groupfour.derek.callServer;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import weshare.groupfour.derek.util.Tools;


public class CallServlet extends AsyncTask<String, Void, String> {
    Context context;

    public CallServlet(Context context) {
        this.context = context;
    }

//    ProgressDialog progressDialog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(!Tools.networkConnected()){
            Tools.Toast(context,"請確認網路連線");
            return;
        }



//        try{
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("請稍等");
//            progressDialog.show();
//        }catch (Exception e){
//
//        }

    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder sb = new StringBuilder();
        String strurl = strings[0];

            HttpURLConnection con = null;
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
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String str;
                    while ((str = br.readLine()) != null)
                        sb.append(str);
                    Log.e("string from servlet", sb.toString());
                }
            } catch (Exception e) {
                Log.e("connection erro", e.toString());
            } finally {
                con.disconnect();
            }
            return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        if (progressDialog != null){
//            progressDialog.dismiss();
//        }

    }
}
