package entity;

import java.io.Serializable;

/**
 * Indecate an order in the system
 * 
 * @author Archer
 *
 */
public class Order implements Serializable {
	/**
	 * The id of the order
	 */
	private int id;
	/**
	 * The item number in total of the order
	 */
	private int itemNumber;
	/**
	 * The unallocate number of the order
	 */
	private int unallocatedNumber;
	/**
	 * The unremoved(or still in stock) number of the order
	 */
	private int unremovedNumber;

	/**
	 * The constructor of this class accept only 2 params
	 * 
	 * @param id
	 *            The order ID.
	 * @param itemNumber
	 *            The item number of this order
	 */
	public Order(int id, int itemNumber) {
		this.id = id;
		this.itemNumber = itemNumber;
		unallocatedNumber = itemNumber;
		unremovedNumber = itemNumber;
	}

	/**
	 * Exam if the order is allocateable
	 * 
	 * @return true for allocateable if unallocate number is not 0, false for
	 *         unallocateable if unallocate number is 0
	 */
	public boolean allocateable() {
		return (unallocatedNumber == 0 ? false : true);
	}

	/**
	 * The getter of the id field
	 * 
	 * @return The id of the order
	 */
	public int getId() {
		return id;
	}

	/**
	 * The getter of the itemNumber field
	 * 
	 * @return The total item number of the order
	 */
	public int getItemNumber() {
		return itemNumber;
	}

	/**
	 * The getter of the unallocateNumber field
	 * 
	 * @return The unallocate item number of the order
	 */
	public int getUnallocatedNumber() {
		return unallocatedNumber;
	}

	/**
	 * The getter of the unremovedNumber field
	 * 
	 * @return The unremoved item number of the order
	 */
	public int getUnremovedNumber() {
		return unremovedNumber;
	}

	/**
	 * Exam if the order is removeable
	 * 
	 * @return true for removeable if unallocate number is 0 and unremoved
	 *         number is not 0, false for unremoveable
	 */
	public boolean removable() {
		return (unallocatedNumber == 0 && unremovedNumber != 0 ? true : false);
	}

	/**
	 * The setter of the unallocateNumber
	 * 
	 * @param unallocatedNumber
	 *            the new unallocate number for the order
	 */
	public void setUnallocatedNumber(int unallocatedNumber) {
		this.unallocatedNumber = unallocatedNumber;
	}

	/**
	 * The setter of the unremovedNumber
	 * 
	 * @param unremovedNumber
	 *            the new unremoved number for the order
	 */
	public void setUnremovedNumber(int unremovedNumber) {
		this.unremovedNumber = unremovedNumber;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[Order: id=");
		sb.append(id);
		if (allocateable()) {
			sb.append(unallocatedNumber == itemNumber ? ", status=establised" : ", status=under allocate");
			sb.append(", detail: nuallocate/all =");
			sb.append(unallocatedNumber);
			sb.append("/");
			sb.append(itemNumber);
		} else if (removable()) {
			sb.append(unremovedNumber == itemNumber ? ", status=in stock" : ", status=under remove");
			sb.append(", detail: stocked/all =");
			sb.append(unremovedNumber);
			sb.append("/");
			sb.append(itemNumber);
		} else {
			sb.append(", order finished, item number:");
			sb.append(itemNumber);
		}
		sb.append("]");
		return sb.toString();
	}

}
