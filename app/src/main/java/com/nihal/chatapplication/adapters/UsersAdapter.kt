package com.nihal.chatapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nihal.chatapplication.databinding.UserItemBinding
import com.nihal.chatapplication.model.User

class UsersAdapter(): RecyclerView.Adapter<UsersAdapter.ViewHolder>(){

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
     * Gets a list to set it to the RecyclerView Adapter.
     * @param list The list to be submitted.
     */
    fun submitList(list: List<User>?) = differ.submitList(list)

    /**
     * Gets the total number of items in this adapter.
     * @return an integer that represents the number of items in adapter.
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * Associates a viewholder with data.
     * @param holder The ViewHolder that represents the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: UsersAdapter.ViewHolder, position: Int) {
        val currentUser = differ.currentList[position]
        holder.bind(currentUser)
        holder.itemView.setOnClickListener{
            onItemClickListener?.let{it(currentUser)}
        }
    }

    /**
     * Holds specifications for the views in the RecyclerView item row.
     */
    inner class ViewHolder(val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(user: User) {
            binding.user = user
            binding.executePendingBindings()
        }
    }

    /*
     * Used for delegating item click events.
     */
    fun setOnItemClickListener(listener: (User) -> Unit){
        onItemClickListener = listener
    }

}