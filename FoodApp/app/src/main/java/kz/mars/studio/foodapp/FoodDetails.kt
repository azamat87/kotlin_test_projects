package kz.mars.studio.foodapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_food_details.*

class FoodDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        val bundle = intent.extras

        food_image.setImageResource(bundle.getInt("image"))
        name_food.text = bundle.getString("name")
        food_detail.text = bundle.getString("des")
    }
}
