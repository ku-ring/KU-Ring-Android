package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme

@Composable
fun NonLazyGrid(
    columns: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable (Int) -> Unit
) {
    val rows = (itemCount - 1) / columns + 1
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
    ) {
        repeat(rows) { row ->
            Row(
                horizontalArrangement = horizontalArrangement,
                modifier = Modifier.fillMaxWidth(),
            ) {
                repeat(columns) { column ->
                    val index = row * columns + column
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (index < itemCount) {
                            content(index)
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun NonLazyGridPreview() {
    KuringTheme {
        NonLazyGrid(
            columns = 3,
            itemCount = 8,
        ) {
            Text(
                text = it.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}