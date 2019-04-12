package weshare.groupfour.derek.CallServer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CallServlet extends AsyncTask<String, Void, List<String>> {
    List<String> list;
    StringBuffer sb = null;
    @Override
    protected List<String> doInBackground(String... strings) {
        list = new ArrayList<>();
        String strurl = strings[0];
        HttpURLConnection con = null;
        try {
            URL url = new URL(strurl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            String data;
            if(strings[1]!=null){
                data = strings[1];
                OutputStream out = con.getOutputStream();
                out.write(data.getBytes());
                out.flush();
            }

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String str;
                while((str = br.readLine()) != null)
                    sb.append(str);
                list.add(sb.toString());
                Log.e("123123",sb.toString());
                return list;

            } else {

            }

        } catch (Exception e) {
            list.add("連線失敗2");
            e.printStackTrace();
        } finally {
            con.disconnect();
        }


        return list;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);

    }

    public List<String> getList() {
        return list;
    }
}
