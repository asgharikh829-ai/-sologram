package org.telegram.ui.Components;

import android.content.Context;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;

public final class HiddenChatsPasscodeView extends PasscodeView {
    private final Runnable onAuthenticated;
    private boolean authenticated;

    public HiddenChatsPasscodeView(Context context, Runnable onAuthenticated) {
        super(context, LocaleController.getString(R.string.HiddenChatsUnlockTitle), LocaleController.getString(R.string.HiddenChatsUnlockDescription));
        this.onAuthenticated = onAuthenticated;
        setDelegate(view -> {
            if (authenticated) {
                return;
            }
            authenticated = true;
            if (this.onAuthenticated != null) {
                this.onAuthenticated.run();
            }
        });
    }

    @Override
    protected boolean isAppLock() {
        return false;
    }

    public void showForAuthentication() {
        onShow(false, true, 0, 0, null, null);
    }
}
