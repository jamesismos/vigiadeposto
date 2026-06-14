package br.com.vigiadeposto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.vigiadeposto.domain.model.Status
import br.com.vigiadeposto.ui.theme.toBackgroundColor
import br.com.vigiadeposto.ui.theme.toColor

enum class StatusIconSize(val size: Int) {
    Small(24),
    Medium(32),
    Large(48)
}

@Composable
fun StatusIcon(
    status: Status,
    size: StatusIconSize = StatusIconSize.Medium,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size.size.dp)
            .clip(CircleShape)
            .background(status.toBackgroundColor()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.icon,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = status.toColor()
        )
    }
}
