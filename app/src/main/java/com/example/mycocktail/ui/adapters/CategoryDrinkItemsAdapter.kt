package com.example.mycocktail.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycocktail.databinding.CategoryItemBinding

open class CategoryDrinkItemsAdapter(private val context: Context, private val list: MutableList<String>): RecyclerView.Adapter<CategoryDrinkItemsAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class MyViewHolder(val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        val binding = holder.binding


        Log.i("Binding", "Binding item at position: $position with data $model")
        binding.btnItem.text = model


        binding.btnItem.setOnClickListener {
            if(onClickListener != null){
                onClickListener?.onClick(position, model)
            }
        }
    }

    fun updateItems(newList: MutableList<String>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, type: String)
    }


}