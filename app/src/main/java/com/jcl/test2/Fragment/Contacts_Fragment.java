package com.jcl.test2.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.jcl.test2.Contacts;
import com.jcl.test2.Db.BlackNumberDao;
import com.jcl.test2.R;
import com.jcl.test2.Util.GetLinkManUtil;
import com.jcl.test2.pojo.ContactsInfo;

import java.util.ArrayList;
import java.util.List;

public class Contacts_Fragment extends Fragment {


    private View view;
    LinearLayout include_empty;
    private Context context;
    private RecyclerView recyclerView;
    ContactsAdapter contactsAdapter;
    private GetData getData;

    public Contacts_Fragment(Context context,GetData getData){
        this.context = context;
        this.getData = getData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contacts_fragment,container,false);
        include_empty = view.findViewById(R.id.include_empty_contacts);
        include_empty.setVisibility(View.GONE);
        List<ContactsInfo> linkMan = GetLinkManUtil.getLinkMan(context);
        recyclerView = view.findViewById(R.id.contact_recycler);


        List<ContactsInfo> contactList =  new ArrayList<>();
        contactList = GetLinkManUtil.getLinkMan(context);
        start(false,contactList);

        return view;
    }
    public void start(Boolean b,List<ContactsInfo> list){
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, b);
        contactsAdapter = new ContactsAdapter(list, context, new ContactsAdapter.addClickListener() {
            @Override
            public void addClick(String phone) {
                if (getData != null){
                    getData.getPhone(phone);
                }
            }
        });
        recyclerView.setLayoutManager(manager);
        if (b){
            recyclerView.scrollToPosition(contactsAdapter.getItemCount()-3);
        }
        recyclerView.setAdapter(contactsAdapter);
    }
    public interface GetData{
        void getPhone(String phone);//在这里可以自定义想要实现的方法，一般是传入adapter里的变量供activity使用。
    }
}