package kz.mars.studio.mediaplayer

/**
 * Created by Asus on 03.03.2018.
 */
class SongInfo {

    var title: String ?=null
    var authorName: String? = null
    var singUrl: String? = null

    constructor(title: String, authorName: String, singUrl: String){
        this.title = title
        this.authorName = authorName
        this.singUrl = singUrl
    }
}