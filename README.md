📁 InventoryPOS/
├── 📁 .github/
│   └── 📁 workflows/
│       └── android-build.yml          # CI/CD GitHub Actions
│
├── 📁 app/
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── 📁 java/com/inventorypos/
│   │   │   │   ├── 📁 data/
│   │   │   │   │   ├── 📁 local/
│   │   │   │   │   │   ├── 📁 database/
│   │   │   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   │   │   ├── Converters.kt
│   │   │   │   │   │   │   └── DatabaseModule.kt
│   │   │   │   │   │   ├── 📁 dao/
│   │   │   │   │   │   │   ├── ProductDao.kt
│   │   │   │   │   │   │   ├── CategoryDao.kt
│   │   │   │   │   │   │   ├── TransactionDao.kt
│   │   │   │   │   │   │   ├── UserDao.kt
│   │   │   │   │   │   │   ├── StockDao.kt
│   │   │   │   │   │   │   └── CustomerDao.kt
│   │   │   │   │   │   ├── 📁 entity/
│   │   │   │   │   │   │   ├── ProductEntity.kt
│   │   │   │   │   │   │   ├── CategoryEntity.kt
│   │   │   │   │   │   │   ├── TransactionEntity.kt
│   │   │   │   │   │   │   ├── TransactionItemEntity.kt
│   │   │   │   │   │   │   ├── UserEntity.kt
│   │   │   │   │   │   │   ├── StockEntity.kt
│   │   │   │   │   │   │   └── CustomerEntity.kt
│   │   │   │   │   │   └── 📁 preferences/
│   │   │   │   │   │       └── UserPreferences.kt
│   │   │   │   │   │
│   │   │   │   │   └── 📁 remote/
│   │   │   │   │       ├── 📁 api/
│   │   │   │   │       │   ├── ApiService.kt
│   │   │   │   │       │   └── ApiResponse.kt
│   │   │   │   │       ├── 📁 model/
│   │   │   │   │       │   ├── ProductDto.kt
│   │   │   │   │       │   └── AuthDto.kt
│   │   │   │   │       └── 📁 repository/
│   │   │   │   │           └── RemoteRepository.kt
│   │   │   │   │
│   │   │   │   ├── 📁 di/
│   │   │   │   │   └── AppModule.kt
│   │   │   │   │
│   │   │   │   ├── 📁 domain/
│   │   │   │   │   ├── 📁 model/
│   │   │   │   │   │   ├── Product.kt
│   │   │   │   │   │   ├── Category.kt
│   │   │   │   │   │   ├── Transaction.kt
│   │   │   │   │   │   ├── User.kt
│   │   │   │   │   │   └── Customer.kt
│   │   │   │   │   ├── 📁 repository/
│   │   │   │   │   │   ├── ProductRepository.kt
│   │   │   │   │   │   ├── TransactionRepository.kt
│   │   │   │   │   │   └── UserRepository.kt
│   │   │   │   │   └── 📁 usecase/
│   │   │   │   │       ├── 📁 product/
│   │   │   │   │       │   ├── GetProductsUseCase.kt
│   │   │   │   │       │   ├── AddProductUseCase.kt
│   │   │   │   │       │   ├── UpdateProductUseCase.kt
│   │   │   │   │       │   └── DeleteProductUseCase.kt
│   │   │   │   │       ├── 📁 transaction/
│   │   │   │   │       │   ├── CreateTransactionUseCase.kt
│   │   │   │   │       │   └── GetTransactionsUseCase.kt
│   │   │   │   │       └── 📁 auth/
│   │   │   │   │           ├── LoginUseCase.kt
│   │   │   │   │           └── RegisterUseCase.kt
│   │   │   │   │
│   │   │   │   ├── 📁 presentation/
│   │   │   │   │   ├── 📁 components/
│   │   │   │   │   │   ├── 📁 common/
│   │   │   │   │   │   │   ├── CustomTopBar.kt
│   │   │   │   │   │   │   ├── CustomBottomBar.kt
│   │   │   │   │   │   │   ├── CustomTextField.kt
│   │   │   │   │   │   │   ├── CustomButton.kt
│   │   │   │   │   │   │   ├── CustomCard.kt
│   │   │   │   │   │   │   ├── CustomDialog.kt
│   │   │   │   │   │   │   ├── LoadingIndicator.kt
│   │   │   │   │   │   │   ├── EmptyState.kt
│   │   │   │   │   │   │   └── ErrorState.kt
│   │   │   │   │   │   ├── 📁 charts/
│   │   │   │   │   │   │   ├── SalesChart.kt
│   │   │   │   │   │   │   └── StockChart.kt
│   │   │   │   │   │   └── 📁 tables/
│   │   │   │   │   │       └── DataTable.kt
│   │   │   │   │   │
│   │   │   │   │   ├── 📁 theme/
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   ├── Typography.kt
│   │   │   │   │   │   └── Shape.kt
│   │   │   │   │   │
│   │   │   │   │   ├── 📁 navigation/
│   │   │   │   │   │   ├── AppNavigation.kt
│   │   │   │   │   │   ├── Screen.kt
│   │   │   │   │   │   └── BottomNavItem.kt
│   │   │   │   │   │
│   │   │   │   │   ├── 📁 screens/
│   │   │   │   │   │   ├── 📁 splash/
│   │   │   │   │   │   │   └── SplashScreen.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 auth/
│   │   │   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   │   │   ├── LoginViewModel.kt
│   │   │   │   │   │   │   ├── RegisterScreen.kt
│   │   │   │   │   │   │   └── RegisterViewModel.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 dashboard/
│   │   │   │   │   │   │   ├── DashboardScreen.kt
│   │   │   │   │   │   │   ├── DashboardViewModel.kt
│   │   │   │   │   │   │   ├── 📁 widgets/
│   │   │   │   │   │   │   │   ├── SalesSummaryCard.kt
│   │   │   │   │   │   │   │   ├── StockAlertCard.kt
│   │   │   │   │   │   │   │   ├── TopProductsCard.kt
│   │   │   │   │   │   │   │   └── RecentTransactionsCard.kt
│   │   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 pos/
│   │   │   │   │   │   │   ├── PosScreen.kt
│   │   │   │   │   │   │   ├── PosViewModel.kt
│   │   │   │   │   │   │   ├── 📁 components/
│   │   │   │   │   │   │   │   ├── ProductGrid.kt
│   │   │   │   │   │   │   │   ├── CartPanel.kt
│   │   │   │   │   │   │   │   ├── PaymentDialog.kt
│   │   │   │   │   │   │   │   └── BarcodeScanner.kt
│   │   │   │   │   │   │   └── 📁 payment/
│   │   │   │   │   │   │       ├── CashPaymentScreen.kt
│   │   │   │   │   │   │       ├── QrisPaymentScreen.kt
│   │   │   │   │   │   │       └── SplitPaymentScreen.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 inventory/
│   │   │   │   │   │   │   ├── InventoryScreen.kt
│   │   │   │   │   │   │   ├── InventoryViewModel.kt
│   │   │   │   │   │   │   │
│   │   │   │   │   │   │   ├── 📁 product/
│   │   │   │   │   │   │   │   ├── ProductListScreen.kt
│   │   │   │   │   │   │   │   ├── ProductListViewModel.kt
│   │   │   │   │   │   │   │   ├── ProductDetailScreen.kt
│   │   │   │   │   │   │   │   ├── ProductDetailViewModel.kt
│   │   │   │   │   │   │   │   │
│   │   │   │   │   │   │   │   ├── 📁 add/
│   │   │   │   │   │   │   │   │   ├── ProductAddScreen.kt
│   │   │   │   │   │   │   │   │   └── ProductAddViewModel.kt
│   │   │   │   │   │   │   │   │
│   │   │   │   │   │   │   │   ├── 📁 edit/
│   │   │   │   │   │   │   │   │   ├── ProductEditScreen.kt
│   │   │   │   │   │   │   │   │   └── ProductEditViewModel.kt
│   │   │   │   │   │   │   │   │
│   │   │   │   │   │   │   │   ├── 📁 delete/
│   │   │   │   │   │   │   │   │   ├── ProductDeleteDialog.kt
│   │   │   │   │   │   │   │   │   └── ProductDeleteViewModel.kt
│   │   │   │   │   │   │   │   │
│   │   │   │   │   │   │   │   └── 📁 barcode/
│   │   │   │   │   │   │   │       ├── BarcodeGenerateScreen.kt
│   │   │   │   │   │   │   │       └── BarcodePrintScreen.kt
│   │   │   │   │   │   │   │
│   │   │   │   │   │   │   ├── 📁 category/
│   │   │   │   │   │   │   │   ├── CategoryListScreen.kt
│   │   │   │   │   │   │   │   ├── CategoryAddScreen.kt
│   │   │   │   │   │   │   │   ├── CategoryEditScreen.kt
│   │   │   │   │   │   │   │   └── CategoryDeleteDialog.kt
│   │   │   │   │   │   │   │
│   │   │   │   │   │   │   ├── 📁 stock/
│   │   │   │   │   │   │   │   ├── StockListScreen.kt
│   │   │   │   │   │   │   │   ├── StockInScreen.kt
│   │   │   │   │   │   │   │   ├── StockOutScreen.kt
│   │   │   │   │   │   │   │   ├── StockTransferScreen.kt
│   │   │   │   │   │   │   │   ├── StockOpnameScreen.kt
│   │   │   │   │   │   │   │   └── StockAdjustmentScreen.kt
│   │   │   │   │   │   │   │
│   │   │   │   │   │   │   └── 📁 supplier/
│   │   │   │   │   │   │       ├── SupplierListScreen.kt
│   │   │   │   │   │   │       ├── SupplierAddScreen.kt
│   │   │   │   │   │   │       ├── SupplierEditScreen.kt
│   │   │   │   │   │   │       └── SupplierDetailScreen.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 reports/
│   │   │   │   │   │   │   ├── ReportsScreen.kt
│   │   │   │   │   │   │   ├── ReportsViewModel.kt
│   │   │   │   │   │   │   ├── 📁 sales/
│   │   │   │   │   │   │   │   ├── SalesReportScreen.kt
│   │   │   │   │   │   │   │   ├── SalesDailyScreen.kt
│   │   │   │   │   │   │   │   ├── SalesMonthlyScreen.kt
│   │   │   │   │   │   │   │   └── SalesByProductScreen.kt
│   │   │   │   │   │   │   ├── 📁 stock/
│   │   │   │   │   │   │   │   ├── StockReportScreen.kt
│   │   │   │   │   │   │   │   └── StockMovementScreen.kt
│   │   │   │   │   │   │   └── 📁 finance/
│   │   │   │   │   │   │       ├── ProfitLossScreen.kt
│   │   │   │   │   │   │       └── CashFlowScreen.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 customers/
│   │   │   │   │   │   │   ├── CustomerListScreen.kt
│   │   │   │   │   │   │   ├── CustomerAddScreen.kt
│   │   │   │   │   │   │   ├── CustomerEditScreen.kt
│   │   │   │   │   │   │   ├── CustomerDetailScreen.kt
│   │   │   │   │   │   │   └── CustomerDeleteDialog.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 users/
│   │   │   │   │   │   │   ├── UserListScreen.kt
│   │   │   │   │   │   │   ├── UserAddScreen.kt
│   │   │   │   │   │   │   ├── UserEditScreen.kt
│   │   │   │   │   │   │   ├── UserDetailScreen.kt
│   │   │   │   │   │   │   └── UserPermissionScreen.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 settings/
│   │   │   │   │   │   │   ├── SettingsScreen.kt
│   │   │   │   │   │   │   ├── StoreProfileScreen.kt
│   │   │   │   │   │   │   ├── PrinterSettingsScreen.kt
│   │   │   │   │   │   │   ├── BackupRestoreScreen.kt
│   │   │   │   │   │   │   └── AboutScreen.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   └── 📁 profile/
│   │   │   │   │   │       ├── ProfileScreen.kt
│   │   │   │   │   │       └── ChangePasswordScreen.kt
│   │   │   │   │   │
│   │   │   │   │   └── 📁 viewmodel/
│   │   │   │   │       └── BaseViewModel.kt
│   │   │   │   │
│   │   │   │   ├── 📁 utils/
│   │   │   │   │   ├── Constants.kt
│   │   │   │   │   ├── DateUtils.kt
│   │   │   │   │   ├── CurrencyFormatter.kt
│   │   │   │   │   ├── BarcodeUtils.kt
│   │   │   │   │   ├── PermissionUtils.kt
│   │   │   │   │   └── ValidationUtils.kt
│   │   │   │   │
│   │   │   │   └── MainActivity.kt
│   │   │   │
│   │   │   ├── 📁 res/
│   │   │   │   ├── 📁 drawable/
│   │   │   │   │   ├── ic_launcher_foreground.xml
│   │   │   │   │   ├── ic_dashboard.xml
│   │   │   │   │   ├── ic_pos.xml
│   │   │   │   │   ├── ic_inventory.xml
│   │   │   │   │   ├── ic_reports.xml
│   │   │   │   │   ├── ic_customers.xml
│   │   │   │   │   └── ic_settings.xml
│   │   │   │   ├── 📁 mipmap-xxxhdpi/
│   │   │   │   │   └── ic_launcher.png
│   │   │   │   ├── 📁 values/
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── 📁 xml/
│   │   │   │       └── file_paths.xml
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   └── 📁 test/                     # Unit tests
│   │   └── 📁 androidTest/              # Instrumentation tests
│   │
│   ├── build.gradle.kts
│   └── proguard-rules.pro
│
├── 📁 gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
│
├── build.gradle.kts (project level)
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── local.properties
