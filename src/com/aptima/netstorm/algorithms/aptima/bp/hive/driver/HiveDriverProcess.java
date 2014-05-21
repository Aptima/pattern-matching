package com.aptima.netstorm.algorithms.aptima.bp.hive.driver;

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
			OutputRedirector errorGobbler = new OutputRedirector(p.getErrorStream(), "ERROR");

			// any output?
			OutputRedirector outputGobbler = new OutputRedirector(p.getInputStream(), "OUTPUT");

			errorGobbler.start();
			outputGobbler.start();

			p.waitFor();
			System.out.println("Exit: " + p.exitValue());
			p.destroy();
		} catch (Exception e) {
		}
	}
}
