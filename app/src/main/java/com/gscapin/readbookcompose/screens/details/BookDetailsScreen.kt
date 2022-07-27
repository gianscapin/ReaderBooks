package com.gscapin.readbookcompose.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gscapin.readbookcompose.components.AdjustSystemBarColor
import com.gscapin.readbookcompose.components.ReaderAppBar
import com.gscapin.readbookcompose.components.RoundedButton
import com.gscapin.readbookcompose.data.Resource
import com.gscapin.readbookcompose.model.Book
import com.gscapin.readbookcompose.model.GBook
import com.gscapin.readbookcompose.model.Item

@Composable
fun BookDetailsScreen(
    navController: NavController,
    bookId: String,
    viewModel: BookDetailsViewModel = hiltViewModel()
) {
    AdjustSystemBarColor()
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Details",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            onBackArrowClicked = {
                navController.popBackStack()
            }
        )
    }) {
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBookInfo(bookId)
                }.value
                if (bookInfo.data == null) {
                    LinearProgressIndicator()
                } else {
                    DetailsBook(bookInfo.data, navController)
                }
            }
        }
    }
}

@Composable
private fun DetailsBook(data: Item, navController: NavController) {
    Card(modifier = Modifier.padding(10.dp), shape = CircleShape) {
        Image(
            painter = rememberAsyncImagePainter(model = data.volumeInfo.imageLinks.thumbnail),
            contentDescription = "image",
            Modifier
                .width(250.dp)
                .height(250.dp)
        )
    }

    Text(
        text = data.volumeInfo!!.title,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h5,
        maxLines = 20
    )
    Text(
        text = "Authors: ${data.volumeInfo!!.authors}",
        style = MaterialTheme.typography.body1,
        maxLines = 20
    )
    Text(
        text = "Pages: ${data.volumeInfo!!.pageCount}",
        style = MaterialTheme.typography.body1,
        maxLines = 20
    )
    Text(
        text = "Categories: ${data.volumeInfo!!.categories.toString()}",
        style = MaterialTheme.typography.body1,
        maxLines = 20
    )
    Text(
        text = "Published: ${data.volumeInfo!!.publishedDate}",
        style = MaterialTheme.typography.body1,
        maxLines = 20
    )

    val cleanDescription =
        HtmlCompat.fromHtml(data.volumeInfo.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

    val localDims = LocalContext.current.resources.displayMetrics
    Surface(
        modifier = Modifier
            .height(localDims.heightPixels.dp.times(0.09f))
            .padding(4.dp),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.Black)
    ) {
        LazyColumn(modifier = Modifier.padding(3.dp)) {
            item {
                Text(text = cleanDescription.toString())
            }
        }
    }

    Row(
        modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        RoundedButton(label = "Save", radius = 50) {
            // save to db

            val book = Book(
                googleBookId = data.id,
                title = data.volumeInfo.title,
                authors = data.volumeInfo.authors.toString(),
                description = data.volumeInfo.description,
                publishedDate = data.volumeInfo.publishedDate,
                pageCount = data.volumeInfo.pageCount.toString(),
                rating = 0.0,
                notes = "",
                userId = FirebaseAuth.getInstance().currentUser!!.uid,
                categories = if(data.volumeInfo.categories != null) data.volumeInfo.categories.toString() else "",
                photoUrl = data.volumeInfo.imageLinks.thumbnail
            )

            saveToFirebase(data = book, navController = navController)
        }
        Spacer(modifier = Modifier.width(20.dp))
        RoundedButton(label = "Cancel", radius = 50) {
            navController.popBackStack()
        }
    }


}

fun saveToFirebase(data: Book, navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    val dbCollection = db.collection("books")

    if (data.toString().isNotEmpty()) {
        dbCollection.add(data).addOnSuccessListener { documentRef ->
            val docId = documentRef.id
            dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        navController.popBackStack()
                    }
                }.addOnFailureListener{
                    Log.w("Save book:", it.message.toString())
                }
        }
    } else {

    }
}
