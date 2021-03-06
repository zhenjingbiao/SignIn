package com.example.jingbiaozhen.sign_in;
/*
 * Created by jingbiaozhen on 2018/5/24.
 **/

import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.jingbiaozhen.sign_in.bean.SignInBean;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>
{

    private Context mContext;

    private List<SignInBean.Seat> mLocationList;

    public LocationAdapter(Context context, List<SignInBean.Seat> locationList)
    {
        mContext = context;
        mLocationList = locationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = View.inflate(mContext, R.layout.item_sign, null);
        return new ViewHolder(view);
    }

    private Integer selectedPosition;

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        SignInBean.Seat location = mLocationList.get(position);
        final boolean isCheck = location.isSigned;
        holder.mCheckBox.setChecked(isCheck);
        if (isCheck)
        {
            holder.mCheckBox.setEnabled(false);
        }
        else
        {
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    if (selectedPosition == null||selectedPosition==holder.getAdapterPosition())
                    {
                        if (b)
                        {
                            Toast.makeText(mContext, "您选择的座位号是" + holder.getAdapterPosition(),
                                    Toast.LENGTH_SHORT).show();
                            selectedPosition = holder.getAdapterPosition();
                        }
                        else
                        {
                            Toast.makeText(mContext, "您已取消选择的座位号" + holder.getAdapterPosition(),
                                    Toast.LENGTH_SHORT).show();
                            selectedPosition = null;
                        }
                        mCheckListener.onLocationCheck(selectedPosition, b);

                    }
                    else
                    {
                        holder.mCheckBox.setChecked(false);
                       // mCheckListener.onLocationCheck(null, false);
                        Toast.makeText(mContext, "只能选择一个座位", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount()
    {
        return mLocationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        CheckBox mCheckBox;

        public ViewHolder(View itemView)
        {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.sign_select_cb);
        }
    }

    public interface OnLocationCheckListener
    {
        void onLocationCheck(Integer position, boolean isCheck);
    }

    private OnLocationCheckListener mCheckListener;

    public void setOnLocationCheckListener(OnLocationCheckListener listener)
    {
        mCheckListener = listener;
    }
}
