package si.blarc.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import si.blarc.MainActivity
import si.blarc.R
import si.blarc.entity.Challenge
import si.blarc.firebase.FirebaseUtils


const val ARG_CHALLENGE_DETAILS = "challenge"

class DetailsChallengeFragment : Fragment() {
    private lateinit var dueOrCompletedTextView: TextView
    private lateinit var challengeFinishButton: Button
    private lateinit var descriptionTextView: TextView
    private lateinit var titleTextView: TextView

    private var challenge: Challenge? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            challenge = it.getSerializable(ARG_CHALLENGE_DETAILS) as Challenge
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dueOrCompletedTextView = view.findViewById(R.id.details_challenge_due_or_completed)
        challengeFinishButton = view.findViewById(R.id.details_challenge_finish_btn)
        titleTextView = view.findViewById(R.id.challenge_title_input)
        descriptionTextView = view.findViewById(R.id.challenge_title_desc)

        titleTextView.text = challenge?.title ?: "No title."
        descriptionTextView.text = challenge?.description ?: "No description"

        if (challenge!!.completed == true) {
            dueOrCompletedTextView.text = "Completed on:"
            challengeFinishButton.visibility = View.GONE

        }
        else {
            dueOrCompletedTextView.text = "Due to:"
            challengeFinishButton.visibility = View.VISIBLE
            challengeFinishButton.setOnClickListener {
                challenge!!.completed = true
                FirebaseUtils.updateChallenge(challenge!!)
                challengeFinishButton.text = "Finished"
                challengeFinishButton.isEnabled = false

                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    companion object {
        @JvmStatic fun newInstance(challenge: Challenge) =
            DetailsChallengeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CHALLENGE_DETAILS, challenge)
                }
            }
    }
}