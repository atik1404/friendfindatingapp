package com.friendfinapp.dating.ui.saveprofile

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
import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.databinding.ActivitySaveProfileBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.USER_INFO
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.ui.landingpage.LandingActivity
import com.friendfinapp.dating.ui.saveprofile.model.SaveProfilePostingModel
import com.friendfinapp.dating.ui.saveprofile.viewmodel.SaveProfileViewModel
import com.friendfinapp.dating.ui.signin.viewmodel.LogInViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class SaveProfileActivity : BaseActivity<ActivitySaveProfileBinding>(),
    AdapterView.OnItemSelectedListener {


    var customDialog: ProgressCustomDialog? = null

    private lateinit var sessionManager: SessionManager

    private lateinit var viewModel: SaveProfileViewModel
    private lateinit var loginViewModel: LogInViewModel


    private var saveProfileList: MutableList<SaveProfilePostingModel> = ArrayList()

    var bodyType = arrayOf("Slim", "Athletic", "Normal", "Few extra pounds")
    var lookingFor = arrayOf(
        "Chat",
        "Dates",
        "Something serious",
        "Long term relationship",
        "Marriage",
        "Friendship"
    )
    var Eyes = arrayOf("Blue", "Green", "Hazel", "Brown", "Gray", "Black", "Blue-green", "Other")
    var EyeHairs = arrayOf("Brown", "Blond", "Black", "Gray", "White", "Red", "Bald")
    var Smoking = arrayOf("No", "Sometimes", "Often", "Smoker", "Total addict")
    var Drinking = arrayOf("No", "Often", "Only in company", "Daily", "Alcoholic")
    var interest = arrayOf("Computer", "Music", "Nature", "Adventure", "Movie", "Chat", "Sports")


    var height = arrayOf(
        "4' 0\" - 121cm",
        "4' 1\" - 124cm",
        "4' 2\" - 127cm",
        "4' 3\" - 130cm",
        "4' 4\" - 132cm",
        "4' 5\" - 135cm",
        "4' 6\" - 137cm",
        "4' 7\" - 140cm",
        "4' 8\" - 142cm",
        "4' 9\" - 145cm",
        "4' 10\" - 147cm",
        "4' 11\" - 150cm",
        "5' 0\" - 152cm",
        "5' 1\" - 155cm",
        "5' 2\" - 157cm",
        "5' 3\" - 160cm",
        "5' 4\" - 163cm",
        "5' 5\" - 165cm",
        "5' 6\" - 168cm",
        "5' 7\" - 170cm",
        "5' 8\" - 173cm",
        "5' 9\" - 175cm",
        "5' 10\" - 178cm",
        "5' 11\" - 180cm",
        "6' 0\" - 183cm",
        "6' 1\" - 185cm",
        "6' 2\" - 188cm",
        "6' 3\" - 190cm",
        "6' 4\" - 193cm",
        "6' 5\" - 196cm",
        "6' 6\" - 198cm",
        "6' 7\" - 201cm",
        "6' 8\" - 203cm",
        "6' 9\" - 206cm",
        "6' 10\" - 208cm",
        "6' 11\" - 211cm",
        "7' 0\" - 213cm"
//        "7' 1' - 215cm",
//        "7' 2' - 217cm",
//        "7' 3' - 220cm",
//        "7' 4' - 222cm",
//        "7' 5' - 225cm",
//        "7' 6' - 227cm",
//        "7' 7' - 230cm",
//        "7' 8' - 232cm",
//        "7' 9' - 235cm",
//        "7' 10' - 237cm",
//        "7' 11' - 240cm",


    )

    var weight = arrayOf(
        "88 lbs - 40 kg",
        "90 lbs - 41 kg",
        "93 lbs - 42 kg",
        "95 lbs - 43 kg",
        "97 lbs - 44 kg",
        "99 lbs - 45 kg",
        "101 lbs - 46 kg",
        "103 lbs - 47 kg",
        "105 lbs - 48 kg",
        "108 lbs - 49 kg",
        "110 lbs - 50 kg",
        "112 lbs - 51 kg",
        "115 lbs - 52 kg",
        "117 lbs - 53 kg",
        "119 lbs - 54 kg",
        "121 lbs - 55 kg",
        "123 lbs - 56 kg",
        "125 lbs - 57 kg",
        "128 lbs - 58 kg",
        "130 lbs - 59 kg",
        "132 lbs - 60 kg",
        "134 lbs - 61 kg",
        "137 lbs - 62 kg",
        "139 lbs - 63 kg",
        "141 lbs - 64 kg",
        "143 lbs - 65 kg",
        "146 lbs - 66 kg",
        "148 lbs - 67 kg",
        "150 lbs - 68 kg",
        "152 lbs - 69 kg",
        "154 lbs - 70 kg",
        "156 lbs - 71 kg",
        "158 lbs - 72 kg",
        "161 lbs - 73 kg",
        "163 lbs - 74 kg",
        "165 lbs - 75 kg",
        "167 lbs - 76 kg",
        "169 lbs - 77 kg",
        "171 lbs - 78 kg",
        "174 lbs - 79 kg",
        "176 lbs - 80 kg",
        "178 lbs - 81 kg",
        "180 lbs - 82 kg",
        "183 lbs - 83 kg",
        "185 lbs - 84 kg",
        "187 lbs - 85 kg",
        "189 lbs - 86 kg",
        "191 lbs - 87 kg",
        "194 lbs - 88 kg",
        "196 lbs - 89 kg",
        "198 lbs - 90 kg",
        "200 lbs - 91 kg",
        "202 lbs - 92 kg",
        "205 lbs - 93 kg",
        "207 lbs - 94 kg",
        "209 lbs - 95 kg",
        "211 lbs - 96 kg",
        "213 lbs - 97 kg",
        "216 lbs - 98 kg",
        "218 lbs - 99 kg",
        "220 lbs - 100 kg",
        "222 lbs - 101 kg",
        "224 lbs - 102 kg",
        "227 lbs - 103 kg",
        "229 lbs - 104 kg",
        "231 lbs - 105 kg",
        "233 lbs - 106 kg",
        "235 lbs - 107 kg",
        "238 lbs - 108 kg",
        "240 lbs - 109 kg",
        "242 lbs - 110 kg",
        "244 lbs - 111 kg",
        "246 lbs - 112 kg",
        "249 lbs - 113 kg",
        "251 lbs - 114 kg",
        "253 lbs - 115 kg",
        "255 lbs - 116 kg",
        "258 lbs - 117 kg",
        "260 lbs - 118 kg",
        "262 lbs - 119 kg",
        "265 lbs - 120 kg",
        "267 lbs - 121 kg",
        "269 lbs - 122 kg",
        "271 lbs - 123 kg",
        "273 lbs - 124 kg",
        "275 lbs - 125 kg",
        "277 lbs - 126 kg",
        "279 lbs - 127 kg",
        "282 lbs - 128 kg",
        "284 lbs - 129 kg",
        "286 lbs - 130 kg",
        "288 lbs - 131 kg",
        "291 lbs - 132 kg",
        "293 lbs - 133 kg",
        "295 lbs - 134 kg",
        "297 lbs - 135 kg",
        "300 lbs - 136 kg",
        "302 lbs - 137 kg",
        "304 lbs - 138 kg",
        "306 lbs - 139 kg",
        "308 lbs - 140 kg",
        "> 308 lbs - 140 kg"
    )

    var profile = ""
    override fun viewBindingLayout(): ActivitySaveProfileBinding =
        ActivitySaveProfileBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {


        setUpView()
        if (intent.getStringExtra("profile") != null) {
            profile = intent.getStringExtra("profile").toString()
            loadUserProfile(USER_INFO.username.toString().trim())
        }
        setUpClickListener()
    }


    private fun loadUserProfile(username: String) {

        Log.d("TAG", "loadUserProfile: " + username)
        customDialog?.show()
        loginViewModel.fetchProfile(username).observe(this) {

            Log.d("TAG", "loadUserProfile: " + it.data?.size)
            customDialog!!.dismiss()
            Constants.USER_PROFILE_INFO.addAll(it.data!!)
//            if (!Constants.USER_OTHER_PROFILE_INFO.isNullOrEmpty()){
            if (it.data!!.size >= 1) {
                // binding.spiner.text = it.data!![0].value.toString()

                for (i in height.indices) {
                    if (height[i].toString() == it.data!![0].value.toString()) {
                        binding.spiner.setSelection(i)
                    }
                }
                // binding.spiner.prompt=it.data!![0].value.toString()
            }
            if (it.data!!.size >= 2) {
                for (i in weight.indices) {
                    if (weight[i].toString() == it.data!![1].value.toString()) {
                        binding.weightSpiner.setSelection(i)
                    }
                }
            }
            if (it.data!!.size >= 3) {
                for (i in bodyType.indices) {
                    if (bodyType[i].toString() == it.data!![2].value.toString()) {
                        binding.spinerbodyType.setSelection(i)
                    }
                }
            }

            if (it.data!!.size >= 4) {
                for (i in lookingFor.indices) {
                    if (lookingFor[i].toString() == it.data!![3].value.toString()) {
                        binding.spinerlookingFor.setSelection(i)
                    }
                }
            }

            if (it.data!!.size >= 5) {
                for (i in Eyes.indices) {
                    if (Eyes[i].toString() == it.data!![4].value.toString()) {
                        binding.spinerEyes.setSelection(i)
                    }
                }
            }

            if (it.data!!.size >= 6) {
                for (i in EyeHairs.indices) {
                    if (EyeHairs[i].toString() == it.data!![5].value.toString()) {
                        binding.spinerHair.setSelection(i)
                    }
                }
            }
            if (it.data!!.size >= 7) {
                for (i in Smoking.indices) {
                    if (Smoking[i].toString() == it.data!![6].value.toString()) {
                        binding.spinerSmoking.setSelection(i)
                    }
                }
            }

            if (it.data!!.size >= 8) {
                for (i in Drinking.indices) {
                    if (Drinking[i].toString() == it.data!![7].value.toString()) {
                        binding.spinerDrinking.setSelection(i)
                    }
                }
            }
            if (it.data!!.size >= 9)
                binding.editTitle.editText?.setText(it.data!![8].value.toString())
            if (it.data!!.size >= 10)
                binding.editTellUs.editText?.setText(it.data!![9].value.toString())
            if (it.data!!.size >= 11)
                binding.editLookingFor.editText?.setText(it.data!![10].value.toString())

            if (it.data!!.size >= 12) {
                if (it.data!![11].value.toString().contains("Computers")) {
                    binding.tvComputer.isChecked = true

                }
                if (it.data!![11].value.toString().contains("Music")) {
                    binding.tvMusic.isChecked = true

                }
                if (it.data!![11].value.toString().contains("Nature")) {
                    binding.tvNature.isChecked = true

                }
                if (it.data!![11].value.toString().contains("Adventures")) {
                    binding.tvAdventure.isChecked = true

                }
                if (it.data!![11].value.toString().contains("Sport")) {
                    binding.tvSports.isChecked = true

                }

                if (it.data!![11].value.toString().contains("Movies")) {
                    binding.tvMovies.isChecked = true

                }
                if (it.data!![11].value.toString().contains("Chat")) {
                    binding.tvChat.isChecked = true

                }


            }

//            binding.height.text= it.data!![0].value.toString()
//            binding.weight.text= it.data!![1].value.toString()+" KG"
//            binding.body.text= it.data!![2].value.toString()
//            binding.looking.text= it.data!![3].value.toString()
//            binding.eyes.text= it.data!![4].value.toString()
//            binding.hair.text= it.data!![5].value.toString()
//            binding.smoking.text= it.data!![6].value.toString()
//                binding.title.text= Constants.USER_PROFILE_INFO[8].value.toString()
//                binding.about.text= Constants.USER_PROFILE_INFO[9].value.toString()
            // binding.genderLooking.text= it.data!![11].value.toString()
            // }

        }
    }

    private fun setUpClickListener() {


        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.saveProfile.setOnClickListener {
            customDialog?.show()

            if (!validTitle() || !validTellUs() || !validwhatareLooking()) {
                customDialog?.dismiss()
                return@setOnClickListener
            } else {
                saveProfile()
            }

        }
    }

    private fun saveProfile() {

        var username = USER_INFO.username.toString()

        Log.d("TAG", "saveProfile: " + username)

        var postingModel: SaveProfilePostingModel =
            SaveProfilePostingModel(username, 1, binding.spiner.selectedItem.toString(), 1)
        var postingModel2: SaveProfilePostingModel =
            SaveProfilePostingModel(username, 2, binding.weightSpiner.selectedItem.toString(), 1)
        var postingModel3: SaveProfilePostingModel =
            SaveProfilePostingModel(username, 3, binding.spinerbodyType.selectedItem.toString(), 1)
        var postingModel4: SaveProfilePostingModel = SaveProfilePostingModel(
            username,
            4,
            binding.spinerlookingFor.selectedItem.toString(),
            1
        )
        var postingModel5: SaveProfilePostingModel =
            SaveProfilePostingModel(username, 5, binding.spinerEyes.selectedItem.toString(), 1)
        var postingModel6: SaveProfilePostingModel =
            SaveProfilePostingModel(username, 6, binding.spinerHair.selectedItem.toString(), 1)
        var postingModel7: SaveProfilePostingModel =
            SaveProfilePostingModel(username, 7, binding.spinerSmoking.selectedItem.toString(), 1)
        var postingModel8: SaveProfilePostingModel =
            SaveProfilePostingModel(username, 8, binding.spinerDrinking.selectedItem.toString(), 1)
        var postingModel9: SaveProfilePostingModel =
            SaveProfilePostingModel(username, 9, binding.editTitle.editText?.text.toString(), 1)
        var postingModel10: SaveProfilePostingModel =
            SaveProfilePostingModel(username, 10, binding.editTellUs.editText?.text.toString(), 1)
        var postingModel11: SaveProfilePostingModel = SaveProfilePostingModel(
            username,
            11,
            binding.editLookingFor.editText?.text.toString(),
            1
        )

        var interest = ""

        if (binding.tvComputer.isChecked) {
            interest = interest + binding.tvComputer.text.toString().trim() + ":"
        }
        if (binding.tvMusic.isChecked) {
            interest = interest + binding.tvMusic.text.toString().trim() + ":"
        }

        if (binding.tvNature.isChecked) {
            interest = interest + binding.tvNature.text.toString().trim() + ":"
        }
        if (binding.tvAdventure.isChecked) {
            interest = interest + binding.tvAdventure.text.toString().trim() + ":"
        }

        if (binding.tvSports.isChecked) {
            interest = interest + binding.tvSports.text.toString().trim() + ":"
        }
        if (binding.tvMovies.isChecked) {
            interest = interest + binding.tvMovies.text.toString().trim() + ":"
        }

        if (binding.tvChat.isChecked) {
            interest = interest + binding.tvChat.text.toString().trim() + ":"
        }


        if (interest.isEmpty()) {
            interest = "--No Answer--"
        }


        var finalInterest = interest.substring(0, interest.length - 1)
        var postingModel12: SaveProfilePostingModel =
            SaveProfilePostingModel(username, 12, finalInterest, 1)

        saveProfileList.add(postingModel)
        saveProfileList.add(postingModel2)
        saveProfileList.add(postingModel3)
        saveProfileList.add(postingModel4)
        saveProfileList.add(postingModel5)
        saveProfileList.add(postingModel6)
        saveProfileList.add(postingModel7)
        saveProfileList.add(postingModel8)
        saveProfileList.add(postingModel9)
        saveProfileList.add(postingModel10)
        saveProfileList.add(postingModel11)
        saveProfileList.add(postingModel12)




        viewModel.saveProfile(saveProfileList).observe(this, {

            if (it.status_code == 200) {

                if (profile.isNotEmpty()) {
                    customDialog?.dismiss()
                    Toast.makeText(
                        this@SaveProfileActivity,
                        "Profile saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@SaveProfileActivity,
                        "User successfully created.",
                        Toast.LENGTH_SHORT
                    ).show()
                    fetchProfileSave(username)
                }
            } else {
                customDialog?.dismiss()
                Toast.makeText(
                    this@SaveProfileActivity,
                    "" + it.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun fetchProfileSave(username: String) {
        loginViewModel.fetchProfile(username).observe(this, {
            customDialog!!.dismiss()
            if (it.count < 12) {
                startActivity(
                    Intent(
                        this@SaveProfileActivity,
                        SaveProfileActivity::class.java
                    )
                )
                finish()
            } else {
                Constants.USER_PROFILE_INFO.addAll(it.data!!)
                Constants.USER_PROFILE_INFO_PICTURE.addAll(it.userimage!!)

                if (!it.userimage.isNullOrEmpty()) {
                    sessionManager.setProfile(it.userimage!![0].userimage.toString())
                    Constants.myProfileImage = it.userimage!![0].userimage.toString()
                } else {
                    sessionManager.setProfile("")
                    Constants.myProfileImage = ""
                }
                startActivity(
                    Intent(
                        this@SaveProfileActivity,
                        LandingActivity::class.java
                    )
                )
                finish()
            }
        })
    }

//    private fun validWeight(): Boolean {
//        val passwordInput = binding!!.WeightLayout.editText!!.text.toString().trim { it <= ' ' }
//        return if (passwordInput == "") {
//            binding!!.WeightLayout.error = "Field can't be empty"
//            false
//        } else {
//            binding!!.WeightLayout.error = null
//            true
//        }
//    }

    private fun validTitle(): Boolean {
        val passwordInput = binding!!.editTitle.editText!!.text.toString().trim { it <= ' ' }
        return if (passwordInput == "") {
            binding!!.editTitle.error = "Field can't be empty"
            false
        } else {
            binding!!.editTitle.error = null
            true
        }
    }

    private fun validTellUs(): Boolean {
        val passwordInput = binding!!.editTellUs.editText!!.text.toString().trim { it <= ' ' }
        return if (passwordInput == "") {
            binding!!.editTellUs.error = "Field can't be empty"
            false
        } else {
            binding!!.editTellUs.error = null
            true
        }
    }

    private fun validwhatareLooking(): Boolean {
        val passwordInput = binding!!.editLookingFor.editText!!.text.toString().trim { it <= ' ' }
        return if (passwordInput == "") {
            binding!!.editLookingFor.error = "Field can't be empty"
            false
        } else {
            binding!!.editLookingFor.error = null
            true
        }
    }

    private fun setUpView() {


        customDialog = ProgressCustomDialog(this)
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(SaveProfileViewModel::class.java)
        loginViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)


        setUpSpinner(binding.spiner, height)
        setUpSpinner(binding.weightSpiner, weight)
        setUpSpinner(binding.spinerDrinking, Drinking)
        setUpSpinner(binding.spinerEyes, Eyes)
        setUpSpinner(binding.spinerHair, EyeHairs)
        setUpSpinner(binding.spinerSmoking, Smoking)
        // setUpSpinner(binding.spinerInterest,interest)
        setUpSpinner(binding.spinerbodyType, bodyType)
        setUpSpinner(binding.spinerlookingFor, lookingFor)

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
                                this@SaveProfileActivity,
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

    var selectedItem = -1
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


}