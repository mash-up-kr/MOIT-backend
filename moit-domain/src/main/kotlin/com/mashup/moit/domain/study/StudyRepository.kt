package com.mashup.moit.domain.study

import org.springframework.data.jpa.repository.JpaRepository

interface StudyRepository : JpaRepository<StudyEntity, Long>
