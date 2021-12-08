package com.example.diplomich

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.diplomich.Authentication.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import okio.Timeout
import java.util.*

class MoreFragment : Fragment(){
    private lateinit var userEmail: TextView
    private lateinit var userPhone: TextView
    private lateinit var userName:TextView
    private lateinit var userPhoto:ImageView
    private lateinit var fAuth:FirebaseAuth
    private lateinit var fStore:FirebaseFirestore
    private lateinit var userId:String
    private lateinit var emailText:TextView
    private lateinit var buttonLogout: Button
    private lateinit var languageButton:Button
    private lateinit var sharedPref:SharedPref
    private lateinit var mySwitch:SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_more, container, false)
        loadLocale()
        userEmail = rootView.findViewById(R.id.userEmailInfo) as TextView
        userPhone = rootView.findViewById(R.id.userPhoneNumber)
        userName = rootView.findViewById(R.id.HelloUserName)
        userPhoto = rootView.findViewById(R.id.imageUser)
        emailText = rootView.findViewById(R.id.verificationEmail)
        languageButton = rootView.findViewById(R.id.languageButton)
        mySwitch = rootView.findViewById(R.id.mySwitch)

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        userId = fAuth.currentUser!!.uid

        buttonLogout = rootView.findViewById(R.id.logoutButton)
        buttonLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireActivity().applicationContext,LoginActivity::class.java))
        }

        val actionBar = this.activity?.actionBar
        actionBar?.title = resources.getString(R.string.app_name)

       // sharedPref = SharedPref(requireActivity().applicationContext)

      /*  if(sharedPref.loadNightModeState()){
            //requireActivity().application.setTheme(R.style.ThemeOverlay_AppCompat_DayNight_ActionBar)
            requireActivity().application.setTheme(AppCompatDelegate.MODE_NIGHT_YES)

        }
        else requireActivity().application.setTheme(AppCompatDelegate.MODE_NIGHT_NO)*/

        /*if(sharedPref.loadNightModeState()){
            mySwitch.isChecked = true
        }*/

            val docu: DocumentReference = fStore.collection("users").document(userId)
            docu.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("TAG", "Failed to read data from Firestore ${e.message}")
                    return@addSnapshotListener
                }
                if (snapshot?.getBoolean("isNight") == false) {
                    mySwitch.isChecked = false
                }
                if (snapshot?.getBoolean("isNight") == true) {
                    mySwitch.isChecked = true
                }
            }

        changeColor()



        languageButton.setOnClickListener {
            showChangeLanguageDialog()
        }

        val user: FirebaseUser = fAuth.currentUser!!
        if(!user.isEmailVerified) {
            emailSent(user)
        }
        val documentReference:DocumentReference = fStore.collection("users").document(userId)
        documentReference.addSnapshotListener{snapshot, e->
            if (e != null) {
                Log.d("TAG", "Failed to read data from Firestore ${e.message}")
                return@addSnapshotListener
            }
            userPhone.text = snapshot!!.getString("PhoneNumber")
            userName.text = "${activity?.getString(R.string.HelloToUser)} ${snapshot!!.getString("Name")}"
            userEmail.text = snapshot!!.getString("Email")
        }
        return rootView
    }

    private fun changeColor() {
        val test: MoreFragment? = activity?.supportFragmentManager
            ?.findFragmentByTag("IDID") as? MoreFragment
        if (test != null && test.isVisible) {}
        else{
            mySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {

                    // restartApp()

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    //sharedPref.setNightModeState(true)
                    fStore.collection("users").document(userId).update("isNight", true)

                } else if (!isChecked) {
                    //restartApp()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    fStore.collection("users").document(userId).update("isNight", false)
                    //sharedPref.setNightModeState(false)
                }
            }
        }
    }


    private fun showChangeLanguageDialog() {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Choose language")
        builder.setSingleChoiceItems(LanguageArray,-1
        ) { dialog, which ->
            if (which == 0) {
                setLocale("en")
            } else if (which == 1) {
                setLocale("pl")
            }
            dialog.dismiss()
        }
        // Get the dialog selected item
        val mDialog:AlertDialog = builder.create()
        mDialog.show()
        }

    @SuppressLint("CommitPrefEdits")
    private fun setLocale(lang: String) {
        val locale: Locale = Locale(lang)
        Locale.setDefault(locale)
        val conf:Configuration = Configuration()
        conf.locale = locale
        activity?.baseContext?.resources?.updateConfiguration(conf, requireActivity().baseContext.resources.displayMetrics)
        val editor = this.activity?.getSharedPreferences("Settings",MODE_PRIVATE)?.edit()
        editor?.putString("My Lang",lang)
        editor?.apply()
    }

    private fun loadLocale(){
        val editor = this.activity?.getSharedPreferences("Settings",MODE_PRIVATE)
        val language: String? = editor?.getString("My Lang","")
        setLocale(language.toString())
    }


    private fun emailSent(user: FirebaseUser) {
            emailText.text = resources.getString(R.string.NotVerifiedEmail)
            emailText.visibility = View.VISIBLE
            emailText.setOnClickListener {
                user.sendEmailVerification().addOnSuccessListener {
                    Toast.makeText(requireActivity().applicationContext,R.string.VerificationEmail,Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener{
                        Log.d("TAG","On Failure ${it.message}")
                    }
            }
    }
    companion object{
        val LanguageArray = arrayOf("English","Polish")
    }



}