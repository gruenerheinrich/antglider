package org.centauron.ant.antrunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
    InputStream is;
	private boolean isError;
	private AntRunnerNode ar;

    // reads everything from is until empty. 
    StreamGobbler(InputStream is,boolean isError,AntRunnerNode ar) {
        this.is = is;
        this.isError=isError;
        this.ar=ar;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
            	if (this.ar!=null) {
            		ar.getAntRunner().console.setMessage(line);
            	} else {
	            	if (this.isError) {
	            		System.err.println(line);
	            	} else {
	            		System.out.println(line);
	            	}
            	}
        } catch (Exception ioe) {
            ioe.printStackTrace();  
        }
    }
}
