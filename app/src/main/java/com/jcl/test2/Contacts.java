package com.jcl.test2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jcl.test2.Adapter.ContactsAdapter;
import com.jcl.test2.Fragment.All_Mag_Fragment;
import com.jcl.test2.Fragment.Contacts_Fragment;
import com.jcl.test2.Fragment.Sms_contacts_Fragment;
import com.jcl.test2.Util.GetLinkManUtil;
import com.jcl.test2.pojo.ContactsInfo;

import java.util.ArrayList;
import java.util.List;


public class Contacts extends AppCompatActivity {
    ContactsAdapter contactsAdapter;
   // RecyclerView recyclerView;
    ImageButton imageButton;
    EditText search_content;
    Button button3;
    Button button4;
    ImageView send_more;
    public static Contacts instance = null;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Contacts_Fragment contacts_fragment;
    private Sms_contacts_Fragment sms_contacts_fragment;
    private String phones="";
    private List<String> pList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);
        imageButton = findViewById(R.id.back_btn_contacts);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        send_more = findViewById(R.id.send_more);
        List<ContactsInfo> linkMan = GetLinkManUtil.getLinkMan(getApplicationContext());
       // recyclerView = (RecyclerView) findViewById(R.id.contact_recycler);
        instance = this;
        search_content = findViewById(R.id.search_content);

        fm = getSupportFragmentManager();
        send_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pList.isEmpty()){
                    String[] phones= new String[pList.size()];
                    Intent intent = new Intent();
                    for (int i = 0; i < pList.size(); i++) {
                        phones[i] = pList.get(i);
                    }
                    intent.putExtra("phones",phones);
                    intent.setClass(Contacts.this,MainActivity2.class);
                    startActivity(intent);
                }
            }
        });
        contacts_fragment = new Contacts_Fragment(Contacts.this, new Contacts_Fragment.GetData() {
            @Override
            public void getPhone(String phone) {
                phones = "";
                if (!pList.contains(phone)){
                    pList.add(phone);
                }

                for (int i = 0; i < pList.size(); i++) {
                    if (i == 0){
                        phones += pList.get(i);
                    }else {
                        phones += ","+pList.get(i);
                    }
                }
                search_content.setText(phones);
                search_content.setSelection(phones.length());
            }
        });
        ft = fm.beginTransaction();
        ft.add( R.id.content_contacts, contacts_fragment).commit();

        search_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int t = 0;
                List<ContactsInfo> list = new ArrayList<>();
                if (charSequence.toString().length()>0){
                    send_more.setVisibility(View.VISIBLE);
                for (int i3 = 0; i3 < linkMan.size(); i3++) {
                    String phone = linkMan.get(i3).getPhone();
                    String name = linkMan.get(i3).getName();
                    if ( phone.indexOf(charSequence.toString()) != -1 || name.indexOf(charSequence.toString()) != -1){
                        t = 1;
                        //list.add(new ContactsInfo(name,phone,linkMan.get(i3).getfName()));
                    }
                }
                pList.clear();
                phones = "";
                String[] sp = charSequence.toString().split(",");
                for (int j = 0; j < sp.length; j++) {
                    if (!pList.contains(sp[j])){
                        if (j == 0){
                            phones += sp[j];
                        }else {
                            phones += ","+sp[j];
                        }
                        pList.add(sp[j]);
                    }else {
                        search_content.setText(phones);
                        search_content.setSelection(phones.length());
                    }
                }

                }else {
                    System.out.println("清空输入框");
                    pList.clear();
                    send_more.setVisibility(View.GONE);
              //  start((true),linkMan);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                hideFragment(ft);
                if (contacts_fragment == null) {
                    contacts_fragment = new Contacts_Fragment(Contacts.this, new Contacts_Fragment.GetData() {
                        @Override
                        public void getPhone(String phone) {
                            phones = "";
                            if (!pList.contains(phone)){
                                pList.add(phone);
                            }
                            for (int i = 0; i < pList.size(); i++) {
                                if (i == 0){
                                   phones += pList.get(i);
                                }else {
                                    phones += ","+pList.get(i);
                                }
                            }
                            search_content.setText(phones);
                            search_content.setSelection(phones.length());
                        }
                    });
                    ft.add( R.id.content_contacts, contacts_fragment );
                } else {
                    ft.show(contacts_fragment);
                }
                ft.commit();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                hideFragment(ft);
                if (sms_contacts_fragment == null) {
                    sms_contacts_fragment = new Sms_contacts_Fragment(Contacts.this, new Sms_contacts_Fragment.Interface() {
                        @Override
                        public void getPhone(String phone) {
                            phones = "";
                            if (!pList.contains(phone)){
                                pList.add(phone);
                            }
                            for (int i = 0; i < pList.size(); i++) {
                                if (i == 0){
                                    phones += pList.get(i);
                                }else {
                                    phones += ","+pList.get(i);
                                }
                            }
                            search_content.setText(phones);
                            search_content.setSelection(phones.length());
                        }
                    });
                    ft.add( R.id.content_contacts, sms_contacts_fragment );
                } else {
                    ft.show(sms_contacts_fragment);
                }
                ft.commit();
            }
        });
       /* List<ContactsInfo> contactList =  new ArrayList<>();
        contactList = GetLinkManUtil.getLinkMan(getApplicationContext());*/
      //  start(true,contactList);

    }
    public void hideFragment(FragmentTransaction ft) {
        System.out.println("开始执行关闭方法");
        if (contacts_fragment != null) {
            System.out.println("contacts_fragment关闭");
            ft.hide(contacts_fragment);
        }
        if (sms_contacts_fragment != null) {
            System.out.println("sms_contacts_fragment关闭");
            ft.hide( sms_contacts_fragment );
        }


    }

    /*public void start(Boolean b,List<ContactsInfo> list){
        LinearLayoutManager manager = new LinearLayoutManager(Contacts.this, LinearLayoutManager.VERTICAL, b);
        contactsAdapter = new ContactsAdapter(list,getApplicationContext());
        recyclerView.setLayoutManager(manager);
        if (b){
            System.out.println("执行");
        recyclerView.scrollToPosition(contactsAdapter.getItemCount()-3);
        }
        recyclerView.setAdapter(contactsAdapter);
    }*/
}