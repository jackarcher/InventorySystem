package entity;

import java.io.Serializable;
import java.util.HashMap;

public class Section implements Serializable {
	/**
	 * The capacity of a section
	 */
	private int capacity;
	/**
	 * The ID of a section
	 */
	private int id;
	/**
	 * The pallet info in a HashMap format, key is pallet ID, value is pallet
	 * object
	 */
	private HashMap<Integer, Pallet> palletMap;

	/**
	 * To initialize the section to contain the specific id and capacity
	 * 
	 * @param id
	 *            The section ID of the section
	 * @param capacity
	 *            The capacity of the section
	 * @throws RuntimeException
	 *             This exception may be thrown for the given capacity being
	 *             negative.
	 */
	public Section(int id, int capacity) throws RuntimeException {
		if (capacity <= 0) {
			throw new RuntimeException("Please input a postive number as capacity");
		}
		this.id = id;
		this.capacity = capacity;
		palletMap = new HashMap<>();
	}

	/**
	 * Create a new pallet in the section
	 * 
	 * @return The ID of the new pallet, -1 if this section is full
	 */
	public int createPallet() {
		for (int i = 0; i <= palletMap.size() && i < capacity; i++) {
			if (palletMap.putIfAbsent(i, null) == null) {
				Pallet pallet = new Pallet();
				palletMap.put(i, pallet);
				return i;
			}
		}
		return -1;
	}

	/**
	 * The getter of the attribute capacity
	 * 
	 * @return The capacity of the section
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * The getter of the free capacity of the section
	 * 
	 * @return The free capacity of the section
	 */
	public int getFreeCapacity() {
		return capacity - palletMap.size();
	}

	/**
	 * The getter of the attribute id
	 * 
	 * @return The id of the section
	 */
	public int getId() {
		return id;
	}

	/**
	 * The getter of the attribute palletMap
	 * 
	 * @return A copy of the palletMap, to prevent the info from illegal change.
	 */
	public HashMap<Integer, Pallet> getPalletMap() {
		return new HashMap<Integer, Pallet>(palletMap);
	}

	/**
	 * Exam whether the section is empty
	 * 
	 * @return true if the section is empty, false if not
	 */
	public boolean isEmpty() {
		if (palletMap.size() == 0)
			return true;
		else
			return false;
	}

	/**
	 * Remove a pallet from the palletMap
	 * 
	 * @param palletId
	 *            the pallet id that to be removed
	 */
	public void palletMapRemove(int palletId) {
		palletMap.remove(palletId);
	}

	/**
	 * The setter to the capacity.
	 * 
	 * @param capacity
	 *            The new capacity of the section
	 * @return True if capacity changed successfully.
	 * @throws RuntimeException
	 *             This exception may be thrown for the following reasons: 1.
	 *             The new capacity is not a positive integer. 2. The new
	 *             capacity is less than the current used capacity
	 */
	public boolean setCapacity(int capacity) throws RuntimeException {
		String error = null;
		if (capacity <= 0) {
			error = "Invalid capacity, please input a positive number";
		} else if (capacity < palletMap.size()) {
			error = "New capacity is less than the current capacity, Nothing changed.";
		} else {
			this.capacity = capacity;
		}

		if (error == null) {
			return true;
		} else {
			throw new RuntimeException(error);
		}
	}

	@Override
	public String toString() {
		return "[Section: id = " + id + ", Capacity = " + capacity + "]";
	}

}
