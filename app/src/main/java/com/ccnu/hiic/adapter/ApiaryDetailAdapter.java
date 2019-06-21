package com.ccnu.hiic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccnu.hiic.Bean.ApiaryDetail;
import com.ccnu.hiic.Bean.GetApiaryDetail;
import com.ccnu.hiic.Bean.Node;
import com.ccnu.hjjc.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ApiaryDetailAdapter extends BaseAdapter {
    private ArrayList<ApiaryDetail> apiaryDetails;
    private LayoutInflater mInflator;
    private Context context;

    public ApiaryDetailAdapter(Context context) {
        super();
        apiaryDetails =new ArrayList<ApiaryDetail>();
        this.context=context;
        mInflator = LayoutInflater.from(context);
    }

    public void add(ArrayList<ApiaryDetail> new_apiaryDetails) {
        for (int i = 0; i< new_apiaryDetails.size(); i++){
            System.out.println("addNodes函数详细数据" + new Gson().toJson(new_apiaryDetails.get(i)));
        }
        apiaryDetails = new_apiaryDetails;
    }

    public ApiaryDetail getApiary(int position) {
        return apiaryDetails.get(position);
    }

    public void clear() {
        apiaryDetails.clear();
    }

    @Override
    public int getCount() {
        return apiaryDetails.size();
    }

    @Override
    public Object getItem(int i) {
        return apiaryDetails.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        System.out.println("详细节点的信息adapter被调用进入.");
        if (view == null) {
            if(mInflator==null){
                System.out.println("mInflator为空");
            }
            view = mInflator.inflate(R.layout.item_apiary_detail, null);
            viewHolder = new ViewHolder();
            viewHolder.it_name= (TextView) view.findViewById(R.id.it_name);
            viewHolder.it_apiary_name = (TextView) view.findViewById(R.id.it_apiary_name);
            viewHolder.it_offline_num = (TextView) view.findViewById(R.id.it_offline_num);
            viewHolder.it_abnormal_num = (TextView) view.findViewById(R.id.it_abnormal_num);
            viewHolder.it_name.setTextSize(16);
            viewHolder.it_apiary_name.setTextSize(16);
            viewHolder.it_apiary_name.setTextColor(Color.BLUE);
            viewHolder.it_apiary_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            viewHolder.it_offline_num.setTextSize(16);
            viewHolder.it_abnormal_num.setTextSize(16);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ApiaryDetail apiaryDetail = apiaryDetails.get(i);
        String name = apiaryDetail.getUsername();
        String apiary_name = apiaryDetail.getFloor_name();
        int dismiss= apiaryDetail.getDismiss();
        int abnormal= apiaryDetail.getAbnormal();

        if (name != null && name.length() > 0) {
            viewHolder.it_name.setText(name);
        }
        if (apiary_name != null && apiary_name.length() > 0){
            viewHolder.it_apiary_name.setText(apiary_name);
        }
        viewHolder.it_offline_num.setText(dismiss+"");
        viewHolder.it_abnormal_num.setText(abnormal+"");


        return view;
    }

    /**
     * 设置Listview的高度
     */
    class ViewHolder {
        TextView it_name;
        TextView it_apiary_name;
        TextView it_offline_num;
        TextView it_abnormal_num;
    }
}




