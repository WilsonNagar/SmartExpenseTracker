#!/bin/bash

# Base path for main source code
BASE_DIR="app/src/main/java/com/wilson/smartexpensetracker"
RES_DIR="app/src/main/res"

# Create package directories
mkdir -p $BASE_DIR/{di,data/local/dao,data/local/entity,data/repository,data/mapper,domain/model,domain/repository,domain/usecase,ui/{nav,theme,components,screens/{ExpenseEntryScreen,ExpenseListScreen,ExpenseReportScreen},widgets},util,export,sync}

# Create Kotlin files
touch $BASE_DIR/MainActivity.kt
touch $BASE_DIR/MainApplication.kt

# DI
touch $BASE_DIR/di/AppModule.kt
touch $BASE_DIR/di/DatabaseModule.kt
touch $BASE_DIR/di/RepositoryModule.kt

# Data layer
touch $BASE_DIR/data/local/AppDatabase.kt
touch $BASE_DIR/data/local/dao/ExpenseDao.kt
touch $BASE_DIR/data/local/entity/ExpenseEntity.kt
touch $BASE_DIR/data/repository/ExpenseRepositoryImpl.kt
touch $BASE_DIR/data/mapper/ExpenseMapper.kt

# Domain layer
touch $BASE_DIR/domain/model/Expense.kt
touch $BASE_DIR/domain/repository/ExpenseRepository.kt
touch $BASE_DIR/domain/usecase/AddExpenseUseCase.kt
touch $BASE_DIR/domain/usecase/GetExpensesUseCase.kt
touch $BASE_DIR/domain/usecase/ExportExpensesUseCase.kt

# UI
touch $BASE_DIR/ui/nav/AppNavGraph.kt
touch $BASE_DIR/ui/theme/{Color.kt,Type.kt,Theme.kt}
touch $BASE_DIR/ui/components/{ExpenseItem.kt,TopBarTotal.kt,ImagePickerComposable.kt}
touch $BASE_DIR/ui/screens/ExpenseEntryScreen/{ExpenseEntryScreen.kt,ExpenseEntryViewModel.kt}
touch $BASE_DIR/ui/screens/ExpenseListScreen/{ExpenseListScreen.kt,ExpenseListViewModel.kt}
touch $BASE_DIR/ui/screens/ExpenseReportScreen/{ExpenseReportScreen.kt,ExpenseReportViewModel.kt}
touch $BASE_DIR/ui/widgets/{EmptyState.kt,GroupToggleRow.kt}

# Utils, export, sync
touch $BASE_DIR/util/{DateUtils.kt,Validation.kt,ImageUtils.kt}
touch $BASE_DIR/export/{CsvExporter.kt,PdfExporter.kt}
touch $BASE_DIR/sync/SyncManager.kt

# Create resource files
mkdir -p $RES_DIR/values $RES_DIR/drawable $RES_DIR/mipmap
touch $RES_DIR/values/{strings.xml,colors.xml,dimens.xml}

# Create manifest
mkdir -p app/src/main
touch app/src/main/AndroidManifest.xml

echo "All folders and empty files created successfully!"
