package com.sumpfhupe.apps.minitodo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerViewEmptySupport extends RecyclerView {
    /* View to display when the model list is empty */
    private View emptyView;

    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkShowEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkShowEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkShowEmptyView();
        }
    };



    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    /* Display empty view when model list is empty */
    protected void checkShowEmptyView(){
        Adapter<?> adapter = getAdapter();
        if(adapter!=null && emptyView!=null){
            if(adapter.getItemCount()==0){
                // no items in model, show empty view
                emptyView.setVisibility(VISIBLE);
                setVisibility(GONE);
            }
            else{
                // hide empty view and show the original list view
                emptyView.setVisibility(GONE);
                setVisibility(VISIBLE);
            }
        }

    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if(adapter!=null){
            adapter.registerAdapterDataObserver(observer);
            observer.onChanged();
        }
    }

    public void setEmptyView(View v){
        emptyView = v;
    }
}
