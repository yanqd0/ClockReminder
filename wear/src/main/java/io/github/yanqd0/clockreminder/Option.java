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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * This contains common options and functions.
 * <p>
 * And yes, it's a silver bullet class. But luckily, the project is small.
 *
 * @author yanqd0
 */
final class Option {
    static final String TAG = "ClockReminder";

    private Option() {
        throw new UnsupportedOperationException("This class should never be instantiated!");
    }

    static boolean isReminding(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.reminder_switch);
        try {
            return preferences.getBoolean(key, false);
        } catch (ClassCastException e) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key);
            editor.apply();
            return false;
        }
    }

    static void startReminding(Context context) {
        if (isReminding(context)) return;
        Log.v(TAG, "startReminding()");
        updatePreferences(context, true);

        Intent intent = new Intent(context, ActionService.class);
        intent.setAction(ActionService.ACTION_START);
        context.startService(intent);
    }

    static void stopReminding(Context context) {
        if (!isReminding(context)) return;
        Log.v(TAG, "stopReminding()");
        updatePreferences(context, false);

        Intent intent = new Intent(context, ActionService.class);
        intent.setAction(ActionService.ACTION_STOP);
        context.startService(intent);
    }

    private static void updatePreferences(Context context, boolean enabled) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.reminder_switch);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, enabled);
        editor.apply();
    }
}
