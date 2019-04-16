package weshare.groupfour.derek.util;

import android.util.Base64;

import java.util.concurrent.ExecutionException;

import weshare.groupfour.derek.CallServer.CallServlet;
import weshare.groupfour.derek.CallServer.ServerURL;

public class Tools {

    public byte[] getPicbymemId(String memId) {
        byte[] bmemImage = null;
        String memBase64 = null;
        try {
            memBase64 = new CallServlet().execute(ServerURL.IP_GET_PIC, "action=get_member_pic&memId=" + memId).get();
            if (memBase64 != null) {
                bmemImage = Base64.decode(memBase64, Base64.DEFAULT);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bmemImage;
    }
}
