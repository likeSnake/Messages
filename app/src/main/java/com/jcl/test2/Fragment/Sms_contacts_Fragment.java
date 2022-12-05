package com.jcl.test2.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.test2.Adapter.ContactsAdapter;
import com.jcl.test2.R;
import com.jcl.test2.Util.GetLinkManUtil;
import com.jcl.test2.pojo.ContactsInfo;
import com.jcl.test2.pojo.MainInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Sms_contacts_Fragment extends Fragment {


    private View view;
    LinearLayout include_empty;
    private Context context;
    private RecyclerView recyclerView;
    ContactsAdapter contactsAdapter;
    List<ContactsInfo> contactList = new ArrayList<>();
    private Interface mListener;


    /*public Sms_contacts_Fragment(Context context){
        this.context = context;
    }*/
    public Sms_contacts_Fragment(Context context,Interface mListener){
        this.context = context;
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sms_contacts_fragment,container,false);
        include_empty = view.findViewById(R.id.sms_include_empty_contacts);
        include_empty.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.sms_contact_recycler);
        initMsg();
        start(false,contactList);

        return view;
    }
    public void start(Boolean b,List<ContactsInfo> list){
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, b);
        contactsAdapter = new ContactsAdapter(list, context, new ContactsAdapter.addClickListener() {
            @Override
            public void addClick(String phone) {
                if (mListener != null){
                    mListener.getPhone(phone);
                }
            }
        });
        recyclerView.setLayoutManager(manager);
        if (b){
            recyclerView.scrollToPosition(contactsAdapter.getItemCount()-3);
        }
        recyclerView.setAdapter(contactsAdapter);
    }
    private void initMsg(){
        System.out.println("执行initMsg");
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "body", "date", "type","read"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<ContactsInfo> linkMan = GetLinkManUtil.getLinkMan(context);
            System.out.println("判断不为空执行");
            int _id;
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
                double t = Double.parseDouble(date);
                String result2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(t);
                for (int i3 = 0; i3 < linkMan.size(); i3++) {
                    String phone = linkMan.get(i3).getPhone();
                    name = linkMan.get(i3).getName();
                    fName = linkMan.get(i3).getfName();
                    if (phone.equals(address) || phone.equals("+86"+address) || ("+86"+phone).equals(address)){
                        break;
                    }else {
                        fName = null;
                        name = null;
                    }
                }
                if(l.contains(address)){
                    continue;
                }else {
                    l.add(address);
                    l.add("+86"+address);
                }
                contactList.add(new ContactsInfo(name,address,fName));
            }
        }
    }
    public interface Interface{
        void getPhone(String phone);//在这里可以自定义想要实现的方法，一般是传入adapter里的变量供activity使用。
    }
}