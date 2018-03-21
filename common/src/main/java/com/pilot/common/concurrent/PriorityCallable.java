package com.pilot.common.concurrent;

import java.util.concurrent.Callable;

public interface PriorityCallable<T> extends Callable<T>, Comparable<PriorityCallable<T>> {

	int getPriority();
}
