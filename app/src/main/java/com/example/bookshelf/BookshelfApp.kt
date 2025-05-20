package com.example.bookshelf

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BookshelfApp(
//    modifier: Modifier = Modifier
//) {
//    // Notes: Set Nav Controller
//    val navController = rememberNavController()
//    // Notes: Get current back stack entry
//    val backStackEntry by navController.currentBackStackEntryAsState()
//
//    // Notes: Get the name of the current screen check for null
//    val currentScreen = AppDestinations.valueOf(
//        backStackEntry?.destination?.route ?: AppDestinations.QueryScreen.name
//    )
//
//    // Notes: Boolean to check if we can nagigate back. Check stack
//    val canNavigateBack = navController.previousBackStackEntry != null
//
//    Scaffold(
//        topBar = {
//            MyTopAppBar(
//                currentScreen = currentScreen,
//                canNavigateBack = canNavigateBack,
//                onNavigateUpClicked = { navController.navigateUp() }
//            )
//        }
//    ) {
//        Surface(
//            modifier = modifier
//                .fillMaxSize()
//                .padding(it),
//            color = MaterialTheme.colorScheme.background
//
//        ) {
//            BookshelfNavHost(
//                navController = navController,
//                modifier = modifier
//            )
//        }
//    }
//}