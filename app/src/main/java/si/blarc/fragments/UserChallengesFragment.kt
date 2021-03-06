package si.blarc.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import si.blarc.R
import si.blarc.ui.BaseViewModel
import si.blarc.activities.CreateChallengeActivity
import si.blarc.adapters.ChallengeAdapter
import si.blarc.entity.Challenge
import si.blarc.entity.User


class UserChallengesFragment : Fragment() {
    private lateinit var createChallengeBtn: FloatingActionButton
    private lateinit var challengesList: RecyclerView;
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: ChallengeAdapter;

    private val baseViewModel: BaseViewModel by activityViewModels()

    companion object {
        @JvmStatic
        fun newInstance() = UserChallengesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Home"


        swipeRefreshLayout = view.findViewById(R.id.user_challenges_swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
//            Toast.makeText(context, "Refreshed", Toast.LENGTH_SHORT).show()
            // TODO @Blarc: Refresh challenges array
            swipeRefreshLayout.isRefreshing = false
        }

        createChallengeBtn = view.findViewById(R.id.user_challenges_create_btn)
        createChallengeBtn.setOnClickListener {
            val intent = Intent(context, CreateChallengeActivity::class.java)
            startActivity(intent)
        }

        challengesList = view.findViewById(R.id.user_challenges_challenges_list)

        baseViewModel.uncompletedChallenges.observe(viewLifecycleOwner) { challenges ->
            // Update the UI
            setupChallengeList(ArrayList(challenges))
        }
    }

    private fun setupChallengeList(challenges : ArrayList<Challenge>) {
        linearLayoutManager = LinearLayoutManager(context)

        setupChallengeListAdapter(challenges)

        challengesList.layoutManager = linearLayoutManager
        challengesList.adapter = adapter
    }

    private fun setupChallengeListAdapter(challenges : ArrayList<Challenge>) {
        adapter = ChallengeAdapter(challenges)
    }
}