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
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

/**
 * It is the main activity, which provides the switch of reminding.
 *
 * @author yanqd0
 */
public final class WearActivity extends Activity implements OnCheckedChangeListener {
    private Switch reminderSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wear_switch);
        reminderSwitch = (Switch) findViewById(R.id.reminder_switch);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reminderSwitch.setOnCheckedChangeListener(this);
        reminderSwitch.setChecked(Option.isReminding(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        reminderSwitch.setOnCheckedChangeListener(null);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Option.startReminding(this);
        } else {
            Option.stopReminding(this);
        }

        changeViewColor(isChecked);
    }

    private void changeViewColor(boolean isChecked) {
        View background = findViewById(R.id.reminder_background);
        TextView hint = (TextView) findViewById(R.id.reminder_hint);
        if (isChecked) {
            @SuppressWarnings("deprecation")
            int checkedColor = getResources().getColor(android.R.color.holo_orange_dark);
            background.setBackgroundColor(checkedColor);

            @SuppressWarnings("deprecation")
            int uncheckedColor = getResources().getColor(android.R.color.holo_blue_dark);
            hint.setTextColor(uncheckedColor);
        } else {
            @SuppressWarnings("deprecation")
            int checkedColor = getResources().getColor(android.R.color.holo_blue_dark);
            background.setBackgroundColor(checkedColor);

            @SuppressWarnings("deprecation")
            int uncheckedColor = getResources().getColor(android.R.color.holo_orange_dark);
            hint.setTextColor(uncheckedColor);
        }
    }

}
