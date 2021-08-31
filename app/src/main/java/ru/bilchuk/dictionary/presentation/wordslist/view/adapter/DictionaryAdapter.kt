package ru.bilchuk.dictionary.presentation.wordslist.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.bilchuk.dictionary.R
import ru.bilchuk.dictionary.databinding.VListItemBinding
import ru.bilchuk.dictionary.domain.models.DictionaryItem
import ru.bilchuk.dictionary.presentation.wordslist.view.adapter.DictionaryAdapter.DictionaryViewHolder

class DictionaryAdapter(private val dictionaryItems: List<DictionaryItem>) :
    RecyclerView.Adapter<DictionaryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        return DictionaryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.v_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        holder.bindView(dictionaryItems[position])
    }

    override fun getItemCount(): Int {
        return dictionaryItems.size
    }

    class DictionaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mBinding: VListItemBinding = VListItemBinding.bind(itemView)
        fun bindView(dictionaryItem: DictionaryItem) {
            mBinding.keyword.text = dictionaryItem.keyword
            mBinding.translation.text = dictionaryItem.translation

            val image = dictionaryItem.imageUri.toUri()

            Glide.with(mBinding.imageWord)
                .load(image)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                    )
                .thumbnail(0.33f)
                .centerCrop()
                .into(mBinding.imageWord)
        }

    }
}