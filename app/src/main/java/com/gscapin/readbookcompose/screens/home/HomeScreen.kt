package com.gscapin.readbookcompose.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.gscapin.readbookcompose.components.*
import com.gscapin.readbookcompose.model.Book
import com.gscapin.readbookcompose.navigation.ReaderScreens

@Composable
fun Home(navController: NavController = NavController(LocalContext.current)) {
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
            HomeContent(navController)
        }
    }
}


@Composable
fun HomeContent(navController: NavController) {

    val listOfBooks = listOf(
        Book(id = "adasda", title = "Hello again", authors = "All of us", notes = null),
        Book(id = "adasda", title = "Hello", authors = "All of us", notes = null),
        Book(id = "adasda", title = "Again", authors = "All of us", notes = null),
    )
    val currentUserName = if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) else
        "N/A"

    Column(Modifier.padding(2.dp), verticalArrangement = Arrangement.Top) {
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
    ListCard()

    TitleSection(label = "Reading list")

    BookListArea(listOfBooks = books, navController = navController)
}

@Composable
fun BookListArea(listOfBooks: List<Book>, navController: NavController) {
    HorizontalScrollableComponent(listOfBooks){
        // Card click, navigate to details
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<Book>, onCardPressed: (String) -> Unit) {
    val scrollableState = rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollableState)){
        for(book in listOfBooks){
            ListCard(book){
                onCardPressed(it)
            }
        }
    }
}






