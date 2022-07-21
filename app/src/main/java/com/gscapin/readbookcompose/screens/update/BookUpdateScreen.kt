package com.gscapin.readbookcompose.screens.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gscapin.readbookcompose.components.ReaderAppBar
import com.gscapin.readbookcompose.data.DataOrException
import com.gscapin.readbookcompose.model.Book
import com.gscapin.readbookcompose.screens.home.HomeViewModel

@Composable
fun BookUpdateScreen(
    navController: NavController,
    bookItemId: String,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Update book",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            showProfile = false
        )
    }) {
        val bookInfo = produceState<DataOrException<List<Book>, Boolean, Exception>>(
            initialValue = DataOrException(
                emptyList(),
                true,
                Exception("")
            )
        ) {
            value = viewModel.data.value
        }.value

        androidx.compose.material.Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp),

            ) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = CenterHorizontally
            ) {
                if(bookInfo.loading == true){
                    LinearProgressIndicator()
                    bookInfo.loading = false
                }else{
                    Text(text = viewModel.data.value.data?.get(0)?.title.toString())
                }
            }
        }
    }
}