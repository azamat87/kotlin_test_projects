package kz.mars.studio.twitterdemo

/**
 * Created by Asus on 26.02.2018.
 */
class PostInfo {

    var userUID: String? = null
    var text: String? =null
    var postImage: String? = null

    constructor(userUID: String, text: String,postImage: String){
        this.userUID = userUID
        this.text = text
        this.postImage = postImage
    }

}