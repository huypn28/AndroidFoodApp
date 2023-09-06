package com.example.androidfoodapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidfoodapp.R
import com.example.androidfoodapp.adapters.CategoryMealsAdapter
import com.example.androidfoodapp.databinding.ActivityCategoryMealsBinding
import com.example.androidfoodapp.fragments.HomeFragment
import com.example.androidfoodapp.videoModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareRecycerView()
        categoryMealsViewModel=ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer {mealsList ->
            binding.tvCategoryCount.text=mealsList.size.toString()
            categoryMealsAdapter.setMealsList(mealsList)

        })
    }

    private fun prepareRecycerView() {
        categoryMealsAdapter= CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=categoryMealsAdapter
        }
    }
}