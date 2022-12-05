package com.jcl.test2.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jcl.test2.Adapter.MainAdapter;
import com.jcl.test2.Contacts;
import com.jcl.test2.R;
import com.jcl.test2.Util.GetLinkManUtil;
import com.jcl.test2.pojo.ContactsInfo;
import com.jcl.test2.pojo.MainInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Unread_Fragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    LinearLayout include_empty;
    private EditText search_content;
    private Context context;
    private List<MainInfo> mainList = new ArrayList<>();
    private MainAdapter mainAdapter;
    private FloatingActionButton contacts;

    public Unread_Fragment(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.unread_msg,container,false);
        recyclerView = view.findViewById(R.id.sms_lists);
        include_empty = view.findViewById(R.id.include_empty_unread);
        include_empty.setVisibility(View.VISIBLE);
        search_content = view.findViewById(R.id.search_contents);
        initMsg();
        start(true,mainList);
        contacts = view.findViewById(R.id.new_sms);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contacts();
            }
        });
        search_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int a = mainList.size();
                List<MainInfo> list = new ArrayList<>();
                if (charSequence.toString().length() > 0) {
                    for (int i3 = 0; i3 < a; i3++) {
                        int id = mainList.get(i3).getId();
                        String phone = mainList.get(i3).getAddress();
                        String body = mainList.get(i3).getBody();
                        String time = mainList.get(i3).getDate();
                        String fName = mainList.get(i3).getfName();
                        String name = mainList.get(i3).getName();
                        int thread_id = mainList.get(i3).getThread_id();
                        if (phone.indexOf(charSequence.toString()) != -1 || body.indexOf(charSequence.toString()) != -1) {
                            list.add(new MainInfo(phone, body, time, fName, name,id,thread_id));
                        }
                    }start(false,list);
                }
                else {
                    start(true,mainList);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    public void contacts(){
        Intent intent = new Intent();
        // left.setVisibility(View.GONE);
        intent.setClass(context, Contacts.class);
        startActivity(intent);
    }
    public void start(Boolean b,List<MainInfo> list){
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mainAdapter = new MainAdapter(list,context);
        if(b){
            recyclerView.scrollToPosition(mainAdapter.getItemCount()-1);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mainAdapter);
    }

    private void initMsg(){
        System.out.println("执行initMsg");
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "body", "date", "type","read","thread_id"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<ContactsInfo> linkMan = GetLinkManUtil.getLinkMan(context);
            System.out.println("判断不为空执行");
            int _id;
            int thread_id;
            String address;
            String body;
            String date;
            String fName = null;
            String name = null;
            int isRead;
            List<String> l = new ArrayList();
            System.out.println("准备执行循环短信内容");
            while (cursor.moveToNext()) {
                _id = cursor.getInt(0);
                address = cursor.getString(1);
                body = cursor.getString(2);
                date = cursor.getString(3);
                isRead = cursor.getInt(5);
                thread_id = cursor.getInt(6);
                double t = Double.parseDouble(date);
                String result2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(t);
                if (isRead == 0) {
                    for (int i3 = 0; i3 < linkMan.size(); i3++) {
                        String phone = linkMan.get(i3).getPhone();
                        name = linkMan.get(i3).getName();
                        fName = linkMan.get(i3).getfName();
                        if (phone.equals(address) || phone.equals("+86" + address) || ("+86" + phone).equals(address)) {
                            break;
                        } else {
                            fName = null;
                        }
                    }
                    if (l.contains(address)) {
                        continue;
                    } else {
                        l.add(address);
                        l.add("+86" + address);
                    }
                    mainList.add(new MainInfo(address, body, result2, fName, name,_id,thread_id));
                }
            }
        }
        if (!mainList.isEmpty()){
            include_empty.setVisibility(View.GONE);
        }
    }
}
