package mars.studio.findphone

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.contact_ticket.view.*

/**
 * Created by Asus on 10.03.2018.
 */
class ContactAdapter (val context: Context, val listOfContact: ArrayList<UserContact>): BaseAdapter(){

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val contact = listOfContact[position]

        if (contact.name == "no_users") {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.no_user, null)

            return view
        } else {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.contact_ticket, null)
            view.tv_name.text = contact.name
            view.tv_number.text = contact.phoneNumber

            return view
        }
    }

    override fun getItem(position: Int): Any {
        return listOfContact[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listOfContact.size
    }

}