package comparator;

import entity.Section;

/**
 * The comparator for the section
 * @author Archer
 *
 */
public class ComparatorForSection implements java.util.Comparator<Section> {
	@Override
	public int compare(Section o1, Section o2) {
		return o2.getFreeCapacity() - o1.getFreeCapacity();
	}

}
