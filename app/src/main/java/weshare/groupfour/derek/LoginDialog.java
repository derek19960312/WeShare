package weshare.groupfour.derek;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;


public class LoginDialog extends DialogFragment {
    final static int SUCCESS = 0;
    final static int FALSE = 1;
    String URL = ServerURL.IP_LOGIN;

    View view;
    EditText etMemId;
    EditText etMemPsw;
    TextInputLayout tilMemId;
    TextInputLayout tilMemPsw;
    AlertDialog alertDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_login,null);
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


                CallServlet callServlet = new CallServlet();
                String requestData = "action=login&memId=" + memId + "&memPsw=" + memPsw;
                Gson gson = new Gson();
                try {

                    String result = new CallServlet().execute(ServerURL.IP_LOGIN,requestData).get();


                    Log.e("result",result);
                    JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
                    Log.e("jsonObject",jsonObject.toString());
                    String i = jsonObject.get("LoginStatus").getAsString();

//                        if () {
//                            tilMemId.setError("請輸入帳號");
//                            return;
//                        } else if (memPsw.isEmpty()) {
//                            tilMemPsw.setError("請輸入密碼");
//                            return;
//                        } else {}

                      //登入成功


                } catch (Exception e) {
                    Log.e("e",e.getLocalizedMessage());
                    Toast.makeText(view.getContext(), "1213211", Toast.LENGTH_SHORT).show();
                }








            }
        });
        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();


        return alertDialog;
    }


}
