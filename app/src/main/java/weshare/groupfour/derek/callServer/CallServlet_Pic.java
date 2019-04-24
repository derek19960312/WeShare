package weshare.groupfour.derek.callServer;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;




public class CallServlet_Pic extends AsyncTask<String, Void, String> {

    private Context context;
    ProgressDialog progressDialog;


    public CallServlet_Pic(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("請稍等........");
        progressDialog.show();
    }
    private final static String INTERNER_ERROR = "0";

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
        progressDialog.dismiss();
    }

    private boolean networkConnected() {
        ConnectivityManager conManager = (ConnectivityManager) new AppCompatActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}


