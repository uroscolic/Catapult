package com.rma.catapult.catImages.grid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rma.catapult.catImages.grid.CatImageGridContract.CatImageGridUiState
import com.rma.catapult.catImages.model.CatImageUiModel
import com.rma.catapult.core.compose.AppIconButton
import com.rma.catapult.core.compose.CatImagePreview
import com.rma.catapult.core.theme.CatapultTheme
import com.rma.catapult.core.theme.Samsung

fun NavGraphBuilder.catImageGrid(
    route: String,
    arguments: List<NamedNavArgument>,
    onImageClick: (String, Int) -> Unit,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->
    val catImageGridViewModel = hiltViewModel<CatImageGridViewModel>(navBackStackEntry)
    val catId = navBackStackEntry.arguments?.getString("catId")
        ?: throw IllegalStateException("catId required")


    val state = catImageGridViewModel.state.collectAsState()
    CatImageGridScreen(
        state = state.value,
        onImageClick = onImageClick,
        onClose = onClose,
        catId = catId,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatImageGridScreen(
    state: CatImageGridUiState,
    onImageClick: (catImageId: String, index : Int) -> Unit,
    onClose: () -> Unit,
    catId: String,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = "Cat Images",
                    textAlign = TextAlign.Center,
                    fontFamily = Samsung,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                ) },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
            )
        },
        content = { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier,
                contentAlignment = Alignment.BottomCenter,
            ) {
                val screenWidth = this.maxWidth
                val cellSize = (screenWidth / 2) - 4.dp

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    itemsIndexed(
                        items = state.catImages,
                        key = { index: Int, catImage: CatImageUiModel ->
                            catImage.id
                        },
                    ) { index: Int, catImage: CatImageUiModel ->
                        Card(
                            modifier = Modifier
                                .size(cellSize)
                                .clickable {
                                    onImageClick(catId, index)
                                },
                        ) {
                            CatImagePreview(
                                modifier = Modifier.fillMaxSize(),
                                catImage = catImage,
                            )
                        }
                    }

                    item(
                        span = {
                            GridItemSpan(2)
                        }
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 32.dp),
                            text = "This is the end.",
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun CatImagePreviewScreen()
{
    val catImagesList = listOf(
        CatImageUiModel(id = "LSaDk6OjY",
        url = "https://cdn2.thecatapi.com/images/LSaDk6OjY.jpg"),
        CatImageUiModel(id = "da",
        url = "https://cdn2.thecatapi.com/images/LSaDk6OjY.jpg"),
        CatImageUiModel(id = "aaaa",
            url = "https://cdn2.thecatapi.com/images/LSaDk6OjY.jpg"),
        CatImageUiModel(id = "LSaDsd",
            url = "https://cdn2.thecatapi.com/images/LSaDk6OjY.jpg")
    )

    CatapultTheme {
        CatImageGridScreen(state = CatImageGridUiState(
            loading = false,
            catImages = catImagesList

        ), onImageClick = { _, _ -> }, onClose = {}, catId = "catId")
            



    }
}
