package com.inventorypos.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.inventorypos.presentation.screens.auth.LoginScreen
import com.inventorypos.presentation.screens.customers.*
import com.inventorypos.presentation.screens.dashboard.DashboardScreen
import com.inventorypos.presentation.screens.inventory.*
import com.inventorypos.presentation.screens.inventory.category.*
import com.inventorypos.presentation.screens.inventory.po.*
import com.inventorypos.presentation.screens.inventory.product.*
import com.inventorypos.presentation.screens.inventory.stock.*
import com.inventorypos.presentation.screens.inventory.supplier.*
import com.inventorypos.presentation.screens.pos.*
import com.inventorypos.presentation.screens.profile.ProfileScreen
import com.inventorypos.presentation.screens.reports.sales.*
import com.inventorypos.presentation.screens.reports.stock.*
import com.inventorypos.presentation.screens.reports.finance.*
import com.inventorypos.presentation.screens.pos.payment.*
import com.inventorypos.presentation.screens.profile.*
import com.inventorypos.presentation.screens.settings.*
import com.inventorypos.presentation.screens.reports.ReportsScreen
import com.inventorypos.presentation.screens.splash.SplashScreen
import com.inventorypos.presentation.screens.users.*

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // === SPLASH & AUTH ===
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        
        // === MAIN SCREENS (Bottom Navigation) ===
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }
        
        composable(Screen.POS.route) {
            PosScreen(navController)
        }
        
        composable(Screen.Inventory.route) {
            InventoryScreen(navController)
        }
        
        composable(Screen.Reports.route) {
            ReportsScreen(navController)
        }
        
        composable(Screen.Customers.route) {
            CustomerListScreen(navController)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        
        // === PRODUCT SCREENS (SEPARATE FILES) ===
        composable(Screen.ProductList.route) {
            ProductListScreen(navController)
        }
        
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
            ProductDetailScreen(navController, productId)
        }
        
        composable(Screen.ProductAdd.route) {
            ProductAddScreen(navController)
        }
        
        composable(
            route = Screen.ProductEdit.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
            ProductEditScreen(navController, productId)
        }
        
        composable(
            route = Screen.ProductDelete.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
            ProductDeleteDialog(navController, productId)
        }

        // Product Suppliers
