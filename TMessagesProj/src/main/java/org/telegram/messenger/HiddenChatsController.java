package org.telegram.messenger;

import android.content.SharedPreferences;
import android.text.TextUtils;

import org.telegram.messenger.support.fingerprint.FingerprintManagerCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class HiddenChatsController {
    private static final String KEY_PREFIX = "sologram_hidden_chats_";

    private HiddenChatsController() {
    }

    private static String getKey(int account) {
        return KEY_PREFIX + account;
    }

    private static Set<String> getStoredIds(int account) {
        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
        Set<String> stored = preferences.getStringSet(getKey(account), null);
        return stored == null ? new HashSet<>() : new HashSet<>(stored);
    }

    public static synchronized boolean isHidden(int account, long dialogId) {
        return getStoredIds(account).contains(Long.toString(dialogId));
    }

    public static synchronized void setHidden(int account, Iterable<Long> dialogIds, boolean hidden) {
        Set<String> ids = getStoredIds(account);
        for (Long dialogId : dialogIds) {
            if (dialogId == null) {
                continue;
            }
            String value = Long.toString(dialogId);
            if (hidden) {
                ids.add(value);
            } else {
                ids.remove(value);
            }
        }
        MessagesController.getGlobalMainSettings().edit().putStringSet(getKey(account), ids).commit();
    }

    public static synchronized ArrayList<Long> getHiddenDialogIds(int account) {
        ArrayList<Long> result = new ArrayList<>();
        for (String value : getStoredIds(account)) {
            try {
                result.add(Long.parseLong(value));
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public static boolean isSecureUnlockAvailable() {
        if (TextUtils.isEmpty(SharedConfig.passcodeHash) || !SharedConfig.useFingerprintLock || !FingerprintController.isKeyReady()) {
            return false;
        }
        try {
            FingerprintManagerCompat manager = FingerprintManagerCompat.from(ApplicationLoader.applicationContext);
            return manager.isHardwareDetected() && manager.hasEnrolledFingerprints() && !FingerprintController.checkDeviceFingerprintsChanged();
        } catch (Throwable e) {
            FileLog.e(e);
            return false;
        }
    }
}
