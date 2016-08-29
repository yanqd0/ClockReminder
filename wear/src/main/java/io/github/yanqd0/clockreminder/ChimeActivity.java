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

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

/**
 * To show a chime interface, which will close automatically.
 *
 * @author yanqd1
 */
public final class ChimeActivity extends Activity {
    private static final long MAX_MILLIS = 10000L;

    private final CountDownTimer countDownTimer = new CountDownTimer(MAX_MILLIS, MAX_MILLIS) {
        @Override
        public void onTick(long millisUntilFinished) {
            // pass
        }

        @Override
        public void onFinish() {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chime);

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        TextView clockText = (TextView) findViewById(R.id.clock_text);
        clockText.setText(String.valueOf(hour));
        Log.i(Option.TAG, "hour: " + hour);
    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
        if (!isFinishing()) {
            finish();
        }
    }
}
