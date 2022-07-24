package com.example.surfgallery.navigation

import androidx.annotation.IdRes
import com.example.surfgallery.R

sealed class Screen(
    @IdRes val route: Int,
    @IdRes val action: Int = route,
    val isBottomBarVisible: Boolean = true
) {
    object Authentication : Screen(
        route = R.id.loginFragment,
        isBottomBarVisible = false
    )

    object Gallery : Screen(
        route = R.id.galleryFragment,
        action = R.id.action_loginFragment_to_galleryFragment
    )

    object Search : Screen(
        route = R.id.searchFragment,
        action = R.id.action_galleryFragment_to_searchFragment
    )

    object PictureInfo : Screen(
        route = R.id.pictureInfoFragment,
        action = R.id.action_galleryFragment_to_pictureInfoFragment
    )

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
                PictureInfo.route -> PictureInfo
                Search.route -> Search
                Profile.route -> Profile
                Favorites.route -> Favorites
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}