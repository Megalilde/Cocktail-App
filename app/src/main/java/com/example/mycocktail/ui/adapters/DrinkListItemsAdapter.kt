package com.example.mycocktail.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycocktail.R
import com.example.mycocktail.databinding.DrinkItemBinding
import com.example.mycocktail.data.datamodels.Drink
import com.squareup.picasso.Picasso

open class DrinkListItemsAdapter (private val context: Context, private val list: MutableList<Drink>): RecyclerView.Adapter<DrinkListItemsAdapter.MyViewHolder>(){

    private var onClickListener: OnClickListener? = null
    private var onClickImageListener: OnClickImageListener? = null


    inner class MyViewHolder(val binding: DrinkItemBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DrinkItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        val binding = holder.binding

        binding.itemText.text = model.strDrink
        //binding.itemImage.setImageResource(model.strDrinkThumb.toInt())
        Picasso.get().load(model.strDrinkThumb).into(binding.itemImage);


        if (model.isFavorite) {
            binding.heartIcon.setImageResource(R.drawable.ic_favorite_24dp_red)
        } else {
            binding.heartIcon.setImageResource(R.drawable.ic_favorite_24dp_platinum)
        }


        binding.cvCard.setOnClickListener {
            if(onClickListener != null){
                onClickListener?.onClick(position, model.idDrink)

            }
        }
        binding.heartIcon.setOnClickListener {
            // Questo è per prova.
            model.isFavorite = !model.isFavorite

            if (model.isFavorite) {
                binding.heartIcon.setImageResource(R.drawable.ic_favorite_24dp_red)
            } else {
                binding.heartIcon.setImageResource(R.drawable.ic_favorite_24dp_platinum)
            }

            if (onClickImageListener != null){
                onClickImageListener?.onClick(model)
            }

        }
    }

    fun updateItems(newList: MutableList<Drink>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener

    }

    fun setOnClickImageListener(onClickImageListener: OnClickImageListener){
        this.onClickImageListener = onClickImageListener

    }

    interface OnClickListener {
        fun onClick(position: Int, id: String)
    }

    interface OnClickImageListener {
        fun onClick(model: Drink)
    }
}