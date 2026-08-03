package com.example.composereordergrid.presentation.draganddrop.pagedgrid.usecase

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun rememberPageFlipState(
    pagerState: PagerState,
    edgeWidth: Dp = 56.dp,
    flipDelayMillis: Long = 600L,
    scope: CoroutineScope = rememberCoroutineScope()
): PageFlipState = remember(pagerState) {
    PageFlipState(
        pagerState = pagerState,
        edgeWidth = edgeWidth,
        flipDelayMillis = flipDelayMillis,
        scope = scope
    )
}

class PageFlipState internal constructor(
    private val pagerState: PagerState,
    private val edgeWidth: Dp,
    private val flipDelayMillis: Long,
    private val scope: CoroutineScope
) {
    private var activeZone: Zone? = null
    private var loopJob: Job? = null

    fun onDrag(position: Offset, containerWidth: Int, density: Density) {
        val edgePx = with(density) { edgeWidth.toPx() }
        activeZone = when {
            position.x < edgePx && pagerState.currentPage > 0 -> Zone.PREVIOUS
            position.x > containerWidth - edgePx && pagerState.currentPage < pagerState.pageCount - 1 -> Zone.NEXT
            else -> null
        }

        if (loopJob == null) {
            loopJob = scope.launch { runFlipLoop() }
        }
    }

    fun onDragEnded() {
        activeZone = null
        loopJob?.cancel()
        loopJob = null
    }

    private suspend fun runFlipLoop() {
        while (currentCoroutineContext().isActive) {
            delay(flipDelayMillis.milliseconds)
            val zone = activeZone ?: continue
            val targetPage = pagerState.currentPage + zone.delta
            if (targetPage !in 0 until pagerState.pageCount) continue
            pagerState.animateScrollToPage(targetPage)
        }
    }

    private enum class Zone(val delta: Int) {
        PREVIOUS(-1),
        NEXT(1)
    }
}
