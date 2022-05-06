package com.example.myapp.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.Release;
import com.example.myapp.databinding.FragmentNotificationsBinding;
import com.example.myapp.login;
import com.example.myapp.register;

public class NotificationsFragment extends Fragment {
    Button bt_fill,bt_ba;
    TextView username;

    Intent i;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        username = root.findViewById(R.id.tx_user);
        bt_fill = root.findViewById(R.id.bt_fill);
        bt_ba = root.findViewById(R.id.bt_ba);
        //username.setText(R.string.user_name);
        //返回登录
        bt_ba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
                getActivity().finish(); //结束当前activity
            }
        });
        //发布
        bt_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Release.class);
                startActivity(intent);
            }
        });
        //final TextView textView = binding.textNotifications;
        //notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}