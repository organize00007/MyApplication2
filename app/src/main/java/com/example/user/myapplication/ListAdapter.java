package com.example.user.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapter extends BaseAdapter
{
    private LayoutInflater mLayInf;
    private Activity activity;
    ArrayList<HashMap<String, Object>> mItemList;

    private static LayoutInflater inflater=null;

    public ListAdapter(Activity a, ArrayList<HashMap<String, Object>> itemList)
    {
        activity = a;
        mItemList = itemList;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public String getData(int position)
    {
        return mItemList.get(position).get("a").toString();
    }

    @Override
    public int getCount()
    {
        //取得 ListView 列表 Item 的數量
        return mItemList.size();
    }

    @Override
    public Object getItem(int position)
    {
        //取得 ListView 列表於 position 位置上的 Item
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        //取得 ListView 列表於 position 位置上的 Item 的 ID
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        final Holder holder;
        if(v==null)
        {
            v = inflater.inflate(R.layout.activity_adapter, parent,false);
            holder = new Holder();
            holder.textView = (TextView) v.findViewById(R.id.textView);
            holder.textView2 = (TextView) v.findViewById(R.id.textView2);
            holder.textView3 = (TextView) v.findViewById(R.id.textView3);

            v.setTag(holder);

        } else
        {
            holder = (Holder) v.getTag();
        }

        HashMap<String, Object> item = mItemList.get(position);

        holder.textView.setText(item.get("a").toString());
        holder.textView2.setText(item.get("b").toString());
        holder.textView3.setText(item.get("c").toString());

        return v;
    }

    class Holder{
        TextView textView;
        TextView textView2;
        TextView textView3;
    }
}
