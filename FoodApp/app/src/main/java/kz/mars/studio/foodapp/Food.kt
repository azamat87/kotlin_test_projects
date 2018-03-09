package kz.mars.studio.foodapp

/**
 * Created by Asus on 30.01.2018.
 */
class Food {

    var name: String? = null
    var des: String? = null
    var image: Int? = null

    constructor(name: String, des: String, image: Int){
        this.name = name
        this.des = des
        this.image = image
    }
}