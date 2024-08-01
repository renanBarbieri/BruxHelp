package br.com.bruxismhelper.feature.registerBruxism.presentation.model

sealed class Identifier

data object TemporalLeftIdentifier : Identifier()
data object TemporalRightIdentifier: Identifier()

data object MasseterLeftIdentifier : Identifier()
data object MasseterRightIdentifier: Identifier()

data object AtmLeftIdentifier : Identifier()
data object AtmRightIdentifier: Identifier()