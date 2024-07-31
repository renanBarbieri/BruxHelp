package br.com.bruxismhelper.feature.registerBruxism.presentation

import br.com.bruxismhelper.feature.registerBruxism.domain.model.BruxismRegion
import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.BottomLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.BottomRightIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.RegisterBruxismViewState
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.SelectableImage
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.TopLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.TopRightIdentifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegsiterBruxismViewMapper @Inject constructor() {
    fun fromViewToDomain(formView: RegisterBruxismViewState): RegisterBruxismForm {
        return RegisterBruxismForm(
            isEating = formView.isEating,
            selectedActivity = formView.selectedActivity,
            isInPain = formView.isInPain,
            painLevel = formView.painLevel,
            stressLevel = formView.stressLevel,
            anxietyLevel = formView.anxietyLevel,
            selectableImages = formView.selectableImages.map { it.toDomain() },
        )
    }

    private fun SelectableImage.toDomain(): BruxismRegion {
        val name = when(this.id) {
            BottomLeftIdentifier -> "CANTO_INFERIOR_ESQUERDO VER COM NAT"
            BottomRightIdentifier -> "CANTO_INFERIOR_DIREITO VER COM NAT"
            TopLeftIdentifier -> "CANTO_SUPERIOR_ESQUERDO VER COM NAT"
            TopRightIdentifier -> "CANTO_SUPERIOR_DIREITO VER COM NAT"
        }

        return BruxismRegion(name, this.isSelected)
    }
}