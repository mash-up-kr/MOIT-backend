package com.mashup.moit.domain.study

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface StudyRepository : JpaRepository<StudyEntity, Long> {
    fun findFirstByMoitIdAndEndAtAfterOrderByStartAtAsc(moitId: Long, endAt: LocalDateTime): StudyEntity?
}
