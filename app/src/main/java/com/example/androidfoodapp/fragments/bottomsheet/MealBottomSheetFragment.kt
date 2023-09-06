package com.example.androidfoodapp.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.androidfoodapp.R
import com.example.androidfoodapp.activities.MainActivity
import com.example.androidfoodapp.activities.MealActivity
import com.example.androidfoodapp.databinding.FragmentMealBottomSheetBinding
import com.example.androidfoodapp.fragments.HomeFragment
import com.example.androidfoodapp.videoModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"


class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding:FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }

        viewModel=(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let { viewModel.getMealById(it) }

        observeBottomSheetMeal()

        onBottomSheetDialogClick()
    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {

            if (mealName!=null&&mealThumb!=null){
                val intent= Intent(activity,MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.Meal_ID,mealId)
                    putExtra(HomeFragment.Meal_NAME,mealName)
                    putExtra(HomeFragment.Meal_THUMB,mealThumb)
                }
            }
        }
    }

    private var mealName:String?=null
    private var mealThumb:String?=null
    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer { meal->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)
            binding.tvBottomSheetArea.text=meal.strArea
            binding.tvBottomSheetCategory.text=meal.strCategory
            binding.tvBottomSheetMealName.text=meal.strMeal

            mealName=meal.strMeal
            mealThumb=meal.strMealThumb

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}