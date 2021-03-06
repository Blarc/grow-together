package si.blarc.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import si.blarc.firebase.FirebaseUtils.getChallengesRef
import si.blarc.firebase.FirebaseUtils.getFriendsRef
import si.blarc.firebase.FirebaseUtils.getUsersRef
import si.blarc.entity.Challenge
import si.blarc.entity.User
import si.blarc.firebase.FirebaseUtils

class BaseViewModel : ViewModel() {

    val challenges = MutableLiveData<MutableList<Challenge>>(mutableListOf())
    val completedChallenges = MutableLiveData<MutableList<Challenge>>(mutableListOf())
    val uncompletedChallenges = MutableLiveData<MutableList<Challenge>>(mutableListOf())
    val users = MutableLiveData<MutableList<User>>(mutableListOf())
    val friends = MutableLiveData<MutableList<User>>(mutableListOf())


    init {
        subscribeToChallengesOnFirebase()
        subscribeToUsersOnFirebase()
        subscribeToFriendsOnFirebase()
    }

    fun getCurrentUser() : User {
        return User(FirebaseUtils.getIdOfCurUser())
    }

    fun addChallenge(challenge: Challenge) {
        FirebaseUtils.addChallenge(challenge)
    }

    fun addChallengeToFriend(challenge: Challenge, user: User) {
        FirebaseUtils.addChallengeToFriend(challenge, user)
    }

    fun updateChallenge(challenge: Challenge) {
        FirebaseUtils.updateChallenge(challenge)
    }

    fun addFriend(user: User) {
        FirebaseUtils.addFriend(user)
    }

    fun isAlreadyFriend(user: User): Boolean {
        return friends.value?.contains(user) ?: false
    }

    private fun subscribeToChallengesOnFirebase() {
        getChallengesRef().addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //snapshot.getValue(List<Challenge::class.java>)
                val challengesList = ArrayList<Challenge>()
                val completedChallengesList = ArrayList<Challenge>()
                val uncompletedChallengesList = ArrayList<Challenge>()

                for (postSnapshot in snapshot.children) {
                    //val curChallenge = Challenge("", "", 0, "", "", "");
                    val challenge = postSnapshot.getValue(Challenge::class.java)
                    challenge!!.id = postSnapshot.key;
                    challengesList.add(challenge)

                    if (challenge.completed == true) {
                        completedChallengesList.add(challenge)
                    }
                    else {
                        uncompletedChallengesList.add(challenge)
                    }
                }

                challenges.value = challengesList
                completedChallenges.value = completedChallengesList
                uncompletedChallenges.value = uncompletedChallengesList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // TODO @Blarc: Add proper logging.
                print("The read of all challenges failed!")
            }
        })
    }

    private fun subscribeToUsersOnFirebase() {
        getUsersRef().addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usersList = ArrayList<User>()

                for (userSnapshot in snapshot.children) {
                    usersList.add(User(userSnapshot.key!!))
                }

                users.value = usersList
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO @Blarc: Add proper logging.
                print("The read of all challenges failed!")
            }

        })
    }


    private fun subscribeToFriendsOnFirebase() {
        val friendsIn = this.friends
        getFriendsRef().addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //snapshot.getValue(List<Challenge::class.java>)
                val friendsList = ArrayList<User>()

                for (postSnapshot in snapshot.children) {
                    //val curChallenge = Challenge("", "", 0, "", "", "");
                    val friend = postSnapshot.getValue(User::class.java)
                    friendsList.add(friend!!)
                }

                friendsIn.value = friendsList
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO @Blarc: Add proper logging.
                print("The read of all challenges failed!")
            }
        })
    }

    private fun getChallengesFromFirebase() {
        getChallengesRef().get().addOnCompleteListener {
            val challengesList = ArrayList<Challenge>()

            for (postSnapshot in it.result.children) {
                //val curChallenge = Challenge("", "", 0, "", "", "");
                val challenge = postSnapshot.getValue(Challenge::class.java)
                challengesList.add(challenge!!)
            }

            this.challenges.value = challengesList
        }
    }

}