package com.aptima.netstorm.algorithms.aptima.bp.hive.driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class StreamGobbler extends Thread {
	InputStream is;
	String type;

	StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				System.out.println(type + ">" + line);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}

public class HiveDriverProcess {

	// called by Akamai and Bitcoin Driver
	public static void main(String[] args) {
		Process p;
		try {
			String command = args[0];
			System.out.println(command);
			String s;
			p = Runtime.getRuntime().exec(args);

			// err
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");

			errorGobbler.start();
			outputGobbler.start();

			p.waitFor();
			System.out.println("Exit: " + p.exitValue());
			p.destroy();
		} catch (Exception e) {
		}
	}
}
