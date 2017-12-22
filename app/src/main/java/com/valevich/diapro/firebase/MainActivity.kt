package com.valevich.diapro.firebase

import android.content.Intent
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.valevich.diapro.R
import com.valevich.diapro.base.view.BaseActivity
import com.valevich.diapro.flows.navigation.NavigationActivity
import com.valevich.diapro.showSnackBar
import kotlinx.android.synthetic.main.activity_main.*


private const val RC_SIGN_IN: Int = 123

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun layoutId(): Int = R.layout.activity_main

    private fun showCategories() {
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
    }

}
