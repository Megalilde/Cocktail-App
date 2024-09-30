package com.example.mycocktail.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycocktail.databinding.SearchItemBinding

class SuggestionDrinkItemsAdapter(private val context: Context, private val list: MutableList<String?>): RecyclerView.Adapter<SuggestionDrinkItemsAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class MyViewHolder(val binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SearchItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        val binding = holder.binding

        binding.tvItemName.text = model


        holder.itemView.setOnClickListener {
            if(onClickListener != null){
                if (model != null) {
                    onClickListener?.onClick(position,model)
                }
            }
        }
    }

    fun updateItems(newList: List<String>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, name: String)
    }



}