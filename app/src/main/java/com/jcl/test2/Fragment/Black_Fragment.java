package com.jcl.test2.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.test2.Adapter.BlackAdapter;
import com.jcl.test2.Adapter.MainAdapter;
import com.jcl.test2.Db.BlackNumberDao;
import com.jcl.test2.R;
import com.jcl.test2.pojo.BlackInfo;
import com.jcl.test2.pojo.MainInfo;

import java.util.ArrayList;
import java.util.List;

public class Black_Fragment extends Fragment {

    public static final Uri uri = Uri.parse("content://blacknum/path_simon");
    private View view;
    LinearLayout include_empty;
    private Context context;
    private BlackAdapter blackAdapter;
    private RecyclerView recyclerView;
    private List<BlackInfo> blackInfoList = new ArrayList<>();

    public Black_Fragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.black_msg, container, false);
        recyclerView = view.findViewById(R.id.black_recycler);
        include_empty = view.findViewById(R.id.include_empty_black);
        include_empty.setVisibility(View.GONE);
        BlackNumberDao blackNumberDao = new BlackNumberDao(context);
        List<String> all = blackNumberDao.findAll();
        if (all.isEmpty()) {
            include_empty.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < all.size(); i++) {
                blackInfoList.add(new BlackInfo(all.get(i)));
            }
            start(false,blackInfoList);
        }
        return view;
    }

    public void start(Boolean b,List<BlackInfo> list){
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        blackAdapter = new BlackAdapter(list,context);
        if(b){
            recyclerView.scrollToPosition(blackAdapter.getItemCount()-1);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(blackAdapter);
    }
}
