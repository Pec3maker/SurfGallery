package com.example.surfgallery.navigation

import androidx.annotation.IdRes
import com.example.surfgallery.R

sealed class Screen(
    @IdRes val route: Int,
    val isBottomBarVisible: Boolean = true
) {
    object Authentication : Screen(
        route = R.id.loginFragment,
        isBottomBarVisible = false
    )

    object Gallery : Screen(
        route = R.id.galleryFragment
    )

    //    object Search : Screen(
//        route = "place_map"
//    )
//
//    object PictureInfo : Screen(
//        route = "route"
//    )
//
    object Profile : Screen(
        route = R.id.profileFragment
    )

    object Favorites : Screen(
        route = R.id.favoritesFragment
    )

    companion object {
        fun fromRoute(@IdRes route: Int): Screen =
            when (route) {
                Authentication.route -> Authentication
                Gallery.route -> Gallery
//                PictureInfo.route -> CardInfo
//                Search.route -> Search
                Profile.route -> Profile
                Favorites.route -> Favorites
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}