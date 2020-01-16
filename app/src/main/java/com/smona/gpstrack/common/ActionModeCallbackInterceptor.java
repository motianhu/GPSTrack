package com.smona.gpstrack.common;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 禁用EditText的长按和复制粘贴。作用于密码框
 */
public class ActionModeCallbackInterceptor  implements ActionMode.Callback {
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
}
