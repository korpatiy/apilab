package com.trrp.apilab.rest.controller

import com.trrp.apilab.module.service.GithubService
import com.trrp.apilab.rest.dto.CreateRepoDTO
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping
class GithubController(
    private val githubService: GithubService,
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

    @GetMapping("/create")
    fun newRepo(@ModelAttribute("repository") repository: CreateRepoDTO): String {
        return "repo-create"
    }

    @PostMapping("/post")
    fun createRepo(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        repository: CreateRepoDTO,
        model: Model
    ): String {
        model.addAttribute("repo", githubService.createRepo(repository, authorizedClient))
        return "repo-created"
    }

    @GetMapping("/delete/{name}")
    fun deleteRepo(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal oauth2User: OAuth2User,
        @PathVariable("name") repoName: String,
        model: Model
    ): String {
        model.addAttribute(
            "repo",
            githubService.deleteRepo(authorizedClient, oauth2User.attributes["login"].toString(), repoName)
        )
        return "redirect:/"
    }

    @GetMapping("/update/{name}")
    fun updRepo(
        @ModelAttribute("repository") repository: CreateRepoDTO,
        @PathVariable("name") repoName: String,
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal oauth2User: OAuth2User,
        model: Model
    ): String {
        val repo = githubService.getRepo(authorizedClient, oauth2User.attributes["login"].toString(), repoName)
        model.addAttribute("repository", repo)
        return "repo-update"
    }

    @PostMapping("/patch/{name}")
    fun updateRepo(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @PathVariable("name") repoName: String,
        @AuthenticationPrincipal oauth2User: OAuth2User,
        @ModelAttribute("repository") repository: CreateRepoDTO,
        model: Model
    ): String {
        model.addAttribute(
            "repository",
            githubService.editRepo(authorizedClient, oauth2User.attributes["login"].toString(), repoName ,repository)
        )
        return "repo-view"
    }

    @GetMapping("/get/{name}")
    fun getRepo(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal oauth2User: OAuth2User,
        @PathVariable("name") repoName: String,
        model: Model
    ): String? {
        model.addAttribute(
            "repository",
            githubService.getRepo(authorizedClient, oauth2User.attributes["login"].toString(), repoName)
        )
        return "repo-view"
    }
}