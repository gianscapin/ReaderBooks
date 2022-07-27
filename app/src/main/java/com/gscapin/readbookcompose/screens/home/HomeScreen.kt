package com.gscapin.readbookcompose.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.gscapin.readbookcompose.components.*
import com.gscapin.readbookcompose.model.Book
import com.gscapin.readbookcompose.navigation.ReaderScreens

@Composable
fun Home(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    AdjustSystemBarColor()
    Scaffold(
        topBar = {
            ReaderAppBar("Reader", navController = navController)
        },
        floatingActionButton = {
            FABContent {
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController, viewModel)
        }
    }
}


@Composable
fun HomeContent(navController: NavController, viewModel: HomeViewModel) {


    var listOfBooks = emptyList<Book>()

    val currentUser = FirebaseAuth.getInstance().currentUser

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks = viewModel.data.value.data!!.toList().filter { book ->
            book.userId == currentUser?.uid.toString()
        }
    }

    val currentUserName = if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) else
        "N/A"

    Column(
        Modifier
            .padding(2.dp)
            .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Your reading activity \n right now.")
            Spacer(modifier = Modifier.width(90.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.StatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant
                )
                Text(
                    text = currentUserName!!,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.body1,
                    color = Color.Red,
                    //fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
        ReadingRightNowArea(books = listOfBooks, navController = navController)
    }
}


@Composable
fun ReadingRightNowArea(books: List<Book>, navController: NavController) {
    val booksReading = books.filter { it.startedReading != null && it.finishedReading == null }
    val booksToRead = books.filter { it.startedReading == null }
    val booksFinished = books.filter { it.finishedReading != null }
    BookListArea(listOfBooks = booksReading, navController = navController)

    TitleSection(label = "To read list")

    BookListArea(listOfBooks = booksToRead, navController = navController)

    TitleSection(label = "Books finished")

    BookListArea(listOfBooks = booksFinished, navController = navController)
}

@Composable
fun BookListArea(listOfBooks: List<Book>, navController: NavController) {
    HorizontalScrollableComponent(listOfBooks) {
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfBooks: List<Book>,
    onCardPressed: (String) -> Unit
) {
    val scrollableState = rememberScrollState()


    if (listOfBooks.isEmpty()) {
        Column(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Any book is here now, start someone!", color = Color.LightGray)
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(280.dp)
                .horizontalScroll(scrollableState)
        ) {
            for (book in listOfBooks) {
                ListCard(book) {
                    onCardPressed(book.googleBookId.toString())
                }
            }

        }
    }


}






