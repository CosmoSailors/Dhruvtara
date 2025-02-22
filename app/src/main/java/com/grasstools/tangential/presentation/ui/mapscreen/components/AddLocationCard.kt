package com.grasstools.tangential.presentation.ui.mapscreen.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grasstools.tangential.R

@Composable
    fun AddLocationCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Card Content",
                modifier = Modifier.padding(16.dp)
            )
            content()

        }


}