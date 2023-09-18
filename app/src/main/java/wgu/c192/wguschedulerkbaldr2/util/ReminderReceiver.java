package wgu.c192.wguschedulerkbaldr2.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {
    static final String ACTION_REMINDER = "wgu.c192.wguschedulerkbaldr2.ACTION_REMINDER";
    private static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null) {
            if (action.equals(ACTION_REMINDER)) {
                // Handle reminder events here
                String customMessage = intent.getStringExtra("data");

                if (customMessage != null && !customMessage.isEmpty()) {
                    // Show a toast message with the custom message (if provided)
                    Toast.makeText(context, "test", Toast.LENGTH_LONG);
                } else {
                    // Handle your alarm logic here
                    // You can use the intent data (e.g., alarm key) to identify the specific alarm
                    String alarmKey = intent.getStringExtra("alarmKey");
                    if (alarmKey != null) {
                        // Handle the alarm based on the alarmKey
                        // For example, you can display a notification or perform a specific action
                    }
                }
            } else if (action.equals(ACTION_BOOT_COMPLETED)) {
                // Handle device boot completed event here
                ReminderManager.registerAllAlarms(context);
            }
        } else{

        }
    }


}
