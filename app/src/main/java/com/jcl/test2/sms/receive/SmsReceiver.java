/*
 * Copyright 2014 Jacob Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jcl.test2.sms.receive;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.jcl.test2.Db.MsgDbUtil;
import com.jcl.test2.R;

/**
 * Needed to make default sms app for testing
 */
public class SmsReceiver extends BroadcastReceiver {

    private NotificationManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("系统短信接收到短信通知");
        String body = MsgDbUtil.getSmsBody(intent);
        String address = MsgDbUtil.getSmsAddress(intent);
        new MsgDbUtil(context).insertSms(context,address,body,1);

    }
}
