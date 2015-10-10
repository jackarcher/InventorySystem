package entity;

import java.io.Serializable;

/**
 * this class refers pallet stored in the section.
 * 
 * @author Archer
 *
 */
public class Pallet implements Serializable {
	/**
	 * usedCapacity refers to the number used out of 75 in a pallet
	 */
	private int usedCapacity;
	private int fullCapacity;

	public Pallet() {
		usedCapacity = 0;
		fullCapacity = 75;
	}

	public boolean isEmpty() {
		if (usedCapacity == 0)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param itemNumber
	 * @return the number of items that unallocated.
	 */
	public int addItem(int itemNumber) {
		int capacity = fullCapacity - usedCapacity;
		if (itemNumber <= capacity) {
			usedCapacity += itemNumber;
			return 0;
		} else {
			usedCapacity += capacity;
			return itemNumber - capacity;
		}
	}

	/**
	 * 
	 * @param itemNumber
	 * @return the number of items that still to be removed from other place.
	 */
	public int removeItem(int itemNumber) {

		if (usedCapacity >= itemNumber) {
			// enough
			usedCapacity -= itemNumber;
			return 0;
		} else {
			int remain = itemNumber - usedCapacity;
			// not enough
			usedCapacity = 0;
			return remain;
		}
	}

	@Override
	public String toString() {
		return "used capacity: " + usedCapacity;
	}
}
