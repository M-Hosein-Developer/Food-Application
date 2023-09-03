package com.example.mainScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.model.Food

class FoodAdaper(private val data: ArrayList<Food>, private val foodEvent : FoodEvent) : RecyclerView.Adapter<FoodAdaper.FoodViewHolder>(){

    inner class FoodViewHolder(itemView : View , private val context: Context) : RecyclerView.ViewHolder(itemView) {

        val img = itemView.findViewById<ImageView>(R.id.img)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtPlace = itemView.findViewById<TextView>(R.id.txtPlace)
        val txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
        val txtMiles = itemView.findViewById<TextView>(R.id.txtMiles)
        val txtRating = itemView.findViewById<TextView>(R.id.txtRating)
        val Rating = itemView.findViewById<RatingBar>(R.id.ratingBar)

        fun bindData(position: Int) {

            txtName.text = data[position].txtName
            txtPlace.text = data[position].txtCity
            txtPrice.text = data[position].txtPrice
            txtMiles.text = data[position].txtDistance
            txtRating.text = "(" + data[position].numberRating.toString() + ")"
            Rating.rating = data[position].rating

            loadImage(position)


            itemView.setOnClickListener {
                foodEvent.onFoodClicked(data[adapterPosition] , adapterPosition)
            }

            itemView.setOnLongClickListener {
                foodEvent.onFoodLongClicked(data[adapterPosition] , adapterPosition)

                true
            }


        }

        fun loadImage(position: Int){
            Glide.with(context)
                .load(data[position].urlImage)
                .into(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent , false)

        return FoodViewHolder(view , parent.context)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addFood(newFood: Food){

        //add food to list
        data.add(0 , newFood)
        notifyItemInserted(0)

    }

    fun removeFood(oldFood: Food, oldPosition : Int){
        data.remove(oldFood)
        notifyItemRemoved(oldPosition)
    }

    fun updateFood(newFood: Food, position: Int){

        data.set(position , newFood)
        notifyItemChanged(position)
    }

    fun setData(newList : ArrayList<Food>){
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    interface FoodEvent{

        fun onFoodClicked(food: Food, adapterPosition: Int)
        fun onFoodLongClicked(food : Food, position: Int)
    }

}