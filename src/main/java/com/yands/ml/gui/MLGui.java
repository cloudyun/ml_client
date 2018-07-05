package com.yands.ml.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MLGui {

	private JFrame ml;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MLGui window = new MLGui();
					window.ml.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MLGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ml = new JFrame();
		ml.setBounds(100, 100, 825, 544);
//		ml.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ml.getContentPane().setLayout(null);
		ml.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int x = JOptionPane.showConfirmDialog(ml, "你确定要关闭程序吗？", "提示", JOptionPane.WARNING_MESSAGE);
				if (x == JOptionPane.OK_OPTION) {
					ml.dispose();
				}
			}
		});
		
		JButton btnAlsgui = new JButton("ALSGui");
		btnAlsgui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new ALSGui().setVisible(true);
			}
		});
		btnAlsgui.setBounds(10, 10, 93, 23);
		ml.getContentPane().add(btnAlsgui);
	}
}
