package me.kl0udy92.apart.features.command.annotation

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandData(
    val name: String,
    val usage: String,
    val syntax: String
)