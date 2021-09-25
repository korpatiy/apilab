package com.trrp.apilab.controller

import com.trrp.apilab.service.GithubService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
class GithubController(
    private val githubService: GithubService
) {

    @GetMapping
    fun index(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal oauth2User: OAuth2User,
        model: Model
    ): String? {
        model.addAttribute("repositories", githubService.getAllRepos(authorizedClient))
        model.addAttribute("login", oauth2User.attributes["login"])
        return "index"
    }


    @GetMapping("/new")
    fun new(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal oauth2User: OAuth2User,
        model: Model
    ): String? {
        model.addAttribute("repo", githubService.getRepo(authorizedClient))
        return "index1"
    }


    /*@GetMapping("/post")
    fun post(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        model: Model
    ): String? {

    }*/

}