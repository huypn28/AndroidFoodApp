package com.example.androidfoodapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.androidfoodapp.R
import com.example.androidfoodapp.databinding.ActivityMealBinding
import com.example.androidfoodapp.db.MealDatabase
import com.example.androidfoodapp.fragments.HomeFragment
import com.example.androidfoodapp.pojo.Meal
import com.example.androidfoodapp.videoModel.HomeViewModel
import com.example.androidfoodapp.videoModel.MealViewModel
import com.example.androidfoodapp.videoModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var mealMvvm: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMealBinding.inflate(layoutInflater)


        setContentView(binding.root)


        val mealDatabase=MealDatabase.getInstance(this)
        val viewModelFactory=MealViewModelFactory(mealDatabase)
        mealMvvm= ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]


        getMealInformationFromIntent()

        setInformationInViews()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailLiveData()

        onYoutubeImageClick()
        onFavoriteClick()

    }

    private fun onFavoriteClick() {
        binding.btnAddToFav.setOnClickListener{
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick(){
        binding.imgYoutube.setOnClickListener{
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal?=null

    private fun observerMealDetailLiveData(){
        mealMvvm.observerMealDetailsLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(t: Meal?) {
                onResponseCase()
                val meal=t
                mealToSave=meal
                binding.tvCategory.text="Category: ${meal!!.strCategory}"
                binding.tvArea.text="Category: ${meal.strArea}"
                binding.tvInstructions.text=meal.strInstructions


                youtubeLink= meal.strYoutube.toString()
            }

        })
    }

    private fun setInformationInViews(){
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title=mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))


    }

    private  fun getMealInformationFromIntent(){
        val intent= intent
        mealId=intent.getStringExtra(HomeFragment.Meal_ID)!!
        mealName=intent.getStringExtra(HomeFragment.Meal_NAME)!!
        mealThumb=intent.getStringExtra(HomeFragment.Meal_THUMB)!!
    }
    private  fun loadingCase(){
        binding.progressBar.visibility= View.VISIBLE
        binding.btnAddToFav.visibility= View.INVISIBLE
        binding.tvInstructions.visibility= View.INVISIBLE
        binding.tvCategory.visibility= View.INVISIBLE
        binding.tvArea.visibility= View.INVISIBLE
        binding.imgYoutube.visibility= View.INVISIBLE
    }
    private  fun onResponseCase(){
        binding.progressBar.visibility= View.INVISIBLE
        binding.btnAddToFav.visibility= View.VISIBLE
        binding.tvInstructions.visibility= View.VISIBLE
        binding.tvCategory.visibility= View.VISIBLE
        binding.tvArea.visibility= View.VISIBLE
        binding.imgYoutube.visibility= View.VISIBLE
    }
}