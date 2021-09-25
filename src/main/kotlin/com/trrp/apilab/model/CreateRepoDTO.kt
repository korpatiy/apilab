package com.trrp.apilab.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
class CreateRepoDTO(
    //todo Not blank valid spring sec
    var name: String = "",

    val description: String? = "",

    @JsonProperty("private")
    private val isPrivate: Boolean? = false
)