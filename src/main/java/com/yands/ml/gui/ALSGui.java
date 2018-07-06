package com.yands.ml.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yands.ml.common.Constant;
import com.yands.ml.common.GuiUtil;
import com.yands.ml.common.Log;
import com.yands.ml.entity.ResponseData;
import com.yands.ml.util.Spider;

public class ALSGui extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel als_panel;
	private JTextField appTextField;
	private JTextField rankTextField;
	private JTextField iteraTextField;
	private JTextField trainDataTextField;
	private JTextField ycUserTextField;
	private JTextField ycProductTextField;
	private JTextField userTopnTextField;
	private JTextField topnUserTextField;
	private JTextField productTponTextField;
	private JTextField topnProductTextField;
	
	private JTextArea showTextArea;
	private Log log;
	private JTextField veriTextField;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ALSGui als = new ALSGui();
					als.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ALSGui() {
		setTitle("ALS");
		als_panel = new JPanel();
		als_panel.setBorder(new TitledBorder(new EmptyBorder(5, 5, 5, 5), "", TitledBorder.LEADING, TitledBorder.TOP,
				null, Color.BLACK));
		als_panel.setLayout(null);
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				int x = JOptionPane.showConfirmDialog(als_panel, "你确定要关闭程序吗？", "提示", JOptionPane.WARNING_MESSAGE);
				if (x == JOptionPane.OK_OPTION) {
					dispose();
					System.exit(3);
				}
			}
		});
		setResizable(false);
		setBounds(600, 400, 934, 345);
		setContentPane(als_panel);

		JPanel trainPanel = new JPanel();
		// trainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		trainPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		trainPanel.setLayout(null);
		trainPanel.setBounds(10, 10, 205, 285);
		als_panel.add(trainPanel);

		JLabel lblAppname = new JLabel("APP Name");
		lblAppname.setBounds(10, 23, 75, 20);
		trainPanel.add(lblAppname);
		lblAppname.setBorder(BorderFactory.createLineBorder(Color.black));
		lblAppname.setHorizontalAlignment(SwingConstants.CENTER);

		appTextField = new JTextField();
		appTextField.setBounds(95, 23, 100, 20);
		trainPanel.add(appTextField);
		appTextField.setText("JavaASLM1M");
		appTextField.setColumns(10);

		final JComboBox<String> modeComboBox = new JComboBox<String>();
		modeComboBox.setBounds(95, 66, 100, 20);
		trainPanel.add(modeComboBox);
		modeComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "local[3]", "local[2]" }));

		JLabel lblRunMode = new JLabel("RUN Mode");
		lblRunMode.setBounds(10, 66, 75, 20);
		trainPanel.add(lblRunMode);
		lblRunMode.setBorder(BorderFactory.createLineBorder(Color.black));
		lblRunMode.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblRank = new JLabel("Rank");
		lblRank.setBounds(10, 109, 75, 20);
		trainPanel.add(lblRank);
		lblRank.setBorder(BorderFactory.createLineBorder(Color.black));
		lblRank.setToolTipText("矩阵分解时对应的低维的维数:即PTm×kQk×nPm×kTQk×n中的维度k.这个值会影响矩阵分解的性能,"
				+ "越大则算法运行的时间和占用的内存可能会越多.通常需要进行调参,一般可以取10-200之间的数.");
		lblRank.setHorizontalAlignment(SwingConstants.CENTER);

		rankTextField = new JTextField();
		rankTextField.setBounds(95, 109, 100, 20);
		trainPanel.add(rankTextField);
		rankTextField.setText("10");
		rankTextField.setColumns(10);

		JLabel lblNumItrea = new JLabel("NUM Itrea");
		lblNumItrea.setBounds(10, 152, 75, 20);
		trainPanel.add(lblNumItrea);
		lblNumItrea.setToolTipText(
				"在矩阵分解用交替最小二乘法求解时，进行迭代的最大次数。这个值取决于评分矩阵的维度，" + "以及评分矩阵的系数程度。一般来说，不需要太大，比如5-20次即可。默认值是5。");
		lblNumItrea.setHorizontalAlignment(SwingConstants.CENTER);
		lblNumItrea.setBorder(BorderFactory.createLineBorder(Color.black));

		iteraTextField = new JTextField();
		iteraTextField.setBounds(95, 152, 100, 20);
		trainPanel.add(iteraTextField);
		iteraTextField.setText("10");
		iteraTextField.setColumns(10);

		trainDataTextField = new JTextField();
		trainDataTextField.setText("点击选择训练数据");
		trainDataTextField.setEditable(false);
		trainDataTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String path = GuiUtil.getSelectFilePath();
				if (path == null) {
					return;
				}
				trainDataTextField.setText(path);
			}
		});
		trainDataTextField.setBounds(10, 195, 185, 20);
		trainPanel.add(trainDataTextField);
		trainDataTextField.setColumns(10);

		JButton trainButton = new JButton("开始训练");
		trainButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JSONObject json = new JSONObject();
				json.put("name", appTextField.getText());
				json.put("mode", modeComboBox.getSelectedItem().toString());
				json.put("rank", rankTextField.getText());
				json.put("itera", iteraTextField.getText());
				json.put("path", trainDataTextField.getText());
				ResponseData post = Spider.post(Constant.BASE_URL + "als/train", json.toJSONString());
				log.log(post.getMessage());
			}
		});
		trainButton.setBounds(10, 238, 185, 23);
		trainPanel.add(trainButton);
		trainButton.setToolTipText("");
		
		JPanel checkPanel = new JPanel();
		checkPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		checkPanel.setLayout(null);
		checkPanel.setBounds(225, 10, 683, 287);
		als_panel.add(checkPanel);
		
		JLabel ycUserLabel = new JLabel("User");
		ycUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ycUserLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		ycUserLabel.setBounds(10, 200, 50, 20);
		checkPanel.add(ycUserLabel);
		
		ycUserTextField = new JTextField();
		ycUserTextField.setText("18");
		ycUserTextField.setColumns(10);
		ycUserTextField.setBounds(70, 200, 72, 20);
		checkPanel.add(ycUserTextField);
		
		JLabel ycProductLabel = new JLabel("Product");
		ycProductLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ycProductLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		ycProductLabel.setBounds(10, 230, 50, 20);
		checkPanel.add(ycProductLabel);
		
		ycProductTextField = new JTextField();
		ycProductTextField.setText("177");
		ycProductTextField.setColumns(10);
		ycProductTextField.setBounds(70, 230, 72, 20);
		checkPanel.add(ycProductTextField);
		
		JButton ycButton = new JButton("预测");
		ycButton.setToolTipText("");
		ycButton.setBounds(10, 260, 132, 23);
		ycButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String userId = ycUserTextField.getText();
				String productId = ycProductTextField.getText();
				JSONObject json = new JSONObject();
				json.put("name", appTextField.getText());
				json.put("userId", userId);
				json.put("productId", productId);
				ResponseData post = Spider.post(Constant.BASE_URL + "als/forecast", json.toJSONString());
				if (post.getCode() == 200) {
					log.log("用户" + userId + "对商品" + productId + "的预测评分为:" + post.getResult());
				} else {
					log.log(post.getMessage());
				}
			}
		});
		checkPanel.add(ycButton);

		JPanel panel_log = new JPanel();
		panel_log.setBounds(152, 45, 521, 238);
		checkPanel.add(panel_log);
		panel_log.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel_log.add(scrollPane, BorderLayout.CENTER);
		
		
		showTextArea = new JTextArea();
		showTextArea.setLineWrap(true);
		showTextArea.setEditable(false);
		showTextArea.setBounds(152, 44, 521, 233);
		scrollPane.add(showTextArea);
		scrollPane.setViewportView(showTextArea);
		log = new Log(showTextArea);
		
		JLabel userTopnLabel = new JLabel("User");
		userTopnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userTopnLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		userTopnLabel.setBounds(10, 14, 50, 20);
		checkPanel.add(userTopnLabel);
		
		userTopnTextField = new JTextField();
		userTopnTextField.setText("1");
		userTopnTextField.setColumns(10);
		userTopnTextField.setBounds(70, 14, 72, 20);
		checkPanel.add(userTopnTextField);
		
		JButton userTopnButton = new JButton("获取最喜欢topn");
		userTopnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JSONObject json = new JSONObject();
				json.put("name", appTextField.getText());
				String userId = userTopnTextField.getText();
				json.put("userId", userId);
				String topn = topnUserTextField.getText();
				json.put("topn", topn);
				ResponseData post = Spider.post(Constant.BASE_URL + "als/getTopnByUser", json.toJSONString());
				if (post.getCode() == 200) {
					log.log("用户" + userId + "最喜欢的" + topn + "个商品为:");
					JSONObject data = JSON.parseObject(post.getResult().toString());
					Set<Entry<String, Object>> entrys = data.entrySet();
					for (Entry<String, Object> entry : entrys) {
						log.log("商品:" + entry.getKey() + " 评分:" + entry.getValue());
					}
				} else {
					log.log(post.getMessage());
				}
			}
		});
		userTopnButton.setToolTipText("");
		userTopnButton.setBounds(10, 74, 132, 23);
		checkPanel.add(userTopnButton);
		
		JLabel topnUserLabel = new JLabel("topn");
		topnUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topnUserLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		topnUserLabel.setBounds(10, 44, 50, 20);
		checkPanel.add(topnUserLabel);
		
		topnUserTextField = new JTextField();
		topnUserTextField.setText("20");
		topnUserTextField.setColumns(10);
		topnUserTextField.setBounds(70, 44, 72, 20);
		checkPanel.add(topnUserTextField);
		
		JLabel lblProduct_1 = new JLabel("Product");
		lblProduct_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblProduct_1.setBorder(BorderFactory.createLineBorder(Color.black));
		lblProduct_1.setBounds(10, 107, 50, 20);
		checkPanel.add(lblProduct_1);
		
		productTponTextField = new JTextField();
		productTponTextField.setText("20");
		productTponTextField.setColumns(10);
		productTponTextField.setBounds(70, 107, 72, 20);
		checkPanel.add(productTponTextField);
		
		JLabel topnProductLabel = new JLabel("topn");
		topnProductLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topnProductLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		topnProductLabel.setBounds(10, 137, 50, 20);
		checkPanel.add(topnProductLabel);
		
		topnProductTextField = new JTextField();
		topnProductTextField.setText("10");
		topnProductTextField.setColumns(10);
		topnProductTextField.setBounds(70, 137, 72, 20);
		checkPanel.add(topnProductTextField);
		
		JButton productTopnButton = new JButton("获取最喜欢topn");
		productTopnButton.setToolTipText("");
		productTopnButton.setBounds(10, 167, 132, 23);
		productTopnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String productId = productTponTextField.getText();
				String topn = topnUserTextField.getText();
				JSONObject json = new JSONObject();
				json.put("name", appTextField.getText());
				json.put("productId", productId);
				json.put("topn", topn);
				ResponseData post = Spider.post(Constant.BASE_URL + "als/getTopnByProduct", json.toJSONString());
				if (post.getCode() == 200) {
					log.log("最喜欢商品" + productId + "的前" + topn + "个用户为:");
					JSONObject data = JSON.parseObject(post.getResult().toString());
					Set<Entry<String, Object>> entrys = data.entrySet();
					for (Entry<String, Object> entry : entrys) {
						log.log("用户:" + entry.getKey() + " 评分:" + entry.getValue());
					}
				} else {
					log.log(post.getMessage());
				}
			}
		});
		checkPanel.add(productTopnButton);
		
		veriTextField = new JTextField();
		veriTextField.setEditable(false);
		veriTextField.setText("点击选择验证数据");
		veriTextField.setColumns(10);
		veriTextField.setBounds(152, 14, 185, 20);
		veriTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String path = GuiUtil.getSelectFilePath();
				if (path == null) {
					return;
				}
				veriTextField.setText(path);
			}
		});
		checkPanel.add(veriTextField);
		
		JButton veriButton = new JButton("开始验证");
		veriButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String path = veriTextField.getText();
				if (path == null || "点击选择验证数据".equals(path)) {
					return;
				}
				JSONObject json = new JSONObject();
				json.put("name", appTextField.getText());
				json.put("path", veriTextField.getText());
				ResponseData post = Spider.post(Constant.BASE_URL + "als/verification", json.toJSONString());
				if (post.getCode() == 200) {
					JSONArray arr = JSON.parseArray(post.getResult().toString());
					for (int x = 0; x < arr.size(); x++) {
						JSONObject entry = arr.getJSONObject(x);
						log.log("用户:" + entry.get("user") + " 商品:" + entry.get("product") + " 评分:" + entry.get("rating"));
					}
				} else {
					log.log(post.getMessage());
				}
			}
		});
		veriButton.setToolTipText("");
		veriButton.setBounds(347, 13, 185, 23);
		checkPanel.add(veriButton);
		
		JButton closeButton = new JButton("CLOSE");
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				JavaALSM1M.close();
			}
		});
		closeButton.setBounds(542, 13, 131, 23);
		checkPanel.add(closeButton);
	}
}