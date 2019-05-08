package weshare.groupfour.derek.home;

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

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.GetMyLikesThread;
import weshare.groupfour.derek.R;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.member.MemberVO;
import weshare.groupfour.derek.member.TeacherVO;
import weshare.groupfour.derek.util.Holder;
import weshare.groupfour.derek.util.Tools;

public class LoginFakeActivity extends AppCompatActivity {

    EditText etMemId;
    EditText etMemPsw;
    TextInputLayout tilMemId;
    TextInputLayout tilMemPsw;
    Button btnLogin;
    Button btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        //先確認是否登入過
        String memId = new Tools().getSharePreAccount().getString("memId", null);
        if (memId != null) {
            finish();
        } else {


            //設定畫面寬高
            Window win = getWindow();
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.dimAmount = 0.2f;
            win.setAttributes(lp);


            findViews();

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tilMemId.setError(null);
                    tilMemPsw.setError(null);
                    String memId = etMemId.getText().toString().trim();
                    String memPsw = etMemPsw.getText().toString().trim();

                    String requestData = "action=login&memId=" + memId + "&memPsw=" + memPsw;

                    try {

                        String result = new CallServlet(LoginFakeActivity.this).execute(ServerURL.IP_MEMBER, requestData).get();
                        //登入失敗
                        if (result.contains("LoginStatus")) {

                            JsonObject jsonObject = Holder.gson.fromJson(result, JsonObject.class);

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

                            //登入成功
                        } else {

                            MemberVO memberVO = Holder.gson.fromJson(result, MemberVO.class);
                            //查圖片
                            Map<String,String> requestMap = new HashMap<>();
                            requestMap.put("action","get_member_pic_base64");
                            requestMap.put("memId",memberVO.getMemId());
                            String requestData1 = Tools.RequestDataBuilder(requestMap);
                            String base64 = new CallServlet(LoginFakeActivity.this).execute(ServerURL.IP_MEMBER,requestData1).get();


                            SharedPreferences sharedPreferences = Tools.getSharePreAccount();
                            sharedPreferences.edit()
                                    .putString("memId", memberVO.getMemId())
                                    .putString("memPsw", memberVO.getMemPsw())
                                    .putString("memImage",base64)
                                    .putString("memName", memberVO.getMemName())
                                    .apply();
                            isAteacher(memId);
                            Tools.Toast(LoginFakeActivity.this,"登入成功");

                            //更新收藏
                            new GetMyLikesThread().start();



                            finish();
                        }
                    } catch (Exception e) {
                        Log.e("e", e.toString());
                    }
                }
            });

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

    private void findViews() {
        etMemId = findViewById(R.id.etMemId);
        etMemPsw = findViewById(R.id.etMemPsw);
        tilMemId = findViewById(R.id.tilMemId);
        tilMemPsw = findViewById(R.id.tilMemPsw);
        btnLogin = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancel);
    }

    public boolean isAteacher(String memId) {
        String action = "action=find_by_memId";
        String requestData = action + "&memId=" + memId;
        try {
            String result = new CallServlet(LoginFakeActivity.this).execute(ServerURL.IP_TEACHER, requestData).get();
            TeacherVO teacherVO = Holder.gson.fromJson(result, TeacherVO.class);
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
