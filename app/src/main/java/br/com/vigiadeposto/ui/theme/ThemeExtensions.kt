package br.com.vigiadeposto.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Extensões para facilitar o acesso às cores do tema
 */

val ColorScheme.bluePetroleo: Color
    @Composable
    get() = BluePetroleo

val ColorScheme.greenCombustivel: Color
    @Composable
    get() = GreenCombustivel

val ColorScheme.yellowEnergia: Color
    @Composable
    get() = YellowEnergia

val ColorScheme.redAlerta: Color
    @Composable
    get() = RedAlerta

val ColorScheme.grayLight: Color
    @Composable
    get() = GrayLight

/**
 * Funções de conveniência para acessar cores do tema
 */
@Composable
fun MaterialTheme.bluePetroleo(): Color = colorScheme.bluePetroleo

@Composable
fun MaterialTheme.greenCombustivel(): Color = colorScheme.greenCombustivel

@Composable
fun MaterialTheme.yellowEnergia(): Color = colorScheme.yellowEnergia

@Composable
fun MaterialTheme.redAlerta(): Color = colorScheme.redAlerta

@Composable
fun MaterialTheme.grayLight(): Color = colorScheme.grayLight
