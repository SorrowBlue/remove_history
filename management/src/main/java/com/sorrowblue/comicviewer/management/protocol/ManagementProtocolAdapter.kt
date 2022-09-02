package com.sorrowblue.comicviewer.management.protocol

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sorrowblue.comicviewer.framework.ui.recyclerview.ViewBindingViewHolder
import com.sorrowblue.comicviewer.domain.model.library.SupportProtocol
import com.sorrowblue.comicviewer.management.databinding.ManagementItemProtocolBinding

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SupportProtocol>() {
    override fun areItemsTheSame(oldItem: SupportProtocol, newItem: SupportProtocol) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: SupportProtocol, newItem: SupportProtocol) =
        oldItem == newItem
}

internal class ManagementProtocolAdapter(private val onCLick: (SupportProtocol) -> Unit) :
    ListAdapter<SupportProtocol, ManagementProtocolAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(parent: ViewGroup) :
        ViewBindingViewHolder<ManagementItemProtocolBinding>(
            parent,
            ManagementItemProtocolBinding::inflate
        ) {

        fun bind(item: SupportProtocol) {
            binding.textView.text = item.name
            binding.root.setOnClickListener {
                onCLick.invoke(item)
            }
        }
    }
}
