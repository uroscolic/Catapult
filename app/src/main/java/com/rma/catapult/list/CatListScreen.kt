package com.rma.catapult.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rma.catapult.core.compose.Loading
import com.rma.catapult.core.compose.TextMessage
import com.rma.catapult.core.theme.CatapultTheme
import com.rma.catapult.core.theme.Samsung
import com.rma.catapult.list.api.CatListUiEvent
import com.rma.catapult.list.api.model.CatListUiModel
import com.rma.catapult.repository.SampleDataUiModel
import com.rma.catapult.R


@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.catList(route : String, navController : NavController) {
    composable(route) {

        val catListViewModel = viewModel<CatListViewModel>()
        val state by catListViewModel.state.collectAsState()

        CatListScreen(
            state = state,
            onCatSelected = { cat ->
                navController.navigate("details/${cat.id}")
            },
            eventPublisher = { event ->
                catListViewModel.setEvent(event)
            }
        )
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun CatListScreen(
    state : CatListState,
    onCatSelected: (CatListUiModel) -> Unit,
    eventPublisher: (CatListUiEvent) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    BackHandler (enabled = state.searchMode) {
        eventPublisher(CatListUiEvent.ClearSearch)
        query = ""
    }
    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row() {

                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.cat),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .weight(0.1f)
                                    .padding(start = 8.dp)
                                    .size(24.dp)
                            )
                        },
                        title = {
                            Text(
                                text = "Cat List",
                                fontFamily = Samsung,
                                fontWeight = FontWeight.Medium,
                                fontSize = 24.sp
                            )
                        }
                    )

                }
                Divider()

                SearchBar(
                    modifier = Modifier
                        .padding(bottom = 4.dp),
                    query = query,
                    onQueryChange = {
                        query = it
                        eventPublisher(CatListUiEvent.SearchQueryChanged(it))
                        Log.d("aaabbb - OnQueryChange", state.searchMode.toString())
                        Log.d("aaabbb - OnQueryChange", state.query)
                    },
                    onSearch = {
                        keyboard?.hide()
                    },
                    active = false,
                    onActiveChange = {},
                    placeholder = {
                        Text(
                            "Search",
                            modifier = Modifier.alpha(0.6f),
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    eventPublisher(CatListUiEvent.ClearSearch)
                                    query = ""
                                    Log.d("aaabbb - X", state.searchMode.toString())
                                    Log.d("aaabbb - X", state.query)
                                }
                        )
                    },
                ) {}
                Spacer(modifier = Modifier.padding(2.dp))


            }

        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item {
                    Spacer(modifier = Modifier.padding(16.dp))
                }

                when {
                    state.loading -> {
                        item {
                            Loading()
                        }
                    }

                    state.error != null -> {
                        item {
                            TextMessage("Error: ${state.error.message}")
                        }
                    }

                    state.cats.isEmpty() -> {
                        item {
                            TextMessage("No cats found! Maybe they are hiding...")
                        }
                    }

                    else -> {
                        items(if(state.searchMode) state.filteredCats else state.cats) { cat ->
                            key(cat.id) {
                                CatListItem(
                                    cat = cat,
                                    onCatSelected = onCatSelected,
                                    color = Color.hsl(23f, 0.9f, 0.5f)
                                )
                                Spacer(modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    )
}



@Composable
fun CatListItem(
    cat: CatListUiModel,
    onCatSelected: (CatListUiModel) -> Unit,
    color: Color
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onCatSelected(cat)
            },
    )
    {
        val alternativeName = if (cat.alt_names.isNotEmpty()) " (" + cat.alt_names + ")" else ""
        Text(
            modifier = Modifier
            .padding(all = 16.dp),
            color = color,
            text = cat.name + alternativeName,
            fontSize = 20.sp,
        )
        Row() {
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                text = cutToNCharacters(250, cat.description),
                fontSize = 18.sp
            )
        }
        val text = cat.temperament.split(",")
        Row() {
            val randomNumbers by remember {
                mutableStateOf(text.indices.shuffled())
            }
            for (i in 0 until 3) {
                val leftPadding = if (i == 0) 10.dp else 5.dp
                ElevatedSuggestionChip(
                    modifier = Modifier
                        .padding(start = leftPadding),
                    onClick = {},
                    label = { Text(text[randomNumbers[i]]) },
                )
            }
        }
        Row()
        {
            Box(
                Modifier.weight(0.1f),
                contentAlignment = Alignment.CenterEnd
            )
            {
                Icon(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .padding(top = 10.dp)
                        .padding(bottom = 10.dp),
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Arrow Forward",
                    tint = color,
                )
            }
        }


    }

}
private fun cutToNCharacters(n:Int, text: String): String {
    if(text.length > n)
        return text.substring(0, n) + "..."
    return text
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CatListScreenPreview() {
    CatalistTheme {
        CatListScreen(
            state = CatListState(
                cats = SampleDataUiModel,
                loading = false,
                //error = Error("Error")
            ),

        ) {}
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CatListScreenPreviewNotLoaded() {
    CatapultTheme {
        CatListScreen(
            state = CatListState(
                cats = SampleDataUiModel,
                loading = false
            ),
            onCatSelected = {},

        ) {}
    }
}