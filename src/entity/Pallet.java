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
	 * Refers to the number used out of 75 in a pallet
	 */
	private int usedCapacity;
	/**
	 * The full capacity a pallet can hold.
	 */
	private static final int FULL_CAPACITY = 75;

	/**
	 * Constructor to initialize the used capacity to 0
	 */
	public Pallet() {
		usedCapacity = 0;
	}

	/**
	 * Exam whether a pallet is empty
	 * 
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		if (usedCapacity == 0)
			return true;
		else
			return false;
	}

	/**
	 * Add item to a pallet with a given item number
	 * 
	 * @param itemNumber
	 *            The number of items will be added to the pallet
	 * @return The number of items that unallocated
	 */
	public int addItem(int itemNumber) {
		int capacity = FULL_CAPACITY - usedCapacity;
		if (itemNumber <= capacity) {
			usedCapacity += itemNumber;
			return 0;
		} else {
			usedCapacity += capacity;
			return itemNumber - capacity;
		}
	}

	/**
	 * Remove item from a pallet with a given item number
	 * 
	 * @param itemNumber
	 *            The number of items to be removed from the pallet
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
