package weshare.groupfour.derek.myCourseOrders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.FriendChat.State;
import weshare.groupfour.derek.callServer.CallServlet;
import weshare.groupfour.derek.callServer.ServerURL;
import weshare.groupfour.derek.courseReservation.CourseReservationVO;
import weshare.groupfour.derek.util.Connect_WebSocket;
import weshare.groupfour.derek.util.RequestDataBuilder;

public class QrcodeCheck extends AppCompatActivity {
    CourseReservationVO crVO;
    Gson gson = new Gson();
    String request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_check);

        crVO = (CourseReservationVO) getIntent().getExtras().getSerializable("CrvVO");
        RequestDataBuilder rdb = new RequestDataBuilder();
        rdb.build()
                .setAction("confirm_for_course")
                .setData("crvId", crVO.getCrvId());
        request = rdb.create();
        startQrcodeScanner();

    }

    private void startQrcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");       //底部提示的文字
        integrator.setCameraId(0);          //前面或後面的相機
        integrator.setBeepEnabled(true);    //掃描成功後發出 BB 聲
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {


            if (crVO.getCrvId().equals(result.getContents())) {
                Toast.makeText(this, "驗證成功", Toast.LENGTH_LONG).show();
                //new CallServlet(this).execute(ServerURL.IP_COURSERESERVATION, request);

                Connect_WebSocket.confirmCourseWebSocketClient.send(gson.toJson(new State("success", crVO.getMemId())));
                finish();

            } else {
                Toast.makeText(this, "請掃描正確QRCODE", Toast.LENGTH_LONG).show();

                Connect_WebSocket.confirmCourseWebSocketClient.send(gson.toJson(new State("fail", crVO.getMemId())));
                finish();
            }

        } else {
            finish();
        }
    }

}
