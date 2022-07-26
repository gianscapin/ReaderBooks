package com.gscapin.readbookcompose.screens.update

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.gscapin.readbookcompose.components.InputField
import com.gscapin.readbookcompose.components.RatingBar
import com.gscapin.readbookcompose.components.ReaderAppBar
import com.gscapin.readbookcompose.components.updateBookButton
import com.gscapin.readbookcompose.data.DataOrException
import com.gscapin.readbookcompose.model.Book
import com.gscapin.readbookcompose.screens.home.HomeViewModel
import com.gscapin.readbookcompose.utils.TimeAgo

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
            showProfile = false,
            onBackArrowClicked = {
                navController.popBackStack()
            }
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
                if (bookInfo.loading == true) {
                    LinearProgressIndicator()
                    bookInfo.loading = false
                } else {
                    Surface(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        shape = CircleShape,
                        border = BorderStroke(width = 2.dp, color = Color.LightGray),
                        elevation = 4.dp
                    ) {
                        ShowBookUpdate(bookInfo = viewModel.data.value, bookItemId = bookItemId)

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    EnterThoughts(
                        bookInfo = viewModel.data.value,
                        bookItemId = bookItemId,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
private fun EnterThoughts(
    bookInfo: DataOrException<List<Book>, Boolean, Exception>,
    bookItemId: String,
    navController: NavController
) {
    if (bookInfo.data != null) {
        val bookSelected = bookInfo.data!!.filter { book ->
            book.googleBookId.toString() == bookItemId
        }
        Log.d("Book", bookSelected.toString())
        if (!bookSelected.isNullOrEmpty()) {
            val notes = remember {
                mutableStateOf("")
            }
            SimpleForm(
                defaultValue = if (bookSelected.get(0).notes?.isNotEmpty() == true) bookSelected.get(
                    0
                ).notes.toString() else "No thoughts yet"
            ) { note ->
                notes.value = note

            }

            val startedReading = remember {
                mutableStateOf(false)
            }

            val finishedReading = remember {
                mutableStateOf(false)
            }
            Row(modifier = Modifier.padding(bottom = 25.dp)) {
                if (bookSelected.get(0).startedReading == null) {
                    TextButton(
                        enabled = if (!bookSelected.get(0).startedReading.toString()
                                .isNullOrEmpty()
                        ) true else false,
                        onClick = {
                            startedReading.value = !startedReading.value
                        }) {
                        Text(
                            text = if (!startedReading.value) "Start reading" else "Started",
                            style = MaterialTheme.typography.h6
                        )
                    }
                }

                if (bookSelected.get(0).finishedReading == null) {
                    TextButton(enabled = if (!bookSelected.get(0).finishedReading.toString()
                            .isNullOrEmpty()
                    ) true else false,
                        onClick = {
                            finishedReading.value = !finishedReading.value
                        }) {
                        Text(
                            text = if (!finishedReading.value) "Mark as read" else "Marked",
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
            }

            if (bookSelected.get(0).startedReading != null) {
                Text(
                    text = "Started at ${TimeAgo.getTime(bookSelected.get(0).startedReading!!.seconds.toInt())}",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }

            if (bookSelected.get(0).finishedReading != null) {
                Text(
                    text = "Finished at ${TimeAgo.getTime(bookSelected.get(0).finishedReading!!.seconds.toInt())}",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }

            Text(
                text = "Rating",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 25.dp)
            )

            val ratingBook = remember {
                mutableStateOf(bookSelected.get(0).rating!!.toInt())
            }

            RatingBar(rating = ratingBook.value) {
                ratingBook.value = it
            }

            val bookBefore = bookSelected.get(0)
            val updateNotes = bookBefore.notes.toString() != notes.value
            val updateStartedReading = if (!bookBefore.startedReading.toString()
                    .isNullOrEmpty() && startedReading.value
            ) Timestamp.now() else null
            val updateFinishedReading =
                if (!bookBefore.finishedReading.toString().isNullOrEmpty() && finishedReading.value
                ) Timestamp.now() else null
            val updateRating = ratingBook.value != bookBefore.rating!!.toInt()
            val context = LocalContext.current

            val bookToUpdate = hashMapOf(
                "finishedReading" to updateFinishedReading,
                "startedReading" to updateStartedReading,
                "rating" to ratingBook.value.toDouble(),
                "notes" to notes.value
            ).toMap()

            val bookUpdate =
                updateNotes || updateRating || startedReading.value || finishedReading.value

            Row(modifier = Modifier.padding(top = 100.dp)) {
                updateBookButton(text = "Update") {
                    if (bookUpdate) {
                        FirebaseFirestore.getInstance().collection("books")
                            .document(bookBefore.id!!).update(bookToUpdate)
                            .addOnCompleteListener {
                                showToast(context, "Book updated!")
                                navController.popBackStack()
                            }
                            .addOnFailureListener {
                                showToast(context, "Error has occurred.")
                            }
                    }
                }
                Spacer(modifier = Modifier.width(30.dp))
                updateBookButton(text = "Cancel") {
                    navController.popBackStack()
                }
            }


        }
    }
}

fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "Great book",
    onSearch: (String) -> Unit
) {
    Column {
        val textFieldValue = rememberSaveable {
            mutableStateOf(defaultValue)
        }

        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember {
            textFieldValue.value.trim().isNotEmpty()
        }

        InputField(
            modifier
                .fillMaxWidth()
                .height(150.dp),
            valueState = textFieldValue,
            labelId = "Enter your thoughts",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(textFieldValue.value.trim())
                if (keyboardController != null) {
                    keyboardController.hide()
                }
            },
            isSingleLine = false
        )
    }
}

@Composable
fun ShowBookUpdate(bookInfo: DataOrException<List<Book>, Boolean, Exception>, bookItemId: String) {
    if (bookInfo.data != null) {
        val bookSelected = bookInfo.data!!.filter { book ->
            book.googleBookId.toString() == bookItemId
        }
        Log.d("Book", bookSelected.toString())
        if (!bookSelected.isNullOrEmpty()) {
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = rememberAsyncImagePainter(bookSelected.get(0).photoUrl),
                    contentDescription = "book image",
                    modifier = Modifier
                        .height(110.dp)
                        .width(80.dp)
                        .padding(5.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = bookSelected.get(0).title.toString(),
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        style = MaterialTheme.typography.h5
                    )
                    Text(
                        text = bookSelected.get(0).authors.toString(),
                        maxLines = 2,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = bookSelected.get(0).publishedDate.toString(),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}
