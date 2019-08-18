package pt.andrew.blisschallenge.Views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by andrew.fernandes on 18/08/2019
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private int _previousTotalItemCount = 0;
    private boolean _loading = true;

    private RecyclerView.LayoutManager _layoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this._layoutManager = layoutManager;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = _layoutManager.getItemCount();

        if (_layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) _layoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (_layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) _layoutManager).findLastVisibleItemPosition();
        } else if (_layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) _layoutManager).findLastVisibleItemPosition();
        }
        if (totalItemCount < _previousTotalItemCount) {
            this._previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this._loading = true;
            }
        }

        if (_loading && (totalItemCount > _previousTotalItemCount)) {
            _loading = false;
            _previousTotalItemCount = totalItemCount;
        }

        int _visibleThreshold = 5;
        if (!_loading && (lastVisibleItemPosition + _visibleThreshold) > totalItemCount) {
            onLoadMore();
            _loading = true;
        }
    }

    public abstract void onLoadMore();

}