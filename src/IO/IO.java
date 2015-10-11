package IO;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import controller.Warehouse;

public class IO {
	/**
	 * Refers to the file store all the info about the warehouse
	 */
	private static final File FILE = new File("src" + File.separator + "db.txt");

	/**
	 * The load operation for the system to the file mentioned above
	 * 
	 * @return the new warehouse
	 */
	public static Warehouse readFromFile() {
		FileInputStream fis;
		ObjectInputStream ois;
		Warehouse wh = null;
		try {
			// System.out.println("[TEST USE ONLY]load from file: " +
			// FILE.getAbsolutePath());
			fis = new FileInputStream(FILE);
			ois = new ObjectInputStream(fis);
			wh = (Warehouse) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException e) {
			wh = null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			wh = null;
		}
		return wh;
	}

	/**
	 * The save operation for the system to the file mentioned above
	 */
	public static void saveToFile() {
		Warehouse wh = Warehouse.getWarehouse();
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			// System.out.println("[TEST USE ONLY]save to file: " +
			// FILE.getAbsolutePath());
			fos = new FileOutputStream(FILE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(wh);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
