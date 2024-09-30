package com.example.mycocktail.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycocktail.data.datamodels.Drink
import com.example.mycocktail.databinding.DrinkItemFavoriteBinding
import com.squareup.picasso.Picasso

open class DrinkListFavoriteItemsAdapter (private val context: Context, private val list: MutableList<Drink>): RecyclerView.Adapter<DrinkListFavoriteItemsAdapter.MyViewHolder>(){

    private var onClickListener: OnClickListener? = null


    inner class MyViewHolder(val binding: DrinkItemFavoriteBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DrinkItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        val binding = holder.binding

        binding.itemFavoriteId.text = model.idDrink
        binding.itemFavoriteTitle.text = model.strDrink
        //binding.itemImage.setImageResource(model.strDrinkThumb.toInt())
        Picasso.get().load(model.strDrinkThumb).into(binding.itemImageFavorite);

        binding.cvCardFavorite.setOnClickListener {
            if(onClickListener != null){
                onClickListener?.onClick(position, model.idDrink)

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

    interface OnClickListener {
        fun onClick(position: Int, id: String)
    }

}