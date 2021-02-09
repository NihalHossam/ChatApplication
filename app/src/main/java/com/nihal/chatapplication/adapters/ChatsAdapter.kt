package com.nihal.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nihal.chatapplication.databinding.UserItemBinding
import com.nihal.chatapplication.model.User

class ChatsAdapter (): RecyclerView.Adapter<ChatsAdapter.ViewHolder>(){

    private var onItemClickListener: ((User) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    /**
     * Associates a viewholder with data.
     * @param holder The ViewHolder that represents the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ChatsAdapter.ViewHolder, position: Int) {
        val currentUser = differ.currentList[position]

        holder.itemView.setOnClickListener{
            onItemClickListener?.let{it(currentUser)}
        }
    }

    /**
     * Gets a list to set it to the RecyclerView Adapter.
     * @param list The list to be submitted.
     */
    fun submitList(list: List<User>?) = differ.submitList(list)

    /**
     * Returns a house object at a specific position.
     * @param position The position of the item within the adapter's data set.
     * @return A house at the specified position.
     */
    fun getHouseAtPosition(position: Int): User = differ.currentList[position]

    /**
     * Gets the total number of items in this adapter.
     * @return an integer that represents the number of items in adapter.
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * Holds specifications for the views in the RecyclerView item row.
     */
    inner class ViewHolder(val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root){
    }

    /*
     * Used for delegating item click events.
     */
    fun setOnItemClickListener(listener: (User) -> Unit){
        onItemClickListener = listener
    }

}