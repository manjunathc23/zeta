package zeta.android.apps.adapters.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import javax.annotation.ParametersAreNonnullByDefault;

import timber.log.Timber;

@ParametersAreNonnullByDefault
public abstract class BaseHeaderAndFooterAdapter extends RecyclerView.Adapter<BaseHeaderAndFooterAdapter.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public static final int HEADER = 1000000000;
    public static final int FOOTER = 1000000001;
    public static final int NO_STICKY_HEADER = -1;
    private static final String TAG = BaseHeaderAndFooterAdapter.class.getSimpleName();
    //Currently we are supporting only one header and footer
    private static final int HEADER_COUNT = 1;
    private static final int FOOTER_COUNT = 1;
    private static final int NO_ITEM_COUNT = 0;
    private static final int HEADER_POSITION_INDEX = 0;

    /**
     * Base class contract to get the sticky header identifier
     * <p>
     * This is wrapper method to make sure to send the right index to the derived classes
     *
     * @param position : item position
     * @return Sticky header identifier
     */
    protected abstract long getStickyHeaderIdentifier(int position);

    /**
     * Create sticky header view holder
     *
     * @param viewGroup : view group
     * @return : Sticky header view holder
     */
    protected abstract View onCreateStickyHeaderViewHolder(ViewGroup viewGroup);

    /**
     * Bind sticky header view holder
     *
     * @param viewHolder : view holder
     * @param position   : position
     */
    protected abstract void onBindStickyHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    /**
     * Base class contract to know if we need to show the header?
     *
     * @return : flag to denote if we need to show header
     */
    protected abstract boolean canShowHeader();

    /**
     * Base class contract to know if we need to show the footer?
     *
     * @return : flag to denote if we need to show footer
     */
    protected abstract boolean canShowFooter();

    /**
     * Base class contact to get the regular item type, Within regular item too we can have multiple view type
     *
     * @return : gets the regular item type
     */
    protected abstract int getRegularItemViewType(final int position);

    /**
     * Base class contact to get the regular item count
     *
     * @return : get the header view
     */
    protected abstract int getRegularItemCount();

    /**
     * Base class contact to get the header view
     *
     * @param parent : parent view
     * @return : get the header view
     */
    protected abstract View onCreateRegularItemHeaderViewHolder(final ViewGroup parent);

    /**
     * Base class contact to get the footer view
     *
     * @param parent : parent view
     * @return : get the footer view
     */
    protected abstract View onCreateRegularItemFooterViewHolder(final ViewGroup parent);

    /**
     * Base class contact to get the regular item view
     *
     * @param parent   : parent view
     * @param viewType : type of view within regular items
     * @return : get the regular item view based in the view type
     */
    protected abstract View onCreateRegularViewHolder(final ViewGroup parent, final int viewType);

    /**
     * Base class contract to bind the header view
     *
     * @param viewHolder : recycler view holder
     * @param position   : position of the item in the recycler view
     */
    protected abstract void onBindRegularItemHeaderViewHolder(final RecyclerView.ViewHolder viewHolder, final int position);

    /**
     * Base class contact to bind the footer view
     *
     * @param viewHolder : recycler view holder
     * @param position   : position of the item in the recycler view
     */
    protected abstract void onBindRegularItemFooterViewHolder(final RecyclerView.ViewHolder viewHolder, final int position);

    /**
     * Base class contact to bind the regular item view
     *
     * @param viewHolder : recycler view holder
     * @param position   : position of the item in the recycler view
     */
    protected abstract void onBindRegularViewHolder(final RecyclerView.ViewHolder viewHolder, final int position);

    @Override
    public final long getHeaderId(int position) {
        //We don't want to show sticky header on top header
        if (canShowHeader() && position == 0) {
            return NO_STICKY_HEADER;
        }

        //Check for the sticky header for last element
        if (canShowFooter() && position == getItemCount() - 1) {
            return NO_STICKY_HEADER;
        }

        //This may happen when the data get changed and there will be some slight delay
        //updating the sticky headers adapter.
        //Note : Sticky header are decoration of the recycler view not the actual header
        if (getRegularItemPosition(position) >= getRegularItemCount()) {
            Timber.e(TAG, "Index out of bound. Make sure to update sticky header decoration after data set change");
            return NO_STICKY_HEADER;
        }

        return getStickyHeaderIdentifier(getRegularItemPosition(position));
    }

    @Override
    public final RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return new ViewHolder(onCreateStickyHeaderViewHolder(viewGroup));
    }

    @Override
    public final void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        onBindStickyHeaderViewHolder(viewHolder, getRegularItemPosition(position));
    }

    @Override
    public final int getItemCount() {
        //header + regular item + footer
        return getRegularItemHeaderCount() + getRegularItemCount() + getRegularItemFooterCount();
    }

    @Override
    public final int getItemViewType(int position) {
        //Check for header position index
        if ((position == getHeaderPositionIndex()) && canShowHeader()) {
            return HEADER;
        }

        //Check for footer position index
        if ((position == getFooterPositionIndex()) && canShowFooter()) {
            return FOOTER;
        }

        //Regular item
        return getRegularItemViewType(getRegularItemPosition(position));
    }

    @Override
    public final ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view;
        switch (viewType) {
            case HEADER:
                view = onCreateRegularItemHeaderViewHolder(parent);
                break;
            case FOOTER:
                view = onCreateRegularItemFooterViewHolder(parent);
                break;
            default:
                view = onCreateRegularViewHolder(parent, viewType);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public final void onBindViewHolder(final ViewHolder holder, final int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case HEADER:
                onBindRegularItemHeaderViewHolder(holder, position);
                break;
            case FOOTER:
                onBindRegularItemFooterViewHolder(holder, position);
                break;
            default:
                onBindRegularViewHolder(holder, getRegularItemPosition(position));
                break;
        }
    }

    /**
     * Helper method to get the header count
     *
     * @return : header count, currently we are supporting only one header count
     */
    private int getRegularItemHeaderCount() {
        return canShowHeader() ? HEADER_COUNT : NO_ITEM_COUNT;
    }

    /**
     * Helper method to get the footer count
     *
     * @return : footer count, currently we are supporting only one footer count
     */
    private int getRegularItemFooterCount() {
        return canShowFooter() ? FOOTER_COUNT : NO_ITEM_COUNT;
    }

    /**
     * Helper method to get the header position index.
     *
     * @return : header position index, currently we are treating our header to be at `0`th position
     */
    private int getHeaderPositionIndex() {
        return HEADER_POSITION_INDEX;
    }

    /**
     * Helper method to get the footer position index.
     *
     * @return : header position index, currently we are last but one position to be footer
     */
    private int getFooterPositionIndex() {
        return (getItemCount() - 1);
    }

    /**
     * Helper method to get the position for the regular item
     * this considers header offsets
     *
     * @return : position for the regular item
     */
    private int getRegularItemPosition(int originalPosition) {
        return originalPosition - getRegularItemPositionOffset();
    }

    /**
     * Helper method to get the position offset because of header
     *
     * @return : position offset for the regular item
     */
    private int getRegularItemPositionOffset() {
        return canShowHeader() ? HEADER_COUNT : NO_ITEM_COUNT;
    }

    /**
     * Notifies the adapter that the header view has changed.
     */
    protected void notifyHeaderChanged() {
        notifyItemChanged(HEADER_POSITION_INDEX);
    }

    /**
     * Recycler View holder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            // All views that would normally be contained in this class can be found in the custom
            // view class used instead.
            super(v);
        }
    }

}


