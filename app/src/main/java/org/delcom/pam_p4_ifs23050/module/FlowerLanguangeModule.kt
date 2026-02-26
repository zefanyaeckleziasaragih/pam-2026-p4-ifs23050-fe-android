package org.delcom.pam_p4_ifs23051.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23051.network.flower.service.FlowerLanguageAppContainer
import org.delcom.pam_p4_ifs23051.network.flower.service.IFlowerLanguageAppContainer
import org.delcom.pam_p4_ifs23051.network.flower.service.IFlowerLanguageRepository

@Module
@InstallIn(SingletonComponent::class)
object FlowerLanguageModule {

    @Provides
    fun provideFlowerLanguageContainer(): IFlowerLanguageAppContainer =
        FlowerLanguageAppContainer()

    @Provides
    fun provideFlowerLanguageRepository(
        container: IFlowerLanguageAppContainer,
    ): IFlowerLanguageRepository = container.flowerLanguageRepository
}