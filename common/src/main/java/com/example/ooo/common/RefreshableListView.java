package com.example.ooo.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by ooo on 2015/10/25.
 */
public class RefreshableListView extends ListView implements AbsListView.OnScrollListener {
	public static final String TAG = "refreshablelistviewloadingtag";
	private LinearLayout mRefreshingContainer;
	private ProgressBar mRefreshingProgressBar;
	private TextView mRefreshingTv;
	private String mDescStr = "努力加载中...";
	private OnClickListener mOnRefreshListener;
	private OnClickListener mOnLoadMoreListener;

	private boolean isLoadingMore;
	private boolean isRefreshing;
	private boolean isLoadAll;

	private LinearLayout mLoadMoreFooter;
	private TextView mLoadMoreTv;
	private ProgressBar mLoadMoreProgressBar;
	private int mCurrentScrollState;

	public RefreshableListView(Context context) {
		super(context);
		initLoadMore(context);
	}

	public RefreshableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLoadMore(context);
	}

	public RefreshableListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		initLoadMore(context);
	}

	private void initLoadMore(Context context) {
		setOnScrollListener(this);
		mLoadMoreFooter = new LinearLayout(context);
		AbsListView.LayoutParams llParams = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, 150);

		mLoadMoreFooter.setOrientation(LinearLayout.HORIZONTAL);
		mLoadMoreFooter.setGravity(Gravity.CENTER);
		mLoadMoreFooter.setLayoutParams(llParams);

		mLoadMoreTv = new TextView(getContext());
		mLoadMoreTv.setEnabled(false);

		LinearLayout.LayoutParams tvlParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		mLoadMoreTv.setLayoutParams(tvlParams);

		mLoadMoreFooter.addView(mLoadMoreTv);

		mLoadMoreProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
		LinearLayout.LayoutParams pblParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mLoadMoreProgressBar.setLayoutParams(pblParams);
		mLoadMoreFooter.addView(mLoadMoreProgressBar);
		mLoadMoreProgressBar.setVisibility(GONE);

		mLoadMoreFooter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isLoadAll) {
					startLoadMore();
				}
			}
		});

		addFooterView(mLoadMoreFooter, null, false);
	}

	private void startLoadMore() {
		isLoadingMore = true;
		mLoadMoreProgressBar.setVisibility(VISIBLE);
		mLoadMoreTv.setText("加载中...");
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onClick(mLoadMoreFooter);
		}
	}

	public void endLoadMore(String desc) {
		isLoadingMore = false;
		if (!TextUtils.isEmpty(desc)) {
			mLoadMoreTv.setText(desc);
			mLoadMoreProgressBar.setVisibility(GONE);
		} else {
			mLoadMoreProgressBar.setVisibility(VISIBLE);

		}
	}

	public boolean isLoadingMore() {
		return isLoadingMore;
	}

	public void setOnRefreshListener(OnClickListener onRefreshListener) {
		mOnRefreshListener = onRefreshListener;
	}

	public void setOnLoadMoreListener(OnClickListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}

	private void initLoadingView() {

		mRefreshingContainer = new LinearLayout(getContext());
		LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		llParams.setMargins(10, 5, 10, 5);
		mRefreshingContainer.setOrientation(LinearLayout.HORIZONTAL);
		mRefreshingContainer.setGravity(Gravity.CENTER);
		mRefreshingContainer.setLayoutParams(llParams);

		mRefreshingProgressBar = new ProgressBar(getContext());
		LinearLayout.LayoutParams pblParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mRefreshingProgressBar.setLayoutParams(pblParams);

		mRefreshingTv = new TextView(getContext());
		LinearLayout.LayoutParams tvlParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		tvlParams.setMargins(10, 0, 0, 0);
		mRefreshingTv.setLayoutParams(tvlParams);
		mRefreshingTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		mRefreshingTv.setText(mDescStr);

		mRefreshingContainer.addView(mRefreshingProgressBar);
		mRefreshingContainer.addView(mRefreshingTv);
		mRefreshingContainer.setTag(TAG);


	}

	public void finishLoadMore() {
		isRefreshing = false;
		isLoadAll = true;
		mLoadMoreTv.setText("全部加载完成");
		mLoadMoreProgressBar.setVisibility(GONE);
	}

	public void startRefreshing() {
		if (mRefreshingContainer == null) {
			initLoadingView();
		}
		isRefreshing = true;
		isLoadAll = false;

		ViewGroup parent = (ViewGroup) getParent();
		if (parent.findViewWithTag(TAG) == null) {
			parent.addView(mRefreshingContainer);
		}
		setVisibility(GONE);
		mRefreshingTv.setText(mDescStr);
		mRefreshingProgressBar.setVisibility(VISIBLE);
		mRefreshingContainer.setVisibility(VISIBLE);
		mRefreshingContainer.setEnabled(false);//for retry
	}

	public void endLoading() {
		isRefreshing = false;
		if (mRefreshingContainer != null) {
			ViewGroup parent = (ViewGroup) getParent();
			if (parent.findViewWithTag(TAG) != null) {
				parent.removeView(mRefreshingContainer);
			}
		}
		setVisibility(VISIBLE);
	}

	public void showError(String errorMsg) {
		if (mRefreshingContainer == null) {
			return;
		}
		isRefreshing = false;
		ViewGroup parent = (ViewGroup) getParent();
		if (parent.findViewWithTag(TAG) == null) {
			return;
		}
		mRefreshingContainer.setEnabled(true);
		mRefreshingTv.setText(errorMsg + ",点击重试");
		mRefreshingProgressBar.setVisibility(GONE);
		setVisibility(GONE);
		mRefreshingContainer.setOnClickListener(mOnRefreshListener);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int lastVisiblePosition = firstVisibleItem + visibleItemCount;
		if (lastVisiblePosition == totalItemCount - 1 && totalItemCount > 0 && !isLoadingMore && mCurrentScrollState != SCROLL_STATE_IDLE && !isLoadAll) {
			startLoadMore();
		}
	}
}
