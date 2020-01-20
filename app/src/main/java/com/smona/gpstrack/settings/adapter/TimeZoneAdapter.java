package com.smona.gpstrack.settings.adapter;

import android.text.TextUtils;
import android.widget.Filter;

import com.smona.gpstrack.settings.bean.TimeZoneItem;
import com.smona.gpstrack.settings.holder.TimeZoneHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimeZoneAdapter extends XBaseAdapter<TimeZoneItem, TimeZoneHolder> {

    private OnClickItemListener listener;
    private SearchFilter filter;

    public TimeZoneAdapter(int resId) {
        super(resId);
    }

    public void setListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(TimeZoneHolder holder, TimeZoneItem item, int pos) {
        holder.bindViews(item);
        holder.itemView.setOnClickListener(v -> clickItem(item, pos));
    }

    private void clickItem(TimeZoneItem item, int pos) {
        if (listener != null) {
            listener.onClickItem(item, pos);
        }
    }

    public interface OnClickItemListener {
        void onClickItem(TimeZoneItem item, int pos);
    }

    class SearchFilter extends Filter {

        private List<TimeZoneItem> original;

        public SearchFilter(List<TimeZoneItem> list) {
            this.original = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (TextUtils.isEmpty(constraint)) {
                results.values = original;
                results.count = original.size();
            } else {
                List<TimeZoneItem> mList = new ArrayList<>();
                for (TimeZoneItem timeZoneItem : original) {
                    if (timeZoneItem.getTimeZone().toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                        mList.add(timeZoneItem);
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }
            // 返回FilterResults对象
            return results;
        }

        /**
         * 该方法用来刷新用户界面，根据过滤后的数据重新展示列表
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // 获取过滤后的数据
            mDataList = (List<TimeZoneItem>) results.values;
            // 刷新数据源显示
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        if(filter == null) {
            filter = new SearchFilter(mDataList);
        }
        return filter;
    }
}
