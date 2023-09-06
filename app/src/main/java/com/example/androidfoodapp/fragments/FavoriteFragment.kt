package com.example.androidfoodapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.example.androidfoodapp.activities.MainActivity
import com.example.androidfoodapp.adapters.MealsAdapter
import com.example.androidfoodapp.databinding.FragmentFavoriteBinding
import com.example.androidfoodapp.videoModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoriteFragment : Fragment() {
    private  lateinit var binding: FragmentFavoriteBinding
    private  lateinit var viewModel: HomeViewModel
    private  lateinit var favoritesAdapter: MealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=(activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding= FragmentFavoriteBinding.inflate(inflater)
        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        prepareRecyclerView()
        observeFavorites()

        val itemTouchHelper=object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            )=true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position=viewHolder.adapterPosition
                viewModel.deleteMeal(favoritesAdapter.differ.currentList[position])

                Snackbar.make(requireView(),"Meals deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertMeal(favoritesAdapter.differ.currentList[position])
                    }
                ).show()
            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
    }

    private fun prepareRecyclerView() {
        favoritesAdapter= MealsAdapter()
        binding.rvFavorites.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=favoritesAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoriteMealsLivedata().observe(requireActivity(), Observer {meals->
            favoritesAdapter.differ.submitList(meals)

        })
    }


}