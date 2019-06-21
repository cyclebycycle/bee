package com.ccnu.hiic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccnu.hiic.MyApplication;
import com.ccnu.hiic.activity.AdminGetClientActivity;
import com.ccnu.hjjc.R;
import com.ccnu.hiic.activity.MessConfigActivity;
import com.ccnu.hiic.activity.NodeRegistActivity;

public class AdminFragment extends Fragment{
    View view;
    private TextView node_regist;
    private TextView mess_config;
    private TextView user_message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_admin,container,false);
        }
        node_regist = (TextView)view.findViewById(R.id.node_regist);
        mess_config = (TextView)view.findViewById(R.id.mess_config);
        user_message = (TextView)view.findViewById(R.id.user_manage);
        user_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyApplication.getContext(),AdminGetClientActivity.class);
                getActivity().startActivityForResult(intent, 0);
            }
        });
        node_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyApplication.getContext(),NodeRegistActivity.class);
                getActivity().startActivityForResult(intent, 0);
            }
        });
        mess_config.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(MyApplication.getContext(),MessConfigActivity.class);
                getActivity().startActivityForResult(intent,0);
            }
        });
//        node_regist.setOnClickListener(this);
//        mess_config.setOnClickListener(this);
//        user_message.setOnClickListener(this);
        return view;
    }

    public void onClick(final View view){


    }


}
