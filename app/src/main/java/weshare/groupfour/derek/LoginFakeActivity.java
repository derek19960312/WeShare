package weshare.groupfour.derek;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.member.TeacherVO;
import weshare.groupfour.derek.util.Join;

import weshare.groupfour.derek.util.Tools;

public class LoginFakeActivity extends AppCompatActivity {

    EditText etMemId;
    EditText etMemPsw;
    TextInputLayout tilMemId;
    TextInputLayout tilMemPsw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        String memId = new Tools().getSharePreAccount().getString("memId", null);
        if (memId != null) {
            finish();
        } else {
            Tools.Toast(this, "請先登入");
            //設定寬高
            Window win = getWindow();
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.dimAmount = 0.2f;
            win.setAttributes(lp);

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

                        String result = new CallServlet().execute(ServerURL.IP_MEMBER, requestData).get();
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
                                    Toast.makeText(LoginFakeActivity.this, "帳號密碼錯誤", Toast.LENGTH_LONG).show();
                                    etMemId.setText("");
                                    etMemPsw.setText("");
                                    return;
                                case "ConnectionProblem":
                                    Toast.makeText(LoginFakeActivity.this, "連線異常", Toast.LENGTH_LONG).show();
                                    return;
                            }
                        } else {
                            //登入成功

                            MemberVO memberVO = gson.fromJson(result, MemberVO.class);
                            String memBase64 = new Join().getMemberPicB64(memberVO.getMemId());

                            SharedPreferences sharedPreferences = Tools.getSharePreAccount();
                            sharedPreferences.edit()
                                    .putString("memId", memberVO.getMemId())
                                    .putString("memPsw", memberVO.getMemPsw())
                                    .putString("memImage", memBase64)
                                    .putString("memName",memberVO.getMemName())
                                    .apply();
                            isAteacher(memId);
                            Toast.makeText(LoginFakeActivity.this, "登入成功", Toast.LENGTH_LONG).show();
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

    public boolean isAteacher(String memId) {
        String action = "action=find_by_memId";
        String requestData = action + "&memId=" + memId;
        try {
            String result = new CallServlet().execute(ServerURL.IP_TEACHER, requestData).get();
            TeacherVO teacherVO = new Gson().fromJson(result, TeacherVO.class);
            if (teacherVO != null) {
                SharedPreferences spf = getSharedPreferences("myAccount", MODE_PRIVATE);
                spf.edit().putString("teacherId", teacherVO.getTeacherId()).apply();
                return true;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


}
