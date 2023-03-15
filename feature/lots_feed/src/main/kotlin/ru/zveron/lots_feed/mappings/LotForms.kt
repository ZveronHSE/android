package ru.zveron.lots_feed.mappings

import ru.zveron.contract.parameter.external.LotForm
import ru.zveron.lots_feed.models.lot_form.LotForm as DomainLotForm

fun LotForm.toDomainModel(): DomainLotForm {
    return DomainLotForm(this.id, this.name)
}