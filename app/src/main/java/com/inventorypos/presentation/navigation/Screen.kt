package com.inventorypos.presentation.navigation

sealed class Screen(val route: String) {
    // Splash & Auth
    object Splash : Screen("splash")
    object Login : Screen("login")
    
    // Main Bottom Nav
    object Dashboard : Screen("dashboard")
    object POS : Screen("pos")
    object Inventory : Screen("inventory")
    object Reports : Screen("reports")
    object Customers : Screen("customers")
    object Settings : Screen("settings")
    
    // Product Screens (each has SEPARATE file)
    object ProductList : Screen("product_list")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Long) = "product_detail/$productId"
    }
    object ProductAdd : Screen("product_add")
    object ProductEdit : Screen("product_edit/{productId}") {
        fun createRoute(productId: Long) = "product_edit/$productId"
    }
    object ProductDelete : Screen("product_delete/{productId}") {
        fun createRoute(productId: Long) = "product_delete/$productId"
    }

    // Product Suppliers
    object ProductSuppliers : Screen("product_suppliers/{productId}") {
    fun createRoute(productId: Long) = "product_suppliers/$productId" 
    }

    // Category Screens
    object CategoryList : Screen("category_list")
    object CategoryAdd : Screen("category_add")
    object CategoryEdit : Screen("category_edit/{categoryId}") {
        fun createRoute(categoryId: Long) = "category_edit/$categoryId"
    }
    object CategoryDelete : Screen("category_delete/{categoryId}") {
        fun createRoute(categoryId: Long) = "category_delete/$categoryId"
    }
    object CategoryDetail : Screen("category_detail/{categoryId}") {
        fun createRoute(categoryId: Long) = "category_detail/$categoryId"
    }
    
    // Stock Screens
    object StockList : Screen("stock_list")
    object StockIn : Screen("stock_in")
    object StockOut : Screen("stock_out")
    object StockTransfer : Screen("stock_transfer")
    object StockOpname : Screen("stock_opname")
    object StockAdjustment : Screen("stock_adjustment")
    
    // User Screens
    object UserList : Screen("user_list")
    object UserAdd : Screen("user_add")
    object UserEdit : Screen("user_edit/{userId}") {
        fun createRoute(userId: Long) = "user_edit/$userId"
    }
    object UserDetail : Screen("user_detail/{userId}") {
        fun createRoute(userId: Long) = "user_detail/$userId"
    }
    object UserPermission : Screen("user_permission/{userId}") {
        fun createRoute(userId: Long) = "user_permission/$userId"
    }
    
    // Customer Screens
    object CustomerList : Screen("customer_list")
    object CustomerAdd : Screen("customer_add")
    object CustomerEdit : Screen("customer_edit/{customerId}") {
        fun createRoute(customerId: Long) = "customer_edit/$customerId"
    }
    object CustomerDetail : Screen("customer_detail/{customerId}") {
        fun createRoute(customerId: Long) = "customer_detail/$customerId"
    }
    object CustomerDelete : Screen("customer_delete/{customerId}") {
        fun createRoute(customerId: Long) = "customer_delete/$customerId"
    }
    
    // Report Screens
    object SalesReport : Screen("sales_report")
    object SalesDaily : Screen("sales_daily")
    object SalesMonthly : Screen("sales_monthly")
    object SalesByProduct : Screen("sales_by_product")
    object StockReport : Screen("stock_report")
    object StockMovement : Screen("stock_movement")
    object ProfitLoss : Screen("profit_loss")
    object CashFlow : Screen("cash_flow")
    
    // Payment Screens
    object CashPayment : Screen("cash_payment")
    object QrisPayment : Screen("qris_payment")
    object SplitPayment : Screen("split_payment")
    
    // Settings
    object StoreProfile : Screen("store_profile")
    object PrinterSettings : Screen("printer_settings")
    object BackupRestore : Screen("backup_restore")
    object About : Screen("about")
    object ChangePassword : Screen("change_password")
    object Profile : Screen("profile")

    // Supplier
    object SupplierList : Screen("supplier_list")
    object SupplierAdd : Screen("supplier_add")
    object SupplierDetail : Screen("supplier_detail/{supplierId}") {
        fun createRoute(supplierId: Long) = "supplier_detail/$supplierId"
    }
    object SupplierEdit : Screen("supplier_edit/{supplierId}") {
        fun createRoute(supplierId: Long) = "supplier_edit/$supplierId"
    }
    object SupplierDelete : Screen("supplier_delete/{supplierId}") {
        fun createRoute(supplierId: Long) = "supplier_delete/$supplierId"
    }
    
}
