package com.example.adutucart5.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.adutucart5.R
import com.example.adutucart5.activity.ProductDetailsActivity
import com.example.adutucart5.adapter.CategoryAdapter
import com.example.adutucart5.adapter.ProductAdapter
import com.example.adutucart5.databinding.FragmentHomeBinding
import com.example.adutucart5.model.AddProductModel
import com.example.adutucart5.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)



        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)

        if(preference.getBoolean("isCart",false)) {
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        }


        getCategories()
        getSliderImage()
        getProducts()



        return binding.root
    }

    private fun getSliderImage() {
        Firebase.firestore.collection("slider").document("item")
            .get().addOnSuccessListener {
                Glide.with(requireContext()).load(it.get("img")).into(binding.sliderImage)
            }

    }

    private fun getProducts() {

        val list = ArrayList<AddProductModel>()

        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
               binding.productRecycler.adapter = ProductAdapter(requireContext(), list)
            }

    }

    private fun getCategories() {

        val list = ArrayList<CategoryModel>()

        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecycler.adapter = CategoryAdapter(requireContext(), list)
            }
    }

}