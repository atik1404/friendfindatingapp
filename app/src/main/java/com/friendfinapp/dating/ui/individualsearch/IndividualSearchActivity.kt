package com.friendfinapp.dating.ui.individualsearch

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ActivityIndividualSearchBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.ui.individualsearch.viewmodel.IndividualSearchViewModel
import com.friendfinapp.dating.ui.othersprofile.OthersUsersProfileActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.HashMap

class IndividualSearchActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{
    private lateinit var binding: ActivityIndividualSearchBinding

    lateinit var viewModel: IndividualSearchViewModel

    private lateinit var countryAdapter: ArrayAdapter<String>
    private lateinit var stateAdapter: ArrayAdapter<String>
    private lateinit var cityAdapter: ArrayAdapter<String>
    var customDialog: ProgressCustomDialog? = null


    private lateinit var sessionManager: SessionManager


    var Gender = arrayOf("Male", "Female")
    var whoIsSeeking = arrayOf("Male", "Female")

    // Pickers

    var countryID = 0
    var stateID = 0
    var ip = ""





    var check = true


    var bodyType = arrayOf("Select", "Slim", "Athletic", "Normal", "Few extra pounds")
    var lookingFor = arrayOf(
        "Select",
        "Chat",
        "Dates",
        "Something serious",
        "Long term relationship",
        "Marriage",
        "Friendship"
    )
    var Eyes =
        arrayOf("Select", "Blue", "Green", "Hazel", "Brown", "Gray", "Black", "Blue-green", "Other")
    var EyeHairs = arrayOf("Select", "Brown", "Blond", "Black", "Gray", "White", "Red", "Bald")
    var Smoking = arrayOf("Select", "No", "Sometimes", "Often", "Smoker", "Total addict")
    var Drinking = arrayOf("Select", "No", "Often", "Only in company", "Daily", "Alcoholic")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_individual_search)


        setUpView()
        setUpClickListener()

        setupAdapters()
        fetchCountries()
    }

    private fun setUpView() {
        setUpSpinner(binding.spinerDrinking, Drinking)
        setUpSpinner(binding.spinerEyes, Eyes)
        setUpSpinner(binding.spinerHair, EyeHairs)
        setUpSpinner(binding.spinerSmoking, Smoking)
        // setUpSpinner(binding.spinerInterest,interest)
        setUpSpinner(binding.spinerbodyType, bodyType)
        setUpSpinner(binding.spinerlookingFor, lookingFor)
        setUpSpinner(binding.whoIsSeekingSpinner, whoIsSeeking)
        // initialize country picker
        // initialize country picker
       // countryPicker = CountryPicker.Builder().with(this).listener(this).build()
//
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
        viewModel = ViewModelProvider(this).get(IndividualSearchViewModel::class.java)

        customDialog = ProgressCustomDialog(this)
        sessionManager = SessionManager(this)


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
                                this@IndividualSearchActivity,
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


    private fun setUpClickListener() {
        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.search.setOnClickListener {
            //  if (!ValidGender() || !ValidSeeking() || !validMinAge() || !validMAxAge()||!validUserNAme()){
//
            if (binding.editUserName.editText!!.text.toString().trim()
                    .isEmpty() && binding.minAge.editText!!.text.toString().trim().isEmpty()
                && binding.maxAge.editText!!.text.toString().trim()
                    .isEmpty() && binding.countrys.editText!!.text.toString().trim().isEmpty()
                && binding.states.editText!!.text.toString().trim()
                    .isEmpty() && binding.citys.editText!!.text.toString().trim().isEmpty()
                && binding.spinerEyes.selectedItem.toString() == "Select"
                && binding.spinerDrinking.selectedItem.toString() == "Select" && binding.spinerHair.selectedItem.toString() == "Select"
                && binding.spinerSmoking.selectedItem.toString() == "Select" && binding.spinerbodyType.selectedItem.toString() == "Select"
                && binding.spinerlookingFor.selectedItem.toString() == "Select" && binding.spiner.selectedItem.toString() == "Select"
                && !binding.tvOnline.isChecked && !binding.tvPhotoRequire.isChecked
            )
            {

                Toast.makeText(
                    this,
                    "Please select minimum One filter for search",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener

            }else{
                sendSearch2(binding.editUserName.editText!!.text.toString().trim(),binding.minAge.editText!!.text.toString().trim()
                    ,binding.maxAge.editText!!.text.toString().trim(),binding.countrys.editText!!.text.toString().trim()
                ,binding.citys.editText!!.text.toString().trim(),binding.states.editText!!.text.toString().trim()
                ,binding.spinerEyes.selectedItem.toString(),binding.spinerDrinking.selectedItem.toString()
                ,binding.spinerHair.selectedItem.toString(),binding.spinerSmoking.selectedItem.toString()
                ,binding.spinerbodyType.selectedItem.toString(),binding.spinerlookingFor.selectedItem.toString()
                ,binding.spiner.selectedItem.toString(),binding.tvOnline.isChecked,binding.tvPhotoRequire.isChecked)
            }
//            if (!validUserNAme()) {
//
//                return@setOnClickListener
//            } else {
//
//
//            }
        }



//        binding.Countrylin.setOnClickListener {
//          //  countryPicker?.showDialog(supportFragmentManager)
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
        binding.spiner.onItemSelectedListener = this

        setUpGenderAndInterestedInSpinner()

    }

    private fun sendSearch2(
        userName: String,
        minAge: String,
        maxAge: String,
        country: String,
        city: String,
        state: String,
        eye: String,
        drinking: String,
        hair: String,
        smoking: String,
        bodyType: String,
        lookingFor: String,
        gender: String,
        isOnline: Boolean,
        isPhotoRequire: Boolean
    ) {

        startActivity(Intent(this,IndividualSearchResult::class.java)
            .putExtra("userName",userName)
            .putExtra("minAge",minAge)
            .putExtra("maxAge",maxAge)
            .putExtra("country",country)
            .putExtra("city",city)
            .putExtra("state",state)
            .putExtra("eye",eye)
            .putExtra("drinking",drinking)
            .putExtra("hair",hair)
            .putExtra("smoking",smoking)
            .putExtra("bodyType",bodyType)
            .putExtra("lookingFor",lookingFor)
            .putExtra("gender",gender)
            .putExtra("isOnline",isOnline)
            .putExtra("isPhotoRequire",isPhotoRequire)
        )

    }

    private fun setUpGenderAndInterestedInSpinner() {
        //Creating the ArrayAdapter instance having the country list

        //Creating the ArrayAdapter instance having the country list
        val aa: ArrayAdapter<*> =
            object : ArrayAdapter<String?>(this, R.layout.simple_spinner_item, Gender) {
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
                                this@IndividualSearchActivity,
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
        binding.spiner.setAdapter(aa)
    }

    private fun sendSearch() {

        customDialog?.show()




        viewModel.getNewIndividualSearch(binding.editUserName.editText!!.text.toString().trim())
            .observe(this, {
                customDialog?.dismiss()
                if (!it.data.isNullOrEmpty()) {
                    startActivity(
                        Intent(this, OthersUsersProfileActivity::class.java).putExtra(
                            "username",
                            it.data[0].username.toString()
                        )
                            .putExtra("userimage", it.data[0].userimage.toString())
                    )
                } else {
                    Toast.makeText(this, "No User Found", Toast.LENGTH_SHORT).show()
                }
            })
    }

//    private fun ValidGender(): Boolean {
//        val passwordInput = binding!!.editGender.editText!!.text.toString().trim { it <= ' ' }
//        return if (passwordInput == "") {
//            binding!!.editGender.error = "Field can't be empty"
//            false
//        } else {
//            binding!!.editGender.error = null
//            true
//        }
//    }
//
//    private fun ValidSeeking(): Boolean {
//        val passwordInput = binding!!.editSeeking.editText!!.text.toString().trim { it <= ' ' }
//        return if (passwordInput == "") {
//            binding!!.editSeeking.error = "Field can't be empty"
//            false
//        } else {
//            binding!!.editSeeking.error = null
//            true
//        }
//    }
//
//    private fun validMinAge(): Boolean {
//        val passwordInput = binding!!.editMinAge.editText!!.text.toString().trim { it <= ' ' }
//        return if (passwordInput == "") {
//            binding!!.editMinAge.error = "Field can't be empty"
//            false
//        } else {
//            binding!!.editMinAge.error = null
//            true
//        }
//    }
//
//    private fun validMAxAge(): Boolean {
//        val passwordInput = binding!!.editMaxAge.editText!!.text.toString().trim { it <= ' ' }
//        return if (passwordInput == "") {
//            binding!!.editMinAge.error = "Field can't be empty"
//            false
//        } else {
//            binding!!.editMinAge.error = null
//            true
//        }
//    }

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
    private fun validUserNAme(): Boolean {
        val passwordInput = binding.editUserName.editText!!.text.toString().trim { it <= ' ' }
        return if (passwordInput == "") {
            binding!!.editUserName.error = "Field can't be empty"
            false
        } else {
            binding!!.editUserName.error = null
            true
        }
    }

    var selectedItem = -1

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

//    override fun onSelectState(state: State?) {
//
//        binding.states.editText?.setText(state!!.stateName)
//        CityPicker.equalCityObject.clear()
//
//        stateID = state?.stateId!!
//
//
//        Log.d("TAG", "onSelectState: " + cityObject?.size)
//
//        for (i in cityObject!!.indices) {
//            cityPicker = CityPicker.Builder().with(this).listener(this).build()
//            val cityData = City()
//            if (cityObject!![i].stateId == stateID) {
//                cityData.cityId = cityObject!![i].cityId
//                cityData.cityName = cityObject!![i].cityName
//                cityData.stateId = cityObject!![i].stateId
//                CityPicker.equalCityObject.add(cityData)
//            }
//        }
//    }
//
//    override fun onSelectCountry(country: Country?) {
//        // get country name and country ID
//        // get country name and country ID
//        binding.countrys.editText?.setText(country?.name)
//
//        countryID = country?.countryId!!
//        StatePicker.equalStateObject.clear()
//        CityPicker.equalCityObject.clear()
//
//        //set state name text view and state pick button invisible
//
//        //set state name text view and state pick button invisible
////        pickStateButton.setVisibility(View.VISIBLE)
////        stateNameTextView.setVisibility(View.VISIBLE)
////        stateNameTextView.setText("Region")
////        cityName.setText("City")
////        // set text on main view
////        // set text on main view
////        countryCode.setText("Country code: " + attr.country.getCode())
////        countryPhoneCode.setText("Country dial code: " + attr.country.getDialCode())
////        countryCurrency.setText("Country currency: " + attr.country.getCurrency())
////        flagImage.setBackgroundResource(attr.country.getFlag())
//
//
//        // GET STATES OF SELECTED COUNTRY
//
//
//        // GET STATES OF SELECTED COUNTRY
//        for (i in stateObject!!.indices) {
//            // init state picker
//            statePicker = StatePicker.Builder().with(this).listener(this).build()
//            val stateData = State()
//            if (stateObject!![i].countryId == countryID) {
//                stateData.stateId = stateObject!![i].stateId
//                stateData.stateName = stateObject!![i].stateName
//                stateData.countryId = stateObject!![i].countryId
//                // stateData.flag = attr.country.getFlag()
//                StatePicker.equalStateObject.add(stateData)
//            }
//        }
//    }
//
//    override fun onSelectCity(city: City?) {
//
//        binding.citys.editText?.setText(city?.cityName)
//    }
//
//
//    // GET STATE FROM ASSETS JSON
//    @Throws(JSONException::class)
//    fun getStateJson() {
//        var json: String? = null
//        try {
//            val inputStream = assets.open("states.json")
//            val size = inputStream.available()
//            val buffer = ByteArray(size)
//            inputStream.read(buffer)
//            inputStream.close()
//            json = String(buffer, charset("UTF-8"))
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val jsonObject = JSONObject(json)
//        val events = jsonObject.getJSONArray("states")
//        for (j in 0 until events.length()) {
//            val cit = events.getJSONObject(j)
//            val stateData = State()
//            stateData.stateId = cit.getString("id").toInt()
//            stateData.stateName = cit.getString("name")
//            stateData.countryId = cit.getString("country_id").toInt()
//            stateObject?.add(stateData)
//        }
//    }
//
//    // GET CITY FROM ASSETS JSON
//    @Throws(JSONException::class)
//    fun getCityJson() {
//        var json: String? = null
//        try {
//            val inputStream = assets.open("cities.json")
//            val size = inputStream.available()
//            val buffer = ByteArray(size)
//            inputStream.read(buffer)
//            inputStream.close()
//            json = String(buffer, charset("UTF-8"))
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val jsonObject = JSONObject(json)
//        val events = jsonObject.getJSONArray("cities")
//        for (j in 0 until events.length()) {
//            val cit = events.getJSONObject(j)
//            val cityData = City()
//            cityData.cityId = cit.getString("id").toInt()
//            cityData.cityName = cit.getString("name")
//            cityData.stateId = cit.getString("state_id").toInt()
//            cityObject?.add(cityData)
//        }
//    }
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
    binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
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
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val selectedState = stateAdapter.getItem(position) ?: return
            val selectedCountry = binding.spinnerStates.selectedItem.toString()
            binding.states.editText!!.isEnabled = true
            binding.spinnerStates.visibility = View.VISIBLE

            binding.states.editText?.setText(selectedState)
            binding.states.editText!!.isEnabled = false

            Log.d("StateSpinner" , "Called")
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
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val selectedState = cityAdapter.getItem(position) ?: return
            binding.citys.editText!!.isEnabled = true
            binding.citys.editText?.setText(selectedState)
            binding.citys.editText!!.isEnabled = false
            binding.spinnerCity.visibility = View.VISIBLE

            Log.d("StateSpinner" , "Called")
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