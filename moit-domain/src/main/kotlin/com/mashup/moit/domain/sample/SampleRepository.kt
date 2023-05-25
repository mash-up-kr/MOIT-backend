package com.mashup.moit.domain.sample

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SampleRepository : JpaRepository<SampleEntity, Long>
