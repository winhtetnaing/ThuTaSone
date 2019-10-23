package com.winhtetnaing.thutasone.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.winhtetnaing.thutasone.ForceUpdateChecker
import com.winhtetnaing.thutasone.MainFragment
import com.winhtetnaing.thutasone.R
import com.winhtetnaing.thutasone.utils.AppUtils

class MainActivity : AppCompatActivity(), ForceUpdateChecker.OnUpdateNeededListener {

    val TAG = javaClass.simpleName

    lateinit var mAdView: AdView


    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.frame,
                MainFragment()
            )
            .commit()

        MobileAds.initialize(this, resources.getString(R.string.APP_ID))

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        if(AppUtils().hasConnection(this)){
            ForceUpdateChecker.with(this).onUpdateNeeded(this).check()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_update -> openMarket(getString(R.string.app_url), getString(R.string.app_package))
        }
        return true
    }

    private fun openMarket(url: String, appPackage: String) {
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackage))
        intent.data = Uri.parse(url)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        ContextCompat.startActivity(this, intent, null)
    }

    override fun onUpdateNeeded(updateUrl: String, version: String, required: Boolean) {
        if (required) {
            AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please update app to new version for latest features. \nLatest Version - ${version}")
                .setPositiveButton("Update") { dialog, which ->
                    openMarket(updateUrl, getString(R.string.app_package))
                }
                .setNegativeButton("No, thanks") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }


}