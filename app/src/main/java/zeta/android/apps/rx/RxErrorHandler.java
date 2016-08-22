package zeta.android.apps.rx;

import rx.plugins.RxJavaErrorHandler;
import timber.log.Timber;
import zeta.android.apps.BuildConfig;
import zeta.android.apps.network.ZetaNoNetworkConnectivityException;

/**
 * This Handler is deprecated now, Use RxJavaHooks.setOnError(new RxErrorHandler2()); instead
 * RxJavaPlugins.getInstance().registerErrorHandler(new RxErrorHandler());
 */
@Deprecated
public class RxErrorHandler extends RxJavaErrorHandler {

    @Override
    public void handleError(Throwable e) {
        if (!BuildConfig.DEBUG) {
            return;
        }

        if (e instanceof ZetaNoNetworkConnectivityException) {
            //We don't want to print stack trace here as we know this exception is caused due to
            //no network connectivity
            Timber.e("RxErrorHandler No network error");
            return;
        }

        Timber.e(e, "RxErrorHandler Uncaught error thrown");
    }
}
