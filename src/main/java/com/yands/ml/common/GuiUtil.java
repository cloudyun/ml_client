package com.yands.ml.common;

import java.io.File;

import javax.swing.JFileChooser;

public class GuiUtil {
	
	public static String getSelectFilePath() {
		String path = System.class.getResource("/").getPath();
		JFileChooser chooser = new JFileChooser(path);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.showOpenDialog(null);
		File file = chooser.getSelectedFile();
		if (file == null) {
			return null; 
		}
		return file.getPath();
	}
}