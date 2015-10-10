package IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import controller.Warehouse;

public class IO {
	public static final File FILE = new File("src" + File.separator + "db.txt");

	// TODO clean the catch.
	public static void saveToFile() {
		Warehouse wh = Warehouse.getWarehouse();
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			System.out.println(FILE.getAbsolutePath());
			fos = new FileOutputStream(FILE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(wh);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static Warehouse readFromFile() {
		FileInputStream fis;
		ObjectInputStream ois;
		Warehouse wh = null;
		try {
			System.out.println(FILE.getAbsolutePath());
			fis = new FileInputStream(FILE);
			ois = new ObjectInputStream(fis);
			wh = (Warehouse) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wh;
	}
}