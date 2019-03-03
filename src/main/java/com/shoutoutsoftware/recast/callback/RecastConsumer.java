package com.shoutoutsoftware.recast.callback;

public interface RecastConsumer<T, V> {
    void accept(T t, V v);
}
