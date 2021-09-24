package com.trrp.apilab.controller

import com.trrp.apilab.model.CreateGuildDTO
import com.trrp.apilab.model.Guild
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
@RequestMapping
class TestController(
    private val webClient: WebClient
) {

    @GetMapping
    fun index(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal oauth2User: OAuth2User,
        model: Model
    ): String? {
        model.addAttribute("repositories", fetchAllRepositories(authorizedClient))
        model.addAttribute("username", oauth2User.attributes["username"])
        return "index"
    }

    private fun fetchAllRepositories(authorizedClient: OAuth2AuthorizedClient): Flux<Guild> {
        return webClient.get()
            .uri("/users/@me/guilds")
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<Guild>>() {})
            .flatMapMany { Flux.fromIterable(it) }
    }

    @GetMapping("/new")
    fun new(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal oauth2User: OAuth2User,
        model: Model
    ): String? {
        model.addAttribute("guild", getGuid(authorizedClient))
        return "index1"
    }

    fun getGuid(authorizedClient: OAuth2AuthorizedClient): Flux<Guild> {
        return webClient.get()
            .uri("/guilds/{guild.id}", 1)
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(Guild::class.java)
            .flux()
    }

    @GetMapping("/post")
    fun post(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        model: Model
    ): String? {
        val x = CreateGuildDTO(name = "test12345korp")

        val bodyToMono = webClient.post()
            .uri("/guilds")
            .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
            .body(Mono.just(x), CreateGuildDTO::class.java)
            .retrieve()
            .bodyToMono(Guild::class.java)

        model.addAttribute("guild", bodyToMono)

        return "index1"
    }

}