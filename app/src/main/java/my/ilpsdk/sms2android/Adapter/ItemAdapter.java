package my.ilpsdk.sms2android.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilpsdk.sms2android.R;

import java.util.ArrayList;
import java.util.List;

import my.ilpsdk.sms2android.Adapter.ViewHolder.ItemViewHolder;
import my.ilpsdk.sms2android.Model.ItemModel;

/**
 * Created with Android Studio
 * User: Xaver
 * Date: 24/05/15
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private final LayoutInflater mInflater;
    private final List<ItemModel> mModels;

    public ItemAdapter(Context context, List<ItemModel> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ItemModel model = mModels.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<ItemModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<ItemModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final ItemModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ItemModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ItemModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ItemModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ItemModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public ItemModel removeItem(int position) {
        final ItemModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ItemModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ItemModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
