package com.trrp.apilab.model

import com.fasterxml.jackson.annotation.JsonProperty

class GithubRepo(

    var id: Long = 0L,

    val name: String = "",

    @JsonProperty("full_name")
    val fullName: String? = "",

    val description: String? = "",

    @JsonProperty("private")
    val isPrivate: String? = "",

    @JsonProperty("fork")
    val isFork: String? = "",

    val url: String? = "",

    @JsonProperty("html_url")
    val htmlUrl: String? = "",

    @JsonProperty("git_url")
    val gitUrl: String? = "",

    @JsonProperty("forks_count")
    val forksCount: Long? = 0L,

    @JsonProperty("stargazers_count")
    val stargazersCount: Long? = 0L,

    @JsonProperty("watchers_count")
    val watchersCount: Long? = 0L,
)