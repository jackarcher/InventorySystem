package entity;

import java.util.HashMap;

//TODO section's capacity and pallet map
public class Section {
	private int id;
	private int capacity;
	private HashMap<Integer, Pallet> palletMap;

	public Section(int id, int capacity) throws RuntimeException {
		if (capacity <= 0) {
			throw new RuntimeException("Please input a postive number as capacity");
		}
		this.id = id;
		this.capacity = capacity;
		palletMap = new HashMap<>();
	}

	public int getCapacity() {
		return capacity;
	}

	public int getFreeCapacity(){
		return 0;
	}
	
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

	public int getId() {
		return id;
	}

	// TODO
	/**
	 * "CREATE NEW PALLET"
	 * 
	 * @return -1 if full
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

	// TODO
	public void palletMapRemove() {

	}

	public HashMap<Integer, Pallet> getPalletMap() {
		return new HashMap<Integer, Pallet>(palletMap);
	}

	public boolean isEmpty() {
		if (palletMap.size() == 0)
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[Section: id = " + id + ", Capacity = " + capacity + "]";
	}

}
