package com.bn.meow.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.bn.meow.R
import com.bn.meow.data.models.CatsResponseItem
import com.bn.meow.data.network.Resource
import com.bn.meow.ui.components.AppBar
import com.bn.meow.ui.components.LoadingAnimation3
import com.bn.meow.ui.components.imageLoadingIndicator
import com.bn.meow.ui.theme.BACKGROUND_COLOR
import com.bn.meow.ui.theme.Purple40


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun MainScreen(mViewModel: MainViewModel? = null) {
    val catss = mViewModel?.cats?.collectAsState()
    mViewModel?.setStateEvent(MainStateEvent.GetCats)

    val isRefreshing by mViewModel?.isRefreshing!!.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        isRefreshing,
        { mViewModel?.setStateEvent(MainStateEvent.GetCats) })


    Scaffold(topBar = {
        AppBar(
            barTitle = stringResource(R.string.app_name),
            appBarBackground = MaterialTheme.colors.primary,
            appBarContentColor = MaterialTheme.colors.onPrimary,
            trailingIcon = R.drawable.cats,
            onTrailingIconClicked = {}
        )
    },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                contentColor = Purple40
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gift),
                    "Prizes"
                )

            }
        }
    ) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Box(
                modifier = Modifier
                    .pullRefresh(pullRefreshState)
                    .background(color = BACKGROUND_COLOR)
                    .fillMaxHeight()
                    .fillMaxWidth(),
            ) {
                catss?.value?.let {
                    when (it) {
                        is Resource.Failure -> {
                            Toast.makeText(
                                LocalContext.current,
                                "${it.exception}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        Resource.Loading -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(1.0f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LoadingAnimation3()
                            }
                        }

                        is Resource.Success -> {
                            val currentList = remember { it.result }
                            catsContent(list = currentList)
                        }
                    }
                }


                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )


            }
        }
    }
}


@Composable
fun catsContent(
    list: List<CatsResponseItem>? = null,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(168.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 2.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(list?.size!!) { index ->
                var cat = list[index]
                catItem(cat)
            }
        }
    )

}

@Composable
private fun catItem(cat: CatsResponseItem) {
    Column(modifier = Modifier.wrapContentHeight()) {
        Card(
            modifier = Modifier
                .padding(4.dp),
            shape = RoundedCornerShape(15.dp),
        ) {
            SubcomposeAsyncImage(
                model = cat.url,
                loading = {
                    imageLoadingIndicator()
                },
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .height(180.dp)
                    .width(170.dp),
                contentScale = ContentScale.FillBounds
            )
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .background(
                        Purple40,
                        shape = RoundedCornerShape(
                            topEnd = 10.dp,
                            bottomEnd = 8.dp
                        )
                    ),
            ) {
                var text = ""
                if (cat.breeds.isNullOrEmpty()) {
                    text = "${cat.id}"
                } else {
                    text = "${cat.breeds!![0].name}"
                }
                Text(
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        )
                        .fillMaxWidth(),
                    maxLines = 1,
                    text = text,
                    color = Color.White,
                )
            }

        }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val likeState = remember { mutableStateOf(false) }
            Image(
                painter = if (likeState.value == true) painterResource(id = R.drawable.liked) else painterResource(
                    id = R.drawable.not_like
                ),
                "Like",
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable {
                        likeState.value = !likeState.value!!
                    },
                contentScale = ContentScale.Fit
            )


        }

    }
}
