package br.com.vigiadeposto.ui.theme

import androidx.compose.ui.graphics.Color
import br.com.vigiadeposto.domain.model.Status

fun Status.toColor(): Color {
    return when (this) {
        Status.GREEN -> GreenCombustivel
        Status.YELLOW -> YellowEnergia
        Status.RED -> RedAlerta
        Status.GREY -> Color(0xFF6B7280)
    }
}

fun Status.toBackgroundColor(): Color {
    return when (this) {
        Status.GREEN -> GreenCombustivel.copy(alpha = 0.1f)
        Status.YELLOW -> YellowEnergia.copy(alpha = 0.1f)
        Status.RED -> RedAlerta.copy(alpha = 0.1f)
        Status.GREY -> Color(0xFF6B7280).copy(alpha = 0.1f)
    }
}
