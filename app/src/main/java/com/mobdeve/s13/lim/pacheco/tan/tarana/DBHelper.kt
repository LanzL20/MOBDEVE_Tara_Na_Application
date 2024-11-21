package com.mobdeve.s13.lim.pacheco.tan.tarana
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.Date

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
            User.FRIEND_REQUESTS_RECEIVED_KEY to user.friendRequestsReceived,
            User.UNAVAILABLE_LIST_KEY to user.unavailableList
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
            result.documents[0].get(User.FRIEND_REQUESTS_RECEIVED_KEY) as ArrayList<User>,
            result.documents[0].get(User.UNAVAILABLE_LIST_KEY) as ArrayList<Unavailable>
        )
    }

    suspend fun getUserFromNumber(number: String): User {
        val db = Firebase.firestore
        // get the user from the database
        val result = db.collection("users")
            .whereEqualTo(User.PHONE_NUMBER_KEY, number)
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
            result.documents[0].get(User.FRIEND_REQUESTS_RECEIVED_KEY) as ArrayList<User>,
            result.documents[0].get(User.UNAVAILABLE_LIST_KEY) as ArrayList<Unavailable>
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
            User.FRIEND_REQUESTS_RECEIVED_KEY to user.friendRequestsReceived,
            User.UNAVAILABLE_LIST_KEY to user.unavailableList
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

    fun addLakwatsa(lakwatsa: Lakwatsa) {
        val dblakwatsa = hashMapOf(
            Lakwatsa.USERS_KEY to lakwatsa.lakwatsaUsers,
            Lakwatsa.LOCATION_KEY to lakwatsa.location,
            Lakwatsa.TITLE_KEY to lakwatsa.lakwatsaTitle,
            Lakwatsa.DATE_KEY to lakwatsa.date,
            Lakwatsa.POLLING_LIST_KEY to lakwatsa.pollingList,
            Lakwatsa.ALBUM_KEY to lakwatsa.album,
            Lakwatsa.STATUS_KEY to lakwatsa.status
        )
        val db = Firebase.firestore

        db.collection("lakwatsas")
            .add(dblakwatsa)
            .addOnSuccessListener { documentReference ->
                Log.d("MainActivity", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("MainActivity", "Error adding document", e)
            }
    }

    suspend fun getAllLakwatsa(): ArrayList<Lakwatsa>{
        val db = Firebase.firestore
        val result = db.collection("lakwatsas")
            .get()
            .await()
        val lakwatsaList = ArrayList<Lakwatsa>()
        for (document in result){
            lakwatsaList.add(Lakwatsa(
                document.id,
                document.get(Lakwatsa.USERS_KEY) as ArrayList<User>,
                document.get(Lakwatsa.LOCATION_KEY).toString(),
                document.get(Lakwatsa.TITLE_KEY).toString(),
                (document.get(Lakwatsa.DATE_KEY) as Timestamp).toDate(),
                document.get(Lakwatsa.POLLING_LIST_KEY) as HashMap<Date, Int>,
                document.get(Lakwatsa.ALBUM_KEY) as ArrayList<String>
            ))
        }
        return lakwatsaList
    }

    suspend fun getLakwatsa(lakwatsaId: String): Lakwatsa{
        val db = Firebase.firestore
        val result = db.collection("lakwatsas")
            .whereEqualTo(FieldPath.documentId(), lakwatsaId)
            .get()
            .await()
        return Lakwatsa(
            result.documents[0].id,
            result.documents[0].get(Lakwatsa.USERS_KEY) as ArrayList<User>,
            result.documents[0].get(Lakwatsa.LOCATION_KEY).toString(),
            result.documents[0].get(Lakwatsa.TITLE_KEY).toString(),
            (result.documents[0].get(Lakwatsa.DATE_KEY) as Timestamp).toDate(),
            result.documents[0].get(Lakwatsa.POLLING_LIST_KEY) as HashMap<Date, Int>,
            result.documents[0].get(Lakwatsa.ALBUM_KEY) as ArrayList<String>
        )
    }

    fun updateLakwatsa(lakwatsa: Lakwatsa) {
        val dblakwatsa = hashMapOf(
            Lakwatsa.USERS_KEY to lakwatsa.lakwatsaUsers,
            Lakwatsa.LOCATION_KEY to lakwatsa.location,
            Lakwatsa.TITLE_KEY to lakwatsa.lakwatsaTitle,
            Lakwatsa.DATE_KEY to lakwatsa.date,
            Lakwatsa.POLLING_LIST_KEY to lakwatsa.pollingList,
            Lakwatsa.ALBUM_KEY to lakwatsa.album,
            Lakwatsa.STATUS_KEY to lakwatsa.status
        )
        val db = Firebase.firestore
        db.collection("lakwatsas")
            .document(lakwatsa.lakwatsaId)
            .set(dblakwatsa)
            .addOnSuccessListener { Log.d("MainActivity", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("MainActivity", "Error updating document", e) }
    }

}
