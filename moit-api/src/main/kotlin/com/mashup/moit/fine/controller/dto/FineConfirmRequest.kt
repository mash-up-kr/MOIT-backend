package com.mashup.moit.fine.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull


@Schema(description = "벌금 송금 평가 RequestBody")
data class FineEvaluateRequest(
    @Schema(description = "벌금 송금 승인 여부")
    @field:NotNull
    val confirm: Boolean,
)
