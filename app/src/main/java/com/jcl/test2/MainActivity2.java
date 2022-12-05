package com.jcl.test2;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.role.RoleManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jcl.test2.Adapter.AllAdapter;
import com.jcl.test2.Adapter.MsgAdapter;
import com.jcl.test2.Db.MsgDbUtil;
import com.jcl.test2.Util.FileUtil;
import com.jcl.test2.Util.GetLinkManUtil;
import com.jcl.test2.pojo.AllInfo;
import com.jcl.test2.pojo.ContactsInfo;
import com.jcl.test2.pojo.MmsInfo;
import com.jcl.test2.pojo.MsgInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
    private int temp = 0;
    private TextView msg;
    private TextView iMsg;
    private TextView yMsg;
    private LinearLayout left;
    private List<MmsInfo> MList = new ArrayList<>();
    private ImageButton back_btn;
    private LinearLayout right;
    List<AllInfo> allInfo = new ArrayList<>();
    private List<MsgInfo> msgList = new ArrayList<>();
    private TextView phone;
    private ImageView addCX;
    private ImageView add_cx;
    private ListView mListView;
    private ImageView send_more;
    private final Uri CONTENT_URI_PART = Uri.parse( "content://mms/part" ); //彩信附件表
    MsgAdapter msgAdapter;
    AllAdapter allAdapter;
    private ImageView call;
    private List<Map<String, Object>> data;
    private static final int SEND_SMS = 100;
    private SimpleAdapter sa;
    private static final int REQUEST_FILE_CODE = 200;
    String url = "";
    private RecyclerView recyclerView;
    Map<String, Object> map = new HashMap<String, Object>();
    public static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    String h;
    String hg;
    String FName ;
    String itemID;
    String all_phone = "";
    String[] phones;
    String thread_id;

    ContentObserver contentObserver = new ContentObserver(new Handler()){
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            initMsg();
            MyTest();
            allAdapter.notifyDataSetChanged();
            System.out.println("Main2监听到短信");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        //取消严格模式  FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy( builder.build() );
        }
        Uri smsUri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(smsUri,true,contentObserver);
        add_cx = findViewById(R.id.add_cx);
        FName = getIntent().getStringExtra("FName");
        h = getIntent().getStringExtra("phone");
        hg ="+86"+getIntent().getStringExtra("phone");
        itemID = getIntent().getStringExtra("ID");
        phones = getIntent().getStringArrayExtra("phones");
        thread_id = getIntent().getStringExtra("thread_id");
        System.out.println("t:"+thread_id);
        if (phones != null){
        for (int i = 0; i < phones.length; i++) {
            if (i == 0){
            all_phone += phones[i];
            }else{
                all_phone += ","+phones[i];
            }
        }
        }
        if (h == null){
            h = phones[0];
        }
        System.out.println("电话号码:"+h+"首字母:"+FName+"itemID:"+itemID+"phones:");

        mmsInbox();
        mmsSent();
        initMsg();
        MyTest();
        ImageView bt_send=(ImageView)findViewById(R.id.bt_send_message);
        msg = findViewById(R.id.msg);
        add_cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intoFileManager();
            }
        });
        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0){
                    bt_send.setVisibility(View.VISIBLE);
                }else {
                    bt_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        call = findViewById(R.id.call_phone);
        registerForContextMenu(call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(call);
            }
        });
        phone = findViewById(R.id.send_name);
        back_btn = findViewById(R.id.back_btn);
        addCX = findViewById(R.id.add_cx);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        start2();
        //iMsg.setVisibility(View.GONE);
        //initView();
        /*Bundle myBundle = this.getIntent().getExtras();
        String ph = myBundle.getString("phone");
        int nph = Integer.valueOf(ph).intValue();
        System.out.println(nph);*/
        //this.phone.setText(nph);
        List<ContactsInfo> linkMan = GetLinkManUtil.getLinkMan(getApplicationContext());
        Boolean bl = true;
        if (phones == null) {
            for (int i3 = 0; i3 < linkMan.size(); i3++) {
                String LPhone = linkMan.get(i3).getPhone();
                String name = linkMan.get(i3).getName();
                if (LPhone.equals(h) || LPhone.equals("+86" + h) || ("+86" + LPhone).equals(h)) {
                    phone.setText(name);
                    bl = false;
                    break;
                }
            }
        }
        if (bl){
            if (phones != null){
                phone.setText(all_phone);
            }else {phone.setText(h);}
        }
        bt_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        //query();
        //MmsTimeTest();
    }

    //弹出按钮框
    private void showPopupMenu(final View view) {
        final PopupMenu popupMenu = new PopupMenu(this,view);
        //menu 布局
        popupMenu.getMenuInflater().inflate(R.menu.send_more,popupMenu.getMenu());
        //点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        if (thread_id != null){
                            if (Telephony.Sms.getDefaultSmsPackage(MainActivity2.this) == null || Telephony.Sms.getDefaultSmsPackage(MainActivity2.this).equals(MainActivity2.this.getPackageName())){
                                Toast.makeText(MainActivity2.this, "Deleted", Toast.LENGTH_SHORT).show();
                                new MsgDbUtil(MainActivity2.this).deleteSmsByTId(Integer.parseInt(thread_id));
                                Intent intent = new Intent();
                                intent.setClass(MainActivity2.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P ) {
                                    RoleManager roleManager = MainActivity2.this.getSystemService(RoleManager.class);
                                    Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                                    ((Activity) MainActivity2.this).startActivityForResult(roleRequestIntent, 12);
                                } else {//获取当前程序包名
                                    Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, MainActivity2.this.getPackageName());
                                    ((Activity) MainActivity2.this).startActivityForResult(intent, 12);
                                }
                            }

                        }
                        break;
                    case R.id.action_call:
                        callPhone();
                        break;
                }
                return false;
            }
        });
        //关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        //显示菜单，不要少了这一步
        popupMenu.show();
    }

    public void callPhone(){
        Intent myCallIntent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + h));
        startActivity(myCallIntent);
    }


    private void intoFileManager() {
        checkPermission();
        //        指定选择的文件类型
        String[] mimeTypes = {"*/*"};
//        ACTION_GET_CONTENT：允许用户选择特殊种类的数据，并返回
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        StringBuilder mimeTypesStr = new StringBuilder();
        for (String mimeType : mimeTypes) {
            mimeTypesStr.append(mimeType).append("|");
        }
        intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        startActivityForResult(Intent.createChooser(intent, "选择文件"), REQUEST_FILE_CODE );
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String content = msg.getText().toString().trim();
        if (requestCode == REQUEST_FILE_CODE  && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String external = Environment.getExternalStorageDirectory().toString();
            url = "file://"+FileUtil.getPath(this, uri);
            if (!TextUtils.isEmpty(url)) {
                Toast.makeText(MainActivity2.this,url,Toast.LENGTH_SHORT).show();
                Uri u = Uri.parse(url);//图片路径
               /* int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= 24) {
                    u = FileProvider.getUriForFile(MainActivity2.this, BuildConfig.APPLICATION_ID+".fileprovider", new File(url));
                    *//*String ss = "file://"+u;
                    Uri uu = Uri.parse(ss);
                    u = uu;*//*
                } else {
                    u = Uri.fromFile(new File(url));
                }*/
                System.out.println(u);
                Intent intent=new Intent();
                PackageManager packageManager = this.getPackageManager();
                //intent = packageManager.getLaunchIntentForPackage("com.android.mms");

                intent.setAction(Intent.ACTION_SEND);

                intent.putExtra("address",h);//邮件地址

                intent.putExtra("sms_body",content);//邮件内容

                intent.putExtra(Intent.EXTRA_STREAM,u);

                intent.setType("image/*");//设置类型
              //  intent.setAction("com.android.mms");
                System.out.println("跳转");
                MainActivity2.this.startActivity(intent);
                System.out.println("跳转结束");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.i("permission", "获取权限成功");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 201);
        }
    }

    public void add_cx(View v){
    }

    public void start(){
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity2.this, LinearLayoutManager.VERTICAL, true);
        msgAdapter = new MsgAdapter(msgList,MList,this);
        recyclerView.scrollToPosition(msgAdapter.getItemCount()-1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(msgAdapter);
    }
    public void start2(){
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity2.this, LinearLayoutManager.VERTICAL, true);
        allAdapter = new AllAdapter(allInfo,this);
        recyclerView.scrollToPosition(allAdapter.getItemCount()-1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(allAdapter);
    }

    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                System.out.println("获取权限");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS);
                return;
            } else {
                System.out.println("已有权限------");
                sendSMSS();
                //已有权限
            }
        } else {
            System.out.println("API低版本 23-----");
            //API 版本在23以下
        }
    }
    private void mmsInbox(){
        MList.clear();
        System.out.println("执行mms");
        Uri uri = Uri.parse("content://mms/inbox");
        Cursor cPart= null ;
        String s = null;
        int thread_id;
        int id;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "date", "thread_id","sub"}, null, null, null);
       /* Cursor cursors = resolver.query(uri,null, null, null, null);
        String[] columnNames = cursors.getColumnNames();
        for (int i = 0; i < cursors.getColumnCount(); i++) {
            String s1 = columnNames[i];
            System.out.println(s1);
        }*/
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()){
                id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String date = cursor.getString(1);
                thread_id = cursor.getInt(cursor.getColumnIndexOrThrow("thread_id"));//会话id
                String subject = cursor.getString(cursor.getColumnIndexOrThrow( "sub" )); //彩信主题
                try {
                    if (subject != null){
                        s = new String(subject.getBytes("iso-8859-1"), "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
              //  System.out.println("id:"+id+"subject:"+s+"time:"+date+"thread_id:" +thread_id);
                //根据彩信ID查询彩信的附件
                String selectionPart = new String( "mid=" +id); //part表中的mid外键为pdu表中的_id
                cPart = getContentResolver().query(CONTENT_URI_PART, null ,selectionPart, null , null );
                String bodyStr= "" ;
                String[] coloumns = null ;
                String[] values = null ;
                while (cPart.moveToNext()){
                    if (values == null){
                        values = new String[cPart.getColumnCount()];
                    }
                    for (int i = 0; i < cPart.getColumnCount(); i++) {
                        values[i] = cPart.getString(i);
                    }
                    if (values[3].equals( "image/jpeg" ) || values[ 3 ].equals( "image/bmp" )|| values[3 ].equals( "image/gif" ) || values[ 3 ].equals( "image/jpg" ) || values[ 3 ].equals( "image/png" )){
                        Bitmap bitmap = getMmsImage(values[0]);
                        Integer t = Integer.valueOf(date);
                        MList.add(new MmsInfo(t,s,bitmap,thread_id,1,666));
                    }else if (values[3].equals( "text/plain" )){
                        if (values[12]!= null ){
                            bodyStr=getMmsText(values[0]);
                        }else {
                            bodyStr = values[13];
                        }
                    }
                }

            }
        }else {
            System.out.println("空");
        }
        if (cursor != null){
            cursor.close();
        }
        if (cPart != null){
            cPart.close();
        }
    }
    private void mmsSent(){
        System.out.println("执行mmsSent");
        Uri uri = Uri.parse("content://mms/sent");
        Cursor cPart= null ;
        String s = null;
        int thread_id;
        int id;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "date", "thread_id","sub"}, null, null, null);
       /* Cursor cursors = resolver.query(uri,null, null, null, null);
        String[] columnNames = cursors.getColumnNames();
        for (int i = 0; i < cursors.getColumnCount(); i++) {
            String s1 = columnNames[i];
            System.out.println(s1);
        }*/
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()){
                id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String date = cursor.getString(1);
                thread_id = cursor.getInt(cursor.getColumnIndexOrThrow("thread_id"));//会话id
                String subject = cursor.getString(cursor.getColumnIndexOrThrow( "sub" )); //彩信主题
                try {
                    if (subject != null){
                        s = new String(subject.getBytes("iso-8859-1"), "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //  System.out.println("id:"+id+"subject:"+s+"time:"+date+"thread_id:" +thread_id);
                //根据彩信ID查询彩信的附件
                String selectionPart = new String( "mid=" +id); //part表中的mid外键为pdu表中的_id
                cPart = getContentResolver().query(CONTENT_URI_PART, null ,selectionPart, null , null );
                String bodyStr= "" ;
                String[] coloumns = null ;
                String[] values = null ;
                while (cPart.moveToNext()){
                    if (values == null){
                        values = new String[cPart.getColumnCount()];
                    }
                    for (int i = 0; i < cPart.getColumnCount(); i++) {
                        values[i] = cPart.getString(i);
                    }
                    if (values[3].equals( "image/jpeg" ) || values[ 3 ].equals( "image/bmp" )|| values[3 ].equals( "image/gif" ) || values[ 3 ].equals( "image/jpg" ) || values[ 3 ].equals( "image/png" )){
                        Bitmap bitmap = getMmsImage(values[0]);
                        Integer t = Integer.valueOf(date);
                       // System.out.println("发送彩信的时间"+t);
                        MList.add(new MmsInfo(t,s,bitmap,thread_id,2,777));
                    }else if (values[3].equals( "text/plain" )){
                        if (values[12]!= null ){
                            bodyStr=getMmsText(values[0]);
                        }else {
                            bodyStr = values[13];
                        }
                    }
                }

            }
        }else {
            System.out.println("空");
        }
        if (cursor != null){
            cursor.close();
        }
        if (cPart != null){
            cPart.close();
        }
    }

    private void initMsg(){
        msgList.clear();
//        System.out.println(phones.length);
        if (phones != null && phones.length > 1){
            System.out.println("群发");
            return;
        }
        System.out.println("单发模式，init初始化sms");
        System.out.println("执行initMsg");
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "body", "date", "type","thread_id"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            System.out.println("判断不为空执行");
            int _id;
            int thread_id;
            String address;
            String body;
            String date;
            // 时间戳格式转换
            int type;
            while (cursor.moveToNext()) {
                _id = cursor.getInt(0);
                address = cursor.getString(1);
                if(address.equals(h) || address.equals(hg)){
                    body = cursor.getString(2);
                    date = cursor.getString(3);
                    double t = Double.parseDouble(date);
                    String substring = date.substring(0, 10);
                    Integer time = Integer.valueOf(substring);
                    //String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(t);
                    String YTime = new SimpleDateFormat("yyyy-MM-dd").format(t);
                    type = cursor.getInt(4);
                    thread_id = cursor.getInt(5);
                    msgList.add(new MsgInfo(_id,address,body,time,type,FName,YTime,thread_id));
                }else {
                    continue;
                }
                /*if(type == 1){
                    yMsg.setVisibility(View.VISIBLE);
                    iMsg.setVisibility(View.GONE);
                }
                else if (type == 2){
                    yMsg.setVisibility(View.GONE);
                    iMsg.setVisibility(View.VISIBLE);
                }*/
            }
            if (cursor != null){
                cursor.close();
            }
           // Collections.reverse(msgList);
        }
    }

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("已有权限");
                    sendSMSS();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity2.this, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case 201:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intoFileManager();
                }else {

                    Toast.makeText(MainActivity2.this, "Read file permission is denied, please give permission!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void sendSMSS() {
        String s = String.valueOf(System.currentTimeMillis());
        String substring = s.substring(0, 10);
        Integer time = Integer.valueOf(substring);
        String content = msg.getText().toString().trim();
        String p = phone.getText().toString().trim();
        if(msg != null){
            SmsManager manager = SmsManager.getDefault();
            ArrayList<String> strings = manager.divideMessage(content);
            if (phones != null && phones.length > 1){
                System.out.println("准备群发");
                for (int i = 0; i < phones.length; i++) {
                    manager.sendTextMessage(phones[i], null, content, null, null);
                }
                    msg.setText("");
                    Toast.makeText(MainActivity2.this, "Send successful!", Toast.LENGTH_SHORT).show();
                    allInfo.add(0,new AllInfo(666,999,content,time,2,null,null));
                    start2();

            }else {
            for (int i = 0; i < strings.size(); i++) {
                System.out.println("执行单发");
                manager.sendTextMessage(p, null, content, null, null);
            }
                msg.setText("");
                Toast.makeText(MainActivity2.this, "Send successful!", Toast.LENGTH_SHORT).show();
               // allInfo.add(0,new AllInfo(666,999,content,time,2,null,null));
                new MsgDbUtil(MainActivity2.this).insertSms(MainActivity2.this,p,content,2);
                System.out.println(time+"me time");
                start2();
                //msgAdapter.notifyDataSetChanged();
            }
        }
    }
    private Bitmap getMmsImage(String _id) { //读取图片附件
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getContentResolver().openInputStream(partURI);
            //byte[] buffer = new byte[256];
            //int len = -1;
            //while ((len = is.read(buffer)) != -1) {
            // baos.write(buffer, 0, len);
            //}
            //bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
            bitmap = BitmapFactory.decodeStream(is);} catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "读取图片异常" + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.v(TAG, "读取图片异常" + e.getMessage());
                }
            }
        }
        return bitmap;
    }
    private String getMmsText(String _id) { //读取文本附件
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = getContentResolver().openInputStream(partURI);
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "读取附件异常" + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.v(TAG, "读取附件异常" + e.getMessage());
                }
            }
        }
        return sb.toString();
    }
    public void MyTest(){
        allInfo.clear();
        // 查询出ssm和mms后存入allInfo
        System.out.println("执行合并");
        for (int i = 0; i < msgList.size(); i++) {
            allInfo.add(new AllInfo(msgList.get(i).getId(),msgList.get(i).getThread_id(),msgList.get(i).getBody(),msgList.get(i).getDate(),msgList.get(i).getType(),null,msgList.get(i).getfName()));
        }
        System.out.println("检测MList是否为空"+MList.isEmpty()+"-长度"+MList.size());
        if(!MList.isEmpty()) {
            if (msgList.isEmpty()){

            }else {
            int thread_id = msgList.get(0).getThread_id();
            for (int i = 0; i < MList.size(); i++) {
                if (thread_id == MList.get(i).getThread_id()){
                    allInfo.add(new AllInfo(MList.get(i).getId(),thread_id,MList.get(i).getBody(),MList.get(i).getTime(),MList.get(i).getType(),MList.get(i).getBitmap(),msgList.get(0).getfName()));
                }
            }
            }
        }
        /*for (int i = 0; i < allInfo.size(); i++) {
            System.out.println("时间---"+allInfo.get(i).getDate());
        }*/
        Collections.sort(allInfo);
       /* for (int i = 0; i < allInfo.size(); i++) {
            System.out.println("时间"+allInfo.get(i).getDate());
        }*/
        msgList.clear();
        MList.clear();
    }
}
