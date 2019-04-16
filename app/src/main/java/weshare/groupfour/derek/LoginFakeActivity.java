package weshare.groupfour.derek;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;

public class LoginFakeActivity extends AppCompatActivity {

    EditText etMemId;
    EditText etMemPsw;
    TextInputLayout tilMemId;
    TextInputLayout tilMemPsw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        etMemId = findViewById(R.id.etMemId);
        etMemPsw = findViewById(R.id.etMemPsw);
        tilMemId = findViewById(R.id.tilMemId);
        tilMemPsw = findViewById(R.id.tilMemPsw);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilMemId.setError(null);
                tilMemPsw.setError(null);
                String memId = etMemId.getText().toString().trim();
                String memPsw = etMemPsw.getText().toString().trim();


                String requestData = "action=login&memId=" + memId + "&memPsw=" + memPsw;
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                try {

                    String result = new CallServlet().execute(ServerURL.IP_LOGIN, requestData).get();
                    if (result.contains("LoginStatus")) {
                        //登入失敗
                        JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
                        String errorMsgs = jsonObject.get("errorMsgs").getAsString();
                        switch (errorMsgs) {
                            case "NoAccount":
                                tilMemId.setError("請輸入帳號");
                                return;
                            case "NoPassword":
                                tilMemPsw.setError("請輸入密碼");
                                return;
                            case "LoginFalse":
                                Toast.makeText(LoginFakeActivity.this ,"帳號密碼錯誤", Toast.LENGTH_LONG).show();
                                etMemId.setText("");
                                etMemPsw.setText("");
                                return;
                            case "ConnectionProblem":
                                Toast.makeText(LoginFakeActivity.this, "連線異常", Toast.LENGTH_LONG).show();
                                return;
                        }
                    } else {
                        //登入成功
                        Toast.makeText(LoginFakeActivity.this, "登入成功", Toast.LENGTH_LONG).show();
                        MemberVO memberVO = gson.fromJson(result, MemberVO.class);
                        //byte[] bmemImage = null;
                        String memBase64 = null;
                        try {
                            memBase64 = new CallServlet().execute(ServerURL.IP_GET_PIC, "action=get_member_pic&memId=" + memId).get();
                            if (memBase64 != null) {
                                //  bmemImage = Base64.decode(memBase64, Base64.DEFAULT);
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        SharedPreferences sharedPreferences = getSharedPreferences("myAccount", Context.MODE_PRIVATE);
                        sharedPreferences.edit()
                                .putString("memId",memberVO.getMemId())
                                .putString("memPsw",memberVO.getMemPsw())
                                .putString("memImage",memBase64)
                                .commit();


                        finish();


                    }

                } catch (Exception e) {
                    Log.e("e", e.toString());
                }


            }
        });
        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                etMemId.setText("");
                etMemPsw.setText("");
            }
        });
    }
}
