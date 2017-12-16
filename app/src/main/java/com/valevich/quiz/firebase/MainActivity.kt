package com.valevich.quiz.firebase

import android.content.Intent
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.valevich.quiz.R
import com.valevich.quiz.base.view.BaseActivity
import com.valevich.quiz.flows.categories.view.QuizCategoriesActivity
import com.valevich.quiz.showSnackBar
import kotlinx.android.synthetic.main.activity_main.*


private const val RC_SIGN_IN: Int = 123

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            showCategories()
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(listOf(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                showCategories()
                return
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackBar(message = "cancelled sign in", rootView = root)
                    return
                }

                if (response.errorCode == ErrorCodes.NO_NETWORK) {
                    showSnackBar(message = "no network", rootView = root)
                    return
                }

                if (response.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(message = "some error", rootView = root)
                    return
                }
            }

        }
    }

    private fun showCategories() {
        startActivity(Intent(this, QuizCategoriesActivity::class.java))
        finish()
    }

}
