package com.friendfinapp.dating.ui.settingspersonal

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.databinding.ActivityPersonalSettingsBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.InternetHelper
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.ui.landingpage.LandingActivity
import com.friendfinapp.dating.ui.settingspersonal.model.PersonalSettingsPostingModel
import com.friendfinapp.dating.ui.settingspersonal.viewmodel.PersonalSettingsViewModel
import com.friendfinapp.dating.ui.signin.SignInActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker

import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.TimeZone
import java.util.regex.Matcher
import java.util.regex.Pattern


class PersonalSettingsActivity : BaseActivity<ActivityPersonalSettingsBinding>(){


    private lateinit var countryAdapter: ArrayAdapter<String>
    private lateinit var stateAdapter: ArrayAdapter<String>
    private lateinit var cityAdapter: ArrayAdapter<String>
    var customDialog: ProgressCustomDialog? = null

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: PersonalSettingsViewModel
    private lateinit var internetHelper: InternetHelper
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    var check = true
    var check2 = true
    var check3 = true

    var whoIsSeeking = arrayOf("Male", "Female")

    lateinit var datePicker: MaterialDatePicker<Long>
    var formatted: String = ""

    var countryID = 0
    var stateID = 0
    var ip = ""


     var birthdate : String = ""
    override fun viewBindingLayout(): ActivityPersonalSettingsBinding =
        ActivityPersonalSettingsBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {
        setUpView()
        getAllPersonalData()
        setupAdapters()
        fetchCountries()
        setUpClickListener()
    }

