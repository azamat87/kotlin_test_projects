package kz.mars.studio.mediaplayer

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*

class MainActivity : AppCompatActivity() {

    var list = ArrayList<SongInfo>()
    var myAdapter: MyListSongAdapter? = null
    var mp: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        loadSongsOnline()
        CheckUserPermsions()
        myAdapter = MyListSongAdapter(list)
        lv_songs.adapter = myAdapter

        val myTrack = mySongTrack()
        myTrack.start()
    }

    fun loadSongsOnline() {
        list.add(SongInfo("","",""))
        list.add(SongInfo("","",""))
        list.add(SongInfo("","",""))
        list.add(SongInfo("","",""))
        list.add(SongInfo("","",""))
    }

    inner class MyListSongAdapter : BaseAdapter{

        var listSong = ArrayList<SongInfo>()

        constructor(list : ArrayList<SongInfo>):super() {
            this.listSong = list
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = layoutInflater.inflate(R.layout.song_ticket, null)

            val song = listSong[position]
            view.tv_song_name.text = song.title
            view.tv_author.text = song.authorName

            view.bu_play.setOnClickListener {
                if (view.bu_play.text.equals("Stop")) {
                    mp!!.stop()
                    view.bu_play.text = "Play"
                } else {
                    mp = MediaPlayer()
                    try {
                        mp!!.setDataSource(song.singUrl)
                        mp!!.prepare()
                        mp!!.start()
                        view.bu_play.text = "Stop"
                        sb_progress.max = mp!!.duration
                    } catch (ex: Exception) {

                    }
                }
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return  listSong[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listSong.size
        }

    }

    inner class mySongTrack: Thread(){

        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (ex: Exception) {

                }
                runOnUiThread {
                    if (mp != null) {
                        sb_progress.progress = mp!!.currentPosition
                    }
                }
            }

        }
    }

    fun CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE_ASK_PERMISSIONS)
                return
            }
        }

        LoadSong()

    }

    private fun LoadSong() {
        val allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        var cursor= contentResolver.query(allSongsUri, null, selection, null, null)
        if (cursor !=null){
            if (cursor.moveToNext()) {
                do {
                    val songURL = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    list.add(SongInfo(songName, songAuthor, songURL))
                } while (cursor.moveToNext())
            }
            cursor.close()
        }

    }

    //get acces to location permsion
    private val REQUEST_CODE_ASK_PERMISSIONS = 123
}
