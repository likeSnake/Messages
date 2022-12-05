package com.jcl.test2.Constant;

import android.provider.Telephony;

public class Constant {
    //public static   int[] colors = {R.drawable.circle_lightblue, R.drawable.circle_lightgreen, R.drawable.circle_seagreen, R.drawable.circle_yellow, R.drawable.circle_magenta, R.drawable.circle_hotpink};

    public static final int ALL = 0;
    public static final int INBOX = 1;
    public static final int SENT = 2;
    public static final int DRAFT = 3;
    public static final int OUTBOX = 4;
    public static final int FAILED = 5;
    public static final int QUEUED = 6;
    public static final int SCHEDULE = 1006;//自定义定时发送的数据
    public static final int EVENT_SMS = 100;
    public static final int RESULT_CODE = 101;
    public static final int ITEM_CONTACTION = Integer.MAX_VALUE;
    public static final int ITEM_SEARCH = Integer.MAX_VALUE - 1;
    public static final String SENT_SMS_ACTION =Constant.class.getPackage().getName()+ ".SMS_SENT";
    public static final String SENT_MMS_ACTION = Constant.class.getPackage().getName() + ".MMS_SENT";
    public static final String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
    public static final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    public static final String SMS_URI_MMS = "content://mms"; //此处查询的表是pdu表

    public static final String SMS_URI_ALL = "content://sms/"; // 所有短信
    public static final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
    public static final String SMS_URI_SEND = "content://sms/sent"; // 已发送
    public static final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿
    public static final String SMS_URI_OUTBOX = "content://sms/outbox"; // 发件箱
    public static final String SMS_URI_FAILED = "content://sms/failed"; // 发送失败
    public static final String SMS_URI_QUEUED = "content://sms/queued"; // 待发送列表
    public static final String SMS_DATABASE_NAME = "schedule_sms";
    public static final String SMS_TABLE_NAME = "schedule_sms_db";

    public static final String BLACK_NUM_DATABASE_NAME = "schedule_sms.db";
    public static final String BLACK_NUM_TABLE_NAME = "classic_blacklist_tab";
    public static final int VERSION_CODE = 1;
    public static final String[] projection = new String[]{Telephony.Sms.THREAD_ID, Telephony.Sms.ADDRESS, Telephony.Sms.READ, Telephony.Sms.BODY, Telephony.Sms.DATE};
    public static final String[] adProjection = new String[]{"_id", "address", "thread_id"};

    public static final String[] rProjection = new String[]{"* from threads --"};
    public static final String[] projectionOne = new String[]{"DISTINCT thread_id"};

    public static final String[] mmsProjections = new String[]{"_id", "thread_id", "date", "sub", "msg_box", "read"};
    public static final String[] smsProjection = new String[]{"_id", "thread_id", "address", "person", "date", "date_sent", "read", "status", "body", "protocol", "type"};
    public static final String[] tProjections = new String[]{"_id", "thread_id","recipient_ids"};


    public static final int WIDTH_DEFAULT = 100;
    public static final int WIDTH_MAX = 1000;
    public static final int WIDTH_MIN = 0;

    public static final int WEIGHT_DEFAULT = 400;
    public static final int WEIGHT_MAX = 1000;
    public static final int WEIGHT_MIN = 0;

    public static final float ITALIC_DEFAULT = 0f;
    public static final float ITALIC_MAX = 1f;
    public static final float ITALIC_MIN = 0f;
}
