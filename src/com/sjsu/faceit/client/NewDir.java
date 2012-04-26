package com.sjsu.faceit.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NewDir {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		File file = new File("/Users/prayag/Desktop/abc");
		file.mkdir();
		
		 FileWriter fstream = new FileWriter(file.getAbsoluteFile() + "/out1.txt");
		 BufferedWriter out = new BufferedWriter(fstream);
		 out.write("Hello Java");
	   
		 //Close the output stream
		 out.close();
	}

}
