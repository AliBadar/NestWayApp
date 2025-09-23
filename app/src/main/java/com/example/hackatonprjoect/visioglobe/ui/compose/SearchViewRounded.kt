package com.hia.android.ui.compose

import android.util.Log
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.visioglobe.ui.model.Poi
import com.hia.android.ui.theme.GreyColor
import com.hia.android.ui.theme.GreyTextColor
import com.hia.android.ui.theme.HiAppRedesignTheme
import com.hia.android.ui.theme.Pink40
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchViewRounded(
    modifier: Modifier = Modifier,
    isSearching: Boolean = false,
    pois: List<Poi> = emptyList(),
    onSearchStateChange: (searching: Boolean) -> Unit = {},
    onQueryChange: (text: String) -> Unit = {},
    cancelSearch: () -> Unit = {},
    goToPoi: (poiId: String) -> Unit = {},
) {

    var searchText by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val coroutineContext = rememberCoroutineScope()


    // TODO : Should be done in the viewModel
    val groupedPois = pois.sortedBy { it.name }.groupBy { it.category }

    Column(
        modifier = modifier
            .padding(20.sdp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchText,
            shape = RoundedCornerShape(30.dp),
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
            label = { Text(stringResource(id = R.string.search), color = GreyTextColor) },
            singleLine = true,
            /*colors = TextFieldDefaults.colors(
                backgroundColor = GreyColor,
                textColor = Color.Black, // Text color
                placeholderColor = GreyColor, // Placeholder text color
                focusedBorderColor = GreyColor, // Border color when focused
                unfocusedBorderColor = GreyColor, // Border color when unfocused
                cursorColor = Color.Red // Cursor color
            ),*/
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
                                .padding(8.sdp)
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
                            contentPadding = PaddingValues(all = 10.sdp),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(poi.name)
                                Text(poi.building + " " + poi.floor)
                            }
                        }
                        HorizontalDivider(
                            thickness = 1.sdp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}






@Preview
@Composable
fun SearchViewRoundedPreview() {
    SearchViewRounded(
        isSearching = false,
    )
}

@Preview
@Composable
fun SearchViewCancelRoundedPreview() {
    SearchViewRounded(
        isSearching = true,
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchViewRoundedWithListPreview() {
    HiAppRedesignTheme {
        SearchViewRounded(
            isSearching = true,
            pois = (1..10).map { Poi.preview(it) }
        )
    }
}

