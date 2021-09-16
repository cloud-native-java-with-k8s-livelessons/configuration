package com.example.basics

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.boot.runApplication
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.Environment
import org.springframework.core.env.PropertySource
import org.springframework.stereotype.Controller
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.ResponseBody
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*

/**
 * 1. show that we can get config values from the Environment. That Environment can in turn get data from all sorts of places,
 *    like environment variables, `--D` arguments, JNDI, etc.
 * 2. show that we can bind that configuration to Spring Boot configuration properties objects
 */
@EnableConfigurationProperties(CnjProperties::class)
@SpringBootApplication
class BasicsApplication

fun main(args: Array<String>) {
    runApplication<BasicsApplication>(*args)
}

@ConstructorBinding
@ConfigurationProperties("cnj")
data class CnjProperties(val message: String)

@Controller
@ResponseBody
class ConfigurationRunner(
    private val env: Environment,
    private val cnjProperties: CnjProperties
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        println("the message is ${env.getProperty("cnj.message")}")
        println("the message is ${cnjProperties.message}")
    }
}

class MyExternalEnvironmentPostProcessor : EnvironmentPostProcessor {

    override fun postProcessEnvironment(
        environment: ConfigurableEnvironment,
        application: SpringApplication
    ) {
        val configFile = File(File(File(System.getenv()["HOME"]!!), "Desktop"), "config.properties")
        Assert.state(configFile.exists(), "the file ${configFile.absolutePath} must exist.")
        environment.propertySources.addLast(MyExternalPropertySource(configFile))
    }
}


class MyExternalPropertySource(config: File) : PropertySource<String>("external") {

    private val configProperties = config

    override fun getProperty(key: String): Any? =
        BufferedReader(FileReader(this.configProperties))
            .use {
                val props = Properties()
                props.load(it)
                props.getProperty(key)?.also {
                    println("mooe")
                }
            }
}
