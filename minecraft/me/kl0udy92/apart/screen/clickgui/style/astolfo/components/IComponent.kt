package me.kl0udy92.apart.screen.clickgui.style.astolfo.components

interface IComponent {

    fun init()

    fun render(mouseX: Int, mouseY: Int)

    fun update(mouseX: Int, mouseY: Int)

    fun mouseClicked(mouseX: Int, mouseY: Int, button: Int)

    fun mouseReleased(mouseX: Int, mouseY: Int, state: Int)

    fun keyTyped(typedChar: Char, key: Int)

    fun closed()

}