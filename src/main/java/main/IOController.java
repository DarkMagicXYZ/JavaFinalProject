package main;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class IOController {
	BufferedWriter writer;
	BufferedReader reader;
	String path;
	public IOController() {
		writer = null;
		reader = null;
		path = System.getProperty("user.dir");
	}
	public void record(ArrayList<ArrayList<String>> record) {
		File file = new File(path + File.separator + "record" + File.separator + "record.xml");
		if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
		try {
			writer =new BufferedWriter(new FileWriter(file));
			//read不明原因少读一行，这里多写一行
			writer.write("\n");
			for (ArrayList<String> string : record) {
				for (String str : string) {
					writer.write(str + "\n");
				}
			}
			writer.close();
		}catch (IOException e) {
            e.printStackTrace();
        }finally {
        	System.out.println("The battle is saved");
        }
	}
	public ArrayList<String> read(File f) {
		ArrayList<String> record = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(f));
			reader.readLine();
			String temp = null;
			while((temp = reader.readLine()) != null) {
				//System.out.println(temp);
				record.add(temp);
			}
			reader.close();
		}catch (IOException e) {
            e.printStackTrace();
        }finally {
        	System.out.println("Already read");
        }
		return record;
	}
}