    private fun getAllPersonalData() {

        var fromUserName = sessionManager.username


        customDialog?.show()

        viewModel.personalSettingsGetData(fromUserName).observe(this){

            customDialog!!.dismiss()

            if (it.status_code==200){

                Log.d("TAG", "getAllPersonalData: ")
                Log.d("TAG", "getAllPersonalData: "+it.status_code)
                Log.d("TAG", "getAllPersonalData: "+it.message)
                Log.d("TAG", "getAllPersonalData: "+it.data)
                if (it.data!=null){

                    Log.d("TAG", "getAllPersonalData: "+it.data!!.country)
                    binding.countrys.editText?.setText(it.data!!.country)

                    it.data!!.country?.let { it1 -> fetchStates(it1) }
                    fetchCities(it.data!!.country.toString(), it.data!!.state.toString())
                    binding.states.editText?.setText(it.data!!.state)

                    binding.citys.editText?.setText(it.data!!.city)

                    binding.editPostal.editText?.setText(it.data!!.zipCode)
                    binding.editFullName.editText?.setText(it.data!!.name)
                    binding.editEmail.editText?.setText(it.data!!.email)

                    if (it.data!!.interestedIn!=0) {
                        binding.whoIsSeekingSpinner.setSelection(it.data!!.interestedIn - 1)
                    }


                    val strs = it.data!!.birthdate.toString().split("T").toTypedArray()

                    Log.d("TAG", "getAllPersonalData: "+strs)
                    birthdate= strs[0]

                    // binding.date.text = USER_INFO.birthdate.toString()
                    binding.dateOfBirths.editText?.setText(strs[0].toString())



                }
            }
        }



    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpClickListener() {

        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.deleteAccount.setOnClickListener {
            openDeleteDialog()
        }



        binding.lin.setOnClickListener {


            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -18)

            val myFormat = "yyyy-MM-dd"
            val formattedDate = birthdate





            val sdf = SimpleDateFormat(myFormat)
            val date = sdf.parse(formattedDate)


            val c = Calendar.getInstance()
            c.time = date
            c.add(Calendar.DATE, 1)
            val date2 = c.time
            val timeInMillis = date2.time





            val constraintsBuilder = CalendarConstraints.Builder().setOpenAt(
                timeInMillis //pass time in milli seconds
            ).setValidator(DateValidatorPointBackward.before(calendar.timeInMillis)).build()



            //view.setMaxDate(calendar.timeInMillis)

            datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date of birth")
                .setCalendarConstraints(constraintsBuilder)
                .setSelection(timeInMillis)
                .build()

            // datePicker.setMinDate()

            datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener { selection: Long ->

                // if the user clicks on the positive
                // button that is ok button update the
                // selected date


                binding.dateOfBirths.editText!!
                    .setText("" + datePicker.headerText.toString())

                val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utc.timeInMillis = selection
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                formatted = format.format(utc.time)

                val strs = formatted.toString().split("T").toTypedArray()

                Log.d("TAG", "getAllPersonalData: "+strs)
                birthdate= strs[0]

            }

        }





        binding.changePassword.setOnClickListener{

            customDialog?.show()
            var currentPassword = sessionManager.password
            var fromUserName = sessionManager.username


            if (!validatePasswordOld() || !validatePasswordNew() || !validatePasswordConfirm()){

                customDialog?.dismiss()
                return@setOnClickListener

            }else{
                if (currentPassword== binding.currentPasswordLayout.editText?.text.toString()){
                    if (binding.newPasswordLayout.editText?.text.toString() == binding.confirmPasswordLayout.editText?.text.toString()){
                        customDialog?.dismiss()
                        viewModel.personalSettingsChangePassword(fromUserName,currentPassword,binding.newPasswordLayout.editText?.text.toString().trim()).observe(this){

                            if (it.status_code==200){
                                Toast.makeText(this, "Password Successfully Changed!", Toast.LENGTH_SHORT).show()
                                sessionManager.setPassword(binding.newPasswordLayout.editText?.text.toString().trim())
                                binding.currentPasswordLayout.editText?.setText("")
                                binding.newPasswordLayout.editText?.setText("")
                                binding.confirmPasswordLayout.editText?.setText("")
                            }
                        }
                    }else{
                        customDialog?.dismiss()
                        Toast.makeText(this, "New Password Not Match!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    customDialog?.dismiss()
                    Toast.makeText(this, "Current Password Not Match!", Toast.LENGTH_SHORT).show()
                }
            }

        }





        binding.currentPasswordLayout.setEndIconOnClickListener {
            if (check) {
                check = false
                binding.currentPasswordLayout.setEndIconDrawable(com.friendfinapp.dating.R.drawable.ic_hidden)
                binding.currentPassword.transformationMethod = null
            } else {
                check = true
                binding.currentPasswordLayout.setEndIconDrawable(com.friendfinapp.dating.R.drawable.ic_show_eye)
                binding.currentPassword.transformationMethod = PasswordTransformationMethod()
            }
            binding.currentPassword.setSelection(binding.currentPassword.length())

        }


        binding.newPasswordLayout.setEndIconOnClickListener {
            if (check2) {
                check2 = false
                binding.newPasswordLayout.setEndIconDrawable(com.friendfinapp.dating.R.drawable.ic_hidden)
                binding.newPassword.transformationMethod = null
            } else {
                check2 = true
                binding.newPasswordLayout.setEndIconDrawable(com.friendfinapp.dating.R.drawable.ic_show_eye)
                binding.newPassword.transformationMethod = PasswordTransformationMethod()
            }
            binding.newPassword.setSelection(binding.newPassword.length())

        }


        binding.confirmPasswordLayout.setEndIconOnClickListener {
            if (check3) {
                check3 = false
                binding.confirmPasswordLayout.setEndIconDrawable(com.friendfinapp.dating.R.drawable.ic_hidden)
                binding.confirmPassword.transformationMethod = null
            } else {
                check3 = true
                binding.confirmPasswordLayout.setEndIconDrawable(com.friendfinapp.dating.R.drawable.ic_show_eye)
                binding.confirmPassword.transformationMethod = PasswordTransformationMethod()
            }
            binding.confirmPassword.setSelection(binding.confirmPassword.length())

        }

        binding.saveChanges.setOnClickListener {
            customDialog?.show()
        if (!validateCountry() || !validateState() || !validateCity() || !validateZip() || !validateFullName() || !isEmailValid(binding.editEmail.editText?.text.toString().trim())
            || !validateDateOfBirths() ){

            customDialog?.dismiss()
            return@setOnClickListener
        }else{

            if(binding.country.editableText.toString().equals("Country")){

                binding.country.error = "Error"
                binding.state.error = "Error"
                binding.city.error = "Error"

                try{
                    customDialog!!.dismiss()
                }catch (e : Exception){}

                return@setOnClickListener

            }


            if(binding.state.editableText.toString().equals("State")){
                binding.states.error = "Error"
                binding.citys.error = "Error"

                try{
                    customDialog!!.dismiss()
                }catch (e : Exception){}

                return@setOnClickListener

            }
            if(binding.city.editableText.toString().equals("City")){
                try{
                    binding.citys.error = "Error"

                    customDialog!!.dismiss()
                }catch (e : Exception){}

                return@setOnClickListener

            }


            if (formatted.isEmpty()){
                formatted=binding.dateOfBirths.editText?.text.toString()
            }

            var userName = sessionManager.username
            var country = binding.countrys.editText?.text.toString()
            var state = binding.states.editText?.text.toString()
            var city = binding.citys.editText?.text.toString()
            var zip = binding.editPostal.editText?.text.toString()
            var fullName = binding.editFullName.editText?.text.toString()
            var email = binding.editEmail.editText?.text.toString()
            var dateOfBirth = formatted.toString()
            var whoIsSeeking = binding.whoIsSeekingSpinner.selectedItem.toString()

            var interest: Int = if (whoIsSeeking == "Male"){
                1
            }else{
                2
            }

            viewModel.updatePersonalSettings(userName,country,state,city,zip,fullName,interest,dateOfBirth,email).observe(this){
                if (it.statusCode==200){
                    getAllPersonalData()
                    finish()
                }else{
                    Toast.makeText(this, "Something Went Wrong! Please try again.", Toast.LENGTH_SHORT).show()
                }
            }


        }



        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openDeleteDialog() {
        val btnSheet: View = layoutInflater.inflate(R.layout.dialog_delete_account, null)
        val dialog: Dialog =
            BottomSheetDialog(this, R.style.AppBottomSheetDialogThemeNew)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(btnSheet)

        val close = dialog.findViewById<View>(R.id.close) as ImageView
        val delete = dialog.findViewById<View>(R.id.delete) as TextView


        close.setOnClickListener { dialog.dismiss() }

        delete.setOnClickListener {

            deleteUser()
        }

        dialog.show()
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun deleteUser() {
        if (internetHelper.isOnline()) {


            Log.d("TAG", "deleteUser: "+Constants.USER_INFO.username)
                customDialog?.show()
                var model = PersonalSettingsPostingModel(
                    Constants.USER_INFO.username,
                    "Normal Reason"
                )

            Log.d("TAG", "deleteUser: "+Constants.USER_INFO.username)
                viewModel.deleteUser(model, customDialog, this).observe(this) {
                    customDialog?.dismiss()
                    it.statusCode.let { it ->
                        if (it == 200) {
                            Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT)
                                .show()

                            customDialog?.show()
                            val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
                            if (googleSignInAccount != null) {
                                val gso: GoogleSignInOptions =
                                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_ids))
                                        .requestEmail()
                                        .build()
                                mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                                mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(this, OnCompleteListener<Void?> {
                                        customDialog?.dismiss()
                                        //  Toast.makeText(this, "Sign out Successful" , Toast.LENGTH_SHORT).show()
                                        sessionManager.login = false
                                        startActivity(Intent(this, SignInActivity::class.java))
                                        LandingActivity.instance?.finish()
                                    })
                            } else {
                                viewModel.getLogOut().observe(this) {
                                    if (it.status_code == 200) {
                                        customDialog?.dismiss()
                                        // Toast.makeText(this, "" + it.message.toString(), Toast.LENGTH_SHORT).show()
                                        sessionManager.login = false
                                        startActivity(Intent(this, SignInActivity::class.java))
                                        LandingActivity.instance?.finish()
                                    }
                                }
                            }
                        }
                    }
                }

        }else{
            Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setUpView() {
        internetHelper = InternetHelper(this)
        customDialog = ProgressCustomDialog(this)
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(PersonalSettingsViewModel::class.java)


        ip = getLocalIpAddress()!!
        // initialize country picker
        // initialize country picker


        setUpSpinner(binding.whoIsSeekingSpinner, whoIsSeeking)

        if (!Constants.IS_SUBSCRIBE) {
            binding.adView.visibility= View.VISIBLE
            val adView = AdView(this)
            val adRequest = AdRequest.Builder().build()

            adView.setAdSize(AdSize.BANNER)

            adView.adUnitId = getString(R.string.BannerAdsUnitId)
            binding.adView.loadAd(adRequest)
        }else{
            binding.adView.visibility= View.GONE
        }
    }


    var selectedItem = -1
    private fun setUpSpinner(spiner: Spinner, array: Array<String>) {
        val aa: ArrayAdapter<*> =
            object : ArrayAdapter<String?>(this, R.layout.simple_spinner_item, array) {
                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    var v: View? = null
                    v = super.getDropDownView(position, null, parent)
                    // If this is the selected item position
                    if (position == selectedItem) {
                        v.setBackgroundColor(
                            ContextCompat.getColor(
                                this@PersonalSettingsActivity,
                                R.color.signInColor
                            )
                        )
                    } else {
                        // for other views
                        v.setBackgroundColor(Color.WHITE)
                    }
                    return v
                }
            }
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        //Setting the ArrayAdapter data on the Spinner
        spiner.adapter = aa

    }




    fun getLocalIpAddress(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
    }

    private fun validatePasswordOld(): Boolean {
        val passwordInput: String = binding.currentPasswordLayout.editText?.text.toString().trim()
        return when {
            passwordInput.isEmpty() -> {
                binding.currentPasswordLayout.error = "Field can't be empty"
                false
            }
            passwordInput.length < 10 -> {
                binding.currentPasswordLayout.error = "Password has minimum 10 characters"
                false
            }
            else -> {
                binding.currentPasswordLayout.error = null
                true
            }
        }
    }

    private fun validatePasswordNew(): Boolean {
        val passwordInput: String = binding.newPasswordLayout.editText?.text.toString().trim()
        return when {
            passwordInput.isEmpty() -> {
                binding.newPasswordLayout.error = "Field can't be empty"
                false
            }
            passwordInput.length < 10 -> {
                binding.newPasswordLayout.error = "Password has minimum 10 characters"
                false
            }
            else -> {
                binding.newPasswordLayout.error = null
                true
            }
        }
    }


    private fun validatePasswordConfirm(): Boolean {
        val passwordInput: String = binding.confirmPasswordLayout.editText?.text.toString().trim()
        return when {
            passwordInput.isEmpty() -> {
                binding.confirmPasswordLayout.error = "Field can't be empty"
                false
            }
            passwordInput.length < 10 -> {
                binding.confirmPasswordLayout.error = "Password has minimum 10 characters"
                false
            }
            else -> {
                binding.confirmPasswordLayout.error = null
                true
            }
        }
    }

    private fun validateFullName(): Boolean {
        val firstnameInput: String = binding.editFullName.editText?.text.toString().trim()
        return when {
            firstnameInput.isEmpty() -> {
                binding.editFullName.error = "Field can't be empty"
                false
            }
            firstnameInput.length > 20 -> {
                binding.editFullName.error = "First name is too long"
                false
            }
            else -> {
                binding.editFullName.error = null
                true
            }
        }
    }
    private fun isEmailValid(email: String): Boolean {
        val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
        val emaill = email.trim { it <= ' ' }
        val inputStr: CharSequence = emaill
        val pattern: Pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            binding.editEmail.error = null
            return true
        } else binding.editEmail.error = "Please Enter Valid Email!"
        return false
    }
    private fun validateDateOfBirths(): Boolean {
        return if (binding.dateOfBirths.editText?.text.isNullOrEmpty()) {
            Toast.makeText(this@PersonalSettingsActivity, "Please select Date Of Birth", Toast.LENGTH_SHORT)
                .show()
            binding.dateOfBirths.error = "Field can't be empty"
            false
        } else {
            binding.dateOfBirths.error = null
            true
        }
    }

    private fun validateZip(): Boolean {
        val firstnameInput: String = binding.editPostal.editText?.text.toString().trim()
        return when {
            firstnameInput.isEmpty() -> {
                binding.editPostal.error = "Field can't be empty"
                false
            }

            else -> {
                binding.editPostal.error = null
                true
            }
        }
    }

    private fun validateCountry(): Boolean {
        val firstnameInput: String = binding.countrys.editText?.text.toString().trim()
        return when {
            firstnameInput.isEmpty() -> {
                binding.countrys.error = "Field can't be empty"
                false
            }

            else -> {
                binding.countrys.error = null
                true
            }
        }
    }

    private fun validateState(): Boolean {
        val firstnameInput: String = binding.states.editText?.text.toString().trim()
        return when {
            firstnameInput.isEmpty() -> {
                binding.states.error = "Field can't be empty"
                false
            }

            else -> {
                binding.states.error = null
                true
            }
        }
    }

    private fun validateCity(): Boolean {
        val firstnameInput: String = binding.citys.editText?.text.toString().trim()
        return when {
            firstnameInput.isEmpty() -> {
                binding.citys.error = "Field can't be empty"
                false
            }

            else -> {
                binding.citys.error = null
                true
            }
        }
    }

//    private fun setupAdapters() {
//        countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
//        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerCountry.adapter = countryAdapter
//
//        stateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
//        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerStates.adapter = stateAdapter
//
//        cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
//        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerCity.adapter = cityAdapter
//
//
//        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                val selectedCountry = countryAdapter.getItem(position) ?: return
//                binding.countrys.editText?.setText(selectedCountry)
//                Toast.makeText(applicationContext, "set country", Toast.LENGTH_SHORT).show()
//                fetchStates(selectedCountry)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }
//
//
//
//
//        binding.spinnerStates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                val selectedState = stateAdapter.getItem(position) ?: return
//                val selectedCountry = binding.spinnerStates.selectedItem.toString()
//                binding.states.editText?.setText(selectedState)
//
//                Log.d("StateSpinner" , "Called")
//                fetchCities(binding.spinnerCountry.selectedItem.toString(), selectedState)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }
//
//
//        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                val selectedState = cityAdapter.getItem(position) ?: return
//                binding.citys.editText?.setText(selectedState)
//
//                Log.d("StateSpinner" , "Called")
//                fetchCities(binding.spinnerCountry.selectedItem.toString(), selectedState)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }
//
//
//    }

    private fun setupAdapters() {
        // Initialize Adapters
        countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = countryAdapter

        stateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStates.adapter = stateAdapter

        cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCity.adapter = cityAdapter

        // Flags to control initial selection
        var isCountrySpinnerInitialized = false
        var isStateSpinnerInitialized = false
        var isCitySpinnerInitialized = false


        binding.callcountry.setOnClickListener {
            binding.spinnerCountry.visibility = View.VISIBLE

            binding.spinnerCountry.performClick()

        }


        binding.callstate.setOnClickListener {
            binding.spinnerStates.visibility = View.VISIBLE
            binding.spinnerStates.performClick()
        }


        binding.callcity.setOnClickListener {
            binding.spinnerCity.visibility = View.VISIBLE

            binding.spinnerCity.performClick()
        }

        // Country Spinner
        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isCountrySpinnerInitialized) {
//                    Toast.makeText(this@PersonalSettingsActivity,position.toString() , Toast.LENGTH_SHORT).show()

                    val selectedCountry = countryAdapter.getItem(position) ?: return
//                    binding.countrys.editText!!.isEnabled = true

                    binding.states.editText?.setText("State")
                    binding.citys.editText?.setText("City")


                    binding.countrys.editText?.setText(selectedCountry)
//                    binding.countrys.editText!!.isEnabled = false
//                    binding.citys.editText!!.isEnabled = false


                    binding.countrys.editText!!.setFocusable(false);
                    binding.countrys.editText!!.setClickable(false);

                    binding.states.editText!!.setFocusable(false);
                    binding.states.editText!!.setClickable(false);



                    binding.citys.editText!!.setFocusable(false);
                    binding.citys.editText!!.setClickable(false);






//                    Toast.makeText(this@PersonalSettingsActivity,isCountrySpinnerInitialized.toString() , Toast.LENGTH_SHORT).show()


//                    Toast.makeText(applicationContext, "Country selected: $selectedCountry", Toast.LENGTH_SHORT).show()
                    fetchStates(selectedCountry)
                } else {
                    isCountrySpinnerInitialized = true // First initialization handled
//                    Toast.makeText(this@PersonalSettingsActivity,isCountrySpinnerInitialized.toString() , Toast.LENGTH_SHORT).show()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // State Spinner
        binding.spinnerStates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isStateSpinnerInitialized) {
                    val selectedState = stateAdapter.getItem(position) ?: return
                    binding.states.editText?.setText(selectedState)



                    binding.countrys.editText!!.setFocusable(false);
                    binding.countrys.editText!!.setClickable(false);

                    binding.states.editText!!.setFocusable(false);
                    binding.states.editText!!.setClickable(false);



                    binding.citys.editText!!.setFocusable(false);
                    binding.citys.editText!!.setClickable(false);



                    binding.states.error = null // Hide error

                    val selectedCountry = binding.spinnerCountry.selectedItem.toString()

                        fetchCities(binding.countrys.editText!!.text.toString(), selectedState)

                    } else {
                   // Toast.makeText(applicationContext, "else", Toast.LENGTH_SHORT).show()
                    isStateSpinnerInitialized = true // First initialization handled
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // City Spinner
        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isCitySpinnerInitialized) {
                    val selectedCity = cityAdapter.getItem(position) ?: return

                    binding.citys.editText?.setText(selectedCity)

                    binding.citys.error = null // Hide error


                    binding.countrys.editText!!.setFocusable(false);
                    binding.countrys.editText!!.setClickable(false);

                    binding.states.editText!!.setFocusable(false);
                    binding.states.editText!!.setClickable(false);



                    binding.citys.editText!!.setFocusable(false);
                    binding.citys.editText!!.setClickable(false);



                } else {
                    isCitySpinnerInitialized = true // First initialization handled
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun fetchCountries() {
        val url = "https://countriesnow.space/api/v0.1/countries"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val data = response.getJSONArray("data")
                    val countryList = mutableListOf<String>()

                    for (i in 0 until data.length()) {
                        val countryObject = data.getJSONObject(i)
                        countryList.add(countryObject.getString("country"))
                    }

//                    Toast.makeText(applicationContext, countryList.size.toString(), Toast.LENGTH_SHORT).show()
                    countryAdapter.clear()
                    countryAdapter.addAll("Country")
                    countryAdapter.addAll(countryList)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parsing countries", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error fetching countries: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonObjectRequest)
    }

    private fun fetchStates(country: String) {

        val url = "https://countriesnow.space/api/v0.1/countries/states"
        val requestQueue = Volley.newRequestQueue(this)

        Log.d("Country " , country)
        val params = JSONObject().apply { put("country", country) }
        Log.d("Params", params.toString()) // Log the request body

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                try {
                    Log.d("Response", response.toString()) // Log the full response

                    val data = response.getJSONObject("data")
                    val statesArray = data.getJSONArray("states")

                    val stateList = mutableListOf<String>()

                    stateList.add("State")
                    for (i in 0 until statesArray.length()) {
                        val stateObject = statesArray.getJSONObject(i)
                        stateList.add(stateObject.getString("name"))
                    }

//                    Toast.makeText(applicationContext, "States: ${stateList.size}", Toast.LENGTH_SHORT).show()

                    stateAdapter.clear()
                    stateAdapter.addAll(stateList)
                    binding.spinnerStates.adapter = stateAdapter
                } catch (e: Exception) {
                    e.printStackTrace()
//                    Toast.makeText(this, "Error parsing states", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
//                Toast.makeText(this, "Error fetching states: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        jsonObjectRequest.setShouldCache(false) // Disable caching
        requestQueue.add(jsonObjectRequest)
    }

    private fun fetchCities(country: String, state: String) {
        val url = "https://countriesnow.space/api/v0.1/countries/state/cities"
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.cache.clear()
        val encodedCountry = URLEncoder.encode(country, "UTF-8")
        val encodedState = URLEncoder.encode(state, "UTF-8")
        val params1 = JSONObject().apply {
            put("country", encodedCountry)
            put("state", encodedState)
        }

//        Toast.makeText(applicationContext, params1.toString(), Toast.LENGTH_SHORT).show()
        Log.d("params cities", params1.toString())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params1,
            { response ->
                try {
                    Log.d("ResponseCities", response.toString())

                    if (!response.getBoolean("error")) {
                        val cityList = mutableListOf<String>()

                        // Directly access the "data" array which contains the list of cities
                        val cities = response.getJSONArray("data")
                        cityList.add("City")

                        for (i in 0 until cities.length()) {
                            cityList.add(cities.getString(i))
                        }

                        Log.d("citieslist" ,cityList.toString())
                        if (cityList.isNotEmpty()) {
                            cityAdapter.clear()
                            cityAdapter.addAll(cityList)
                            binding.spinnerCity.adapter = cityAdapter
//                            Toast.makeText(applicationContext, "set", Toast.LENGTH_SHORT).show()
                        } else {
//                            Toast.makeText(this, "No cities found for the selected country", Toast.LENGTH_SHORT).show()
                        }
                    } else {
//                        Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("Responsecities", e.toString())
//                    Toast.makeText(this, "Error parsing cities", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Log.d("Responsecities", error.toString())
//                Toast.makeText(this, "Error fetching cities: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonObjectRequest)
    }



}