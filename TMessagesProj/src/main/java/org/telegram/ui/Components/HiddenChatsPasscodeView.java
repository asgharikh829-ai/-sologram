package org.telegram.ui.Components;

import android.content.Context;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;

public final class HiddenChatsPasscodeView extends PasscodeView {
    private final Runnable onAuthenticated;
    private boolean passcodeAccepted;
    private boolean fingerprintAccepted;
    private boolean authenticated;

    public HiddenChatsPasscodeView(Context context, Runnable onAuthenticated) {
        super(context, LocaleController.getString(R.string.HiddenChatsUnlockTitle), LocaleController.getString(R.string.HiddenChatsUnlockDescription));
        this.onAuthenticated = onAuthenticated;
        setDelegate(view -> {
            if (passcodeAccepted && fingerprintAccepted) {
                authenticated = true;
                if (this.onAuthenticated != null) {
                    this.onAuthenticated.run();
                }
            } else {
                AndroidUtilities.runOnUIThread(() -> {
                    if (!authenticated) {
                        onShow(false, true, 0, 0, null, null);
                    }
                }, 400);
            }
        });
    }

    @Override
    protected boolean isAppLock() {
        return false;
    }

    @Override
    protected void processDone(boolean fingerprint) {
        if (fingerprint) {
            fingerprintAccepted = true;
        } else {
            passcodeAccepted = true;
        }
        super.processDone(fingerprint);
    }

    public void showForAuthentication() {
        onShow(false, true, 0, 0, null, null);
    }
}
