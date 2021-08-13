package my.ilpsdk.sms2android.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilpsdk.sms2android.R;

import java.util.ArrayList;
import java.util.List;


import my.ilpsdk.sms2android.Adapter.ViewHolder.StorViewHolder;
import my.ilpsdk.sms2android.Model.StorModel;

/**
 * Created with Android Studio
 * User: Xaver
 * Date: 24/05/15
 */
public class StorAdapter extends RecyclerView.Adapter<StorViewHolder> {

    private final LayoutInflater mInflater;
    private final List<StorModel> mModels;

    public StorAdapter(Context context, List<StorModel> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
    }

    @Override
    public StorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.from(parent.getContext()).inflate(R.layout.stor_list, parent, false);
        return new StorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StorViewHolder holder, int position) {
        final StorModel model = mModels.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<StorModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<StorModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final StorModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<StorModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final StorModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<StorModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final StorModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public StorModel removeItem(int position) {
        final StorModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, StorModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final StorModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
