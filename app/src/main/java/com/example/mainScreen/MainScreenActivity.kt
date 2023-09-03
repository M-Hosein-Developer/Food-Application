package com.example.mainScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.ActivityMainBinding
import com.example.foodapp.databinding.DialogAddNewItemBinding
import com.example.foodapp.databinding.DialogDeleteItemBinding
import com.example.model.Database
import com.example.model.Food
import com.example.model.FoodDao

//implement Room
//Entity
//Dao
//create kotlin class for dtatbase

class MainScreenActivity : AppCompatActivity(), FoodAdaper.FoodEvent , MainScreenContract.View{

    lateinit var binding: ActivityMainBinding
    lateinit var mainAdapter: FoodAdaper
    lateinit var foodDao: FoodDao
    private lateinit var presenter : MainScreenContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //-------------------------------------------------------------------------------
        presenter = MainScreenPresenter(Database.getDatabase(this).foodDao)
        foodDao = Database.getDatabase(this).foodDao

        val sharedPreferences = getSharedPreferences("first", MODE_PRIVATE)
        if (sharedPreferences.getBoolean("firstRun", true)) {
            presenter.firstRun()
            sharedPreferences.edit().putBoolean("firstRun", false).apply()
        }
        presenter.onAttach(this)

//        showAllData()
        deleteAllData()
        addNewFood()
        search()
    }

    private fun deleteAllData() {

        binding.btnDeleteAllFood.setOnClickListener {

            presenter.onDeleteAllClicked()

        }

    }

    private fun showAllData() {

        val foodData = foodDao.getAllFood()

        mainAdapter = FoodAdaper(ArrayList(foodData), this)
        binding.recyclerMain.adapter = mainAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }

    private fun addNewFood() {

        binding.btnAddFood.setOnClickListener {

            val dialog = AlertDialog.Builder(this).create()
            val dialogBinding = DialogAddNewItemBinding.inflate(layoutInflater)
            dialog.setView(dialogBinding.root)
            dialog.setCancelable(true)
            dialog.show()

            dialogBinding.btnDone.setOnClickListener {

                if (dialogBinding.edtName.length() > 0 && dialogBinding.edtCity.length() > 0 && dialogBinding.edtPrice.length() > 0 && dialogBinding.edtDistance.length() > 0) {

                    val txtName = dialogBinding.edtName.text.toString()
                    val txtCity = dialogBinding.edtCity.text.toString()
                    val txtPrice = dialogBinding.edtPrice.text.toString()
                    val txtDistance = dialogBinding.edtDistance.text.toString()

                    val txtRatingNumber: Int = (1..300).random()
                    val ratingBarStar: Float = (1..5).random().toFloat()

                    val randomUrlPic = (0 until 10).random()
                    val urlPic = foodDao.getAllFood()[randomUrlPic].urlImage


                    val newFood = Food(
                        txtName = txtName,
                        txtPrice = txtPrice,
                        txtDistance = txtDistance,
                        txtCity = txtCity,
                        urlImage = urlPic,
                        numberRating = txtRatingNumber,
                        rating = ratingBarStar
                    )

                    presenter.onAddNewFoodClicked(newFood)
                    dialog.dismiss()

                } else {
                    Toast.makeText(this, "Complete the fields", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

    private fun search() {

        binding.rdtSearch.addTextChangedListener {
            if (it!!.length > 0) {

               presenter.onSearchFood(it.toString())

            }
        }
    }

    override fun onFoodClicked(food: Food, position: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val updateItemBinding = DialogAddNewItemBinding.inflate(layoutInflater)
        dialog.setView(updateItemBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        updateItemBinding.edtName.setText(food.txtName)
        updateItemBinding.edtCity.setText(food.txtCity)
        updateItemBinding.edtPrice.setText(food.txtPrice)
        updateItemBinding.edtDistance.setText(food.txtDistance)



        updateItemBinding.btnDone.setOnClickListener {

            if (updateItemBinding.edtName.length() > 0 && updateItemBinding.edtCity.length() > 0 && updateItemBinding.edtPrice.length() > 0 && updateItemBinding.edtDistance.length() > 0) {

                val txtName = updateItemBinding.edtName.text.toString()
                val txtCity = updateItemBinding.edtCity.text.toString()
                val txtPrice = updateItemBinding.edtPrice.text.toString()
                val txtDistance = updateItemBinding.edtDistance.text.toString()

                val newFood = Food(
                    id = food.id,
                    txtName = txtName,
                    txtPrice = txtPrice,
                    txtDistance = txtDistance,
                    txtCity = txtCity,
                    urlImage = food.urlImage,
                    numberRating = food.numberRating,
                    rating = food.rating
                )

                //data Update
                presenter.onUpdateFood(newFood , position)

                dialog.dismiss()

            } else {
                Toast.makeText(this, "Complete the fields", Toast.LENGTH_SHORT).show()
            }
        }


}

    override fun onFoodLongClicked(food: Food, position: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val dialogDeleteItemBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogDeleteItemBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogDeleteItemBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogDeleteItemBinding.btnSure.setOnClickListener {

            presenter.onDeleteFood(food , position)
            dialog.dismiss()
        }
    }

    override fun showFood(data: List<Food>) {
        mainAdapter = FoodAdaper(ArrayList(data) , this)
        binding.recyclerMain.adapter = mainAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this)
    }

    override fun refreshFood(data: List<Food>) {
        mainAdapter.setData(ArrayList(data))
    }

    override fun addNewFood(newFood: Food) {
        mainAdapter.addFood(newFood)
    }

    override fun deleteFood(oldFood: Food, pos: Int) {
        mainAdapter.removeFood(oldFood , pos)
    }

    override fun updateFood(editingFood: Food, pos: Int) {
        mainAdapter.updateFood(editingFood , pos)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

}