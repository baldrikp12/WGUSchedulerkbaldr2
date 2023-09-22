package wgu.c192.wguschedulerkbaldr2.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ReminderManager {
    private static final String PREF_NAME = "MyReminders";

    public static void setReminder(Context context, String alarmKeyDate, String dateString) {
        // Parse the date string into milliseconds
        long alarmTimeMillis = parseDateStringToMillis(dateString);

        if (alarmTimeMillis == -1) {
            // Handle invalid date string
            showShortToast(context, "Invalid date format");
            return;
        }

        // Create an Intent for the BroadcastReceiver
        Intent intent = new Intent(context, ReminderReceiver.class);

        // Create a PendingIntent with the same Intent and flags
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager service and set the alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Set the alarm to trigger at the specified time
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);

        // Store reminder in shared preferences
        updateSharedPrefs(context, alarmKeyDate, alarmTimeMillis);

        // Show a toast message indicating the reminder was set
        showShortToast(context, "Reminder Set");
    }

    public static void cancelReminder(Context context, String alarmKeyDate) {
        // Create an Intent for the BroadcastReceiver
        Intent intent = new Intent(context, ReminderReceiver.class);

        // Create a PendingIntent with the same Intent and flags
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager service and cancel the alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        // Remove reminder from shared preferences
        removeFromSharedPrefs(context, alarmKeyDate);

        // Show a toast message indicating the reminder was canceled
        showShortToast(context, "Reminder Canceled");
    }

    public static boolean isReminderSet(Context context, String alarmKey) {
        // Use shared preferences to check if the reminder is set
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        System.out.println(alarmKey);
        return sharedPreferences.contains(alarmKey);
    }

    public static void registerAllAlarms(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Iterate through your shared preferences and re-register alarms
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("Alarm_course_") && entry.getValue() instanceof Long) {
                String alarmKeyDate = entry.getKey() + "_date";
                long alarmTimeMillis = (long) entry.getValue();

                // Check if the alarm is enabled
                boolean isAlarmEnabled = isReminderSet(context, alarmKeyDate);

                if (isAlarmEnabled) {
                    // Create an Intent for the BroadcastReceiver
                    Intent intent = new Intent(context, ReminderReceiver.class);
                    intent.setAction(ReminderReceiver.ACTION_REMINDER);
                    intent.putExtra(alarmKeyDate, entry.getKey());

                    // Create a PendingIntent with the same Intent and flags
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                    // Get the AlarmManager service and set the alarm
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    // Set the alarm to trigger at the specified time
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
                }
            }
        }
    }

    private static long parseDateStringToMillis(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void showShortToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private static void updateSharedPrefs(Context context, String alarmKeyDate, long alarmTimeMillis) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(alarmKeyDate, alarmTimeMillis);
        editor.apply();
    }

    private static void removeFromSharedPrefs(Context context, String alarmKeyDate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(alarmKeyDate);
        editor.apply();
    }
}
