package com.example.bookshelf

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookshelf.screens.favorite_screen.FavoritesScreen
import com.example.bookshelf.screens.menuScreen.MenuScreen
import com.example.bookshelf.screens.queryScreen.SearchScreen



@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Menu.route
    ) {
        composable(NavigationItem.Menu.route) {
            // الشاشة الرئيسية مع شريط التنقل
            MenuScreen(
                currentScreen = NavigationItem.Menu,
                onSearchClick = { navController.navigate(NavigationItem.Search.route) },
                onFavClick = { navController.navigate(NavigationItem.Favorites.route) }
            )
        }
        composable(NavigationItem.Search.route) {
            // شاشة البحث
            SearchScreen(
                onBack = { navController.popBackStack() },
                modifier = TODO(),
                viewModel = TODO(),
                retryAction = TODO(),
                onDetailsClick = TODO()
            )
        }
        composable(NavigationItem.Favorites.route) {
            // شاشة المفضلات
            FavoritesScreen(
                onBack = { navController.popBackStack() },
                modifier = TODO(),
                viewModel = TODO(),
                bookshelfList = TODO(),
                retryAction = TODO()
            )
        }
    }
}
