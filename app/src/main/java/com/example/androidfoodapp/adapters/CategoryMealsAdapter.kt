package com.example.androidfoodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidfoodapp.databinding.MealItemsBinding
import com.example.androidfoodapp.pojo.MealsByCategory

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>(){
    private var mealsList=ArrayList<MealsByCategory>()

    fun  setMealsList(mealsList:List<MealsByCategory>){
        this.mealsList=mealsList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    inner class CategoryMealsViewModel(val binding: MealItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(
            MealItemsBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        Glide.with(holder.itemView).load(mealsList[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text=mealsList[position].strMeal
    }

}