package entity;

import java.io.Serializable;
import java.util.*;

public class Order implements Serializable{
	private int id;
	private int itemNumber;
	private int unallocatedNumber;
	private int unremovedNumber;
	//TODO 
	HashMap<Integer, ArrayList<Integer>> storageMap;
	// private boolean orderEstablished;
	// private boolean orderPayed;
	// private boolean orderUnderExcution;
	// private boolean orderFinished;
	// private boolean orderCancelled;

	public Order(int id, int itemNumber) {
		this.id = id;
		this.itemNumber = itemNumber;
		unallocatedNumber = itemNumber;
		unremovedNumber = itemNumber;
		// orderEstablished = true;
		// orderPayed = false;
		// orderUnderExcution = false;
		// orderFinished = false;
		// orderCancelled = false;
	}

	// public boolean cancelOrder() {
	// orderCancelled = true;
	// return orderCancelled;
	// }

	public int getId() {
		return id;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public int getUnallocatedNumber() {
		return unallocatedNumber;
	}

	public void setUnallocatedNumber(int unallocatedNumber) {
		this.unallocatedNumber = unallocatedNumber;
	}

	public boolean allocateable() {
		return (unallocatedNumber == 0 ? false : true);
	}

	public boolean removable() {
		return (unallocatedNumber == 0 && unremovedNumber != 0 ? true : false);
	}

	// public String getOrderStatus() {
	// if (orderCancelled)
	// return "Order cancelled";
	// // else if (!orderPayed)
	// // return "Order is waiting payment";
	// else if (!orderUnderExcution)
	// return "Order wait excution";
	// else if (!orderFinished)
	// return "Order under execution";
	// else
	// return "Order finished";
	// }

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
			sb.append(unremovedNumber == itemNumber ? ", status=stock" : ", status=under remove");
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

	public int getUnremovedNumber() {
		return unremovedNumber;
	}

	public void setUnremovedNumber(int unremovedNumber) {
		this.unremovedNumber = unremovedNumber;
	}

}
