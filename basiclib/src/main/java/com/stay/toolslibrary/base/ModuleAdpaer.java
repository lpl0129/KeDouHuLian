package com.stay.toolslibrary.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 公用ViewHolder 适配器 2.0
 * 用于listView or gridView
 *
 * @author huangyuru
 *         <p/>
 *         2016-9-12
 */
public abstract class ModuleAdpaer<T> extends BaseAdapter {

    protected Context context;

    protected List<T> result;

    protected LayoutInflater inflater;

    public ModuleAdpaer(Context context, List<T> result) {
        this.context = context;
        this.result = result;
        this.inflater = LayoutInflater.from(context);

    }

    public ModuleAdpaer(Context context, List<T> result, int defalutimage) {
        this.context = context;
        this.result = result;
        this.inflater = LayoutInflater.from(context);
    }

    public ModuleAdpaer(Context context, ArrayList<T> result) {
        this.context = context;
        this.result = result;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return result == null ? 0 : result.size();
    }

    @Override
    public Object getItem(int position) {
        return result == null ? 0 : result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    public List<T> getItems() {
        return result;
    }

    public void setItem(List<T> items) {
        this.result = items;
        if (this.result == null)
            this.result = new ArrayList<T>();
    }

    public void addItem(List<T> items) {
        if (this.result == null)
            this.result = new ArrayList<T>();
        this.result.addAll(items);
    }

    public void addItem(T items) {
        if (this.result == null)
            this.result = new ArrayList<T>();
        this.result.add(items);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ModuleViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(getLayoutIdType(getItemViewType(i)), null);
            viewHolder = new ModuleViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ModuleViewHolder) view.getTag();
        }
        T bean = null;
        try {
            bean = result.get(i);
        } catch (Exception e) {
        }

        bindData(viewHolder, bean, i);
        return view;
    }

    public abstract int getLayoutIdType(int type);

    public abstract void bindData(ModuleViewHolder holder, T bean, int position);

    public void setList(List<T> list) {
        this.result = list;
        notifyDataSetChanged();
    }
}
