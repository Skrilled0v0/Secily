package me.kl0udy92.apart.features.module.implementations.visual

import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData

@ModuleData(
    "CameraNoClip",
    "镜头无修剪",
    ["ViewClip", "CameraClip"],
    "Camera No Clip",
    Category.VISUAL,
    "Make camera not be clipped at some time."
)
class CameraNoClipModule : Module()