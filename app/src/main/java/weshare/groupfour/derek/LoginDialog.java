package weshare.groupfour.derek;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;
import weshare.groupfour.derek.CourseType.CourseTypeVO;
import weshare.groupfour.derek.util.Tools;


public class LoginDialog extends DialogFragment {



    View view;
    EditText etMemId;
    EditText etMemPsw;
    TextInputLayout tilMemId;
    TextInputLayout tilMemPsw;
    AlertDialog alertDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_login, null);
        etMemId = view.findViewById(R.id.etMemId);
        etMemPsw = view.findViewById(R.id.etMemPsw);
        tilMemId = view.findViewById(R.id.tilMemId);
        tilMemPsw = view.findViewById(R.id.tilMemPsw);
        Button btnLogin = view.findViewById(R.id.btnLogin);
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
                                Toast.makeText(getActivity(), "帳號密碼錯誤", Toast.LENGTH_LONG);
                                etMemId.setText("");
                                etMemPsw.setText("");
                                return;
                            case "ConnectionProblem":
                                Toast.makeText(getActivity(), "連線異常", Toast.LENGTH_LONG);
                                return;
                        }
                    } else {
                        //登入成功
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

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myAccount", Context.MODE_PRIVATE);
                        sharedPreferences.edit()
                                .putString("memId",memberVO.getMemId())
                                .putString("memPsw",memberVO.getMemPsw())
                                .putString("memImage",memBase64)
                                .commit();

                        alertDialog.dismiss();
                        etMemId.setText("");
                        etMemPsw.setText("");

                    }

                } catch (Exception e) {
                    Log.e("e", e.toString());
                }


            }
        });
        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                etMemId.setText("");
                etMemPsw.setText("");
            }
        });
        alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        return alertDialog;
    }


}
