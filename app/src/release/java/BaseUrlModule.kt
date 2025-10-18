import com.friend.di.qualifier.AppBaseUrl
import com.friend.di.qualifier.AppImageBaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class BaseUrlModule{
    @Provides
    @AppBaseUrl
    fun provideBaseUrl():String = "https://dev.api.nativeapp.intercity.jatri.co/"

    @Provides
    @AppImageBaseUrl
    fun provideImageBaseUrl():String = "https://storage.jatri.co/"
}

