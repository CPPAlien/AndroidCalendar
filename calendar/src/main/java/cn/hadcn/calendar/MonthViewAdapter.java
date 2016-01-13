package cn.hadcn.calendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * MonthViewAdapter
 * Created by 90Chris on 2015/11/5.
 */
public class MonthViewAdapter extends BaseAdapter {
    private Context mContext = null;
    List<DayItem> mDayItems;
    int mItemWidth;
    int mItemHeight;

    public MonthViewAdapter( Context context, List<DayItem> dayItems, int width, int height) {
        mDayItems = dayItems;
        mContext = context;
        mItemWidth = width;
        mItemHeight = height;
    }

    public void setItemSize(int width, int height) {
        mItemWidth = width;
        mItemHeight = height;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDayItems.size();
    }

    @Override
    public DayItem getItem(int position) {
        return mDayItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        RelativeLayout layout;
        TextView tvDay;
        ViewHolder(View view) {
            tvDay = (TextView)view.findViewById(R.id.item_day_num);
            layout = (RelativeLayout)view.findViewById(R.id.item_layout);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if ( null == convertView ) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.had_item_day, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.layout.setLayoutParams(new AbsListView.LayoutParams(mItemWidth, mItemHeight));
        int color = R.color.had_black;
        switch ( getItem(position).getStatus()) {
            case OTHER:
                color = R.color.had_grey;
                break;
            case CURRENT_DAY:
                color = R.color.had_blue;
                break;
            case CURRENT_MONTH:
                color = R.color.had_black;
                break;
        }
        viewHolder.tvDay.setTextColor(ContextCompat.getColor(mContext, color));
        viewHolder.tvDay.setText(String.valueOf(getItem(position).getDay()));
        return convertView;
    }
}
