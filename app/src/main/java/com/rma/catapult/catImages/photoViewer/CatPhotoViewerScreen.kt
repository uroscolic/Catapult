package com.rma.catapult.catImages.photoViewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rma.catapult.catImages.photoViewer.CatPhotoViewerContract.CatPhotoViewerUiState
import com.rma.catapult.core.compose.AppIconButton
import com.rma.catapult.core.compose.CatImagePreview
import com.rma.catapult.core.theme.Samsung

fun NavGraphBuilder.catPhotoViewer(
    route: String,
    arguments: List<NamedNavArgument>,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->

    val catPhotoViewerViewModel = hiltViewModel<CatPhotoViewerViewModel>(navBackStackEntry)

    val startIndex = navBackStackEntry.arguments?.getInt("startIndex")
        ?: throw IllegalStateException("startIndex required")


    val state = catPhotoViewerViewModel.state.collectAsState()
    CatPhotoViewer(
        state = state.value,
        onClose = onClose,
        startIndex = startIndex,

    )
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CatPhotoViewer(
    state: CatPhotoViewerUiState,
    onClose: () -> Unit,
    startIndex: Int = 0,
) {
    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = {
            state.catImages.size
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
            )
        },
        content = { paddingValues ->
            if (state.catImages.isNotEmpty()) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues,
                    pageSize = PageSize.Fill,
                    pageSpacing = 16.dp,
                    state = pagerState,
                    key = {
                        val image = state.catImages[it]
                        image.id
                    }
                ) { pageIndex ->
                    val image = state.catImages[pageIndex]
                    CatImagePreview(
                        modifier = Modifier,
                        catImage = image,
                    )
                }

            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No images.",
                    fontFamily = Samsung,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                )
            }
        },
    )
}