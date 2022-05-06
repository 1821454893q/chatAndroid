package com.example.myapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.chat;
import com.example.myapp.databinding.FragmentHomeBinding;
import com.example.myapp.login;
import com.example.myapp.socket.Client;
import com.example.myapp.socket.i.CallClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private Button chat;

    View root;
    private Button bt_refresh;
    int index = 0;
    int size = -1;
    List<Map<String, String>> splist;
    private Mis_Adapter adapter;
    private RecyclerView recyclerView;
    String getsMeg = "";
    String gets = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        //chat = root.findViewById(R.id.makechat);
        recyclerView = root.findViewById(R.id.recyclerview);
        //设置LayoutManager，以LinearLayoutManager为例子进行线性布局
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        //设置分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), LinearLayoutManager.VERTICAL));

        index = 0;

        Get_info();
        bt_refresh = root.findViewById(R.id.ref);
        bt_refresh.setOnClickListener(v -> {
            Get_info();
        });

//        chat.setOnClickListener(v -> {
//            Intent nIntent = new Intent(root.getContext(),chat.class);
//            startActivity(nIntent);
//        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void show_init(List<Map<String, String>> result) {
//        recyclerView = root.findViewById(R.id.recyclerview);
//        //设置LayoutManager，以LinearLayoutManager为例子进行线性布局
//        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
//        //设置分割线
//        recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), LinearLayoutManager.VERTICAL));
        //创建适配器
        adapter = new Mis_Adapter(sendUmeg(result, getsMeg, gets));
        //设置适配器
        recyclerView.setAdapter(adapter);

    }

    //获取发布信息
    private void Get_info() {

        try {
            Client.SendThread send = Client.getClient().Send("status:getRelease", info -> {
            });
            send.join(1000);
            // 1.根据获得第一条消息的size数量 获取到足够的list
            // 1.1 取出当前info中的size
            String m_info = new String();
            for (int i = 0; i < Client.getServiceMessageList().size(); i++) {
                m_info += Client.getServiceMessageList().get(i);
            }

            String s1 = m_info.substring(m_info.indexOf("size:") + 5, m_info.indexOf("\r\n"));
            int size = Integer.parseInt(s1);

            // 1.2 根据size 继续获取足够的list 因为要获取socket的数据 所以要调用线程方法继续写
            String temp = m_info;
            List<Map<String, String>> mapList;
            while (true) {
                temp += Client.getServiceMessageList().get(0);
                mapList = spiltDou(temp, size);
                if (mapList.size() == size) break;
            }

            if(!mapList.isEmpty())
            {
                //Toast.makeText()
            }
            //创建适配器
            adapter = new Mis_Adapter(sendUmeg(mapList, getsMeg, gets));
            //设置适配器
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(root.getContext(), "No data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private List<Map<String, String>> spiltDou(String temp, int size) {
        List<Map<String, String>> result = new ArrayList<>();
        while (true) {
            if (result.size() == size) break;
            if (!temp.contains("<id>")) break;
            String id = temp.substring(temp.indexOf("<id>") + 4, temp.indexOf("</id>"));
            String account = temp.substring(temp.indexOf("<account>") + 9, temp.indexOf("</account>"));
            String title = temp.substring(temp.indexOf("<title>") + 7, temp.indexOf("</title>"));
            String context = temp.substring(temp.indexOf("<context>") + 9, temp.indexOf("</context>"));
            String hot = temp.substring(temp.indexOf("<hot>") + 5, temp.indexOf("</hot>"));
            String releaseDate = temp.substring(temp.indexOf("<releaseDate>") + 13, temp.indexOf("</releaseDate>"));
            temp = temp.substring(temp.indexOf("</releaseDate>") + 10);
            Map<String, String> m = new HashMap<>();
            m.put("id", id);
            m.put("account", account);
            m.put("title", title);
            m.put("context", context);
            m.put("hot", hot);
            m.put("releaseDate", releaseDate);
            result.add(m);
        }
        return result;
    }



    private List<User_Mis> sendUmeg(List<Map<String, String>> Ulist, String s, String ss) {
        List<User_Mis> um = new ArrayList<>();
        //List<Map<String,String>> gilt = spiltDou(getMeg);
        for (int i = 0; i < Ulist.size(); i++) {
            User_Mis us = new User_Mis(Ulist.get(i).get("account"), Ulist.get(i).get("title"), Ulist.get(i).get("context"), Ulist.get(i).get("releaseDate"));
//            User_Mis us = new User_Mis("aa",s,ss,"022");
            um.add(us);
        }
        return um;
    }


}