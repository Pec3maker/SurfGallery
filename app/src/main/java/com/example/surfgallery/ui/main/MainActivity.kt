package com.example.surfgallery.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.surfgallery.R
import com.example.surfgallery.databinding.ActivityMainBinding
import com.example.surfgallery.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val navHostFragment = binding.navHostFragment.getFragment<NavHostFragment>()
        val navController = navHostFragment.navController

        configureBottomBar(navController = navController)
        setStartDestination(navController = navController, navHostFragment = navHostFragment)
    }

    private fun configureBottomBar(navController: NavController) {
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController = navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.visibility =
                if (Screen.fromRoute(destination.id).isBottomBarVisible) View.VISIBLE else View.GONE
        }
    }

    private fun setStartDestination(
        navHostFragment: NavHostFragment,
        navController: NavController
    ) {
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)

        navGraph.setStartDestination(
            if (viewModel.isTokenContained()) {
                Screen.Gallery.route
            } else {
                Screen.Authentication.action
            }
        )
        navController.graph = navGraph
    }
}