package com.example.webmobtechexmp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.webmobtechexmp.R
import com.example.webmobtechexmp.model.User
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class UserAdapter(private val context: Context) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<User>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newUsers: List<User>) {
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount() = users.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivProfilePicture = itemView.findViewById<ImageView>(R.id.iv_profile_picture)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvEmail = itemView.findViewById<TextView>(R.id.tv_email)
        private val tvCity = itemView.findViewById<TextView>(R.id.tv_city)
        private val tvDob = itemView.findViewById<TextView>(R.id.tv_dob)
        private val tvUsername = itemView.findViewById<TextView>(R.id.tv_username)
        private val tvPhone = itemView.findViewById<TextView>(R.id.tv_phone)

        fun bind(user: User) {
            Picasso.get().load(user.picture.thumbnail).into(ivProfilePicture)
            tvName.text = "Name : ${user.name.first} ${user.name.last}"
            tvEmail.text = "Email : " + user.email
            tvCity.text = "Street : " + user.location.street.name
            tvDob.text = "DOB : " + getDOB(user.dob.date)
            tvUsername.text = "Username : " + user.login.username
            tvPhone.text = "Phone : " + user.phone
        }

        private fun getDOB(date: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val date = inputFormat.parse(date)
            val formattedDate = outputFormat.format(date)

            return formattedDate
        }
    }
}



