package com.example.smartkitbeta.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartkitbeta.R
import com.example.smartkitbeta.models.RoomItems
import kotlinx.android.synthetic.main.room_items.view.*

class Room_adapter(private val roomItemList: List<RoomItems>, val clickListener: OnRoomClickListener)
    : RecyclerView.Adapter<Room_adapter.RoomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.room_items, parent, false)

        return  RoomViewHolder(itemView)
    }

    override fun getItemCount() = roomItemList.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val currentItem = roomItemList[position]
        holder.RoomPhoto.setImageResource(currentItem.ImageResource)
        holder.RoomName.text = currentItem.RoomName

        holder.initialize( clickListener)
    }

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val RoomPhoto: ImageView = itemView.room_photo
        val RoomName: TextView = itemView.room_name

        fun initialize(action: OnRoomClickListener){
            itemView.setOnClickListener{
                action.onRoomClick(adapterPosition)
            }
        }

    }

    interface OnRoomClickListener {
        fun onRoomClick(position: Int){}

    }
}
