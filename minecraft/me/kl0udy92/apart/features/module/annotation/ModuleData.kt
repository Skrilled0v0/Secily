package me.kl0udy92.apart.features.module.annotation

import me.kl0udy92.apart.features.module.Category
import org.lwjgl.input.Keyboard


@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModuleData(
    val name: String,
    val chineseName: String,
    val aliases: Array<String> = [""],
    val namebreak: String,
    val category: Category,
    val description: String = "This module dont have any description.",
    val keyCode: Int = Keyboard.KEY_NONE,
    val visible: Boolean = true
)