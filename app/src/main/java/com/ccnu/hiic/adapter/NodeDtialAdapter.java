package com.ccnu.hiic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccnu.hiic.Bean.NodeDetial;
import com.ccnu.hjjc.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NodeDtialAdapter extends BaseAdapter {
    private ArrayList<NodeDetial> nodes;
    private LayoutInflater mInflator;
    private Context context;

    public NodeDtialAdapter(Context context) {
        super();
        nodes=new ArrayList<NodeDetial>();
        this.context=context;
        mInflator = LayoutInflater.from(context);
    }

    public void addNodes(ArrayList<NodeDetial> newnodes) {
        for (int i=0;i< newnodes.size();i++){
            System.out.println("addNodes函数详细数据" + new Gson().toJson(newnodes.get(i)));
        }
        nodes=newnodes;
    }

    public NodeDetial getNodes(int position) {
        return nodes.get(position);
    }

    public void clear() {
        nodes.clear();
    }

    @Override
    public int getCount() {
        return nodes.size();
    }

    @Override
    public Object getItem(int i) {
        return nodes.get(i);
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
            view = mInflator.inflate(R.layout.detial_item_node, null);
            viewHolder = new ViewHolder();

            viewHolder.it_dev= (TextView) view.findViewById(R.id.it_dev);
            viewHolder.it_type = (TextView) view.findViewById(R.id.it_type);
            viewHolder.it_value = (TextView) view.findViewById(R.id.it_value);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        NodeDetial node = nodes.get(i);
        String dev_eui = node.getDev_eui();


        if (dev_eui != null && dev_eui.length() > 0) {
            viewHolder.it_dev.setText(dev_eui);
        }

        StringBuilder showStr=new StringBuilder();

        viewHolder.it_value.setText(showStr.toString());

        return view;
    }

    /**
     * 设置Listview的高度
     */
    class ViewHolder {
        TextView it_dev;
        TextView it_value;
        TextView it_type;
    }
}




