package com.mobdeve.s13.lim.pacheco.tan.tarana
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.util.Date
import com.google.type.LatLng
import java.sql.Time

object DBHelper {

    var userListenerActive = false

    fun activateUserListener(){
        if(!userListenerActive){
            userListenerActive = true
            val db = Firebase.firestore
            db.collection("users")
                .document(UserSession.getUser().uid)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w("MainActivity", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        val tempUnavailable = ArrayList<Unavailable>()

                        val unavailableList = value.get(User.UNAVAILABLE_LIST_KEY) as? ArrayList<HashMap<String, Any>>
                        unavailableList?.forEach { unavailable ->
                            val name = unavailable["name"] as? String ?: "Unknown"
                            val startDate = unavailable["startDate"] as? String ?: ""
                            val endDate = unavailable["endDate"] as? String ?: ""
                            tempUnavailable.add(Unavailable(name, startDate, endDate))
                        }

                        val tempNotificationList = ArrayList<Notification>()
                        for(notification in value.get(User.NOTIFICATION_LIST_KEY) as ArrayList<HashMap<String, Any>>){
                            var tempdate=notification["date"] as Timestamp
                            var date=tempdate.toDate()
                            tempNotificationList.add(Notification(
                                notification["message"] as String,
                                date,
                                notification["read"] as Boolean,
                                notification["sender"] as String,
                                notification["receiver"] as String,
                                notification["type"] as Long,
                                notification["image"] as Long
                            ))
                        }

                        UserSession.setUser(
                            User(
                                value.get(User.NAME_KEY).toString(),
                                value.get(User.USERNAME_KEY).toString(),
                                value.get(User.PASSWORD_KEY).toString(),
                                value.get(User.PHONE_NUMBER_KEY).toString(),
                                value.get(User.PROFILE_PICTURE_KEY).toString().toInt(),
                                value.get(User.FRIENDS_LIST_KEY) as ArrayList<String>,
                                value.get(User.LAKWATSA_LIST_KEY) as ArrayList<String>,
                                value.get(User.FRIEND_REQUESTS_SENT_KEY) as ArrayList<String>,
                                value.get(User.FRIEND_REQUESTS_RECEIVED_KEY) as ArrayList<String>,
                                tempUnavailable,
                                value.id,
                                value.get(User.LATITUDE_KEY).toString()?.toDoubleOrNull() ?: 0.0,
                                value.get(User.LONGITUDE_KEY).toString()?.toDoubleOrNull() ?: 0.0,
                                value.get(User.SALT_KEY).toString(),
                                tempNotificationList
                            )
                        )
                    }
                }
        }
    }

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
            User.UNAVAILABLE_LIST_KEY to user.unavailableList,
            User.UID_KEY to user.uid,
            User.LATITUDE_KEY to user.latitude,
            User.LONGITUDE_KEY to user.longitude,
            User.SALT_KEY to user.salt,
            User.NOTIFICATION_LIST_KEY to user.notificationList
        )
        val db = Firebase.firestore
        // add the user to the database
        db.collection("users")
            .document(user.username)
            .set(dbuser)
            .addOnSuccessListener { documentReference ->
                Log.d("MainActivity", "DocumentSnapshot added with ID: ${documentReference}")
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
        val tempUnavailable = ArrayList<Unavailable>()
        for(unavailable in result.documents[0].get(User.UNAVAILABLE_LIST_KEY) as ArrayList<HashMap<String, Any>>){
            tempUnavailable.add(Unavailable(
                unavailable["name"] as String,
                unavailable["startDate"] as String,
                unavailable["endDate"] as String
            ))
        }

        val tempNotificationList = ArrayList<Notification>()
        for(notification in result.documents[0].get(User.NOTIFICATION_LIST_KEY) as ArrayList<HashMap<String, Any>>){
            var tempdate=notification["date"] as Timestamp
            var date=tempdate.toDate()
            tempNotificationList.add(Notification(
                notification["message"] as String,
                date,
                notification["read"] as Boolean,
                notification["sender"] as String,
                notification["receiver"] as String,
                notification["type"] as Long,
                notification["image"] as Long
            ))
        }
        return User(
            result.documents[0].get(User.NAME_KEY).toString(),
            result.documents[0].get(User.USERNAME_KEY).toString(),
            result.documents[0].get(User.PASSWORD_KEY).toString(),
            result.documents[0].get(User.PHONE_NUMBER_KEY).toString(),
            result.documents[0].get(User.PROFILE_PICTURE_KEY).toString().toInt(),
            result.documents[0].get(User.FRIENDS_LIST_KEY) as ArrayList<String>,
            result.documents[0].get(User.LAKWATSA_LIST_KEY) as ArrayList<String>,
            result.documents[0].get(User.FRIEND_REQUESTS_SENT_KEY) as ArrayList<String>,
            result.documents[0].get(User.FRIEND_REQUESTS_RECEIVED_KEY) as ArrayList<String>,
            tempUnavailable,
            result.documents[0].id,
            result.documents[0].get(User.LATITUDE_KEY).toString().toDouble(),
            result.documents[0].get(User.LONGITUDE_KEY).toString().toDouble(),
            result.documents[0].get(User.SALT_KEY).toString(),
            tempNotificationList
        )
    }

    suspend fun getUserbyUid(uid:String): User {
        val db = Firebase.firestore
        // get the user from the database
        val result = db.collection("users")
            .whereEqualTo(User.UID_KEY, uid)
            .get()
            .await()
        // return the user
        val tempUnavailable = ArrayList<Unavailable>()
        for(unavailable in result.documents[0].get(User.UNAVAILABLE_LIST_KEY) as ArrayList<HashMap<String, Any>>){
            tempUnavailable.add(Unavailable(
                unavailable["name"] as String,
                unavailable["startDate"] as String,
                unavailable["endDate"] as String
            ))
        }

        val tempNotificationList = ArrayList<Notification>()
        for(notification in result.documents[0].get(User.NOTIFICATION_LIST_KEY) as ArrayList<HashMap<String, Any>>){
            var tempdate=notification["date"] as Timestamp
            var date=tempdate.toDate()
            tempNotificationList.add(Notification(
                notification["message"] as String,
                date,
                notification["read"] as Boolean,
                notification["sender"] as String,
                notification["receiver"] as String,
                notification["type"] as Long,
                notification["image"] as Long
            ))
        }
        return User(
            result.documents[0].get(User.NAME_KEY).toString(),
            result.documents[0].get(User.USERNAME_KEY).toString(),
            result.documents[0].get(User.PASSWORD_KEY).toString(),
            result.documents[0].get(User.PHONE_NUMBER_KEY).toString(),
            result.documents[0].get(User.PROFILE_PICTURE_KEY).toString().toInt(),
            result.documents[0].get(User.FRIENDS_LIST_KEY) as ArrayList<String>,
            result.documents[0].get(User.LAKWATSA_LIST_KEY) as ArrayList<String>,
            result.documents[0].get(User.FRIEND_REQUESTS_SENT_KEY) as ArrayList<String>,
            result.documents[0].get(User.FRIEND_REQUESTS_RECEIVED_KEY) as ArrayList<String>,
            tempUnavailable,
            result.documents[0].id,
            result.documents[0].get(User.LATITUDE_KEY).toString().toDouble(),
            result.documents[0].get(User.LONGITUDE_KEY).toString().toDouble(),
            result.documents[0].get(User.SALT_KEY).toString(),
            tempNotificationList
        )
    }

    suspend fun getUsersByUsernameOrPhoneNumber(search: String): ArrayList<User>{
        val db = Firebase.firestore
        val users = ArrayList<User>()
        if(search.isEmpty()){
            return users
        }
        val result = db.collection("users")
            .where(Filter.or(
                Filter.and(
                    Filter.greaterThanOrEqualTo(User.USERNAME_KEY, search),
                    Filter.lessThanOrEqualTo(User.USERNAME_KEY, search+ '\uf8ff')
                ),
                Filter.and(
                    Filter.greaterThanOrEqualTo(User.PHONE_NUMBER_KEY, "+63" + search.substring(1)),
                    Filter.lessThanOrEqualTo(User.PHONE_NUMBER_KEY, "+63" + search.substring(1) + '\uf8ff')
                )
            ))
            .get()
            .await()
        for(document in result.documents){
            val tempUnavailable = ArrayList<Unavailable>()
            for(unavailable in document.get(User.UNAVAILABLE_LIST_KEY) as ArrayList<HashMap<String, Any>>){
                tempUnavailable.add(Unavailable(
                    unavailable["name"] as String,
                    unavailable["startDate"] as String,
                    unavailable["endDate"] as String
                ))
            }

            val tempNotificationList = ArrayList<Notification>()
            for(notification in document.get(User.NOTIFICATION_LIST_KEY) as ArrayList<HashMap<String, Any>>){
                var tempdate=notification["date"] as Timestamp
                var date=tempdate.toDate()
                tempNotificationList.add(Notification(
                    notification["message"] as String,
                    date,
                    notification["read"] as Boolean,
                    notification["sender"] as String,
                    notification["receiver"] as String,
                    notification["type"] as Long,
                    notification["image"] as Long
                ))
            }
            users.add(User(
                document.get(User.NAME_KEY).toString(),
                document.get(User.USERNAME_KEY).toString(),
                document.get(User.PASSWORD_KEY).toString(),
                document.get(User.PHONE_NUMBER_KEY).toString(),
                document.get(User.PROFILE_PICTURE_KEY).toString().toInt(),
                document.get(User.FRIENDS_LIST_KEY) as ArrayList<String>,
                document.get(User.LAKWATSA_LIST_KEY) as ArrayList<String>,
                document.get(User.FRIEND_REQUESTS_SENT_KEY) as ArrayList<String>,
                document.get(User.FRIEND_REQUESTS_RECEIVED_KEY) as ArrayList<String>,
                tempUnavailable,
                document.id,
                document.get(User.LATITUDE_KEY).toString().toDouble(),
                document.get(User.LONGITUDE_KEY).toString().toDouble(),
                document.get(User.SALT_KEY).toString(),
                tempNotificationList
            ))
        }
        return users
    }

    suspend fun getUserFromNumber(number: String): User? {
        val db = Firebase.firestore
        // get the user from the database
        val result = db.collection("users")
            .whereEqualTo(User.PHONE_NUMBER_KEY, number)
            .get()
            .await()

        if(result.isEmpty()){
            return null
        }

        // return the user
        val document = result.documents[0]
        val tempUnavailable = ArrayList<Unavailable>()
        val unavailableList = document.get(User.UNAVAILABLE_LIST_KEY) as? ArrayList<HashMap<String, Any>>
        unavailableList?.forEach { unavailable ->
            tempUnavailable.add(
                Unavailable(
                    unavailable["name"] as String,
                    unavailable["startDate"] as String,
                    unavailable["endDate"] as String
                )
            )
        }

        val tempNotificationList = ArrayList<Notification>()
        for(notification in document.get(User.NOTIFICATION_LIST_KEY) as ArrayList<HashMap<String, Any>>){
            var tempdate=notification["date"] as Timestamp
            var date=tempdate.toDate()
            tempNotificationList.add(Notification(
                notification["message"] as String,
                date,
                notification["read"] as Boolean,
                notification["sender"] as String,
                notification["receiver"] as String,
                notification["type"] as Long,
                notification["image"] as Long
            ))
        }


        val latitude = document.get(User.LATITUDE_KEY)?.toString()?.toDoubleOrNull() ?: 0.0
        val longitude = document.get(User.LONGITUDE_KEY)?.toString()?.toDoubleOrNull() ?: 0.0

        return User(
            document.get(User.NAME_KEY).toString(),
            document.get(User.USERNAME_KEY).toString(),
            document.get(User.PASSWORD_KEY).toString(),
            document.get(User.PHONE_NUMBER_KEY).toString(),
            document.get(User.PROFILE_PICTURE_KEY).toString().toInt(),
            document.get(User.FRIENDS_LIST_KEY) as ArrayList<String>,
            document.get(User.LAKWATSA_LIST_KEY) as ArrayList<String>,
            document.get(User.FRIEND_REQUESTS_SENT_KEY) as ArrayList<String>,
            document.get(User.FRIEND_REQUESTS_RECEIVED_KEY) as ArrayList<String>,
            tempUnavailable,
            document.id,
            latitude,
            longitude,
            document.get(User.SALT_KEY).toString(),
            tempNotificationList
        )
    }

    suspend fun getUsers(ids: ArrayList<String>):ArrayList<User>{
        val db = Firebase.firestore

        if(ids.isEmpty()){
            return ArrayList<User>()
        }

        val result = db.collection("users")
            .whereIn(User.USERNAME_KEY, ids)
            .get()
            .await()

        val users = ArrayList<User>()
        for(document in result.documents){
            val tempUnavailable = ArrayList<Unavailable>()
            for(unavailable in document.get(User.UNAVAILABLE_LIST_KEY) as ArrayList<HashMap<String, Any>>){
                tempUnavailable.add(Unavailable(
                    unavailable["name"] as String,
                    unavailable["startDate"] as String,
                    unavailable["endDate"] as String
                ))
            }

            val tempNotificationList = ArrayList<Notification>()
            for(notification in document.get(User.NOTIFICATION_LIST_KEY) as ArrayList<HashMap<String, Any>>){
                var tempdate=notification["date"] as Timestamp
                var date=tempdate.toDate()
                tempNotificationList.add(Notification(
                    notification["message"] as String,
                    date,
                    notification["read"] as Boolean,
                    notification["sender"] as String,
                    notification["receiver"] as String,
                    notification["type"] as Long,
                    notification["image"] as Long
                ))
            }
            users.add(User(
                document.get(User.NAME_KEY).toString(),
                document.get(User.USERNAME_KEY).toString(),
                document.get(User.PASSWORD_KEY).toString(),
                document.get(User.PHONE_NUMBER_KEY).toString(),
                document.get(User.PROFILE_PICTURE_KEY).toString().toInt(),
                document.get(User.FRIENDS_LIST_KEY) as ArrayList<String>,
                document.get(User.LAKWATSA_LIST_KEY) as ArrayList<String>,
                document.get(User.FRIEND_REQUESTS_SENT_KEY) as ArrayList<String>,
                document.get(User.FRIEND_REQUESTS_RECEIVED_KEY) as ArrayList<String>,
                tempUnavailable,
                document.id,
                document.get(User.LATITUDE_KEY).toString().toDouble(),
                document.get(User.LONGITUDE_KEY).toString().toDouble(),
                document.get(User.SALT_KEY).toString(),
                tempNotificationList
            ))
        }
        return users
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
            User.UNAVAILABLE_LIST_KEY to user.unavailableList,
            User.UID_KEY to user.uid,
            User.LATITUDE_KEY to user.latitude,
            User.LONGITUDE_KEY to user.longitude,
            User.SALT_KEY to user.salt,
            User.NOTIFICATION_LIST_KEY to user.notificationList
        )
        val db = Firebase.firestore
        // update the user in the database
        db.collection("users")
            .document(user.uid)
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

    suspend fun addLakwatsa(lakwatsa: Lakwatsa): String {
        val dblakwatsa = hashMapOf(
            Lakwatsa.USERS_KEY to lakwatsa.lakwatsaUsers,
            Lakwatsa.LATITUDE_KEY to lakwatsa.locationLatitude,
            Lakwatsa.LONGITUDE_KEY to lakwatsa.locationLongitude,
            Lakwatsa.TITLE_KEY to lakwatsa.lakwatsaTitle,
            Lakwatsa.DATE_KEY to lakwatsa.date,
            Lakwatsa.POLLING_LIST_KEY to lakwatsa.pollingList,
            Lakwatsa.ALBUM_KEY to lakwatsa.album,
            Lakwatsa.STATUS_KEY to lakwatsa.status,
            Lakwatsa.TIME_KEY to lakwatsa.time,
            Lakwatsa.ADMIN_KEY to lakwatsa.lakwatsaAdmin,
            Lakwatsa.LOCATION_NAME_KEY to lakwatsa.locationName
        )
        val db = Firebase.firestore

        val result = db.collection("lakwatsas")
            .add(dblakwatsa).await()

        return result.id
    }

    suspend fun getAllLakwatsaFromList(lakwatsaIds: ArrayList<String>): ArrayList<Lakwatsa>{
        val db = Firebase.firestore
        val lakwatsas = ArrayList<Lakwatsa>()
        for(lakwatsaId in lakwatsaIds){
            val result = db.collection("lakwatsas")
                .whereEqualTo(FieldPath.documentId(), lakwatsaId)
                .get()
                .await()
            lakwatsas.add(Lakwatsa(
                result.documents[0].id,
                result.documents[0].get(Lakwatsa.USERS_KEY) as ArrayList<String>,
                result.documents[0].get(Lakwatsa.LATITUDE_KEY).toString().toDouble(),
                result.documents[0].get(Lakwatsa.LONGITUDE_KEY).toString().toDouble(),
                result.documents[0].get(Lakwatsa.TITLE_KEY).toString(),
                result.documents[0].get(Lakwatsa.DATE_KEY).toString(),
                result.documents[0].get(Lakwatsa.TIME_KEY).toString(),
                result.documents[0].get(Lakwatsa.POLLING_LIST_KEY) as HashMap<String, ArrayList<String>>,
                result.documents[0].get(Lakwatsa.ALBUM_KEY) as ArrayList<String>,
                result.documents[0].get(Lakwatsa.STATUS_KEY).toString().toInt(),
                result.documents[0].get(Lakwatsa.ADMIN_KEY).toString(),
                result.documents[0].get(Lakwatsa.LOCATION_NAME_KEY).toString()
            ))
        }
        return lakwatsas
    }

    suspend fun getLakwatsa(lakwatsaId: String): Lakwatsa{
        val db = Firebase.firestore
        val result = db.collection("lakwatsas")
            .whereEqualTo(FieldPath.documentId(), lakwatsaId)
            .get()
            .await()
        return Lakwatsa(
            result.documents[0].id,
            result.documents[0].get(Lakwatsa.USERS_KEY) as ArrayList<String>,
            result.documents[0].get(Lakwatsa.LATITUDE_KEY).toString().toDouble(),
            result.documents[0].get(Lakwatsa.LONGITUDE_KEY).toString().toDouble(),
            result.documents[0].get(Lakwatsa.TITLE_KEY).toString(),
            result.documents[0].get(Lakwatsa.DATE_KEY).toString(),
            result.documents[0].get(Lakwatsa.TIME_KEY).toString(),
            result.documents[0].get(Lakwatsa.POLLING_LIST_KEY) as HashMap<String, ArrayList<String>>,
            result.documents[0].get(Lakwatsa.ALBUM_KEY) as ArrayList<String>,
            result.documents[0].get(Lakwatsa.STATUS_KEY).toString().toInt(),
            result.documents[0].get(Lakwatsa.ADMIN_KEY).toString(),
            result.documents[0].get(Lakwatsa.LOCATION_NAME_KEY).toString()
        )
    }

    fun updateLakwatsa(lakwatsa: Lakwatsa) {
        val dblakwatsa = hashMapOf(
            Lakwatsa.USERS_KEY to lakwatsa.lakwatsaUsers,
            Lakwatsa.LATITUDE_KEY to lakwatsa.locationLatitude,
            Lakwatsa.LONGITUDE_KEY to lakwatsa.locationLongitude,
            Lakwatsa.TITLE_KEY to lakwatsa.lakwatsaTitle,
            Lakwatsa.DATE_KEY to lakwatsa.date,
            Lakwatsa.POLLING_LIST_KEY to lakwatsa.pollingList,
            Lakwatsa.ALBUM_KEY to lakwatsa.album,
            Lakwatsa.STATUS_KEY to lakwatsa.status,
            Lakwatsa.TIME_KEY to lakwatsa.time,
            Lakwatsa.ADMIN_KEY to lakwatsa.lakwatsaAdmin,
            Lakwatsa.LOCATION_NAME_KEY to lakwatsa.locationName
        )
        val db = Firebase.firestore
        db.collection("lakwatsas")
            .document(lakwatsa.lakwatsaId)
            .set(dblakwatsa)
            .addOnSuccessListener { Log.d("MainActivity", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("MainActivity", "Error updating document", e) }
    }

    fun saveLocation(latitude: Double, longitude: Double){
        if(UserSession.isLoggedIn()==false){
            return
        }

        val db = Firebase.firestore
        val user = UserSession.getUser()
        user.latitude = latitude
        user.longitude = longitude
        updateUser(user)
    }

    suspend fun getLocation(userID:String): LatLng{
        val db = Firebase.firestore
        val result = db.collection("users")
            .whereEqualTo(User.UID_KEY, userID)
            .get()
            .await()
        return LatLng.newBuilder()
            .setLatitude(result.documents[0].get(User.LATITUDE_KEY).toString().toDouble())
            .setLongitude(result.documents[0].get(User.LONGITUDE_KEY).toString().toDouble())
            .build()
    }

    suspend fun sendNotification(message: String, sender: String,receiver:String, type: Long){
        val userReceiver= getUserbyUid(receiver)
        val userSender= getUserbyUid(sender)
        Log.d("DBHelper", type.toString())
        userReceiver.notificationList.add(0,Notification(message, Date(), false, sender, receiver, type,userSender.profilePicture.toLong()))
        updateUser(userReceiver)
    }

    suspend fun setAllNotificationsRead(user: User){
        for(notification in user.notificationList){
            notification.markAsRead()
        }
        updateUser(user)
    }

    fun deleteNotification(notification: Notification, position: Int) {
        val user = UserSession.getUser()
        user.notificationList.remove(notification)
        updateUser(user)
    }

}
