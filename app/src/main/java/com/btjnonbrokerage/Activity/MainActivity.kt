package com.btjnonbrokerage.Activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.NavHostFragment
import com.btjnonbrokerage.Base.AndroidBug5497Workaround
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var icons: List<Int>

    lateinit var imageViews: List<ImageView>


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        fullScreen()
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidBug5497Workaround(this).addListener()


        icons = listOf(
            R.drawable.homebutton2,
            R.drawable.ic_search,
            R.drawable.icc_favourite,
            R.drawable.user
        )


        imageViews = listOf(
            binding.iccHome,
            binding.iccSearch,
            binding.iccFavourite,
            binding.iccAccount)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController
        
        val navGraph = navController.navInflater.inflate(R.navigation.auth_nav_graph)
        val token = com.btjnonbrokerage.Base.MySharedPreferences(this).getToken()
        if (token != null) {
            navGraph.setStartDestination(R.id.homeFragment)
        } else {
            navGraph.setStartDestination(R.id.splashFragment)
        }
        navController.graph = navGraph

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.iccHome.setImageResource(R.drawable.homebutton)
                    outlineOthers(R.drawable.homebutton2)
                    binding.llbottomnav.visibility = View.VISIBLE
                }

                R.id.searchFragment -> {
                    outlineOthers(R.drawable.ic_search)
                    binding.iccSearch.setImageResource(R.drawable.icc_search_filled)
                    binding.llbottomnav.visibility = View.GONE
                }

                R.id.favoriteFragment -> {
                    binding.iccFavourite.setImageResource(R.drawable.icc_favorite_filled)
                    outlineOthers(R.drawable.icc_favourite)
                    binding.llbottomnav.visibility = View.VISIBLE
                }

                R.id.profileFragment -> {
                    binding.iccAccount.setImageResource(R.drawable.user2)
                    outlineOthers(R.drawable.user)
                    binding.llbottomnav.visibility = View.VISIBLE
                }
                R.id.payRentFragment -> {
                    outlineOthers(R.drawable.wallet)
                    binding.llbottomnav.visibility = View.GONE
                }
                else -> {
                    binding.llbottomnav.visibility = View.GONE
                }
            }
        }

        binding.homeIcon.setOnClickListener {
            if (navController.currentDestination?.id != R.id.homeFragment) {
                navController.navigate(R.id.homeFragment)
            }
        }

        binding.searchIcon.setOnClickListener {
            if (navController.currentDestination?.id != R.id.searchFragment) {
                navController.navigate(R.id.searchFragment)
            }
        }

        binding.favoriteIcon.setOnClickListener {
            if (navController.currentDestination?.id != R.id.favoriteFragment) {
                navController.navigate(R.id.favoriteFragment)
            }
        }

        binding.profileIcon.setOnClickListener {
            if (navController.currentDestination?.id != R.id.profileFragment) {
                navController.navigate(R.id.profileFragment)
            }
        }
        binding.payRentIcon.setOnClickListener {
            if (navController.currentDestination?.id != R.id.paymentVerificationFragment) {
                navController.navigate(R.id.paymentVerificationFragment)
            }
        }


    }

    private fun outlineOthers(safeIcon: Int) {
        for (i in 0..3) {
            if (icons[i] != safeIcon) {
                imageViews[i].setImageResource(icons[i])

        }else {

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun fullScreen() {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
            view.onApplyWindowInsets(windowInsets)
        }
    }

}
