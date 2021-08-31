package ru.bilchuk.dictionary.presentation.wordslist.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.bilchuk.dictionary.R;
import ru.bilchuk.dictionary.databinding.VListItemBinding;
import ru.bilchuk.dictionary.domain.models.DictionaryItem;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> {

    private final List<DictionaryItem> dictionaryItems;

    public DictionaryAdapter(@NonNull List<DictionaryItem> dictionaryItems) {
        this.dictionaryItems = dictionaryItems;
    }

    @NonNull
    @Override
    public DictionaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DictionaryViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.v_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryViewHolder holder, int position) {
        holder.bindView(dictionaryItems.get(position));
    }

    @Override
    public int getItemCount() {
        return dictionaryItems.size();
    }

    static class DictionaryViewHolder extends RecyclerView.ViewHolder {

        private final VListItemBinding mBinding;

        DictionaryViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = VListItemBinding.bind(itemView);
        }

        void bindView(@NonNull DictionaryItem dictionaryItem) {
            mBinding.keyword.setText(dictionaryItem.getKeyword());
            mBinding.translation.setText(dictionaryItem.getTranslation());
        }
    }
}

