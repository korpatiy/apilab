package com.trrp.apilab.rest.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
class CreateRepoDTO(

    @field:NotEmpty(message = "Name should not be empty")
    //@field:Size(min = 1, max = 50, message = "Название должно содержать хотя бы 1 символ и не превышать 50")
    var name: String = "",

    var description: String? = "",

    @JsonProperty("private")
    @field:NotEmpty(message = "Private should not be empty")
    var isPrivate: Boolean? = false
)