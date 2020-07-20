package com.dkatalis.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dkatalis.R
import com.dkatalis.model.User
import com.dkatalis.utils.Utils
import com.dkatalis.utils.Utils.getShortDate
import kotlinx.android.synthetic.main.item_user.view.*

class CardStackAdapter(
    private var users: List<User> = emptyList(), private val context: Context
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]

        Glide.with(holder.image)
            .load(user.picture)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.image)

        updateLocation(holder, user)

        holder.ivPerson.setOnClickListener {
            clearImageColor(holder)
            clearIndicator(holder)


            holder.ivPerson.setColorFilter(
                ContextCompat.getColor(context, R.color.green),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            holder.ivIndicator1.setImageResource(R.drawable.minus)
            holder.tvTitle.text = context.getString(R.string.my_name)
            holder.tvTitleValue.text = setName(user)
        }




        holder.ivCalender.setOnClickListener {
            clearImageColor(holder)
            clearIndicator(holder)
            holder.ivCalender.setColorFilter(
                ContextCompat.getColor(holder.itemView.context, R.color.green),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            holder.ivIndicator2.setImageResource(R.drawable.minus)
            holder.tvTitle.text = context.getString(R.string.my_dob)
            holder.tvTitleValue.text = getShortDate(user.dob.toLong())
        }
        holder.ivLocation.setOnClickListener {
            updateLocation(holder, user)
        }
        holder.ivPhone.setOnClickListener {
            clearImageColor(holder)
            clearIndicator(holder)
            holder.ivPhone.setColorFilter(
                ContextCompat.getColor(holder.itemView.context, R.color.green),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            holder.ivIndicator4.setImageResource(R.drawable.minus)
            holder.tvTitle.text = context.getString(R.string.my_phone)
            holder.tvTitleValue.text = user.phone
        }
        holder.ivLock.setOnClickListener {
            clearImageColor(holder)
            clearIndicator(holder)
            holder.ivLock.setColorFilter(
                ContextCompat.getColor(holder.itemView.context, R.color.green),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            holder.ivIndicator5.setImageResource(R.drawable.minus)
            holder.tvTitle.text = context.getString(R.string.my_password)
            holder.tvTitleValue.text = user.password
        }
    }

    private fun updateLocation(holder: ViewHolder, user: User) {
        clearImageColor(holder)
        clearIndicator(holder)
        holder.ivLocation.setColorFilter(
            ContextCompat.getColor(holder.itemView.context, R.color.green),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        holder.ivIndicator3.setImageResource(R.drawable.minus)
        holder.tvTitle.text = context.getString(R.string.my_address)
        holder.tvTitleValue.text = setAddress(user)
    }

    private fun setName(user: User): String? {
        return user.name.title.capitalize() + " " + user.name.first.capitalize() + " " +
                user.name.last.capitalize()
    }

    private fun setAddress(user: User): String? {
        return Utils.capitalize(user.location.street) + ", " + Utils.capitalize(user.location.city) +
                ", " + Utils.capitalize(user.location.state) + ", " + user.location.zip
    }

    private fun clearImageColor(holder: ViewHolder) {
        holder.ivPerson.setColorFilter(
            ContextCompat.getColor(holder.itemView.context, R.color.textGrey),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        holder.ivCalender.setColorFilter(
            ContextCompat.getColor(holder.itemView.context, R.color.textGrey),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        holder.ivLocation.setColorFilter(
            ContextCompat.getColor(holder.itemView.context, R.color.textGrey),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        holder.ivPhone.setColorFilter(
            ContextCompat.getColor(holder.itemView.context, R.color.textGrey),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        holder.ivLock.setColorFilter(
            ContextCompat.getColor(holder.itemView.context, R.color.textGrey),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    private fun clearIndicator(holder: ViewHolder) {
        holder.run {
            ivIndicator1.setImageResource(0)
            ivIndicator2.setImageResource(0)
            ivIndicator3.setImageResource(0)
            ivIndicator4.setImageResource(0)
            ivIndicator5.setImageResource(0)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun setUsers(users: List<User>) {
        this.users = users
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.tvTitle
        var tvTitleValue: TextView = view.tvTitleValue
        var ivPerson: ImageView = view.ivPerson
        val ivCalender: ImageView = view.ivCalender
        val ivLocation: ImageView = view.ivLocation
        val ivPhone: ImageView = view.ivPhone
        val ivLock: ImageView = view.ivLock

        val ivIndicator1: ImageView = view.ivIndicator1
        val ivIndicator2: ImageView = view.ivIndicator2
        val ivIndicator3: ImageView = view.ivIndicator3
        val ivIndicator4: ImageView = view.ivIndicator4
        val ivIndicator5: ImageView = view.ivIndicator5
        var image: ImageView = view.imgUser
    }

}