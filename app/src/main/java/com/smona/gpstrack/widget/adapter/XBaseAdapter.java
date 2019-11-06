package com.smona.gpstrack.widget.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 6/26/19 8:54 AM
 */
public abstract class XBaseAdapter<D, H extends XViewHolder> extends RecyclerView.Adapter<H> {

    protected List<D> mDataList;
    private int mResId;

    public XBaseAdapter(int resId) {
        mResId = resId;
        mDataList = new ArrayList<>();
    }

    public void setNewData(List<D> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addData(List<D> dataList) {
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void removeData(int pos) {
        if (mDataList.size() <= pos) {
            return;
        }
        mDataList.remove(pos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = View.inflate(parent.getContext(), mResId, null);
        return createBaseViewHolder(root);
    }

    private H createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        H k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (H) new XViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (H) new XViewHolder(view);
    }

    private H createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (H) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (H) constructor.newInstance(new Object[]{view});
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (XViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && XViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(H holder, int position) {
        convert(holder, mDataList.get(position), position);
    }

    protected abstract void convert(H holder, D item, int pos);

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    public D getItem(int pos) {
        if(mDataList == null || mDataList.isEmpty() || mDataList.size() <= pos) {
            return null;
        }
        return mDataList.get(pos);
    }
}
