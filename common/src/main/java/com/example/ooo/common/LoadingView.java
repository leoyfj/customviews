package com.example.ooo.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by peter on 15/10/27.
 */
public class LoadingView extends LinearLayout {
	private ImageView mLoadingImg;
	private ProgressBar mLoadingProgress;
	private TextView mLoadingTv;


	public LoadingView(Context context) {
		super(context);
		initLoadingView();
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLoadingView();
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initLoadingView();
	}

	private void initLoadingView() {

		LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER);
		setLayoutParams(llParams);

	}

	public void setRefreshing() {

	}

	public void endRefreshing(String msg) {

	}



}
