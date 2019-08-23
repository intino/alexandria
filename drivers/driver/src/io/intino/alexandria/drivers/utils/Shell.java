package io.intino.alexandria.drivers.utils;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Shell {
	private String info;
	private String response;
	
	public Shell() {
		info = "";
	}

	public Integer executeCommand(String command, File path) {
		Integer minimalExitValue = 0;
		return executeCommand(command, path, minimalExitValue);
	}
	
	public Integer executeCommand(String command, File path, Integer minimalExitValue) {
		String[] commands = null;
		Process process = null;
		response = "";
		InputStream out = null;
		
		if (SystemOS.isWindows())
			commands = new String[] { "cmd", "/c", command };
		else
  		commands = new String[] { "sh", "-c", command };


		Logger.debug("Execute command: " + command);
		try {
			process = Runtime.getRuntime().exec(commands, null, path);
			process.waitFor();
      out = process.getInputStream();
		} catch (IOException | InterruptedException e) {
			Logger.error("Process error: " + e.getMessage());
		}

		StringUtils stringUtils = new StringUtils();
		response = stringUtils.getResponse(out); 
		info = "Exit value: " + process.exitValue() + ". \nOut:\n" + stringUtils.getResponse(process.getInputStream());

		if (process.exitValue() > minimalExitValue) {
			info = "Failed to execute: " + command + ", Details: " + info + "\nError:\n" + stringUtils.getResponse( process.getErrorStream());
			Logger.error(info);
			return process.exitValue();
		} else {
			Logger.debug(info);
		}

		return process.exitValue();
	}

	public String executeCommandWithResponse(String command, File path) {
		Integer minimalExitValue = 0;
		return executeCommandWithResponse(command, path, minimalExitValue);
	}

	public String executeCommandWithResponse(String command, File path, Integer minimalExitValue) {
		if (executeCommand(command, path, minimalExitValue) == 0)				
		  return response;
		else return "";
	}
	
	public Integer executeDaemon(String command, File path) {
		String[] commands = null;

		if (SystemOS.isWindows())
			commands = new String[] { "cmd", "/c", command };
		else
  		commands = new String[] { "sh", "-c", command };

		Logger.info("Execute command: " + command);
		try {
			Runtime.getRuntime().exec(commands, null, path);
		} catch (IOException e) {
		}
		return 0;
	}

	public String lastInfo() {
		return info;
	}
}
