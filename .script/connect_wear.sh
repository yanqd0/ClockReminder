#!/usr/bin/env bash

adb forward tcp:4444 localabstract:/adb-hub
# adb connect localhost:4444
adb connect 127.0.0.1:4444
adb devices
