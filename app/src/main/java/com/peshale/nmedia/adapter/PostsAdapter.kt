package com.peshale.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.peshale.nmedia.R
import com.peshale.nmedia.databinding.CardPostBinding
import com.peshale.nmedia.dto.Post
import com.peshale.nmedia.utils.Utils

interface OnItemClickListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onView(post: Post) {}
    fun onDelete(post: Post) {}
    fun onEdit(post: Post) {}
    fun onCancelEdit(post: Post) {}
}

class PostAdapter(private val onItemClickListener: OnItemClickListener): ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder (
    private val binding: CardPostBinding,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind (post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeButton.text = Utils.counter(post.likes)
            shareButton.text = Utils.counter(post.shares)
            numberOfViews.text = Utils.counter(post.views)
            likeButton.isChecked = post.likedByMe

            //when click on Menu item, we do either edit or delete
            menu.setOnClickListener { it ->
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_main)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menuItemDelete -> {
                                onItemClickListener.onDelete(post)
                                true
                            }
                            R.id.menuItemEdit -> {
                                onItemClickListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            likeButton.setOnClickListener {
                onItemClickListener.onLike(post)
            }
            shareButton.setOnClickListener {
                onItemClickListener.onShare(post)
            }
            viewsButton.setOnClickListener {
                onItemClickListener.onView(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}