package com.example.buynow.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.buynow.R
import com.example.buynow.utils.Extensions.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SettingsActivity : AppCompatActivity() {

    private lateinit var nameEt_SettingsPage: EditText
    private lateinit var EmailEt_SettingsPage: EditText
    private lateinit var saveSetting_SettingsBtn: Button
    private lateinit var logoutBtn: Button
    private val userCollectionRef = Firebase.firestore.collection("Users")
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        runOnUiThread {
            toast("Error: ${throwable.message}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize views
        nameEt_SettingsPage = findViewById(R.id.nameEt_SettingsPage)
        EmailEt_SettingsPage = findViewById(R.id.EmailEt_SettingsPage)
        saveSetting_SettingsBtn = findViewById(R.id.saveSetting_SettingsBtn)
        logoutBtn = findViewById(R.id.logoutBtn_SettingsPage)
        val backIv_ProfileFrag: ImageView = findViewById(R.id.backIv_ProfileFrag)

        // Set click listeners
        backIv_ProfileFrag.setOnClickListener { onBackPressed() }
        saveSetting_SettingsBtn.setOnClickListener { textCheck() }
        logoutBtn.setOnClickListener { showLogoutConfirmationDialog() }

        // Fetch and display user data
        getUserData()

        // Watch for text changes
        textAutoCheck()
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { _, _ -> performLogout() }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    private fun performLogout() {
        try {
            firebaseAuth.signOut()
            toast("Logged out successfully")
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            toast("Logout failed: ${e.message}")
        }
    }

    private fun getUserData() = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
        val uid = firebaseAuth.uid
        if (uid == null) {
            withContext(Dispatchers.Main) {
                toast("User not logged in")
            }
            return@launch
        }

        try {
            val querySnapshot = userCollectionRef.document(uid).get().await()
            val userName = querySnapshot.getString("userName") ?: "Default Name"
            val userEmail = querySnapshot.getString("userEmail") ?: "Default Email"

            withContext(Dispatchers.Main) {
                nameEt_SettingsPage.setText(userName)
                EmailEt_SettingsPage.setText(userEmail)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                toast("Error loading user data: ${e.message}")
            }
        }
    }

    private fun textCheck() {
        if (nameEt_SettingsPage.text.isEmpty()) {
            toast("Name can't be empty")
            return
        }
        if (EmailEt_SettingsPage.text.isEmpty()) {
            toast("Email can't be empty")
            return
        }
        saveNameAndEmailToFireStore()
    }

    private fun saveNameAndEmailToFireStore() = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
        val uid = firebaseAuth.uid ?: run {
            withContext(Dispatchers.Main) {
                toast("User not logged in")
            }
            return@launch
        }

        try {
            val updates = mapOf(
                "userName" to nameEt_SettingsPage.text.toString(),
                "userEmail" to EmailEt_SettingsPage.text.toString()
            )
            userCollectionRef.document(uid).set(updates).await()

            withContext(Dispatchers.Main) {
                toast("Changes saved successfully")
                saveSetting_SettingsBtn.visibility = View.GONE
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                toast("Error saving data: ${e.message}")
            }
        }
    }

    private fun textAutoCheck() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveSetting_SettingsBtn.visibility = View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        nameEt_SettingsPage.addTextChangedListener(textWatcher)
        EmailEt_SettingsPage.addTextChangedListener(textWatcher)
    }
}
