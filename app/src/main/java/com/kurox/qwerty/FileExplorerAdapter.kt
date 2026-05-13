package com.kurox.qwerty

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FileExplorerAdapter :
    RecyclerView.Adapter<FileExplorerAdapter.ViewHolder>() {

    class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

    }
}