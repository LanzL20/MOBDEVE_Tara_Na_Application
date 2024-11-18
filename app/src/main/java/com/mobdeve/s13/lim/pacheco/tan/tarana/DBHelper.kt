package com.mobdeve.s13.lim.pacheco.tan.tarana
import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

object DBHelper {

    // this function will be used to add a user to the database
    fun addUser(user: User) {
        val dbuser = hashMapOf(
            User.USERNAME_KEY to user.username,
            User.PASSWORD_KEY to user.password,
            User.PROFILE_PICTURE_KEY to user.profilePicture,
            User.PHONE_NUMBER_KEY to user.phoneNumber,
            User.FRIENDS_LIST_KEY to user.friendsList,
            User.LAKWATSA_LIST_KEY to user.lakwatsaList,
            User.FRIEND_REQUESTS_SENT_KEY to user.friendRequestsSent,
            User.FRIEND_REQUESTS_RECEIVED_KEY to user.friendRequestsReceived
        )
        val db = Firebase.firestore
        // add the user to the database
        db.collection("users")
            .add(dbuser)
            .addOnSuccessListener { documentReference ->
                Log.d("MainActivity", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("MainActivity", "Error adding document", e)
            }
    }

    // this function will be used to get a user from the database
    fun getUser(username: String) {
        val db = Firebase.firestore
        // get the user from the database
        db.collection("users")
            .whereEqualTo(User.USERNAME_KEY, username)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("MainActivity", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents.", exception)
            }
    }

    // this function will be used to update a user in the database
    fun updateUser(user: User) {
        val dbuser = hashMapOf(
            User.USERNAME_KEY to user.username,
            User.PASSWORD_KEY to user.password,
            User.PROFILE_PICTURE_KEY to user.profilePicture,
            User.PHONE_NUMBER_KEY to user.phoneNumber,
            User.FRIENDS_LIST_KEY to user.friendsList,
            User.LAKWATSA_LIST_KEY to user.lakwatsaList,
            User.FRIEND_REQUESTS_SENT_KEY to user.friendRequestsSent,
            User.FRIEND_REQUESTS_RECEIVED_KEY to user.friendRequestsReceived
        )
        val db = Firebase.firestore
        // update the user in the database
        db.collection("users")
            .document(user.username)
            .set(dbuser)
            .addOnSuccessListener { Log.d("MainActivity", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("MainActivity", "Error updating document", e) }
    }

    // this function will be used to delete a user from the database
    fun deleteUser(username: String) {
        val db = Firebase.firestore
        // delete the user from the database
        db.collection("users")
            .document(username)
            .delete()
            .addOnSuccessListener { Log.d("MainActivity", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("MainActivity", "Error deleting document", e) }
    }

    //upload image to firebase storage


}
