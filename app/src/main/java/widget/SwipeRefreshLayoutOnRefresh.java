package widget;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by wuli on 16/11/9.
 */
public class SwipeRefreshLayoutOnRefresh implements SwipeRefreshLayout.OnRefreshListener {
    private PullToLoaderRecylerView mPullLoadMoreRecyclerView;
    public SwipeRefreshLayoutOnRefresh(PullToLoaderRecylerView pullLoadMoreRecyclerView) {
        this.mPullLoadMoreRecyclerView = pullLoadMoreRecyclerView;
    }

    @Override
    public void onRefresh() {
        if (!mPullLoadMoreRecyclerView.isRefresh()) {
            mPullLoadMoreRecyclerView.setIsRefresh(true);
            mPullLoadMoreRecyclerView.refresh();
        }
    }
}
