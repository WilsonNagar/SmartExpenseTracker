package com.wilson.smartexpensetracker.presentation.screens.entry

import CategoryBottomSheetSelectorCustom
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(
    onExpenseSaved: () -> Unit,
    viewModel: ExpenseEntryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Image picker launcher for receipt images
    val launcher = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        viewModel.onReceiptSelected(uri?.toString())
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ExpenseEntryViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ExpenseEntryViewModel.UiEvent.ExpenseAdded -> {
                    Toast.makeText(context, "Expense added", Toast.LENGTH_SHORT).show()
                    onExpenseSaved()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Expense") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Animate total spent today changes smoothly
            Text(
                text = "Total Spent Today: ₹${"%.2f".format(state.totalSpentToday)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.animateContentSize(animationSpec = tween(500))
            )

            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.amount,
                onValueChange = { viewModel.onAmountChange(it) },
                label = { Text("Amount (₹)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            CategoryBottomSheetSelectorCustom(
                selectedCategory = state.category,
                onCategorySelected = { viewModel.onCategoryChange(it) }
            )

            OutlinedTextField(
                value = state.notes,
                onValueChange = {
                    if (it.length <= 100) viewModel.onNotesChange(it)
                },
                label = { Text("Notes (Optional)") },
                singleLine = false,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            // Show remaining chars for notes
            Text(
                text = "${state.notes.length}/100 characters",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )

            ReceiptImagePicker(
                receiptPath = state.receiptPath,
                onAddClicked = { launcher.launch("image/*") },
                onImageClicked = { viewModel.onReceiptSelected(null) }
            )

            Button(
                onClick = { viewModel.saveExpense() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = state.isValidForSubmit()
            ) {
                Text("Save Expense")
            }
        }
    }
}

@Composable
fun ReceiptImagePicker(
    receiptPath: String?,
    onAddClicked: () -> Unit,
    onImageClicked: () -> Unit
) {
    Column {
        Text("Receipt (Optional)", style = MaterialTheme.typography.bodyMedium)

        if (receiptPath != null) {
            Image(
                painter = rememberAsyncImagePainter(receiptPath),
                contentDescription = "Receipt Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable { onImageClicked() },
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Tap image to remove",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .clickable { onAddClicked() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Receipt",
                    tint = Color.Gray
                )
            }
        }
    }
}

// Helper extension to check if form is valid for enabling Save button
private fun ExpenseEntryState.isValidForSubmit(): Boolean {
    val amt = amount.toDoubleOrNull()
    return title.isNotBlank() && (amt != null && amt > 0) && category.isNotBlank()
}
