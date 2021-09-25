package com.trrp.apilab.module.service

import com.trrp.apilab.model.GithubRepo
import com.trrp.apilab.rest.dto.CreateRepoDTO
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

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
            .map { it }
        // .map(jsonNode -> jsonNode.get("full_name").asText());*/
    }

    fun getRepo(authorizedClient: OAuth2AuthorizedClient): Flux<GithubRepo> {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", "korpatiy", "trrp0")
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(GithubRepo::class.java)
            .flux()
    }

    fun createRepo(repository: CreateRepoDTO, authorizedClient: OAuth2AuthorizedClient): Mono<GithubRepo> {
        return webClient.post()
            .uri("/user/repos")
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .body(Mono.just<Any>(repository), CreateRepoDTO::class.java)
            .retrieve()
            .bodyToMono(GithubRepo::class.java)
    }
}