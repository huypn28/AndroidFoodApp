package com.example.androidfoodapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.androidfoodapp.R
import com.example.androidfoodapp.activities.CategoryMealsActivity
import com.example.androidfoodapp.activities.MainActivity
import com.example.androidfoodapp.activities.MealActivity
import com.example.androidfoodapp.adapters.CategoriesAdapter
import com.example.androidfoodapp.adapters.MostPopularAdapter
import com.example.androidfoodapp.databinding.FragmentHomeBinding
import com.example.androidfoodapp.fragments.bottomsheet.MealBottomSheetFragment
import com.example.androidfoodapp.pojo.MealsByCategory
import com.example.androidfoodapp.pojo.Meal
import com.example.androidfoodapp.videoModel.HomeViewModel

class HomeFragment : Fragment() {
    private  lateinit var binding: FragmentHomeBinding
    private  lateinit var viewModel: HomeViewModel
    private  lateinit var randomMeal: Meal
    private  lateinit var popularItemsAdapter: MostPopularAdapter
    private  lateinit var categoriesAdapter: CategoriesAdapter


    companion object{
        const val Meal_ID="com.example.androidfoodapp.fragments.idMeal"
        const val Meal_NAME="com.example.androidfoodapp.fragments.nameMeal"
        const val Meal_THUMB="com.example.androidfoodapp.fragments.thumbMeal"
        const val CATEGORY_NAME="com.example.androidfoodapp.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=(activity as MainActivity).viewModel
        popularItemsAdapter=MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observerPopularItemsLivedata()
        preparePopularItemsRecyclerView()

        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLivedata()
//hoat dong cua danh muc san pham
        onCategoryClick()

        onPopularItemLongClick()

        onSearchIconClick()

    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick={meal->
            val mealBottomSheetFragment=MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={category ->  
            val intent=Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter=CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager=GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }

    private fun observeCategoriesLivedata() {
        viewModel.observeCategoriesLivedata().observe(viewLifecycleOwner, Observer { categories->
                categoriesAdapter.setCategoryList(categories)
        })
    }

    //popular
    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick={meal->
            val intent=Intent(activity,MealActivity::class.java)
            intent.putExtra(Meal_ID,meal.idMeal)
            intent.putExtra(Meal_NAME,meal.strMeal)
            intent.putExtra(Meal_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter=popularItemsAdapter
        }
    }

    private fun observerPopularItemsLivedata() {
        viewModel.observePopularItemsLivedata().observe(viewLifecycleOwner
        ) { mealList->
            popularItemsAdapter.setMeals(mealsList =mealList as ArrayList<MealsByCategory>)
        }
    }
//random meal
    private fun onRandomMealClick(){
        binding.randomMealCard.setOnClickListener{
            val intent=Intent(activity,MealActivity::class.java)
            intent.putExtra(Meal_ID,randomMeal.idMeal)
            intent.putExtra(Meal_NAME,randomMeal.strMeal)
            intent.putExtra(Meal_THUMB,randomMeal.strMealThumb)

            startActivity(intent)

        }
    }

    private fun observerRandomMeal(){
        viewModel.observeRandomMealLivedata().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imageRandomMeal)

            this.randomMeal=meal

        }
    }


}