package weshare.groupfour.derek.CallServer;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CallServlet extends AsyncTask<String, Void, List<String>> {
    List<String> list;

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




            if (con.getResponseCode() == 200) {

                InputStream is = con.getInputStream();
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                int len = 0;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    message.write(buffer, 0, len);
                }
                is.close();
                message.close();
                String msg = new String(message.toByteArray());
                list.add(msg);

                return list;
            } else {
                list.add("連線失敗1");
                return list;
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
