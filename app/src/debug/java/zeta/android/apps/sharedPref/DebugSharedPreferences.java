package zeta.android.apps.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;
import zeta.android.apps.environments.FlickrDebugEnv;

public class DebugSharedPreferences {

    private static final String TAG = DebugSharedPreferences.class.getSimpleName();
    private static final String KEY_DEBUG_PREFS = TAG + "zetaDebugSharedPrefs";

    private static final String KEY_ENABLE_STETHO = "enableSteho";
    private static final String KEY_ENABLE_STRICT_MODE = "enableStrictMode";
    private static final String KEY_ENABLE_TINY_DANCER = "enableTinyCanary";
    private static final String KEY_ENABLE_LEAKY_CANARY = "enableLeakyCanary";
    private static final String KEY_HTTP_LOGGING_LEVEL = "httpLoggingLevel";
    private static final String KEY_FLICKR_CAST_ENV = "flickrEnv";

    private final Context mContext;

    public DebugSharedPreferences(Context context) {
        mContext = context;
    }

    private SharedPreferences getDebugPrefs() {
        return mContext.getSharedPreferences(KEY_DEBUG_PREFS, Context.MODE_PRIVATE);
    }

    public boolean getLeakyCanaryEnabled() {
        return getDebugPrefs().getBoolean(KEY_ENABLE_LEAKY_CANARY, false);
    }

    public boolean getStethoEnabled() {
        return getDebugPrefs().getBoolean(KEY_ENABLE_STETHO, false);
    }

    public boolean getStrictModeEnabled() {
        return getDebugPrefs().getBoolean(KEY_ENABLE_STRICT_MODE, false);
    }

    public boolean getTinyDancerEnabled() {
        return getDebugPrefs().getBoolean(KEY_ENABLE_TINY_DANCER, false);
    }

    @SuppressWarnings("WrongConstant")
    @FlickrDebugEnv
    public int getCurrentFlickrEnvironment() {
        return getDebugPrefs().getInt(KEY_FLICKR_CAST_ENV, FlickrDebugEnv.DEBUG_PROD);
    }

    public void saveStethoEnabled(boolean enableStetho) {
        saveBoolean(KEY_ENABLE_STETHO, enableStetho);
    }

    public void saveLeakyCanaryEnabled(boolean enableLeakyCanary) {
        saveBoolean(KEY_ENABLE_LEAKY_CANARY, enableLeakyCanary);
    }

    public void saveStrictModeEnabled(boolean enableStrictMode) {
        saveBoolean(KEY_ENABLE_STRICT_MODE, enableStrictMode);
    }

    public void saveTinyDancerEnabled(boolean enableTinyDancer) {
        saveBoolean(KEY_ENABLE_TINY_DANCER, enableTinyDancer);
    }

    public void saveHttpLoggingLevel(@FlickrDebugEnv int env) {
        saveInteger(KEY_FLICKR_CAST_ENV, env);
    }

    @NonNull
    public HttpLoggingInterceptor.Level getHttpLoggingLevel() {
        final String savedHttpLoggingLevel = getDebugPrefs().getString(KEY_HTTP_LOGGING_LEVEL, HttpLoggingInterceptor.Level.NONE.toString());

        try {
            if (savedHttpLoggingLevel != null) {
                return HttpLoggingInterceptor.Level.valueOf(savedHttpLoggingLevel);
            }
        } catch (IllegalArgumentException noSuchLoggingLevel) {
            // After OkHttp update old logging level may be removed/renamed so we should handle such case.
            Timber.w("No such Http logging level in current version of the app. Saved loggingLevel = %s", savedHttpLoggingLevel);
        }
        return HttpLoggingInterceptor.Level.BASIC;
    }

    public void saveHttpLoggingLevel(@NonNull HttpLoggingInterceptor.Level loggingLevel) {
        saveString(KEY_HTTP_LOGGING_LEVEL, loggingLevel.toString());
    }

    private void saveString(String key, String value) {
        getDebugPrefs().edit()
                .putString(key, value)
                .apply();
    }

    private void saveBoolean(String key, boolean value) {
        getDebugPrefs().edit()
                .putBoolean(key, value)
                .apply();
    }

    private void saveInteger(String key, int value) {
        getDebugPrefs().edit()
                .putInt(key, value)
                .apply();
    }
}
