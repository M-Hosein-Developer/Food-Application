package com.example.model

import androidx.room.*

@Dao
interface FoodDao {

    //با استفاده از این میتونیم بررسی کنیم که غذا جدید هست یا نه. اگر جدید بود اضافه میشه و اگر از قبل وجود داشت آپدیت میشه
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(food: Food)

    @Insert
    fun insertFood(food: Food)

    @Insert
    fun insertAllFood(data : List<Food>)

    @Update
    fun updateFood(food: Food)

    @Delete
    fun deleteFood(food: Food)

    @Query("DELETE FROM table_food")
    fun deleteAllFood()

    @Query("SELECT * FROM table_food")
    fun getAllFood() : List<Food>

    @Query("SELECT * FROM table_food WHERE txtName LIke '%' || :searching || '%' ")
    fun searchFood(searching : String) : List<Food>
}