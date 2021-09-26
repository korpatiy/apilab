package com.trrp.apilab.module.service

import com.trrp.apilab.model.GithubRepo
import com.trrp.apilab.rest.dto.CreateRepoDTO
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class GithubService(
    private val webClient: WebClient,
) {

    fun getAllRepos(authorizedClient: OAuth2AuthorizedClient): Flux<GithubRepo> {
        return webClient.get()
            .uri("/user/repos")
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<GithubRepo>>() {})
            .flatMapMany { Flux.fromIterable(it) }
        //.map { it }
    }

    fun getRepo(authorizedClient: OAuth2AuthorizedClient, owner: String, repo: String): Mono<GithubRepo> {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(GithubRepo::class.java)
    }

    fun createRepo(repository: CreateRepoDTO, authorizedClient: OAuth2AuthorizedClient): Mono<GithubRepo> {
        return webClient.post()
            .uri("/user/repos")
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .body(Mono.just<Any>(repository), CreateRepoDTO::class.java)
            .retrieve()
            .bodyToMono(GithubRepo::class.java)

    }

    fun editRepo(
        authorizedClient: OAuth2AuthorizedClient,
        owner: String,
        repo: String,
        repository: CreateRepoDTO
    ): Mono<GithubRepo> {
        return webClient.patch()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .body(Mono.just<Any>(repository), CreateRepoDTO::class.java)
            .retrieve()
            .bodyToMono(GithubRepo::class.java)
    }

    fun deleteRepo(authorizedClient: OAuth2AuthorizedClient, owner: String, repo: String): Mono<Unit> {
        return webClient.delete()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(Unit::class.java)
    }
}