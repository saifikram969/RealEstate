package com.btjnonbrokerage.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Model.ChatFirebase.ChatMessage
import com.btjnonbrokerage.Model.ChatFirebase.FirebaseUser
import com.btjnonbrokerage.adapters.ChatAdapter
import com.btjnonbrokerage.databinding.FragmentFirebaseChatBinding

class FirebaseChatFragment : BaseFragment<FragmentFirebaseChatBinding>() {

    lateinit var auth : FirebaseAuth
    lateinit var firestore : FirebaseFirestore

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFirebaseChatBinding  = FragmentFirebaseChatBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

    }


    private fun createAnonymousAccount() {

        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                // Optional: Use default values or prompt for username later
                val dataHashMap = hashMapOf(
                    "userid" to user!!.uid,
                    "username" to "Anonymous",
                    "useremail" to "",
                    "status" to "default",
                    "imageUrl" to "https://www.pngarts.com/files/6/User-Avatar-in-Suit-PNG.png"
                )

                firestore.collection("Users").document(user.uid).set(dataHashMap)
                    .addOnSuccessListener {


                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()

                    }
            } else {

                Toast.makeText(requireContext(), task.exception?.message.toString(), Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
        }
    }



}
