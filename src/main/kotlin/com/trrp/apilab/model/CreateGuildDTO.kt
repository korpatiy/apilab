package com.trrp.apilab.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.jetbrains.annotations.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
class CreateGuildDTO(
    @NotNull
    var name: String? = null
)