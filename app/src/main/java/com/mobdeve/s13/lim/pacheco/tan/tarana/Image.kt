package com.mobdeve.s13.lim.pacheco.tan.tarana

class Image {

    var imageId: Int
        private set
    //var imageDescription: String
       // private set
    //var imageDate: String
       // private set
   //var user: User
        //private set
    var imageUri: String = ""
        private set

    /**
     * Constructor for the Image class
     * @param imageId the id of the image
     * @param imageDescription the description of the image
     * @param imageDate the date the image was uploaded
     * @param user the user who uploaded the image
     */
    //constructor(imageId: Int, imageDescription: String, imageDate: String, user: User, imageUri: String) {
    constructor(imageId: Int, imageUri: String) {
        this.imageId = imageId
        this.imageUri = imageUri
    }

}