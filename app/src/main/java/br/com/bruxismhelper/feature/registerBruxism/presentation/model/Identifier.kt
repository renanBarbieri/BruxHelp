package br.com.bruxismhelper.feature.registerBruxism.presentation.model

sealed class Identifier

data object TopLeftIdentifier : Identifier()
data object TopRightIdentifier: Identifier()
data object BottomLeftIdentifier: Identifier()
data object BottomRightIdentifier: Identifier()