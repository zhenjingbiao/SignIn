package com.example.jingbiaozhen.sign_in;
/*
 * Created by jingbiaozhen on 2018/5/25.
 **/

import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jingbiaozhen.sign_in.bean.ItemDesc;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
{

    private Context mContext;

    private List<ItemDesc> mItemDescs;

    public ListAdapter(Context context, List<ItemDesc> itemDescs)
    {
        mContext = context;
        mItemDescs = itemDescs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_desc, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if (!mItemDescs.isEmpty())
        {
            ItemDesc itemDesc = mItemDescs.get(position);
            if (itemDesc != null)
            {
                holder.mDescTv.setText(itemDesc.desc);
                holder.mTitleTv.setText(itemDesc.title);
            }

        }

    }

    @Override
    public int getItemCount()
    {
        return mItemDescs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView mTitleTv;

        TextView mDescTv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            mTitleTv=itemView.findViewById(R.id.title_tv);
            mDescTv=itemView.findViewById(R.id.desc_tv);
        }
    }
}
