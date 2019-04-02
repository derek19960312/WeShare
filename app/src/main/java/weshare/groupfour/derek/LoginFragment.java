package weshare.groupfour.derek;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;


public class LoginFragment extends Fragment {
    String URL = "http://192.168.168.123:8081/WeShare_web/Login";

    //String URL = "http://10.120.26.19:8081/WeShare_web/Login";

//    String URL = "http://18.191.251.39:8081/WeShare_web/Login";
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login,container,false);
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

                    CallServlet callServlet = new CallServlet();
                    String requestData = "memId=" + memId + "&memPsw=" + memPsw;
                    callServlet.execute(URL, requestData);

                    try {
                        Thread.sleep(500);

                        List<String> list = callServlet.getList();

                        if( list.get(0).trim().equals("登入成功")){


                            etMemId.setText("");
                            etMemPsw.setText("");

                            Toast.makeText(view.getContext(), "登入成功", Toast.LENGTH_SHORT).show();
                        }else{
                            etMemId.setText("");
                            etMemPsw.setText("");
                            Toast.makeText(view.getContext(), "帳號密碼錯誤請重新輸入", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), "連線錯誤", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });




        return view;
    }

}
