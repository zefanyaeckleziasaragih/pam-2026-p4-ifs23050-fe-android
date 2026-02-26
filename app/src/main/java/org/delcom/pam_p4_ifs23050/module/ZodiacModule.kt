package org.delcom.pam_p4_ifs23050.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23050.network.zodiac.service.IZodiacAppContainer
import org.delcom.pam_p4_ifs23050.network.zodiac.service.IZodiacRepository
import org.delcom.pam_p4_ifs23050.network.zodiac.service.ZodiacAppContainer

@Module
@InstallIn(SingletonComponent::class)
object ZodiacModule {

    @Provides
    fun provideZodiacContainer(): IZodiacAppContainer =
        ZodiacAppContainer()

    @Provides
    fun provideZodiacRepository(
        container: IZodiacAppContainer,
    ): IZodiacRepository = container.zodiacRepository
}