composable(
    route = Screen.ProductSuppliers.route,
    arguments = listOf(navArgument("productId") { type = NavType.LongType })
) { backStackEntry ->
    val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
    ProductSupplierScreen(navController, productId)
}

        // === CATEGORY SCREENS (SEPARATE FILES) ===
        composable(Screen.CategoryList.route) {
            CategoryListScreen(navController)
        }

        composable(
            route = Screen.CategoryDetail.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: 0L
            CategoryDetailScreen(navController, categoryId)
        }
                
        composable(Screen.CategoryAdd.route) {
            CategoryAddScreen(navController)
        }
        
        composable(
            route = Screen.CategoryEdit.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: 0L
            CategoryEditScreen(navController, categoryId)
        }
        
        composable(
            route = Screen.CategoryDelete.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: 0L
            CategoryDeleteDialog(navController, categoryId)
        }

        // === SUPPLIER SCREENS (SEPARATE FILES) ===
        composable(Screen.SupplierList.route) {
            SupplierListScreen(navController)
        }

        composable(Screen.SupplierAdd.route) {
            SupplierAddScreen(navController)
        }

        composable(
            route = Screen.SupplierDetail.route,
            arguments = listOf(navArgument("supplierId") { type = NavType.LongType })
        ) { backStackEntry ->
            val supplierId = backStackEntry.arguments?.getLong("supplierId") ?: 0L
            SupplierDetailScreen(navController, supplierId)
        }

        composable(
            route = Screen.SupplierEdit.route,
            arguments = listOf(navArgument("supplierId") { type = NavType.LongType })
        ) { backStackEntry ->
            val supplierId = backStackEntry.arguments?.getLong("supplierId") ?: 0L
            SupplierEditScreen(navController, supplierId)
        }

        dialog(
            route = Screen.SupplierDelete.route,
            arguments = listOf(navArgument("supplierId") { type = NavType.LongType })
        ) { backStackEntry ->
            val supplierId = backStackEntry.arguments?.getLong("supplierId") ?: 0L
            SupplierDeleteDialog(navController, supplierId)
        }
        
        // === STOCK SCREENS (SEPARATE FILES) ===
        composable(Screen.StockList.route) {
            StockListScreen(navController)
        }
        
        composable(Screen.StockIn.route) {
            StockInScreen(navController)
        }
        
        composable(Screen.StockOut.route) {
            StockOutScreen(navController)
        }
        
        composable(Screen.StockTransfer.route) {
            StockTransferScreen(navController)
        }
        
        composable(Screen.StockOpname.route) {
            StockOpnameScreen(navController)
        }
        
        composable(Screen.StockAdjustment.route) {
            StockAdjustmentScreen(navController)
        }
        
        // === USER SCREENS (SEPARATE FILES) ===
        composable(Screen.UserList.route) {
            UserListScreen(navController)
        }
        
        composable(Screen.UserAdd.route) {
            UserAddScreen(navController)
        }
        
        composable(
            route = Screen.UserEdit.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
            UserEditScreen(navController, userId)
        }
        
        composable(
            route = Screen.UserDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
            UserDetailScreen(navController, userId)
        }
        
        composable(
            route = Screen.UserPermission.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
            UserPermissionScreen(navController, userId)
        }
        
        // === CUSTOMER SCREENS (SEPARATE FILES) ===
        composable(Screen.CustomerList.route) {
            CustomerListScreen(navController)
        }
        
        composable(Screen.CustomerAdd.route) {
            CustomerAddScreen(navController)
        }
        
        composable(
            route = Screen.CustomerEdit.route,
            arguments = listOf(navArgument("customerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getLong("customerId") ?: 0L
            CustomerEditScreen(navController, customerId)
        }
        
        composable(
            route = Screen.CustomerDetail.route,
            arguments = listOf(navArgument("customerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getLong("customerId") ?: 0L
            CustomerDetailScreen(navController, customerId)
        }
        
        composable(
            route = Screen.CustomerDelete.route,
            arguments = listOf(navArgument("customerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getLong("customerId") ?: 0L
            CustomerDeleteDialog(navController, customerId)
        }
        
        // === REPORT SCREENS (SEPARATE FILES) ===
        composable(Screen.SalesReport.route) {
            SalesReportScreen(navController)
        }
        
        composable(Screen.SalesDaily.route) {
            SalesDailyScreen(navController)
        }
        
        composable(Screen.SalesMonthly.route) {
            SalesMonthlyScreen(navController)
        }
        
        composable(Screen.SalesByProduct.route) {
            SalesByProductScreen(navController)
        }
        
        composable(Screen.StockReport.route) {
            StockReportScreen(navController)
        }
        
        composable(Screen.StockMovement.route) {
            StockMovementScreen(navController)
        }
        
        composable(Screen.ProfitLoss.route) {
            ProfitLossScreen(navController)
        }
        
        composable(Screen.CashFlow.route) {
            CashFlowScreen(navController)
        }
        
        // === PAYMENT SCREENS (SEPARATE FILES) ===
        composable(Screen.CashPayment.route) {
            CashPaymentScreen(navController)
        }
        
        composable(Screen.QrisPayment.route) {
            QrisPaymentScreen(navController)
        }
        
        composable(Screen.SplitPayment.route) {
            SplitPaymentScreen(navController)
        }
        
        // === SETTINGS SCREENS (SEPARATE FILES) ===
        composable(Screen.StoreProfile.route) {
            StoreProfileScreen(navController)
        }
        
        composable(Screen.PrinterSettings.route) {
            PrinterSettingsScreen(navController)
        }
        
        composable(Screen.BackupRestore.route) {
            BackupRestoreScreen(navController)
        }
        
        composable(Screen.About.route) {
            AboutScreen(navController)
        }
        
        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(navController)
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // === PURCHASE ORDER (SMART PO) SCREENS ===
        composable(Screen.SmartPo.route) {
            SmartPoScreen(navController)
        }
        
        composable(Screen.PoList.route) {
            PoListScreen(navController)
        }
        
        composable(
            route = Screen.PoDetail.route,
            arguments = listOf(navArgument("poId") { type = NavType.LongType })
        ) { backStackEntry ->
            val poId = backStackEntry.arguments?.getLong("poId") ?: 0L
            PoDetailScreen(navController, poId)
        }
    }
}
