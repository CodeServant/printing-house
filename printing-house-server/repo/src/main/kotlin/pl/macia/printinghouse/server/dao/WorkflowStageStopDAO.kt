package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.WorkflowStageStop

internal interface WorkflowStageStopDAO : JpaRepository<WorkflowStageStop, Int>, WorkflowStageStopDAOCustom