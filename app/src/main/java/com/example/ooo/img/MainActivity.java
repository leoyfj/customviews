package com.example.ooo.img;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.ooo.common.RefreshableListView;

import java.util.ArrayList;


public class MainActivity extends Activity {
	private RefreshableListView mListView;
	private boolean flag;
	private ArrayList<String> mList;
	private ArrayAdapter<String> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mList = new ArrayList<>();

		mListView = (RefreshableListView) findViewById(R.id.list);
		mAdapter = new ArrayAdapter<>(
				this,
				android.R.layout.simple_list_item_1,
				mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshData();
			}
		});
		mListView.setOnLoadMoreListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		refreshData();

	}

	private void loadMore() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (flag) {
							mListView.endLoadMore("出错了,点击重试");
							flag = false;
						} else {
							mListView.endLoadMore(null);
							flag = true;
							int last = Integer.parseInt(mList.get(mList.size() - 1));
							for (int i = 1; i <= 20; i++) {
								mList.add(String.valueOf(last + i));
							}
							mAdapter.notifyDataSetChanged();
							if (mList.size() > 50) {
								mListView.finishLoadMore();
							}
						}

					}
				});
			}
		}).start();
	}

	private void refreshData() {
		mListView.startRefreshing();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (flag) {
							mListView.endLoading();

							flag = false;
							mAdapter.notifyDataSetChanged();

//							mList.clear();
						} else {
							mListView.showError("这里什么都没有~");
							flag = true;
							mAdapter.notifyDataSetChanged();

							for (int i = 0; i < 20; i++) {
								mList.add(String.valueOf(i));
							}
						}

					}
				});
			}
		}).start();
	}

}
