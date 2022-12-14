package com.jcl.test2.Adapter;

import static com.jcl.test2.Adapter.MainAdapter.setDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.test2.Db.MsgDbUtil;
import com.jcl.test2.MainActivity;
import com.jcl.test2.MainActivity2;
import com.jcl.test2.R;
import com.jcl.test2.pojo.AllInfo;
import com.jcl.test2.pojo.MsgInfo;

import java.text.SimpleDateFormat;
import java.util.List;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.ViewHolder> {
    private List<AllInfo> allInfo;
    Context context;
    public AllAdapter(List<AllInfo> allInfo, Context context) {
        this.allInfo = allInfo;
        this.context = context;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView address;
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        ImageView isSend;
        ImageView default_avatar;
        TextView user_time;
        TextView me_time;
        TextView name_tag;
        TextView time_tag;
        ImageView mms_left;
        ImageView mms_right;
        LinearLayout time_left;
        LinearLayout time_right;
        LinearLayout chat_left;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.send_name);
            leftLayout = itemView.findViewById(R.id.chat_left);
            rightLayout = itemView.findViewById(R.id.chat_right);
            leftMsg = itemView.findViewById(R.id.user_content);
            rightMsg = itemView.findViewById(R.id.me_content);
            isSend = itemView.findViewById(R.id.send_status);
            name_tag = itemView.findViewById(R.id.name_tag);
            default_avatar = itemView.findViewById(R.id.default_avatar);
            user_time = itemView.findViewById(R.id.user_time);
            me_time = itemView.findViewById(R.id.me_time);
            time_tag = itemView.findViewById(R.id.time_tag);
            mms_left = itemView.findViewById(R.id.mms_left);
            mms_right = itemView.findViewById(R.id.mms_right);
            time_left = itemView.findViewById(R.id.time_left);
            time_right = itemView.findViewById(R.id.time_right);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AllInfo msg = allInfo.get(position);
        String s = String.valueOf(msg.getDate()+"000");
        long l = Long.parseLong(s);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(l);
        if(msg.getBitmap() != null){
            if (msg.getType() == 1){
                holder.mms_left.setImageBitmap(msg.getBitmap());
                holder.mms_left.setVisibility(View.VISIBLE);
                holder.leftMsg.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.name_tag.setVisibility(View.GONE);
                holder.default_avatar.setVisibility(View.VISIBLE);
                holder.time_left.setVisibility(View.VISIBLE);
                holder.time_right.setVisibility(View.GONE);
                holder.user_time.setText(time);
            }else if (msg.getType() == 2){
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.mms_right.setImageBitmap(msg.getBitmap());
                holder.mms_right.setVisibility(View.VISIBLE);
                holder.time_left.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightMsg.setVisibility(View.GONE);
                holder.me_time.setText(time);
                holder.time_right.setVisibility(View.VISIBLE);
            }

        }else{
            if (msg.getType() == (MsgInfo.TYPE_RECEIVED) || msg.getType() == 5) {
                if (false) {
                    holder.user_time.setText("");
                    holder.time_tag.setVisibility(View.GONE);
                } else {
                    holder.user_time.setText(time);
                }
                holder.time_left.setVisibility(View.VISIBLE);
                holder.time_right.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.leftMsg.setText(msg.getBody());
                if (msg.getfName() != null) {
                    holder.name_tag.setText(msg.getfName());
                } else {
                    holder.name_tag.setVisibility(View.GONE);
                    holder.default_avatar.setVisibility(View.VISIBLE);
                }
            } else if (msg.getType() == (MsgInfo.TYPE_SENT)) {
                if (false) {
                    holder.me_time.setText("");
                    holder.time_tag.setVisibility(View.GONE);
                } else {
                    holder.me_time.setText(time);
                }
                holder.time_left.setVisibility(View.GONE);
                holder.time_right.setVisibility(View.VISIBLE);
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightMsg.setText(msg.getBody());
            }
            holder.mms_left.setVisibility(View.GONE);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //showPopupMenu(v);
                //  Toast.makeText(context, "??????" + position, Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog2 = new AlertDialog.Builder(context)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {//??????"Yes"??????
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Telephony.Sms.getDefaultSmsPackage(context) == null || Telephony.Sms.getDefaultSmsPackage(context).equals(context.getPackageName())){
                                    allInfo.remove(position);
                                    MsgDbUtil msgDbUtil = new MsgDbUtil(context);
                                    msgDbUtil.deleteSmsById(msg.getId());
                                    // ScheduleSmsHelper.getInstance(context).deleteSms(msg.getId());
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                }else {
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P ) {
                                        RoleManager roleManager = context.getSystemService(RoleManager.class);
                                        Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                                        ((Activity) context).startActivityForResult(roleRequestIntent, 12);
                                    } else {//????????????????????????
                                        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                                        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.getPackageName());
                                        ((Activity) context).startActivityForResult(intent, 12);
                                    }
                                }


                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {//????????????
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();
                alertDialog2.show();
                setDialog(alertDialog2);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return allInfo.size();
    }


    //???????????????
    private void showPopupMenu(final View view) {
        final PopupMenu popupMenu = new PopupMenu(context,view);
        //menu ??????
        popupMenu.getMenuInflater().inflate(R.menu.delete_msg,popupMenu.getMenu());
        //????????????
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_msg:
                        break;
                    case R.id.Cancel:

                }
                return false;
            }
        });
        //????????????
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        //????????????????????????????????????
        popupMenu.show();
    }
}
