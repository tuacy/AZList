package com.pilot.common.widget.wheelview;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract Wheel adapter.
 * 
 * @see <a href="https://code.google.com/p/android-wheel/source/browse/trunk/wheel/src/kankan/wheel/widget/adapters/AbstractWheelAdapter.java">
 *     	https://code.google.com/p/android-wheel/source/browse/trunk/wheel/src/kankan/wheel/widget/adapters/AbstractWheelAdapter.java
 *     	</a>
 */
public abstract class AbstractWheelAdapter implements WheelViewAdapter {

	// Observers
	private List<DataSetObserver> dataSetObservers;

	@Override
	public View getEmptyItem(View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		if (dataSetObservers == null) {
			dataSetObservers = new LinkedList<DataSetObserver>();
		}
		dataSetObservers.add(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (dataSetObservers != null) {
			dataSetObservers.remove(observer);
		}
	}

	/**
	 * Notifies observers about data changing
	 */
	protected void notifyDataChangedEvent() {
		if (dataSetObservers != null) {
			for (DataSetObserver observer : dataSetObservers) {
				observer.onChanged();
			}
		}
	}

	/**
	 * Notifies observers about invalidating data
	 */
	protected void notifyDataInvalidatedEvent() {
		if (dataSetObservers != null) {
			for (DataSetObserver observer : dataSetObservers) {
				observer.onInvalidated();
			}
		}
	}
}
