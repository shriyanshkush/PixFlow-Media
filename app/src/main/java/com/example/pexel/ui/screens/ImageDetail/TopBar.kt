package com.example.pexel.ui.screens.ImageDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pexel.R
import com.example.pexel.ui.components.HeartButton

@Composable
fun TopBar(
    onBackClick: () -> Unit = {},
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Section (Back)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBackClick() }
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_back_basic_svgrepo_com),
                contentDescription = "Back",
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Back",
                fontSize = 16.sp
            )
        }

        // Heart button in top-right corner
        HeartButton(
            isFavorite = isFavorite,
            onToggle = onToggleFavorite,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}
