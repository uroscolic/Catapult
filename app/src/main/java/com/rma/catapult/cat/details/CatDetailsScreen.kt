package com.rma.catapult.cat.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.SubcomposeAsyncImage
import com.rma.catapult.core.compose.AppIconButton
import com.rma.catapult.core.compose.Loading
import com.rma.catapult.core.compose.NoCatFound
import com.rma.catapult.core.compose.TextMessage
import com.rma.catapult.cat.repository.Repository
import com.rma.catapult.core.theme.CatapultTheme
import com.rma.catapult.core.theme.Samsung


fun NavGraphBuilder.details(route : String, navController : NavController) {
    composable(route, arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
            nullable = false
        }
    )
    ){
        navBackStackEntry -> val catId = navBackStackEntry.arguments?.getString("id") ?: throw IllegalStateException("Missing ID")
        val catDetailsViewModel = viewModel<CatDetailsViewModel>(
            factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CatDetailsViewModel(catId = catId) as T
                }
            },
        )
        val state = catDetailsViewModel.state.collectAsState()
        CatDetailsScreen(
            state = state.value,
            onGalleryClick = {
                navController.navigate("catImages/$catId")
            },
            onBack = {
                navController.navigateUp()
            }
        )
    }
}


val orange = Color.hsl(23f, 0.8f, 0.65f)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CatDetailsScreen(
    state : CatDetailsState,
    onGalleryClick: () -> Unit,
    onBack: () -> Unit
) {

    val cat = state.cat
    Scaffold(
        topBar = {
            Column(

                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row() {

                    CenterAlignedTopAppBar(

                        navigationIcon = {
                            AppIconButton(
                                imageVector = Icons.Default.ArrowBack,
                                onClick = onBack,
                            )
                        },
                        title = {
                            Text(
                                text = "Details",
                                fontFamily = Samsung,
                                fontWeight = FontWeight.Medium,
                                fontSize = 24.sp
                            )
                        }
                    )

                }
                Divider()
            }
        },
        content = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(it),
                    //.background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {

                if(cat != null)
                {
                    Spacer(
                        modifier = Modifier.padding(16.dp)
                    )
                    Log.d("CatDetailsScreen", "cat.image.url: ${cat.image.url}")
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = cat.image.url,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = cat.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if(cat.alt_names.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            text = "(${cat.alt_names})",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(
                        modifier = Modifier.padding(16.dp)
                    )
                    RowForShortText(label = "Origin:", text = cat.origin)
                    RowForShortText(label = "Life Span:", text = cat.life_span)
                    RowForShortText(label = "Weight:", text = cat.weight.metric)
                    RowForShortText(label = "Rare:", text = if(cat.rare == 0) "No" else "Yes")

                    val uriHandler = LocalUriHandler.current

                    CustomButton(uriHandler, cat.wikipedia_url)
                    Button(
                        modifier = Modifier
                            .padding(top = 15.dp, bottom = 10.dp),
                        onClick = onGalleryClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = orange
                        )
                    ) {
                            Text(
                                text = "Gallery",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = Samsung
                            )
                    }

                    ColumnForLongText(label = "Temperament:", text = cat.temperament)

                    Spacer(modifier = Modifier.padding(10.dp))

                    CharacteristicWithProgressIndicator(
                        label = "Adaptability",
                        progress = cat.adaptability.toFloat()
                    )
                    CharacteristicWithProgressIndicator(
                        label = "Affection Level",
                        progress = cat.affection_level.toFloat()
                    )
                    CharacteristicWithProgressIndicator(
                        label = "Child Friendly",
                        progress = cat.child_friendly.toFloat()
                    )
                    CharacteristicWithProgressIndicator(
                        label = "Dog Friendly",
                        progress = cat.dog_friendly.toFloat()
                    )
                    CharacteristicWithProgressIndicator(
                        label = "Energy Level",
                        progress = cat.energy_level.toFloat()
                    )

                    ColumnForLongText(label = "Description:", text = cat.description)
                    Spacer(modifier = Modifier.padding(20.dp))

                }
                else
                {
                    if(state.loading)
                        Loading()
                    else if(state.error != null)
                        TextMessage(text = "Error: ${state.error}")
                    else
                        NoCatFound(catId = state.catId)

                }
            }
        }
    )
}

@Composable
private fun CustomButton(
    uriHandler: UriHandler,
    text: String
) {
    Button(
        modifier = Modifier
            .padding(top = 15.dp, bottom = 10.dp),
        onClick = {
            uriHandler.openUri(text)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = orange
        )
    )
    {
        Text(
            text = "Wikipedia link",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Samsung
        )
    }
}

@Composable
fun CharacteristicWithProgressIndicator(label: String, progress: Float) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = Samsung,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                //color = Color.Black
            )
        )
        val strength = progress / 5
        LinearProgressIndicator(

            progress = strength,
            color =
            if(strength >= 0.6f)
                orange
            else
                Color.hsl(23f, 0.5f, 0.4f),

            trackColor = Color(0xFFE0E0E0),
            modifier = Modifier
                .padding(top = 10.dp)
            
        )
    }
}

@Composable
fun ColumnForLongText(label: String, text: String) {
    Spacer(modifier = Modifier.padding(10.dp))
    Column {
        Text(
            modifier = Modifier
                .padding(start = 15.dp, bottom = 10.dp),
            text = label,
            fontSize = 20.sp,
            color = orange,
            fontWeight = FontWeight.Medium
        )
        Text(
            modifier = Modifier
                .padding(start = 15.dp, bottom = 10.dp),
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun RowForShortText(label: String, text: String) {
    Row {
        Text(
            modifier = Modifier
                .padding(start = 15.dp, bottom = 10.dp)
                .weight(0.5f),
            text = label,
            fontSize = 20.sp,
            color = orange,
            fontWeight = FontWeight.Medium
        )
        if(text.contains("http")) {
            Text(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .weight(1f),
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                style = TextStyle(
                    color = Color.hsl(221f, 0.50f, 0.60f),
                    fontFamily = Samsung,
                    textDecoration = TextDecoration.Underline
                )
            )
        } else {
            Text(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .weight(1f),
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }


    }
}

/*
@Composable
@Preview
fun DetailsScreenPreview() {
    CatapultTheme {
        CatDetailsScreen(
            state = CatDetailsState(cat = Repository.allData().first(), catId = "1"),
            onBack = {},
            onGalleryClick = {}
        )
    }
}*/