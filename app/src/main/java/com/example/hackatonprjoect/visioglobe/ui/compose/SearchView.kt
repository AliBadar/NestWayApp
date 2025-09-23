package com.example.hackatonprjoect.visioglobe.ui.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hackatonprjoect.ui.theme.HackatonPrjoectTheme
import com.example.hackatonprjoect.visioglobe.ui.model.Poi
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    isSearching: Boolean = false,
    pois: List<Poi> = emptyList(),
    onSearchStateChange: (searching: Boolean) -> Unit = {},
    onQueryChange: (text: String) -> Unit = {},
    cancelSearch: () -> Unit = {},
    goToPoi: (poiId: String) -> Unit = {},
) {

    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val coroutineContext = rememberCoroutineScope()

    // TODO : Should be done in the viewModel
    val groupedPois = pois.sortedBy { it.name }.groupBy { it.category }

    Card(
        modifier = modifier
            .padding(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = RoundedCornerShape(20.0f),
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchText,
            onValueChange = {
                searchText = it
                if (searchText.isNotEmpty()){
                    onSearchStateChange(true)
                    coroutineContext.launch {
                        onQueryChange(searchText)
                    }
                } else {
                    onSearchStateChange(false)
                    cancelSearch()
                }
            },
            label = { Text("Search") },
            singleLine = true,
            trailingIcon = {
                if (isSearching) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Cancel",
                        modifier = Modifier
                            .clickable {
                                cancelSearch()
                                onSearchStateChange(false)
                                searchText = ""
                                focusManager.clearFocus()
                            }
                    )
                }
            },
        )
        androidx.compose.animation.AnimatedVisibility(
            modifier = modifier,
            visible = (isSearching && pois.isNotEmpty()),
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            LazyColumn(
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(0.0f, 0.0f, 10.0f, 10.0f)
                    )
            ) {
                groupedPois.forEach { (category, pois) ->
                    stickyHeader {
                        Text(
                            text = category.name,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp)
                        )
                    }
                    items(pois) { poi ->
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                focusManager.clearFocus()
                                onSearchStateChange(false)
                                searchText = ""
                                cancelSearch()
                                goToPoi(poi.id)
                            },
                            contentPadding = PaddingValues(all = 10.dp),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(poi.name)
                                Text(poi.building + " " + poi.floor)
                            }
                        }
                        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchViewPreview() {
    SearchView(
        isSearching = false,
    )
}

@Preview
@Composable
fun SearchViewCancelPreview() {
    SearchView(
        isSearching = true,
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchViewWithListPreview() {
    HackatonPrjoectTheme {
        SearchView(
            isSearching = true,
            pois = (1..10).map { Poi.preview(it) }
        )
    }
}

