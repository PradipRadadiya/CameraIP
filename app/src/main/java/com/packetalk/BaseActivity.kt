package com.packetalk

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD
import com.ligl.android.widget.iosdialog.IOSDialog
import com.packetalk.login.activity.LoginAct
import com.packetalk.util.LogoutDialogClass
import com.packetalk.util.NetworkUtils
import com.packetalk.util.SharedPreferenceSession
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


abstract class BaseActivity : AppCompatActivity(), BindingListener {

    private var hud: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(getLayoutResourceId())
        if (!NetworkUtils.isOnline(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.no_internet))
            builder.setMessage(getString(R.string.internet_msg))
            builder.setNegativeButton(getString(R.string.close),
                { dialog, which -> dialog.dismiss() })
            val alertDialog = builder.create()
            alertDialog.show()
        }

        hud = KProgressHUD(this);
        init()
        initView()
        postInitView()
        addListener()
        loadData()
    }

    override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }

    protected fun startNewActivity(activity: Class<out Activity>) {
        startActivity(Intent(this, activity))
        overridePendingTransitionEnter()
    }

    protected fun startNewActivityWithIntent(intent: Intent) {
        startActivity(intent)
        overridePendingTransitionEnter()
    }

    protected fun showProgressDialog(title: String, message: String) {
        hud?.dismiss()
        hud = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel(title)
            .setDetailsLabel(message)
            .show()
    }

    protected fun hideProgressDialog() {
        if (hud != null && hud!!.isShowing()) {
            hud!!.dismiss()
            hud = null
        }
    }

    private fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    protected abstract fun getLayoutResourceId(): Int

    fun replaceFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.frameContainer, fragment)
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit()
    }

    private fun selectContentFragment(fragmentToSelect: Fragment) {
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()

        if (this.supportFragmentManager.fragments.contains(fragmentToSelect)) {
            // Iterate through all cached fragments.
            for (cachedFragment in this.supportFragmentManager.fragments) {
                if (cachedFragment !== fragmentToSelect) {
                    // Hide the fragments that are not the one being selected.
                    fragmentTransaction.hide(cachedFragment)
                }
            }
            // Show the fragment that we want to be selected.
            fragmentTransaction.show(fragmentToSelect)
        } else {
            // The fragment to be selected does not (yet) exist in the fragment manager, add it.
            fragmentTransaction.add(R.id.frameContainer, fragmentToSelect)
        }
        fragmentTransaction.commit()
    }

    fun bindToolBar(headerTitle: String) {
        toolbar.title = headerTitle
        setSupportActionBar(toolbar)
    }

    fun bindToolBarReverce(headerTitle: String) {
        toolbar.title = headerTitle
        setSupportActionBar(toolbar)
        // add back arrow to toolbar
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    fun bindToolBarBack(headerTitle: String) {
        toolbar.title = headerTitle
        setSupportActionBar(toolbar)
        // add back arrow to toolbar
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            finish()
            onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_user -> {
                IOSDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Do you want to logout this application ?")
                    .setPositiveButton(
                        this.getString(R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        // continue with delete
                        val session = SharedPreferenceSession(this)
                        session.saveUserType("")
                        dialog.cancel()
                        startNewActivity(LoginAct::class.java)
                        finishAffinity()

                    }
                    .setNegativeButton(
                        this.getString(R.string.cancel)
                    ) { dialog, _ -> dialog.dismiss() }.show()


              /*
                // Do something
                val dialogBuilder = AlertDialog.Builder(this)
                // set message of alert dialog
                dialogBuilder.setMessage("Do you want to logout this application ?")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                        val session = SharedPreferenceSession(this)
                        session.saveUserType("")
                        dialog.cancel()
                        startNewActivity(LoginAct::class.java)
                        finishAffinity()
                    })
                    // negative button text and action
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Logout")
                // show alert dialog
                alert.show()
                return true*/
            }
        }
        return super.onOptionsItemSelected(item)
    }

}