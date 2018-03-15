package mars.studio.twitterweb

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_ticket.*
import kotlinx.android.synthetic.main.add_ticket.view.*
import kotlinx.android.synthetic.main.tweet_ticket.view.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    private var listTweets = ArrayList<Ticket>()
    var adapter: MyTweetAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveSettings = SaveSettings(this)
        saveSettings.loadSettings()


        adapter = MyTweetAdapter(this, listTweets)
        lv_tweets.adapter = adapter

        listTweets.clear()
        listTweets.add(Ticket("0","","","add"))
        adapter!!.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if (menu != null) {
            val sv: SearchView = menu.findItem(R.id.app_bar_search).actionView as SearchView

            val sm= getSystemService(Context.SEARCH_SERVICE) as SearchManager
            sv.setSearchableInfo(sm.getSearchableInfo(componentName))


            sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
//                    LoadQuery("%"+query+"%")
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

                }
                R.id.app_bar_search -> {

                }
            }
        }


        return super.onOptionsItemSelected(item)
    }

    // http
    inner class MyAsyncTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: String?): String {
            try {
                Log.e("myLog", " doInBackground ")
                var url = URL(params[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 7000
                Log.e("myLog", " urlConnect " + urlConnect.responseMessage)
                var inString = convertStremToString(urlConnect.inputStream)
                publishProgress(inString)
            } catch (ex: Exception) {
                Log.e("myLog", " Exception ")
                ex.printStackTrace()
            }
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val json = JSONObject(values[0])

                Toast.makeText(applicationContext, json.getString("msg"), Toast.LENGTH_SHORT).show()
                if (json.getString("msg") == "tweet is added") {
                    downloadUrl = ""
                    listTweets.removeAt(0)
                    adapter!!.notifyDataSetChanged()
                }
            } catch (ex: Exception) {

            }
        }

    }

    inner class MyTweetAdapter : BaseAdapter {

        var listTweet = ArrayList<Ticket>()
        var ctx: Context? = null

        constructor(context: Context, listTweet: ArrayList<Ticket>) : super() {
            this.listTweet = listTweet
            this.ctx =context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var tweet = listTweet[position]

            if (tweet.tweetPersonUID.equals("add")) {
                var view = layoutInflater.inflate(R.layout.add_ticket, null)

                view.iv_attach.setOnClickListener {
                    loadImage()
                }
                view.iv_post.setOnClickListener {
                    listTweets.add(0, Ticket("0", "him", "url", "loading"))
                    adapter!!.notifyDataSetChanged()

                    val url = "http://localhost/TwitterWebServer/TweetAdd.php?user_id="+SaveSettings.userID+"&tweet_text="+ed_text_post.text.toString()+"&tweet_picture=" + downloadUrl
                    MyAsyncTask().execute(url)
                    ed_text_post.setText("")
                }

                return view
            } else if(tweet.tweetPersonUID.equals("loading")){
                var view = layoutInflater.inflate(R.layout.loading_ticket, null)
                return view
            } else {
                var view = layoutInflater.inflate(R.layout.tweet_ticket, null)

                view.txt_tweet.text = tweet.tweetText

                Picasso.with(ctx).load(tweet.tweetImageURL).into(view.tweet_picture)

//                Picasso.with(ctx).load(userInfo).into(view.picture_path)
//                view.txtUserName.text = userInfo

                return view
            }
        }

        override fun getItem(position: Int): Any {
            return listTweet[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listTweet.size
        }

    }

    val PICK_IMAGE_CODE = 123
    private fun loadImage() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColum = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage, filePathColum, null, null, null)
            cursor.moveToFirst()
            val colummIndex = cursor.getColumnIndex(filePathColum[0])
            val picturePath = cursor.getString(colummIndex)
            cursor.close()
            upLoadImage(BitmapFactory.decodeFile(picturePath))
        }
    }

    var downloadUrl:String ?= null

    fun upLoadImage(bitmap: Bitmap) {
        listTweets.add(Ticket("0","","","loading"))
        adapter!!.notifyDataSetChanged()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://twitterweb-dbcb6.appspot.com")
        val df = SimpleDateFormat("ddMMyyHHmmss")
        val dateObj = Date()
        val imagePath = SaveSettings.userID + "." + df.format(dateObj) + ".jpg"
        val imageRef = storageRef.child("imagePosts/" + imagePath)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(this, "fail upload ", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            downloadUrl = taskSnapshot.downloadUrl.toString()
            listTweets.removeAt(0)
            adapter!!.notifyDataSetChanged()
        }
    }


}
