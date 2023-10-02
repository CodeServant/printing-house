package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.WorkflowDirGraph

internal interface WorkflowDirGraphDAO : JpaRepository<WorkflowDirGraph, Int>, WorkflowDirGraphDAOCustom