package kz.mars.studio.zooapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_animal_info.*

class AnimalInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_info)

        val bundle: Bundle = intent.extras
        val name = bundle.getString("name")
        val des = bundle.getString("des")
        val image: Int = bundle.getInt("image")

        iv_animal.setImageResource(image)
        tv_name.setText(name)
        tv_des.setText(des)
    }
}
