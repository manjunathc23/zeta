package zeta.android.apps.adapters.home;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import zeta.android.apps.R;
import zeta.android.apps.adapters.common.BaseHeaderAndFooterAdapter;
import zeta.android.apps.views.home.FlickrListViewComponent;
import zeta.apps.flickr.models.flickr.FlickrImages;

@ParametersAreNonnullByDefault
public class FlickrHomeAdapter extends BaseHeaderAndFooterAdapter {

    private List<FlickrImages> mImages;
    private FlickrHomeAdapterListener mListener;

    public FlickrHomeAdapter() {
        mImages = new ArrayList<>(0);
    }

    public void setAdapterClickListener(@Nullable FlickrHomeAdapterListener listener) {
        mListener = listener;
    }

    public void updateImagesModel(List<FlickrImages> imagesList, int previousSize) {
        mImages = imagesList;
        if (previousSize == 0) {
            notifyDataSetChanged();
        } else {
            final int positionStart = previousSize + 1;
            notifyItemRangeInserted(positionStart, mImages.size());
        }
    }

    public void showFooterProgress(boolean show) {
        //todo
    }

    public void showFooterRetry(boolean show) {
        //todo
    }

    @Override
    protected long getStickyHeaderIdentifier(int position) {
        return 0;
    }

    @Override
    protected View onCreateStickyHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    protected void onBindStickyHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        //Sticky Header goes here
    }

    @Override
    protected boolean canShowHeader() {
        return false;
    }

    @Override
    protected boolean canShowFooter() {
        return false;
    }

    @Override
    protected int getRegularItemViewType(int position) {
        return 0;
    }

    @Override
    protected int getRegularItemCount() {
        return mImages.size();
    }

    @Override
    protected View onCreateRegularItemHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected View onCreateRegularItemFooterViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected View onCreateRegularViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.view_flickr_list_item_inflatable, parent, false);
    }

    @Override
    protected void onBindRegularItemHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        //Header goes here
    }

    @Override
    protected void onBindRegularItemFooterViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        //Footer goes here
    }

    @Override
    protected void onBindRegularViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final FlickrImages flickrImages = mImages.get(position);
        final FlickrListViewComponent listViewComponent = ((FlickrListViewComponent) viewHolder.itemView);
        listViewComponent.setItemData(flickrImages);
        listViewComponent.setOnClickListener(v -> {
            if (mListener == null) {
                return;
            }
            mListener.onItemClick(flickrImages);
        });
    }

    public interface FlickrHomeAdapterListener {
        void onItemClick(FlickrImages flickrImages);
    }
}
