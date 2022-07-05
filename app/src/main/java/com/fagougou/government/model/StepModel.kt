package com.fagougou.government.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class StepModel(
    val steps: SnapshotStateList<String> = mutableStateListOf(),
    val currentIndex: MutableState<Int> = mutableStateOf(-1)
)