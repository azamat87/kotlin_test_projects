package kz.mars.studio.noteapp

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_motes.*

class AddNotes : AppCompatActivity() {

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_motes)


        try {
            val bundle: Bundle = intent.extras
            id = bundle.getInt("ID",0)
            if (id != 0) {
                edTitle.setText(bundle.getString("title").toString())
                edDes.setText(bundle.getString("des").toString())
            }

        } catch (ex: Exception) {

        }
    }

    fun buAdd(view: View) {
        val dbManager = DbManager(this)
        var values = ContentValues()

        values.put("Title", edTitle.text.toString())
        values.put("Description", edDes.text.toString())

        if (id == 0) {
            val ID = dbManager.insert(values)
            if (ID > 0) {
                Toast.makeText(this, " Note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, " Can not added", Toast.LENGTH_SHORT).show()
            }
        } else {
            var arrayArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID?=", arrayArgs)
            if (ID > 0) {
                Toast.makeText(this, " Note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, " Can not added", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
