package com.jcl.test2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.test2.pojo.ContactsInfo;
import com.jcl.test2.MainActivity2;
import com.jcl.test2.R;
import com.jcl.test2.Util.RandomColor;

import java.util.List;
import java.util.Random;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
    private List<ContactsInfo> cContactsList;
    private Context context;
    private addClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView contactsListPhone;
        TextView contactsName;
        TextView contactsFirstName;
        LinearLayout linearLayout;
        EditText search_content;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactsListPhone = itemView.findViewById(R.id.person_phone);
            contactsName = itemView.findViewById(R.id.person_name);
            contactsFirstName = itemView.findViewById(R.id.name_tag);
            linearLayout = itemView.findViewById(R.id.layout_search_fragment);
            search_content = itemView.findViewById(R.id.search_content);
            imageView = itemView.findViewById(R.id.imageView_fragment);

        }
    }

    public ContactsAdapter(List<ContactsInfo> contactsList,Context context1,addClickListener listener) {
        cContactsList = contactsList;
        context = context1;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactsInfo contactsInfo = cContactsList.get(position);
        String name = contactsInfo.getName();
        String phone = contactsInfo.getPhone();
        String FName = contactsInfo.getfName();

        if (name == null){
            holder.contactsName.setText(phone);
        }else {
            holder.contactsName.setText(name);
        }
        holder.contactsListPhone.setText(phone);
        holder.contactsFirstName.setText(FName);
        Random random = new Random();
        int r = random.nextInt(RandomColor.getI().length);
        holder.contactsFirstName.setBackgroundColor(RandomColor.i[r]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = contactsInfo.getPhone();
                if (listener != null){
                    listener.addClick(s);
                }

                // left.setVisibility(View.GONE);

              /* Intent intent = new Intent();
               intent.setClass(context,MainActivity2.class);
               intent.putExtra("phone",s);
               context.startActivity(intent);*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return cContactsList.size();
    }

    public static interface addClickListener{

        public void addClick(String phone);

    }
}
