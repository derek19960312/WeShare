package weshare.groupfour.derek;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import weshare.groupfour.derek.CallServlet;
import weshare.groupfour.derek.R;


public class LoginDialog extends DialogFragment {

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
        view = inflater.inflate(R.layout.fragment_login,null);
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
                        if (list.get(0).trim().equals("成功")) {
                            etMemId.setText("");
                            etMemPsw.setText("");
                            Toast.makeText(view.getContext(), "登入成功", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } else {
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

//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//
//
//        switch (which) {
//            case DialogInterface.BUTTON_POSITIVE:
//                tilMemId.setError(null);
//                tilMemPsw.setError(null);
//                String memId = etMemId.getText().toString().trim();
//                String memPsw = etMemPsw.getText().toString().trim();
//                if (memId.isEmpty()) {
//                    tilMemId.setError("請輸入帳號");
//                    return;
//                } else if (memPsw.isEmpty()) {
//                    tilMemPsw.setError("請輸入密碼");
//                    return;
//                } else {
//                    Toast.makeText(view.getContext(), "比對中", Toast.LENGTH_SHORT).show();
//                    CallServlet callServlet = new CallServlet();
//                    String requestData = "memId=" + memId + "&memPsw=" + memPsw;
//                    callServlet.execute(URL, requestData);
//                    try {
//                        Thread.sleep(500);
//                        List<String> list = callServlet.getList();
//                        if (list.get(0).trim().equals("成功")) {
//                            etMemId.setText("");
//                            etMemPsw.setText("");
//                            Toast.makeText(view.getContext(), "登入成功", Toast.LENGTH_SHORT).show();
//                        } else {
//                            etMemId.setText("");
//                            etMemPsw.setText("");
//                            Toast.makeText(view.getContext(), "帳號密碼錯誤請重新輸入", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        Toast.makeText(view.getContext(), "連線錯誤", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//            case DialogInterface.BUTTON_NEGATIVE:
//                dialog.cancel();
//                break;
//            default:
//                break;
//        }
//    }
}
