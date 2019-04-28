package weshare.groupfour.derek.callServer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import weshare.groupfour.derek.R;
import weshare.groupfour.derek.home.MainActivity;
import weshare.groupfour.derek.util.Tools;

public class ChangeIPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ip);

        final EditText etIP = findViewById(R.id.etIP);


        final Button btn1233 = findViewById(R.id.btn1233);

        btn1233.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String IP = etIP.getText().toString().trim();
                if (!IP.isEmpty()) {
                    //ServerURL.IP = "http://" + IP + ":8081";
                    Tools.Toast(ChangeIPActivity.this, ServerURL.IP);
                    if (Tools.networkConnected(ChangeIPActivity.this)) {
                        Intent intent = new Intent(ChangeIPActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Tools.Toast(ChangeIPActivity.this, "請確認網路連線正常");
                    }

                }else{
                    Tools.Toast(ChangeIPActivity.this, "請勿空白");
                }
            }
        });




    }
}
