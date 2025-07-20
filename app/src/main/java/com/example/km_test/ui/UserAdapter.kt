package com.example.km_test.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.km_test.R
import com.example.km_test.databinding.ItemUserBinding
import com.example.km_test.network.User

class UserAdapter(private val onUserClicked: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<User>()

    fun addUsers(newUsers: List<User>) {
        val startPosition = users.size
        users.addAll(newUsers)
        notifyItemRangeInserted(startPosition, newUsers.size)
    }

    fun clearUsers() {
        users.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.userNameTextView.text = user.getFullName()
            binding.userEmailTextView.text = user.email
            binding.avatarImageView.load(user.avatar) {
                placeholder(R.drawable.ic_default_profile_pic)
                error(R.drawable.ic_default_profile_pic)
            }
            binding.userItemLayout.setOnClickListener {
                onUserClicked(user)
            }
        }
    }
}