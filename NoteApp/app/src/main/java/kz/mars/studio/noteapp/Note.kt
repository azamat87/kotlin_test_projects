package kz.mars.studio.noteapp

/**
 * Created by Asus on 03.02.2018.
 */
class Note {

    var nodeID: Int? = null
    var nodeName: String? = null
    var nodeDes: String? = null

    constructor(nodeID: Int, nodeName: String, nodeDes: String){
        this.nodeID = nodeID
        this.nodeName = nodeName
        this.nodeDes = nodeDes
    }


}