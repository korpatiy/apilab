package com.trrp.apilab.service

import com.trrp.apilab.model.GithubRepo
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

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
    }

    fun getRepo(authorizedClient: OAuth2AuthorizedClient): Flux<GithubRepo> {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", "korpatiy", "trrp0")
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(GithubRepo::class.java)
            .flux()
    }

    fun createRepo(){
        /*val bodyToMono = webClient.post()
            .uri("/guilds")
            .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
            .body(Mono.just(x), CreateGuildDTO::class.java)
            .retrieve()
            .bodyToMono(Guild::class.java)*/
    }
}