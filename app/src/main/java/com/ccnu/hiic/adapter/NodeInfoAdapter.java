package com.ccnu.hiic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ccnu.hiic.Bean.Node;
import com.ccnu.hjjc.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NodeInfoAdapter extends BaseAdapter {
    private ArrayList<Node> nodes;
    private LayoutInflater mInflator;
    private Context context;

    public NodeInfoAdapter(Context context) {
        super();
        nodes=new ArrayList<Node>();
        this.context=context;
        mInflator = LayoutInflater.from(context);
    }

    public void addNodes(ArrayList<Node> newnodes) {
        for (int i=0;i< newnodes.size();i++){
            System.out.println("addNodes函数详细数据" + new Gson().toJson(newnodes.get(i)));
        }
        nodes=newnodes;
    }

    public Node getNodes(int position) {
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
        System.out.println("ListView的函数是否被调用进入.");
        if (view == null) {
            if(mInflator==null){
                System.out.println("mInflator为空");
            }
            view = mInflator.inflate(R.layout.item_node_info, null);
            viewHolder = new ViewHolder();

            viewHolder.it_floor = (TextView) view.findViewById(R.id.it_floor);
            viewHolder.it_room = (TextView) view.findViewById(R.id.it_room);
            viewHolder.it_temp = (TextView) view.findViewById(R.id.it_temp);
            viewHolder.it_humi = (TextView) view.findViewById(R.id.it_humi);
            viewHolder.it_floor.setTextSize(16);
            viewHolder.it_room.setTextSize(16);
            viewHolder.it_room.setTextColor(Color.BLUE);
            viewHolder.it_room.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            viewHolder.it_temp.setTextSize(16);
            viewHolder.it_humi.setTextSize(16);
//            viewHolder.it_smoke = (TextView) view.findViewById(R.id.it_smoke);
//            viewHolder.it_door = (TextView) view.findViewById(R.id.it_door);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Node node = nodes.get(i);
        String floorId = node.getFloor_id();
        String roomId = node.getRoom_id();
        String temp_value=node.getTemp_value();
        String humi_value=node.getHumi_value();


        System.out.println("temp_value"+temp_value+"humi_value");

        if (floorId != null && floorId.length() > 0) {
            viewHolder.it_floor.setText(floorId);
        }
        if (roomId != null && roomId.length() > 0)
            viewHolder.it_room.setText(roomId);
//        if (door != null && door.length() > 0){
//            viewHolder.it_door.setText(door);
//            if (door.equals("异常") || door.equals("报警")){
//                viewHolder.it_door.setBackgroundColor(context.getResources().getColor(R.color.warn_color));
//            }else if (door.equals("离线")){
//                viewHolder.it_door.setBackgroundColor(context.getResources().getColor(R.color.gary_color));
//            }else{
//                viewHolder.it_door.setBackgroundColor(context.getResources().getColor(R.color.listview_bg));
//            }
//        }

        if (temp_value != null && temp_value.length() > 0){
            viewHolder.it_temp.setText(temp_value);
            if (temp_value.equals("异常") || temp_value.equals("报警")){
                viewHolder.it_temp.setBackgroundColor(context.getResources().getColor(R.color.warn_color));
            }else if (temp_value.equals("离线")){
                viewHolder.it_temp.setBackgroundColor(context.getResources().getColor(R.color.gary_color));
            }else{
                viewHolder.it_temp.setBackgroundColor(context.getResources().getColor(R.color.listview_bg));
            }
        }

        if (humi_value != null && humi_value.length() > 0){
            viewHolder.it_humi.setText(humi_value);
            if (humi_value.equals("异常") || humi_value.equals("报警")){
                viewHolder.it_humi.setBackgroundColor(context.getResources().getColor(R.color.warn_color));
            }else if (humi_value.equals("离线")){
                viewHolder.it_humi.setBackgroundColor(context.getResources().getColor(R.color.gary_color));
            }else{
                viewHolder.it_humi.setBackgroundColor(context.getResources().getColor(R.color.listview_bg));
            }
        }

//        if (smoke != null && smoke.length() > 0){
//            viewHolder.it_smoke.setText(smoke);
//            if (smoke.equals("异常") || smoke.equals("报警")){
//                viewHolder.it_smoke.setBackgroundColor(context.getResources().getColor(R.color.warn_color));
//            }else if (smoke.equals("离线")){
//                viewHolder.it_smoke.setBackgroundColor(context.getResources().getColor(R.color.gary_color));
//            }else{
//                viewHolder.it_smoke.setBackgroundColor(context.getResources().getColor(R.color.listview_bg));
//            }
//        }

        return view;
    }

    /**
     * 设置Listview的高度
     */
    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    class ViewHolder {
        TextView it_floor;
        TextView it_room;
        TextView it_temp;
        TextView it_humi;
//        TextView it_smoke;
//        TextView it_door;
    }
}




