package com.aptima.netstorm.algorithms.aptima.bp.hive.driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OutputRedirector extends Thread {
	InputStream is;
	String type;
	String match = null;
	boolean console = true;
	boolean detected = false;

	public OutputRedirector(InputStream is, String type){
		this.is = is;
		this.type = type;
	}
	
	public OutputRedirector(InputStream is, String type, boolean console) {
		this.is = is;
		this.type = type;
		this.console = console;
	}
	
	public OutputRedirector(InputStream is, String type, boolean console, String match) {
		this.is = is;
		this.type = type;
		this.console = console;
		this.match = match;
	}
	    
	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);
	        String line = null;
	        while ((line = br.readLine()) != null) {
	        	if (console)
	        		System.out.println(type + "> " + line);
	        	if (match != null && match.equals(line))
	        		detected = true;
	        }
        } catch (IOException ioE) {
        }
    }
	
	public boolean isMatchDeteced() {
		return detected;
	}
}
