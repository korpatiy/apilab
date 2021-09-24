package com.trrp.apilab.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.web.reactive.function.client.*
import java.util.function.Consumer
import reactor.core.publisher.Mono

import org.springframework.web.reactive.function.client.ExchangeFilterFunction


@Configuration
class WebClientConfig(
    private val logger: Logger = LoggerFactory.getLogger(WebClientConfig::class.java)
) {

    companion object {
        private const val DISCORD_API_URL = "https://discord.com/api"
    }

    @Bean
    fun webClient(authorizedClientManager: ReactiveOAuth2AuthorizedClientManager?): WebClient? {
        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        return WebClient.builder()
            .baseUrl(DISCORD_API_URL)
            .filter(oauth)
            .filter(logRequest())
            .filter(logResponseStatus())
            .build()
    }

    @Bean
    fun authorizedClientManager(
        clientRegistrationRepository: ReactiveClientRegistrationRepository?,
        authorizedClientRepository: ServerOAuth2AuthorizedClientRepository?
    ): ReactiveOAuth2AuthorizedClientManager? {
        val authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .build()
        val authorizedClientManager = DefaultReactiveOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientRepository
        )
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)
        return authorizedClientManager
    }


    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction { clientRequest: ClientRequest, next: ExchangeFunction ->
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url())
            clientRequest.headers()
                .forEach { name: String?, values: List<String?> ->
                    values.forEach(
                        Consumer { value: String? ->
                            logger.info(
                                "{}={}",
                                name,
                                value
                            )
                        })
                }
            next.exchange(clientRequest)
        }
    }

    private fun logResponseStatus(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
            logger.info("Response Status {}", clientResponse.statusCode())
            Mono.just(clientResponse)
        }
    }
}