package weshare.groupfour.derek;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Login_Fragment extends Fragment {

    //String URL = "http://192.168.168.123:8081/WeShare_web/Login";

    //String URL = "http://10.120.26.19:8081/WeShare_web/Login";

    String URL = "http://18.191.251.39:8081/WeShare_web/Login";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button btnMemSubmit = view.findViewById(R.id.btnMemSubmit);
        final EditText etMemId = view.findViewById(R.id.etMemId);
        final EditText etMemPsw = view.findViewById(R.id.etMemPsw);
        final TextInputLayout tilMemId = view.findViewById(R.id.tilMemId);
        final TextInputLayout tilMemPsw = view.findViewById(R.id.tilMemPsw);
        btnMemSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilMemId.setError(null);
                tilMemPsw.setError(null);
                String memId = etMemId.getText().toString().trim();
                String memPsw = etMemPsw.getText().toString().trim();
                if (memId.isEmpty()) {
                    tilMemId.setError("請輸入帳號");
                    return;
                } else if (memPsw.isEmpty()) {
                    tilMemPsw.setError("請輸入密碼");
                    return;
                } else {
                    Toast.makeText(view.getContext(), "比對中", Toast.LENGTH_SHORT).show();

                    CompareAccount compareAccount = new CompareAccount();
                    compareAccount.execute(URL, memId, memPsw);

                    try {
                        Thread.sleep(500);

                        List<String> list = compareAccount.getList();
                        Toast.makeText(view.getContext(), list.get(0).trim(), Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = fragmentManager.findFragmentByTag("LOGIN");
                fragmentTransaction.detach(fragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }


    class CompareAccount extends AsyncTask<String, Void, List<String>> {
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


                String data = "memId=" + strings[1] + "&memPsw=" + strings[2];

                OutputStream out = con.getOutputStream();
                out.write(data.getBytes());
                out.flush();

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


            } catch (IOException e) {
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
}

