package kz.mars.studio.zooapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.animal_ticket.view.*

class MainActivity : AppCompatActivity() {

    var listOfAnimals = ArrayList<Animal>()
    var adapter: AnimalAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // load animals
        listOfAnimals.add(
                Animal("Baboon","Baboon live in  big place with tree",R.drawable.baboon,false))
        listOfAnimals.add(
                Animal("Bulldog","Bulldog live in  big place with tree",R.drawable.bulldog,false))
        listOfAnimals.add(
                Animal("Panda","Panda live in  big place with tree",R.drawable.panda,true))
        listOfAnimals.add(
                Animal("Swallow","Swallow live in  big place with tree",R.drawable.swallow_bird,false))
        listOfAnimals.add(
                Animal("Tiger","Tiger live in  big place with tree",R.drawable.white_tiger,true))
        listOfAnimals.add(
                Animal("Zebra","Zebra live in  big place with tree",R.drawable.zebra,false))

        adapter = AnimalAdapter(this,listOfAnimals)

        listAnimal.adapter = adapter
    }

    fun delete(index: Int) {
        listOfAnimals.removeAt(index)
        adapter!!.notifyDataSetChanged()
    }

    fun add(index: Int) {
        listOfAnimals.add(index, listOfAnimals[index])
        adapter!!.notifyDataSetChanged()
    }

    inner class AnimalAdapter : BaseAdapter {

        private var listOfAnimals = ArrayList<Animal>()
        private var context:Context?=null

        constructor(context:Context, listOfAnimals: ArrayList<Animal>):super(){
            this.context = context
            this.listOfAnimals = listOfAnimals
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val animal = listOfAnimals[position]
            var inlater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            if (animal.isKiller == true) {
                val view = inlater.inflate(R.layout.animal_killer_ticket, null)
                view.tv_name.text = animal.name
                view.tv_des.text = animal.des
                view.iv_animal.setImageResource(animal.image!!)
                view.iv_animal.setOnClickListener {
//                    val intent = Intent(context, AnimalInfo::class.java)
//                    intent.putExtra("name", animal.name)
//                    intent.putExtra("des", animal.des)
//                    intent.putExtra("image", animal.image!!)
//                    context!!.startActivity(intent)
                    delete(position)
                }
                return view
            } else {
                val view = inlater.inflate(R.layout.animal_ticket, null)
                view.tv_name.text = animal.name
                view.tv_des.text = animal.des
                view.iv_animal.setImageResource(animal.image!!)
                view.iv_animal.setOnClickListener {
//                    val intent = Intent(context, AnimalInfo::class.java)
//                    intent.putExtra("name", animal.name)
//                    intent.putExtra("des", animal.des)
//                    intent.putExtra("image", animal.image!!)
//                    context!!.startActivity(intent)
                    delete(position)
                }
                return view
            }
        }

        override fun getItem(position: Int): Any {
            return listOfAnimals[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfAnimals.size
        }
    }
}
