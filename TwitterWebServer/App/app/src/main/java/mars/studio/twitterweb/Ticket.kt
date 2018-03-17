package mars.studio.twitterweb

/**
 * Created by Asus on 15.03.2018.
 */
class Ticket {

    var tweetID: String ?= null
    var tweetText: String ?= null
    var tweetImageURL: String ?= null
    var personName: String ?= null
    var personImage: String ?= null
    var tweetDate: String ?= null
    var personId: String ? = null
    constructor(tweetID: String,tweetText: String,tweetImageURL: String,
                tweetDate: String, personName: String,personImage: String,personId: String){
        this.tweetID = tweetID
        this.tweetText = tweetText
        this.tweetImageURL = tweetImageURL
        this.personName = personName
        this.personImage = personImage
        this.tweetDate = tweetDate
        this.personId = personId
    }

}