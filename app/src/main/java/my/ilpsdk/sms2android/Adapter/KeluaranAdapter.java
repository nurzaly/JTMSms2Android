package my.ilpsdk.sms2android.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilpsdk.sms2android.R;

import java.util.ArrayList;
import java.util.List;


import my.ilpsdk.sms2android.Adapter.ViewHolder.StatusViewHolder;
import my.ilpsdk.sms2android.Model.KeluaranModel;

/**
 * Created with Android Studio
 * User: Xaver
 * Date: 24/05/15
 */
public class KeluaranAdapter extends RecyclerView.Adapter<StatusViewHolder> {

    private final LayoutInflater mInflater;
    private final List<KeluaranModel> mModels;

    public KeluaranAdapter(Context context, List<KeluaranModel> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
    }

    @Override
    public StatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.from(parent.getContext()).inflate(R.layout.status_list, parent, false);
        return new StatusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StatusViewHolder holder, int position) {
        final KeluaranModel model = mModels.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<KeluaranModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<KeluaranModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final KeluaranModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<KeluaranModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final KeluaranModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<KeluaranModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final KeluaranModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public KeluaranModel removeItem(int position) {
        final KeluaranModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, KeluaranModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final KeluaranModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
