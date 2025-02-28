package com.xenon.nocturne

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.xenon.nocturne.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var mainLayout: ConstraintLayout
    private var doubleBackToExitPressedOnce = false
    private val doubleBackToExitMessage: String by lazy { getString(R.string.double_back_to_exit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the navigation bar and status bar
        hideSystemUI()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainLayout = binding.mainLayout

        setSupportActionBar(binding.appMain.toolbar)

        val navRail: NavigationRailView = binding.navRail
        // Use a temporary variable to handle the potential nullability
        val tempBottomNavView = binding.bottomNavView
        bottomNavView = tempBottomNavView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_now, R.id.nav_recent, R.id.nav_library, R.id.nav_artists, R.id.nav_radio, R.id.nav_settings
            )
        )

        //Setup Navigation
        setupNavigation(navController, navRail)
    }

    private fun setupNavigation(
        navController: NavController,
        navRail: NavigationRailView
    ) {
        val orientation = resources.configuration.orientation
        val screenWidthDp = resources.configuration.screenWidthDp

        if (orientation == Configuration.ORIENTATION_PORTRAIT && screenWidthDp < 600) {
            // Portrait phone mode
            bottomNavView.visibility = View.VISIBLE
            navRail.visibility = View.GONE
            binding.navRailScrollView!!.visibility = View.GONE
            setBottomNavConstraints()
            bottomNavView.setupWithNavController(navController)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_now, R.id.nav_recent, R.id.nav_library, R.id.nav_artists, R.id.nav_radio
                )
            )
        } else {
            // Landscape phone or tablet mode
            bottomNavView.visibility = View.GONE
            navRail.visibility = View.VISIBLE
            // Check if it's a small landscape or not
            if (screenWidthDp < 600) {
                binding.navRailScrollView!!.visibility = View.VISIBLE
                setSmallLandscapeRailNavConstraints()
            } else {
                binding.navRailScrollView!!.visibility = View.GONE
                setRailNavConstraints()
            }
            navRail.setupWithNavController(navController)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_now, R.id.nav_recent, R.id.nav_library, R.id.nav_artists, R.id.nav_radio, R.id.nav_settings
                )
            )
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for sticky immersive mode, replace with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun setBottomNavConstraints() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(mainLayout)
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.BOTTOM,
            bottomNavView.id,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )

        constraintSet.applyTo(mainLayout)
    }

    private fun setRailNavConstraints() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(mainLayout)
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.START,
            binding.navRail.id,
            ConstraintSet.END
        )
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.applyTo(mainLayout)
    }

    private fun setSmallLandscapeRailNavConstraints() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(mainLayout)
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.START,
            binding.navRailScrollView!!.id,
            ConstraintSet.END
        )
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            binding.appMain.root.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.applyTo(mainLayout)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this, doubleBackToExitMessage, Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}