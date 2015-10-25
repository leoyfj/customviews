package com.example.ooo.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by ooo on 2015/10/25.
 */
public class RefreshableListView extends ListView {
    public static final String TAG = "refreshablelistviewloadingtag";
    private LinearLayout mLoadingContainer;
    private ProgressBar mProgressBar;
    private TextView mTvDesc;
    private String mDescStr = "努力加载中...";
    private OnClickListener mOnRetryListener;

    public RefreshableListView(Context context) {
        super(context);
    }

    public RefreshableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnRetryListener(OnClickListener onRetryListener) {
        mOnRetryListener = onRetryListener;
    }

    private void initLoadingView() {
        mLoadingContainer = new LinearLayout(getContext());
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        llParams.setMargins(10, 5, 10, 5);
        mLoadingContainer.setOrientation(LinearLayout.HORIZONTAL);
        mLoadingContainer.setGravity(Gravity.CENTER);
        mLoadingContainer.setLayoutParams(llParams);
        // create ProgressBar
        mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);
        LinearLayout.LayoutParams pblParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pblParams.setMargins(0, 0, 0, 0);
        mProgressBar.setLayoutParams(pblParams);
        // create TextView
        mTvDesc = new TextView(getContext());
        LinearLayout.LayoutParams tvlParams = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        tvlParams.setMargins(10, 0, 0, 0);
        mTvDesc.setLayoutParams(tvlParams);
        mTvDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        mTvDesc.setText(mDescStr);
        // add view
        mLoadingContainer.addView(mProgressBar);
        mLoadingContainer.addView(mTvDesc);
        mLoadingContainer.setTag(TAG);
    }

    public void showLoading() {
        if (mLoadingContainer == null) {
            initLoadingView();
        }

        ViewGroup parent = (ViewGroup) getParent();
        if (parent.findViewWithTag(TAG) == null) {
            parent.addView(mLoadingContainer);
        }
        setVisibility(View.GONE);
        mTvDesc.setText(mDescStr);
        mProgressBar.setVisibility(VISIBLE);
        mLoadingContainer.setVisibility(VISIBLE);
        mLoadingContainer.setEnabled(false);//for retry
    }

    public void dismissLoading() {
        if (mLoadingContainer != null) {
            ViewGroup parent = (ViewGroup) getParent();
            if (parent.findViewWithTag(TAG) != null) {
                parent.removeView(mLoadingContainer);
            }
        }
        setVisibility(VISIBLE);
    }

    public void showError(String errorMsg) {
        if (mLoadingContainer == null) {
            return;
        }
        ViewGroup parent = (ViewGroup) getParent();
        if (parent.findViewWithTag(TAG) == null) {
            return;
        }
        mLoadingContainer.setEnabled(true);
        mTvDesc.setText(errorMsg+",点击重试");
        mProgressBar.setVisibility(GONE);
        setVisibility(GONE);
        mLoadingContainer.setOnClickListener(mOnRetryListener);
    }
}
