package kz.mars.studio.noteapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket_note.view.*

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // add data
//        listNotes.add(Note(1, "meet professor", " asfsdgf  fsdhk  fhgfkfhcnv"))
//        listNotes.add(Note(2, "meet professor 2", " asfsdgf  fsdhk  fhgfkfhcnv"))

        // load from DB

        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    private fun LoadQuery(title: String) {
        val dbManager = DbManager(this)
        val projection = arrayOf("ID","Title","Description")
        val selectionArgs = arrayOf(title)
        var cursor = dbManager.query(projection, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val description = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID,title,description))
            }while (cursor.moveToNext())
        }

        val adapter = MyNotesAdapter(this, listNotes)
        listNote.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if (menu != null) {
            val sv:SearchView = menu.findItem(R.id.app_bar_search).actionView as SearchView

            val sm= getSystemService(Context.SEARCH_SERVICE) as SearchManager
            sv.setSearchableInfo(sm.getSearchableInfo(componentName))


            sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    LoadQuery("%"+query+"%")
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addNote -> {
                    val intent = Intent(this, AddNotes::class.java)
                    startActivity(intent)
                }
                R.id.app_bar_search -> {

                }
            }
        }


        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter : BaseAdapter {

        var listNotesAdapter = ArrayList<Note>()
        var ctx: Context? = null

        constructor(context: Context, listNotes: ArrayList<Note>) : super() {
            this.listNotesAdapter = listNotes
            this.ctx =context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = layoutInflater.inflate(R.layout.ticket_note, null)
            var note = listNotesAdapter[position]

            view.tv_title.setText(note.nodeName)
            view.tv_des.setText(note.nodeDes)

            view.btnDelete.setOnClickListener {
                val dbManager = DbManager(ctx!!)
                val selectionArgs = arrayOf(note.nodeID.toString())
                dbManager.delete("ID?=",selectionArgs)
                LoadQuery("%")
            }

            view.btnEdit.setOnClickListener {
                val intent = Intent(ctx, AddNotes::class.java)
                intent.putExtra("ID", note.nodeID)
                intent.putExtra("title", note.nodeName)
                intent.putExtra("des", note.nodeDes)
                startActivity(intent)
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

    }
}
