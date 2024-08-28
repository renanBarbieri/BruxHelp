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
class RegisterBruxismViewMapper @Inject constructor() {
    fun fromViewToDomain(formView: RegisterBruxismViewState): RegisterBruxismForm {
        with(formView.registerBruxismForm){
            return RegisterBruxismForm(
                isEating = isEating,
                selectedActivity = selectedActivity.getOrNullWhenFalse(isEating),
                stressLevel = stressLevel.getOrNullWhenFalse(isEating),
                anxietyLevel = anxietyLevel.getOrNullWhenFalse(isEating),
                isInPain = isInPain.getOrNullWhenFalse(isEating),
                painLevel = painLevel.getOrNullWhenFalse(isEating || isInPain.not()),
                selectableImages = selectableImages
                    .getOrNullWhenFalse(isEating || isInPain.not())?.map { it.toDomain() },
            )

        }
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