package com.mashup.moit.domain.study

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface StudyRepository : JpaRepository<StudyEntity, Long> {
    // findFirst By MoitId And EndAtAfter OrderByStartAtAsc
    fun findFirstByMoitIdAndEndAtAfterOrderByStartAtAsc(moitId: Long, endAt: LocalDateTime): StudyEntity?

    // findAll By StartAtBefore And IsInitializedFalse
    fun findAllByStartAtBeforeAndIsInitializedFalse(startAt: LocalDateTime): List<StudyEntity>
}
