package io.jkratz.mediatr.spring

import io.jkratz.mediatr.core.CommandHandler
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass

/**
 *
 */
internal class CommandProvider<T> (
    private val applicationContext: ApplicationContext,
    private val type: KClass<T>
) where T: CommandHandler<*, *> {

    fun get(): T {
        return applicationContext.getBean(type.java)
    }
}