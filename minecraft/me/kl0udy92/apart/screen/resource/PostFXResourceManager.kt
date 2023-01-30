package me.kl0udy92.apart.screen.resource

import net.minecraft.client.resources.FallbackResourceManager
import net.minecraft.client.resources.IResource
import net.minecraft.client.resources.IResourceManager
import net.minecraft.client.resources.data.IMetadataSerializer
import net.minecraft.util.ResourceLocation

class PostFXResourceManager(frmMetadataSerializerIn: IMetadataSerializer) : FallbackResourceManager(frmMetadataSerializerIn), IResourceManager {

    override fun getResourceDomains(): Set<String?>? {
        return null
    }

    override fun getResource(location: ResourceLocation?): IResource {
        return PostFXResource()
    }

    override fun getAllResources(location: ResourceLocation?): List<IResource?>? {
        return null
    }

}
