package com.gscapin.readbookcompose.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.gscapin.readbookcompose.components.InputField
import com.gscapin.readbookcompose.components.ReaderAppBar
import com.gscapin.readbookcompose.model.Book

@Composable
fun SearchScreen(navController: NavController, viewModel: BookSearchViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                onBackArrowClicked = {
                    navController.popBackStack()
                },
                backgroundColor = Color(0xfff44336)
            )
        }
    ) {
        Surface {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    viewModel = viewModel
                ) { query ->
                    viewModel.searchBooks(query)
                }

                Spacer(modifier = Modifier.height(13.dp))

                BookList(navController)
            }
        }
    }
}

@Composable
fun BookList(navController: NavController) {
    val listOfBooks = listOf(
        Book(id = "adasda", title = "Hello again", authors = "All of us", notes = null),
        Book(id = "adasda", title = "Hello", authors = "All of us", notes = null),
        Book(id = "adasda", title = "Again", authors = "All of us", notes = null),
    )
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        items(items = listOfBooks) { book ->
            BookRow(book, navController)
        }
    }
}

@Composable
fun BookRow(book: Book, navController: NavController) {
    Card(
        modifier = Modifier
            .clickable {

            }
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp
    ) {
        Row(Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {

            val imageUrl = "http://books.google.com/books/content?id=ogXfDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            Image(painter = rememberAsyncImagePainter(model = imageUrl), contentDescription = "image", Modifier.width(80.dp))
            Column {
                Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Authors: ${book.authors.toString()}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    viewModel: BookSearchViewModel,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")
        }

        val keyboardController = LocalSoftwareKeyboardController.current

        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })
    }
}