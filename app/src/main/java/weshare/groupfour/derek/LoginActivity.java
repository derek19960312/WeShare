package weshare.groupfour.derek;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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


public class LoginActivity extends AppCompatActivity {

    String URL = "http://192.168.168.123:8081/WeShare_web/Login";

    //String URL = "http://10.120.26.19:8081/WeShare_web/Login";

//    String URL = "http://18.191.251.39:8081/WeShare_web/Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button btnMemSubmit = findViewById(R.id.btnMemSubmit);
        final EditText etMemId = findViewById(R.id.etMemId);
        final EditText etMemPsw = findViewById(R.id.etMemPsw);
        final TextInputLayout tilMemId = findViewById(R.id.tilMemId);
        final TextInputLayout tilMemPsw = findViewById(R.id.tilMemPsw);
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
                    Toast.makeText(LoginActivity.this, "比對中", Toast.LENGTH_SHORT).show();

                    CallServlet callServlet = new CallServlet();
                    String requestData = "memId=" + memId + "&memPsw=" + memPsw;
                    callServlet.execute(URL, requestData);

                    try {
                        Thread.sleep(500);

                        List<String> list = callServlet.getList();

                        if( list.get(0).trim().equals("登入成功")){


                            etMemId.setText("");
                            etMemPsw.setText("");
                            finish();
                            Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                        }else{
                            etMemId.setText("");
                            etMemPsw.setText("");
                            Toast.makeText(LoginActivity.this, "帳號密碼錯誤請重新輸入", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "連線錯誤", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        ImageView ivClose = findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



}

