package com.example.kmprpc

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import kotlinx.rpc.krpc.ktor.server.Krpc
import kotlinx.rpc.krpc.ktor.server.rpc
import kotlinx.rpc.krpc.serialization.json.json

fun main() {
    val environment = Environment()
    embeddedServer(
        factory = Netty,
        port = environment.http.port,
        host = environment.http.host,
        module = Application::module
    ).start(wait = true)
}

@Suppress("unused")
fun Application.module() {
    install(Krpc)

    installCORS(environment = Environment().cors)

    routing {
        rpc("/api") {
            rpcConfig {
                serialization {
                    json()
                }
            }

            registerService<NewsService> { ctx -> NewsServiceImpl(ctx) }
        }
    }
}


fun Application.installCORS(environment: Environment.Cors) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.Upgrade)
        allowNonSimpleContentTypes = true
        allowCredentials = true
        allowSameOrigin = true

        environment.allowedHosts.forEach { host ->
            allowHost(host, listOf("http", "https", "ws", "wss"))
        }
    }
}