package com.mobdeve.s13.lim.pacheco.tan.tarana
import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

object DBHelper {

    // this function will be used to add a user to the database
    fun addUser(user: User) {
        val dbuser = hashMapOf(
            User.NAME_KEY to user.name,
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
    suspend fun getUser(username: String): User {
        val db = Firebase.firestore
        // get the user from the database
        val result = db.collection("users")
            .whereEqualTo(User.USERNAME_KEY, username)
            .get()
            .await()
        // return the user
        return User(
            result.documents[0].get(User.NAME_KEY).toString(),
            result.documents[0].get(User.USERNAME_KEY).toString(),
            result.documents[0].get(User.PASSWORD_KEY).toString(),
            result.documents[0].get(User.PHONE_NUMBER_KEY).toString(),
            result.documents[0].get(User.PROFILE_PICTURE_KEY).toString().toInt(),
            result.documents[0].get(User.FRIENDS_LIST_KEY) as ArrayList<User>,
            result.documents[0].get(User.LAKWATSA_LIST_KEY) as ArrayList<Lakwatsa>,
            result.documents[0].get(User.FRIEND_REQUESTS_SENT_KEY) as ArrayList<User>,
            result.documents[0].get(User.FRIEND_REQUESTS_RECEIVED_KEY) as ArrayList<User>
        )
    }

    // this function will be used to update a user in the database
    fun updateUser(user: User) {
        val dbuser = hashMapOf(
            User.NAME_KEY to user.name,
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

    //check if username already exists or phone number already exists
    suspend fun checkIfUserExists(username: String, number: String):Int{
        val db = Firebase.firestore
        //dont create toasts here, just return a boolean value 1 if user exists, 2 if phone number exists, 0 if both do not exist

        //could put this in a try catch block
        val resultuser = db.collection("users")
            .whereEqualTo(User.USERNAME_KEY, username)
            .get()
            .await()

        val resultnumber=db.collection("users")
            .whereEqualTo(User.PHONE_NUMBER_KEY, number)
            .get()
            .await()

        if(resultuser.isEmpty && resultnumber.isEmpty()){
            //user does not exist
            //phone number does not exist
            //create user
            return 0
        }
        else if(resultuser.isEmpty && !resultnumber.isEmpty()){
            //user does not exist
            //phone number exists
            return 2
        }
        else if(!resultuser.isEmpty && resultnumber.isEmpty()){
            //user exists
            //phone number does not exist
            return 1
        }
        else{
            //user exists
            //phone number exists
            return 3
        }
    }

    //upload image to firebase storage

}
