package com.friendfinapp.dating.ui.signup

import kotlinx.coroutines.Dispatchers

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beastwall.localisation.Localisation
import com.beastwall.localisation.model.City
import com.friendfinapp.dating.CountryStateCityRepository
import com.friendfinapp.dating.GeonamesApi
import com.friendfinapp.dating.R
import com.friendfinapp.dating.RestCountriesApi
import com.friendfinapp.dating.RetrofitClient1
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.databinding.ActivitySignUpBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.USER_ID
import com.friendfinapp.dating.helper.Constants.USER_INFO
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.ui.network.RetrofitClient
import com.friendfinapp.dating.ui.privacypolicy.PrivacyPolicyActivity
import com.friendfinapp.dating.ui.saveprofile.SaveProfileActivity
import com.friendfinapp.dating.ui.signup.viewmodel.RegisterViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.recaptcha.RecaptchaHandle
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

data class Country(val name: String, val states: List<State>)
data class State(val name: String, val cities: List<String>)

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(), AdapterView.OnItemSelectedListener,
    GoogleApiClient.ConnectionCallbacks {

    private lateinit var countryStateCityRepository: CountryStateCityRepository
    private lateinit var customDialog: ProgressCustomDialog

    private lateinit var sessionManager: SessionManager

    private lateinit var viewModel: RegisterViewModel

    lateinit var datePicker: MaterialDatePicker<Long>

    var gender = arrayOf("Gender", "Male", "Female")
    var interestedIn = arrayOf("Interested in", "Male", "Female")


    private lateinit var countryAdapter: ArrayAdapter<String>
    private lateinit var stateAdapter: ArrayAdapter<String>
    private lateinit var cityAdapter: ArrayAdapter<String>

    // Pickers

    var countryID = 0
    var stateID = 0
    var ip = ""


    var check = true

    var email = ""


    // recatcha
    private lateinit var googleApiClient: GoogleApiClient

    var siteKey = "6LdP7O0hAAAAAEFAktvMUshwnUMijHKgHIhFaD7i"


    private var handle: RecaptchaHandle? = null
    override fun viewBindingLayout(): ActivitySignUpBinding =
        ActivitySignUpBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {
        val restCountriesApi = RetrofitClient1.create(RestCountriesApi::class.java)
        val geonamesApi = RetrofitClient1.create(GeonamesApi::class.java)
        countryStateCityRepository = CountryStateCityRepository(restCountriesApi, geonamesApi)

        setupAdapters()
        fetchCountries()

        if (intent.getStringExtra("email") != null) {
            email = intent.getStringExtra("email").toString()

            binding.editEmail.editText?.setText(email)
        }

//        Toast.makeText(applicationContext, "oncreate", Toast.LENGTH_SHORT).show()
        Log.d("CheckToast", "ONCREATE")
        // Set up spinners with data
//        setupSpinners()


        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(SafetyNet.API)
            .addConnectionCallbacks(this)
            .build()

        googleApiClient.connect()

        setUpView()
        setUpClickListener()

    }


    private fun setupAdapters() {
        countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = countryAdapter

        stateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStates.adapter = stateAdapter

        cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCity.adapter = cityAdapter

        // Set listeners for cascading spinners
        binding.spinnerCountry.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCountry = countryAdapter.getItem(position) ?: return
                    binding.countrys.editText!!.isEnabled = true
                    binding.spinnerCountry.visibility = View.VISIBLE

                    binding.countrys.editText?.setText(selectedCountry)

                    binding.countrys.editText!!.isEnabled = false
                    fetchStates(selectedCountry)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }




        binding.spinnerStates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedState = stateAdapter.getItem(position) ?: return
                binding.spinnerStates.selectedItem.toString()
                binding.states.editText!!.isEnabled = true
                binding.spinnerStates.visibility = View.VISIBLE

                binding.states.editText?.setText(selectedState)
                binding.states.editText!!.isEnabled = false

                Log.d("StateSpinner", "Called")
                fetchCities(binding.spinnerCountry.selectedItem.toString(), selectedState)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

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


        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedState = cityAdapter.getItem(position) ?: return
                binding.citys.editText!!.isEnabled = true
                binding.citys.editText?.setText(selectedState)
                binding.citys.editText!!.isEnabled = false
                binding.spinnerCity.visibility = View.VISIBLE

                Log.d("StateSpinner", "Called")
                fetchCities(binding.spinnerCountry.selectedItem.toString(), selectedState)

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

                    countryAdapter.clear()
                    countryAdapter.addAll(countryList)
                } catch (e: Exception) {
                    e.printStackTrace()
//                    Toast.makeText(this, "Error parsing countries", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
//                Toast.makeText(this, "Error fetching countries: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonObjectRequest)
    }

    private fun fetchStates(country: String) {

        val url = "https://countriesnow.space/api/v0.1/countries/states"
        val requestQueue = Volley.newRequestQueue(this)

        Log.d("Country ", country)
        val params = JSONObject().apply { put("country", country) }
        Log.d("Params", params.toString()) // Log the request body

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, params,
            { response ->
                try {
                    Log.d("Response", response.toString()) // Log the full response

                    val data = response.getJSONObject("data")
                    val statesArray = data.getJSONArray("states")

                    val stateList = mutableListOf<String>()
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

                        for (i in 0 until cities.length()) {
                            cityList.add(cities.getString(i))
                        }

                        Log.d("citieslist", cityList.toString())
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


    var formatted: String = ""

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setUpClickListener() {


        binding.imageBack.setOnClickListener {
            finish()
        }
        binding.SignUp.setOnClickListener {
            customDialog.show()
            if (!validateUserName() || !validateFullName() || !isEmailValid(binding.editEmail.editText?.text.toString())
                || !validatePassword() || !validateDateOfBirths() || !validateGender() || !validateInterestedIn() || !validateCountry()
                || !validateState() || !validateCity() || !validateZip()
            ) {
                customDialog.dismiss()
                return@setOnClickListener
            } else {

                if (!validatePolicy()) {
                    customDialog.dismiss()
                    Toast.makeText(this, "Please check the privacy policy", Toast.LENGTH_SHORT)
                        .show()
                } else if (!validateRobot()) {
                    customDialog.dismiss()
                    Toast.makeText(this, "Please check I am not a robot", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    signUp(
                        binding.editUserName.editText?.text.toString().trim(),
                        binding.editPassword.editText?.text.toString(),
                        binding.editEmail.editText?.text.toString(),
                        binding.editFullName.editText?.text.toString(),
                        binding.spiner.selectedItem.toString(),
                        1,
                        1,
                        binding.spinerInterestedIn.selectedItem.toString(),
                        formatted,
                        formatted,
                        binding.countrys.editText?.text.toString(),
                        binding.states.editText?.text.toString(),
                        binding.editPostal.editText?.text.toString(),
                        binding.citys.editText?.text.toString(),
                        ip,
                        5,
                        1,
                        null,
                        "",
                        null,
                        0,
                        0,
                        0,
                        0,
                        "",
                        0,
                        0,
                        0,
                        0,
                        "",
                        "",
                        0,
                        "",
                        0,
                        0
                    )
                }

            }
        }

        binding.lin.setOnClickListener {
            try {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.YEAR, -18)

                val constraintsBuilder =
                    CalendarConstraints.Builder()
                        .setValidator(
                            DateValidatorPointBackward.before(calendar.timeInMillis)
                        )

                //view.setMaxDate(calendar.timeInMillis)

                datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date of birth")
                    .setCalendarConstraints(constraintsBuilder.build())
                    .setSelection(calendar.timeInMillis)
                    .build()

                // datePicker.setMinDate()

                datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
                datePicker.addOnPositiveButtonClickListener { selection: Long ->

                    // if the user clicks on the positive
                    // button that is ok button update the
                    // selected date

                    val headerText = datePicker.headerText.toString()

                    binding.dateOfBirth.setText(headerText)
                    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    utc.timeInMillis = selection
                    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    formatted = format.format(utc.time)

                }
            } catch (ex: Exception) {
                FirebaseCrashlytics.getInstance().recordException(ex)
            }
        }

//        binding.Countrylin.setOnClickListener {
//            countryPicker?.showDialog(supportFragmentManager)
//        }
//
//        binding.statelin.setOnClickListener {
//
//            if (!StatePicker.equalStateObject.isNullOrEmpty()) {
//                statePicker?.showDialog(supportFragmentManager)
//            } else {
//                Toast.makeText(this, "No state found", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.citylin.setOnClickListener {
//            if (!CityPicker.equalCityObject.isNullOrEmpty()) {
//
//                cityPicker?.showDialog(supportFragmentManager)
//            } else {
//                Toast.makeText(this, "No city found", Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.terms.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, PrivacyPolicyActivity::class.java))
        }

        binding.editPassword.setEndIconOnClickListener {
            if (check) {
                check = false
                binding.editPassword.setEndIconDrawable(R.drawable.ic_hidden)
                binding.password.transformationMethod = null
            } else {
                check = true
                binding.editPassword.setEndIconDrawable(R.drawable.ic_show_eye)
                binding.password.transformationMethod = PasswordTransformationMethod()
            }
            binding.password.setSelection(binding.password.length())

        }


//        binding.tvRobotCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//
//
//
//        })

        binding.tvRobotCheck.setOnClickListener {
            if (binding.tvRobotCheck.isChecked) {

//
//                Recaptcha.getClient(this)
//                    .execute(
//                        handle!!,
//                        RecaptchaAction(RecaptchaActionType(RecaptchaActionType.LOGIN))
//                    )
//                    .addOnSuccessListener(
//                        this
//                    ) { response ->
//                        val token = response.tokenResult
//                        // Handle success ...
//                        if (!token.isEmpty()) {
//                            Log.d("TAG", "reCAPTCHA response token: $token")
//
//                            Toast.makeText(this, "Successfully verified", Toast.LENGTH_SHORT).show()
//                            // Validate the response token by following the instructions
//                            // when creating an assessment.
//                        }
//                    }
//                    .addOnFailureListener(
//                        this
//                    ) { e ->
//                        if (e is ApiException) {
//                            val apiException = e as ApiException
//                            val apiErrorStatus: Status =
//                                apiException.status
//                            // Handle api errors ...
//                            binding.tvRobotCheck.isChecked=false
//                        } else {
//                            // Handle other failures ...
//                            binding.tvRobotCheck.isChecked=false
//                        }
//                    }


                SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, siteKey)
                    .setResultCallback {
                        var status: Status = it.status

                        if (status != null && status.isSuccess) {
                            Toast.makeText(this, "Successfully verified", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.tvRobotCheck.isChecked = false
                        }
                    }
            }
        }
        binding.spinerInterestedIn.onItemSelectedListener = this
        binding.spiner.onItemSelectedListener = this

        setUpGenderAndInterestedInSpinner()


    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//        Recaptcha.getClient(this)
//            .close(handle!!)
//            .addOnSuccessListener(
//                this
//            ) {
//                // Handle success ...
//            }
//            .addOnFailureListener(
//                this
//            ) { e ->
//                if (e is ApiException) {
//                    val apiException = e as ApiException
//                    val apiErrorStatus: Status =
//                        apiException.status
//                    // Handle api errors ...
//                } else {
//                    // Handle other failures ...
//                }
//            }
//
//    }

    private fun signUp(
        userName: String,
        password: String,
        email: String,
        fullName: String,
        gender: String,
        active: Int,
        receiveEmails: Int,
        interestedin: String,
        dateofbirth: String,
        dateofbirth2: String,
        country: String,
        state: String,
        postalcode: String,
        city: String,
        ip: String,
        messageVerificationsLeft: Int,
        languageId: Int,
        billingDetails: String?,
        invitedBy: String,
        incomingMessagesRestrictions: String?,
        affiliateID: Int,
        options: Int,
        longitude: Int,
        latitude: Int,
        tokenUniqueId: String?,
        credits: Int,
        moderationScore: Int,
        spamSuspected: Int,
        faceControlApproved: Int,
        profileSkin: String,
        statusText: String,
        featuredMember: Int,
        mySpaceID: String,
        facebookID: Int,
        eventsSettings: Int

    ) {

        var gen: Int = 0
        var interest: Int = 0
        gen = if (gender == "Male") {
            1
        } else {
            2
        }

        interest = if (interestedin == "Male") {
            1
        } else {
            2
        }

        viewModel.signUpUser(
            userName,
            password,
            email,
            fullName,
            gen,
            active,
            receiveEmails,
            interest,
            dateofbirth,
            dateofbirth2,
            country,
            state,
            postalcode,
            city,
            ip,
            messageVerificationsLeft,
            languageId,
            billingDetails,
            invitedBy,
            incomingMessagesRestrictions,
            affiliateID,
            options,
            longitude,
            latitude,
            tokenUniqueId,
            credits,
            moderationScore,
            spamSuspected,
            faceControlApproved,
            profileSkin,
            statusText,
            featuredMember,
            mySpaceID,
            facebookID,
            eventsSettings
        ).observe(this, {
            customDialog.dismiss()
            val statusCode = it.status_code ?: 0
            if (statusCode == 201) {

                sessionManager.login = true
                USER_INFO = it.data!!
                USER_ID = USER_INFO.username.toString()
                sessionManager.setInfo(
                    password,
                    it.data?.email,
                    it.data?.username,
                    it.data?.name,
                    it.data?.interestedIn,
                    it.data?.gender,
                    it.data?.active,
                    it.data?.country,
                    it.data?.state,
                    it.data?.city,
                    it.data?.birthdate
                )
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@SignUpActivity, SaveProfileActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
            }
        })
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
                        return inetAddress.hostAddress
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
    }

    private fun validatePolicy(): Boolean {
        return binding.tvPolicy.isChecked
    }

    private fun validateRobot(): Boolean {
        return binding.tvRobotCheck.isChecked
    }

    private fun validateGender(): Boolean {
        return if (binding.spiner.selectedItem.toString() == "Gender") {
            Toast.makeText(this@SignUpActivity, "Please select Gender", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }


    }

    private fun validateInterestedIn(): Boolean {
        return if (binding.spinerInterestedIn.selectedItem.toString() == "Interested in") {
            Toast.makeText(this@SignUpActivity, "Please select Interested in", Toast.LENGTH_SHORT)
                .show()
            false
        } else {
            true
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

    private fun validateDateOfBirths(): Boolean {
        return if (binding.dateOfBirths.editText?.text.isNullOrEmpty()) {
            Toast.makeText(this@SignUpActivity, "Please select Date Of Birth", Toast.LENGTH_SHORT)
                .show()
            binding.dateOfBirths.error = "Field can't be empty"
            false
        } else {
            binding.dateOfBirths.error = null
            true
        }
    }


    private fun validatePassword(): Boolean {
        val passwordInput: String = binding.editPassword.editText?.text.toString().trim()
        return when {
            passwordInput.isEmpty() -> {
                binding.editPassword.error = "Field can't be empty"
                false
            }

            passwordInput.length < 10 -> {
                binding.editPassword.error = "Password has minimum 10 characters"
                false
            }

            else -> {
                binding.editPassword.error = null
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

    private fun validateUserName(): Boolean {


        val firstnameInput: String = binding.editUserName.editText?.text.toString()

        val regExpn = ("[a-zA-Z0-9]*")

        val inputStr: CharSequence = firstnameInput
        val pattern: Pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(inputStr)
        return when {
            !matcher.matches() -> {
                binding.editUserName.error = "Invalid chars in username!"
                false
            }

            firstnameInput.isEmpty() -> {
                binding.editUserName.error = "Field can't be empty!"
                false
            }

            firstnameInput.length > 20 -> {
                binding.editUserName.error = "Username too long! Should be at most {20} chars."
                false
            }

            firstnameInput.length < 3 -> {
                binding.editUserName.error = "Username too short! Should be at least {3} chars."
                false
            }

            firstnameInput.contains(" ") -> {
                binding.editUserName.error = "Invalid chars in username!"
                false
            }

            else -> {
                binding.editUserName.error = null
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


    private fun setUpGenderAndInterestedInSpinner() {
        //Creating the ArrayAdapter instance having the country list

        //Creating the ArrayAdapter instance having the country list
        val aa: ArrayAdapter<*> =
            object : ArrayAdapter<String?>(this, R.layout.simple_spinner_item, gender) {
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
                                this@SignUpActivity,
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
        binding.spiner.adapter = aa


        val a: ArrayAdapter<*> =
            object : ArrayAdapter<String?>(this, R.layout.simple_spinner_item, interestedIn) {
                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    var va: View? = null
                    va = super.getDropDownView(position, null, parent)
                    // If this is the selected item position
                    if (position == selectedItem) {
                        va.setBackgroundColor(
                            ContextCompat.getColor(
                                this@SignUpActivity,
                                R.color.signInColor
                            )
                        )
                    } else {
                        // for other views
                        va.setBackgroundColor(Color.WHITE)
                    }
                    return va
                }
            }
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        //Setting the ArrayAdapter data on the Spinner
        binding.spinerInterestedIn.adapter = a

    }

    private fun setUpView() {
        ip = getLocalIpAddress()!!
        // initialize country picker
        // initialize country picker
        //  countryPicker = CountryPicker.Builder().with(this).listener(this).build()

//        try {
//            getStateJson()
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        // get City from assets JSON
//        // get City from assets JSON
//        try {
//            getCityJson()
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }

        customDialog = ProgressCustomDialog(this)
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)


        if (!Constants.IS_SUBSCRIBE) {
            binding.adView.visibility = View.VISIBLE
            val adView = AdView(this)
            val adRequest = AdRequest.Builder().build()

            adView.setAdSize(AdSize.BANNER)

            adView.adUnitId = getString(R.string.BannerAdsUnitId)
            binding.adView.loadAd(adRequest)
        } else {
            binding.adView.visibility = View.GONE
        }
    }


    var selectedItem = -1

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

//    private fun getCountryForSpinner() {
//
//        val countries: List<Country> = Localisation.getCountriesList() // Fetch country list
//
//
//        Log.d("TAG", "getCountryForSpinner: "+countries.size)
//        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
//        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerCountry.adapter = countryAdapter
//
//
//
//
//        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                val selectedCountry = binding.spinnerCountry.selectedItem as Country
//
//                // Fetch states for the selected country
//                val states: List<State> = selectedCountry.states
//                val stateAdapter = ArrayAdapter(this@SignUpActivity, android.R.layout.simple_spinner_item, states)
//                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                binding.spinnerStates.adapter = stateAdapter
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Do nothing
//            }
//        }
//
//        binding.spinnerStates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                val selectedState = binding.spinnerStates.selectedItem as State
//
//                // Fetch cities for the selected state
//                val cities:  List<City> = selectedState.cities
//
//                val cityAdapter = ArrayAdapter(this@SignUpActivity, android.R.layout.simple_spinner_item, cities)
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                binding.spinnerCity.adapter = cityAdapter
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Do nothing
//            }
//        }
//    }
//

    // Sample data for countries, states, and cities


    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onBackPressed() {
        super.onBackPressed()
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_ids))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this, OnCompleteListener<Void?> {
                // ...
            })
    }

    override fun onConnected(p0: Bundle?) {


    }

    override fun onConnectionSuspended(p0: Int) {

    }

}