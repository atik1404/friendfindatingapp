package com.friendfinapp.dating.application

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.ChatFragment
import kotlinx.coroutines.android.HandlerDispatcher

abstract class BaseActivity<D:ViewBinding> : AppCompatActivity(){

    protected lateinit var binding:D
    protected var activityContext: Activity? = null
    protected abstract fun viewBindingLayout(): D
    protected abstract fun initializeView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewBindingLayout()
        setContentView(binding.root)
        activityContext = this
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val topInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            val bottomInset = maxOf(imeInsets.bottom, navInsets.bottom)

            view.updatePadding(top = topInsets.top, bottom = bottomInset)

            WindowInsetsCompat.CONSUMED
        }
        initializeView(savedInstanceState)
    }

    protected fun showToastMessage(message:String?){
        Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()
    }

    protected fun showProgressBar(isLoading: Boolean,view: View){
        if (isLoading) {
            view.visibility = View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            view.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityContext = null
        //finish()
    }

    open fun onBackPressedDispatcher(handler: ()-> Unit){
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handler.invoke()

                // then perform the default back action
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        })
    }
}