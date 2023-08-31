package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.BindingForm

internal interface BindingFormDAO : JpaRepository<BindingForm, Int>, BindingFormDAOCustom