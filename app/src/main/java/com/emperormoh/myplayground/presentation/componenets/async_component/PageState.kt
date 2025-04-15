package com.emperormoh.myplayground.presentation.componenets.async_component

sealed interface PageState {

    data object Loaded : PageState

    data object Loading : PageState

    data class Error(val error: String) : PageState

}