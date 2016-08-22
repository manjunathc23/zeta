package zeta.android.apps.modules;

import android.content.Context;

import com.github.pedrovgs.lynx.LynxConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import zeta.android.apps.developer.tools.DeveloperToolsImpl;
import zeta.android.apps.sharedPref.DebugSharedPreferences;
import zeta.android.apps.tools.DeveloperTools;

@Module
@Singleton
public class DebugModule {

    @Provides
    DeveloperTools providesDeveloperTools(DebugSharedPreferences preferences) {
        return new DeveloperToolsImpl(preferences);
    }

    @Provides
    DebugSharedPreferences providesDebugSharedPreferences(Context context) {
        return new DebugSharedPreferences(context);
    }

    @Provides
    public LynxConfig provideLynxConfig() {
        return new LynxConfig();
    }
}
