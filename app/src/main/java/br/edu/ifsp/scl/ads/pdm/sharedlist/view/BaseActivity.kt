package br.edu.ifsp.scl.ads.pdm.sharedlist.view

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

sealed class BaseActivity : AppCompatActivity() {
    protected val EXTRA_TASK = "Task"
    protected val EXTRA_VIEW_TASK = "ViewTask"

    protected val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("330540662929-vtatr5556d4ae6fsscvo3r7u5b5latbf.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

    protected val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(this, googleSignInOptions)
    }
}
