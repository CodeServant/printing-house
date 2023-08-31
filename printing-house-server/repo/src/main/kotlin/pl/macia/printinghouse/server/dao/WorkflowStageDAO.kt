package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.WorkflowStage

internal interface WorkflowStageDAO : JpaRepository<WorkflowStage, Int>, WorkflowStageDAOCustom