package com.xenon.nocturne

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.xenon.nocturne.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var mainLayout: ConstraintLayout
    private var doubleBackToExitPressedOnce = false
    private val doubleBackToExitMessage: String by lazy { getString(R.string.double_back_to_exit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        hideSystemUI()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainLayout = binding.mainLayout
        val menuButton: ImageButton = binding.menuButton

        val navRail: NavigationRailView = binding.navRail

        val tempBottomNavView = binding.bottomNavView
        bottomNavView = tempBottomNavView
        val navController = findNavController(R.id.nav_host_fragment_content_main)


        setupNavigation(navController, navRail)

        menuButton.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.main, popup.menu)

        popup.setOnMenuItemClickListener { item: MenuItem ->
            onOptionsItemSelected(item)
        }


        popup.show()
    }

    private fun setupNavigation(
        navController: NavController,
        navRail: NavigationRailView
    ) {
        val orientation = resources.configuration.orientation
        val screenWidthDp = resources.configuration.screenWidthDp

        if (orientation == Configuration.ORIENTATION_PORTRAIT && screenWidthDp < 600) {

            bottomNavView.visibility = View.VISIBLE
            navRail.visibility = View.GONE
            binding.navRailScrollView.visibility = View.GONE
            setBottomNavConstraints()
            bottomNavView.setupWithNavController(navController)
        } else {

            bottomNavView.visibility = View.GONE
            navRail.visibility = View.VISIBLE

            if (screenWidthDp < 600) {
                binding.navRailScrollView.visibility = View.VISIBLE
                setSmallLandscapeRailNavConstraints()
            } else {
                binding.navRailScrollView.visibility = View.GONE
                setRailNavConstraints()
            }
            navRail.setupWithNavController(navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {

                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideSystemUI() {


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
            binding.navRailScrollView.id,
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