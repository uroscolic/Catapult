package com.rma.catapult.cat.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rma.catapult.cat.details.gold
import com.rma.catapult.core.compose.Loading
import com.rma.catapult.core.compose.TextMessage
import com.rma.catapult.core.theme.Samsung
import com.rma.catapult.cat.list.api.CatListUiEvent
import com.rma.catapult.cat.list.api.model.CatListUiModel
import com.rma.catapult.core.compose.AppIconButton
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.catList(
    route : String,
    onProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onCatSelected: (CatListUiModel) -> Unit,
    onQuizClick: () -> Unit
) {
    composable(route) {

        val catListViewModel = hiltViewModel<CatListViewModel>()
        val state by catListViewModel.state.collectAsState()


        CatListScreen(
            state = state,
            onCatSelected = onCatSelected,
            eventPublisher = { event ->
                catListViewModel.setEvent(event)
            },
            onProfileClick = onProfileClick,
            onEditProfileClick = onEditProfileClick,
            onLeaderboardClick = onLeaderboardClick,
            onQuizClick = onQuizClick

        )
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun CatListScreen(
    state : CatListState,
    onCatSelected: (CatListUiModel) -> Unit,
    eventPublisher: (CatListUiEvent) -> Unit,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    onProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onQuizClick: () -> Unit

) {
    val uiScope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    var showDialog by remember { mutableStateOf(false) }

    BackHandler (enabled = state.searchMode) {
        eventPublisher(CatListUiEvent.ClearSearch)
        query = ""
    }
    BackHandler (enabled = drawerState.isOpen) {
        uiScope.launch {
            drawerState.close()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Starting the Quiz",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Samsung
                ) },
            text = {
                Text(text = "Are you ready?",
                    fontFamily = Samsung

                ) },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onQuizClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gold
                    )
                ) {
                    Text(text = "Yes",
                        fontFamily = Samsung
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gold
                    )
                ) {
                    Text(text = "No",
                        fontFamily = Samsung
                    )
                }
            }
        )
    }

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerState = drawerState,
        drawerContent = {
            CatListDrawer(
                onProfileClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onProfileClick()
                },
                onEditProfileClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onEditProfileClick()
                },
                onLeaderboardClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onLeaderboardClick()
                },
                onQuizClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    showDialog = true
                }
            )
        },
        content = {
            CatListScaffold(
                query,
                eventPublisher,
                state,
                keyboard,
                state.error,
                onCatSelected,
                onDrawerMenuClick = {
                    uiScope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    )

}


@Composable
private fun AppDrawerMenuItem(
    icon: ImageVector,
    text: String,
) {
    Row {
        Icon(imageVector = icon, contentDescription = null)
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = text,
        )
    }
}

@Composable
private fun AppDrawerActionItem(
    icon: ImageVector,
    text: String,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = Modifier.clickable(
            enabled = onClick != null,
            onClick = { onClick?.invoke() }
        ),
        leadingContent = {
            Icon(imageVector = icon,
                contentDescription = null,
            )
        },
        headlineContent = {
            Text(text = text)
        }
    )
}


@Composable
private fun CatListDrawer(
    onProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onQuizClick: () -> Unit
) {

    BoxWithConstraints {

        ModalDrawerSheet(
            modifier = Modifier.width(maxWidth * 3 / 4),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomStart,
                ) {
                    Text(
                        modifier = Modifier.padding(all = 16.dp),
                        text = "Catapult",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,

                    )
                }

                Column(modifier = Modifier.weight(1f)) {

                    AppDrawerActionItem(
                        icon = Icons.Default.Person,
                        text = "Profile",
                        onClick = onProfileClick,
                    )
                    AppDrawerActionItem(
                        icon = Icons.Default.Edit,
                        text = "Edit Profile",
                        onClick = onEditProfileClick,
                    )
                    AppDrawerActionItem(
                        icon = Icons.Default.Star,
                        text = "Leaderboard",
                        onClick = onLeaderboardClick,
                    )
                    AppDrawerActionItem(
                        icon = Icons.Default.PlayArrow,
                        text = "Quiz",
                        onClick = onQuizClick,
                    )

                    /*NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Passwords",
                            )
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                        },
                        selected = false,
                        onClick = {

                        },
                    )*/
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun CatListScaffold(
    searchQuery: String,
    eventPublisher: (CatListUiEvent) -> Unit,
    state: CatListState,
    keyboard: SoftwareKeyboardController?,
    error: Throwable?,
    onCatSelected: (CatListUiModel) -> Unit,
    onDrawerMenuClick: () -> Unit
) {
    var query by remember {
        mutableStateOf(searchQuery)

    }
    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row() {

                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            AppIconButton(imageVector = Icons.Default.Menu, onClick = onDrawerMenuClick)
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
                        if (query.isNotEmpty())
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
                            if (error != null) {
                                TextMessage("Error: ${error.message}")
                            }
                        }
                    }

                    state.cats.isEmpty() -> {
                        item {
                            TextMessage("No cats found! Maybe they are hiding...")
                        }
                    }

                    else -> {
                        items(if (state.searchMode) state.filteredCats else state.cats) { cat ->
                            key(cat.id) {
                                CatListItem(
                                    cat = cat,
                                    onCatSelected = onCatSelected,
                                    color = gold
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
                if(randomNumbers.size > i) {
                    ElevatedSuggestionChip(
                        modifier = Modifier
                            .padding(start = leftPadding),
                        onClick = {},
                        label = { Text(text[randomNumbers[i]]) },
                    )
                }
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
/*
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
}*/