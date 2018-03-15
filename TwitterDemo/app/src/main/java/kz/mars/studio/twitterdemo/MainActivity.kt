package kz.mars.studio.twitterdemo

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_ticket.view.*
import kotlinx.android.synthetic.main.ads_ticket.view.*
import kotlinx.android.synthetic.main.tweets_ticket.view.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val dataBase = FirebaseDatabase.getInstance()
    private var myRef = dataBase.reference

    private var listTweets = ArrayList<Ticket>()
    var adapter: MyTweetAdapter? = null

    private var myEmail: String ?= null
    private var myUID:String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val b: Bundle = intent.extras
        myEmail = b.getString("email")
        myUID = b.getString("uid")

        listTweets.add(Ticket("0","","","add"))
        loadPosts()

        adapter = MyTweetAdapter(this, listTweets)
        lv_tweets.adapter = adapter
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
                    myRef.child("posts").push().setValue(PostInfo(myUID!!, view.ed.text.toString(), downloadUrl!!))

                }

                return view
            } else if(tweet.tweetPersonUID.equals("loading")){
                var view = layoutInflater.inflate(R.layout.loading_ticket, null)
                return view
            } else if(tweet.tweetPersonUID.equals("ads")){
                var view = layoutInflater.inflate(R.layout.ads_ticket, null)
                val mAdView = view.adView
                val adRequest = AdRequest.Builder().build()
                mAdView.loadAd(adRequest)


                return view
            } else {
                var view = layoutInflater.inflate(R.layout.tweets_ticket, null)

                view.txt_tweet.text = tweet.tweetText

                Picasso.with(ctx).load(tweet.tweetImageURL).into(view.tweet_picture)

                myRef.child("Users").child(tweet.tweetPersonUID)
                        .addValueEventListener(object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError?) {

                            }

                            override fun onDataChange(p0: DataSnapshot?) {
                                try {
                                    var td = p0!!.value as HashMap<String, Any>
                                    for (key in td.keys) {
                                        var userInfo = td[key] as String
                                        if (userInfo.equals("ProfileImage")) {
                                            Picasso.with(ctx).load(userInfo).into(view.picture_path)
                                        } else {
                                            view.txtUserName.text = userInfo
                                        }
                                    }
                                } catch (ex: Exception) {
                                }
                            }
                        })

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
        var intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
        val storageRef = storage.getReferenceFromUrl("gs://twitterdemo-e7270.appspot.com")
        val df = SimpleDateFormat("ddMMyyHHmmss")
        val dateObj = Date()
        val imagePath = splitString(myEmail!!)+"." + df.format(dateObj)+".jpg"

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

    private fun splitString(str: String):String {
        var split = str.split("@")
        return split[0]
    }

    fun loadPosts() {
        myRef.child("posts")
                .addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        try {
                            listTweets.clear()
                            listTweets.add(Ticket("0","","","add"))
                            listTweets.add(Ticket("0","","","ads"))
                            var td = p0!!.value as HashMap<String, Any>
                            for (key in td.keys) {
                                var post = td[key] as HashMap<String, Any>

                                listTweets.add(Ticket(key,
                                        post["text"] as String,
                                        post["postImage"] as String,
                                        post["userUID"] as String))
                            }

                            adapter?.notifyDataSetChanged()
                        } catch (ex: Exception) {

                        }
                    }
                })
    }
}
