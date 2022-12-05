package com.jcl.test2.sms.receive;

import static android.app.Activity.RESULT_OK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jcl.test2.Constant.Constant;

/*
 * 发送消息广播
 * */
public class SentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("发送消息");
        Log.d("---Receiver---", "-----发送消息-----:" + intent.getAction());

        if (intent.getAction().equals(Constant.SENT_SMS_ACTION)) {
            int code = getResultCode();
            if (code == RESULT_OK) {
                //        ToastUtils.s(context, "Send successful");
                Bundle bundle = intent.getExtras();
                if (null != bundle) {
                    int type = bundle.getInt("type");
                    if (0 == type) {
                        System.out.println("发送成功存储");
                    } else {
                        System.out.println("发送失败");
                    }
                }

                Log.e("---Receiver---", "--------------Send Sms success ");

            } else {
                Log.e("---Receiver---", "----------Send Sms failed  ");
            }


        }

    }
}
