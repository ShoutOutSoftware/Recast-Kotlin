package com.shoutoutsoftware.recast.callback;

import java.util.HashMap;

public class RecastCallback {

    private final RecastConsumer<HashMap<String, Object>, String> onComplete;

    public RecastCallback(RecastConsumer<HashMap<String, Object>, String> onComplete) {
        this.onComplete = onComplete;
    }

    public void accept(HashMap<String, Object> alteredMap, String keyAltered) {
        onComplete.accept(alteredMap, keyAltered);
    }

}
