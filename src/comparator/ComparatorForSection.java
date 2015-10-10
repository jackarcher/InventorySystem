package comparator;

import entity.Section;

public class ComparatorForSection implements java.util.Comparator<Section> {

	@Override
	public int compare(Section o1, Section o2) {
		return o2.getFreeCapacity() - o1.getFreeCapacity();
	}

}
