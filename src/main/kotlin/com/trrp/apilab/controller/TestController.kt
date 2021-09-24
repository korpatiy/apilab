package com.trrp.apilab.controller

import com.trrp.apilab.model.CreateGuildDTO
import com.trrp.apilab.model.Guild
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
@RequestMapping("/")
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
            .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<Guild>>() {})
            .flatMapMany { Flux.fromIterable(it) }
        //            .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
    }

    @GetMapping("/new")
    fun new(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal oauth2User: OAuth2User,
        model: Model
    ): String {

        val x = CreateGuildDTO(name = "test12345korp")

        val bodyToMono = webClient.post()
            .uri("/guilds")
            .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
            .body(Mono.just(x), CreateGuildDTO::class.java)
            .retrieve()
            .bodyToMono(Guild::class.java)

        return "index"
    }

}