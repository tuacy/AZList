package com.pilot.common.concurrent;

import java.util.concurrent.RunnableFuture;

public interface ComparableFuture<V> extends RunnableFuture<V>, Comparable<V> {

}
