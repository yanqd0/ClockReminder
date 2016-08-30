/*
 * ClockReminder: To remind you in every clock on Android Wear.
 * Copyright (C) 2016 Yan QiDong
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.yanqd0.clockreminder;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import java.util.Calendar;

import static io.github.yanqd0.clockreminder.BuildConfig.APPLICATION_ID;

/**
 * To take some actions in the background.
 *
 * @author yanqd0
 */
public final class ActionService extends IntentService {
    static final String ACTION_START = APPLICATION_ID + ".action.ACTION_START";
    static final String ACTION_STOP = APPLICATION_ID + ".action.ACTION_STOP";
    private static final String ACTION_CHIME = APPLICATION_ID + ".action.ACTION_CHIME";

    public ActionService() {
        super("ActionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;

        String action = intent.getAction();
        if (action == null) return;

        switch (action) {
            case ACTION_CHIME:
                handleActionChime();
            case ACTION_START:
                handleActionStart();
                break;
            case ACTION_STOP:
                handleActionStop();
                break;
            default:
                break;
        }
    }

    private void handleActionChime() {
        Log.d(Option.TAG, "handleActionChime()");
        wakeLock();
        vibrate();
        startChimeActivity();
        scheduleNextChime();
    }

    private void wakeLock() {
        PowerManager pm = (PowerManager) this.getSystemService(POWER_SERVICE);
        @SuppressWarnings("deprecation")
        int levelAndFlags =
                PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK;
        PowerManager.WakeLock wakeLock = pm.newWakeLock(levelAndFlags, Option.TAG);
        wakeLock.acquire(1000);
    }

    private void vibrate() {
        final long[] intervals = new long[]{0, 150, 100, 250, 100, 400};
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(intervals, -1);
    }

    private void startChimeActivity() {
        Log.v(Option.TAG, "startChimeActivity()");
        Intent intent = new Intent(this, ChimeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void handleActionStart() {
        Log.d(Option.TAG, "handleActionStart()");
        scheduleNextChime();
    }

    private void scheduleNextChime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        long nextTime = cal.getTimeInMillis();
        nextTime += AlarmManager.INTERVAL_HOUR;

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, nextTime, getClockIntent());
    }

    private PendingIntent getClockIntent() {
        Intent intent = new Intent(this, ActionService.class);
        intent.setAction(ACTION_CHIME);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void handleActionStop() {
        Log.d(Option.TAG, "handleActionStop()");
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(getClockIntent());
    }
}
