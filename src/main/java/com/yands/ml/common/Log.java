package com.yands.ml.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

public class Log implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1045966775413666028L;
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss#");
	private JTextArea logTextArea;
	
	public Log() {}
	
	public Log(JTextArea logTextArea) {
		this.logTextArea = logTextArea;
	}
	
	public void log(String text) {
		logTextArea.append(FORMAT.format(new Date()) + text + "\r\n");
		//立马刷新
		logTextArea.paintImmediately(logTextArea.getBounds());
		//显示最新行
		logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
		//日志超过1000条清空
		if (logTextArea.getRows() >= 1000) {
			logTextArea.setText("");
		}
	}
	
	public void log(String path, String text){
		File file = new File(path);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, true);
			fw.write(text + "\r\n");
			fw.close();
		} catch (IOException ex) {}
	}

}
