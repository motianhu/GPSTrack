package com.smona.gpstrack.main.adapter;

import com.smona.gpstrack.db.table.Alarm;
import com.smona.gpstrack.main.holder.AlarmHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 2:06 PM
 */
public class AlarmAdapter extends XBaseAdapter<Alarm, AlarmHolder> {


    private OnRemoveMessageListener listener;

    public AlarmAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void setClickListener(OnRemoveMessageListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(AlarmHolder holder, Alarm item, int pos) {
        holder.bindViews(item, pos, listener);
    }


    public interface OnRemoveMessageListener {
        void onRemoveMessage(Alarm alarm, int position);
    }
}
