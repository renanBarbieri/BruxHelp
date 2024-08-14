package br.com.bruxismhelper.feature.registerBruxism.presentation

import br.com.bruxismhelper.feature.registerBruxism.domain.model.BruxismRegion
import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.AtmLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.AtmRightIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.MasseterLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.MasseterRightIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.RegisterBruxismViewState
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.SelectableImage
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.TemporalLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.TemporalRightIdentifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegsiterBruxismViewMapper @Inject constructor() {
    fun fromViewToDomain(formView: RegisterBruxismViewState): RegisterBruxismForm {
        return RegisterBruxismForm(
            isEating = formView.isEating,
            selectedActivity = formView.selectedActivity.getOrNullWhenFalse(formView.isEating),
            stressLevel = formView.stressLevel.getOrNullWhenFalse(formView.isEating),
            anxietyLevel = formView.anxietyLevel.getOrNullWhenFalse(formView.isEating),
            isInPain = formView.isInPain.getOrNullWhenFalse(formView.isEating),
            painLevel = formView.painLevel
                .getOrNullWhenFalse(formView.isEating || formView.isInPain.not()),
            selectableImages = formView.selectableImages
                .getOrNullWhenFalse(formView.isEating || formView.isInPain.not())?.map { it.toDomain() },
        )
    }

    private fun SelectableImage.toDomain(): BruxismRegion {
        val name = when (this.id) {
            AtmLeftIdentifier -> "Atm Esquerda"
            AtmRightIdentifier -> "Atm Direita"
            MasseterLeftIdentifier -> "Masseter Esquedo"
            MasseterRightIdentifier -> "Masseter Direito"
            TemporalLeftIdentifier -> "Temporal Esquedo"
            TemporalRightIdentifier -> "Temporal Direito"
        }

        return BruxismRegion(name, this.isSelected)
    }

    private fun <T> T.getOrNullWhenFalse(condition: Boolean): T? {
        return if (condition.not()) this else null
    }
}