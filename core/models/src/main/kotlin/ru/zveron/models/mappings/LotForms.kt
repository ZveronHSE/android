package ru.zveron.models.mappings

import ru.zveron.contract.parameter.external.LotForm
import ru.zveron.models.lot_form.LotForm as DomainLotForm

fun LotForm.toLotFormDomainModel(): DomainLotForm {
    return DomainLotForm(this.id, this.name)
}