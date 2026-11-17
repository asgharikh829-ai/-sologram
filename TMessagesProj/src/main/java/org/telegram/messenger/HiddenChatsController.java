package org.telegram.messenger;

import android.content.SharedPreferences;
import android.text.TextUtils;

import org.telegram.messenger.support.fingerprint.FingerprintManagerCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class HiddenChatsController {
    private static final String KEY_PREFIX = "sologram_hidden_chats_";
    private static final Map<Integer, HashSet<Long>> hiddenIdsCache = new HashMap<>();

    private HiddenChatsController() {
    }

    private static String getKey(int account) {
        return KEY_PREFIX + account;
    }

    private static HashSet<Long> getIds(int account) {
        HashSet<Long> ids = hiddenIdsCache.get(account);
        if (ids == null) {
            ids = new HashSet<>();
            SharedPreferences preferences = MessagesController.getGlobalMainSettings();
            Set<String> stored = preferences.getStringSet(getKey(account), null);
            if (stored != null) {
                for (String value : stored) {
                    try {
                        ids.add(Long.parseLong(value));
                    } catch (Exception ignore) {
                    }
                }
            }
            hiddenIdsCache.put(account, ids);
        }
        return ids;
    }

    public static synchronized boolean isHidden(int account, long dialogId) {
        return getIds(account).contains(dialogId);
    }

    public static synchronized void setHidden(int account, Iterable<Long> dialogIds, boolean hidden) {
        HashSet<Long> ids = new HashSet<>(getIds(account));
        for (Long dialogId : dialogIds) {
            if (dialogId == null) {
                continue;
            }
            if (hidden) {
                ids.add(dialogId);
            } else {
                ids.remove(dialogId);
            }
        }
        hiddenIdsCache.put(account, ids);
        HashSet<String> stored = new HashSet<>();
        for (Long dialogId : ids) {
            stored.add(Long.toString(dialogId));
        }
        MessagesController.getGlobalMainSettings().edit().putStringSet(getKey(account), stored).commit();
    }

    public static synchronized ArrayList<Long> getHiddenDialogIds(int account) {
        return new ArrayList<>(getIds(account));
    }

    public static boolean isSecureUnlockAvailable() {
        if (ApplicationLoader.applicationContext == null || TextUtils.isEmpty(SharedConfig.passcodeHash) || !SharedConfig.useFingerprintLock || !FingerprintController.isKeyReady()) {
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
