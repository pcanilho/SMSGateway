package ch.cern.it.cs.cs.display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import ch.cern.it.cs.cs.CMD;
import ch.cern.it.cs.cs.QP_SMS;
import ch.cern.it.cs.cs.assets.HintTextField;

public class Display extends JFrame {

	private static final long serialVersionUID = 9171335126735787277L;
	// STATIC
	private static final Dimension WINDOW_DIMENSION = new Dimension(300, 400);
	private static final String WINDOW_TITLE = "SMS Gateway - QP Restart © CERN Paulo Canilho - IT/CS/CS - 28/09/2015";

	// COMPS
	private JPanel multiple_sms_panel;
	private JRadioButton del_mode, keep_mode, apn_reset, ftp_reset;
	private DefaultTableModel sms_list_model;
	private JTextField number_destination;
	private JTextField unit_name;
	private JTable sms_list;
	private JButton send_sms;
	private JRadioButton sms_selected, sms_all;

	// ASSETS
	private JPanel main_panel, button_panel, console_panel;

	public Display() {
		// SET LOOK AND FEEL
		setCustomLookandFeel(DISPLAY_MODE.Nimbus);

		// SETUP
		setSize(WINDOW_DIMENSION);
		setMinimumSize(WINDOW_DIMENSION);
		setTitle(WINDOW_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// INIT MENU
		setUpMenuBar();

		// INIT CONTAINER
		main_panel = new JPanel();
		button_panel = new JPanel();
		console_panel = new JPanel(new BorderLayout());

		add(main_panel, BorderLayout.NORTH);

		add(console_panel, BorderLayout.CENTER);
		console_panel.add(button_panel, BorderLayout.NORTH);
		initContainer();

		// ADD TEXT INPUTs
		addTextInputs();

		// INIT
		setVisible(true);

		del_mode.setSelected(true);
		del_mode.requestFocus();
	}

	public enum DISPLAY_MODE {
		Nimbus, Tattoo
	}

	private void setCustomLookandFeel(DISPLAY_MODE mode) {
		switch (mode) {
		case Nimbus:
			try {
				for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (Exception e) {
				// If Nimbus is not available, you can set the GUI to another
				// look and feel.
			}
			break;

		case Tattoo:
			try {
				UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}

		SwingUtilities.updateComponentTreeUI(this);
	}

	private void setUpMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu system = new JMenu("System");
		JMenu sms = new JMenu("SMS");

		JMenu settings = new JMenu("Settings");
		JMenu display = new JMenu("Window");

		// SMS >
		JMenu send = new JMenu("Send");
		JMenuItem options = new JMenuItem("OPTIONS");
		JCheckBox sms_multiple = new JCheckBox("Multiple SMS");

		options.setEnabled(false);
		send.add(options);
		send.addSeparator();
		send.add(sms_multiple);

		sms_multiple.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				showMultipleSMS(sms_multiple.isSelected());
			}
		});

		sms.add(send);
		// SMS />

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		// LOOK AND FEEL >
		JMenu lookandfeel = new JMenu("Look & Feel");
		JMenuItem lookandfeel_label = new JMenuItem("MODE");
		lookandfeel_label.setEnabled(false);
		JRadioButton radio_light = new JRadioButton("Light");
		radio_light.setSelected(true);

		radio_light.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setCustomLookandFeel(DISPLAY_MODE.Nimbus);
			}
		});
		JRadioButton radio_dark = new JRadioButton("Dark");
		radio_dark.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setCustomLookandFeel(DISPLAY_MODE.Tattoo);
			}
		});

		ButtonGroup radio_group = new ButtonGroup();
		radio_group.add(radio_dark);
		radio_group.add(radio_light);

		lookandfeel.add(lookandfeel_label);
		lookandfeel.addSeparator();
		lookandfeel.add(radio_dark);
		lookandfeel.add(radio_light);

		display.add(lookandfeel);
		settings.add(display);

		// LOOK AND FEEL />

		system.add(exit);

		menuBar.add(system);
		menuBar.add(sms);
		menuBar.add(settings);

		setJMenuBar(menuBar);
	}

	private void showMultipleSMS(boolean selected) {
		multiple_sms_panel.setVisible(selected);
		if (selected) {
			setSize(WINDOW_DIMENSION.width, WINDOW_DIMENSION.height + 150);
			setMinimumSize(new Dimension(WINDOW_DIMENSION.width, WINDOW_DIMENSION.height + 150));
			send_sms.setEnabled(false);
		} else {
			setSize(WINDOW_DIMENSION.width, WINDOW_DIMENSION.height);
			setMinimumSize(new Dimension(WINDOW_DIMENSION.width, WINDOW_DIMENSION.height));
			send_sms.setEnabled(true);
		}
	}

	private void addSMStoList(QP_SMS sms) {
		sms_list_model.addRow(sms.tableEntryFormat());
	}

	private void addTextInputs() {

		// NUMBER
		JLabel number_label = new JLabel("Destination number:");
		number_destination = new HintTextField("(e.g. 075411XXXX)");
		number_destination.setToolTipText("Insert the SMS destination number here (e.g. 075411XXXX)");

		main_panel.add(number_label);
		main_panel.add(number_destination);

		// MULTIPLE SMS
		multiple_sms_panel = new JPanel(new BorderLayout());
		JPanel button_panel_sms = new JPanel(new FlowLayout());

		JButton add_sms = new JButton();
		JButton rem_sms = new JButton();
		add_sms.setIcon(new ImageIcon("imgs/add_icon.png"));
		rem_sms.setIcon(new ImageIcon("imgs/minus_icon.png"));

		// SEND
		sms_selected = new JRadioButton("Selected");
		sms_all = new JRadioButton("All");
		ButtonGroup sms_rdo_group = new ButtonGroup();
		sms_rdo_group.add(sms_selected);
		sms_rdo_group.add(sms_all);

		JButton start_sending_sms = new JButton("Start");

		// SEND MULTIPLE SMSs >
		start_sending_sms.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Timer send_timer = new Timer();
				send_timer.schedule(new TimerTask() {

					public void run() {
						start_sending_sms.setEnabled(false);

						int[] sms_index_to_send = null;

						if (sms_selected.isSelected()) {
							sms_index_to_send = sms_list.getSelectedRows();
							start_sending_sms.setText("Sending: " + sms_index_to_send.length + " SMS...");
							if (sms_index_to_send != null && sms_index_to_send.length > 0) {
								for (int i = 0; i < sms_index_to_send.length; i++) {
									try {
										QP_SMS sms = new QP_SMS(
												sms_list_model.getValueAt(sms_index_to_send[i], 0).toString(),
												sms_list_model.getValueAt(sms_index_to_send[i], 1).toString(),
												(QP_SMS.MODE) sms_list_model.getValueAt(sms_index_to_send[i], 2));
										CMD.SEND_SMS(sms);

										start_sending_sms
												.setText("Sending: [" + i + "/" + sms_index_to_send.length + "]");
										Thread.sleep(2000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							} else {
								System.out.println("No SMS to send.");
								return;
							}
						} else if (sms_all.isSelected()) {
							int sms_count = sms_list.getRowCount();
							start_sending_sms.setText("Sending: " + sms_count + " SMS...");
							if (sms_count == 0) {
								System.out.println("No SMS to send.");
								return;
							}
							for (int i = 0; i < sms_count; i++) {
								try {
									QP_SMS sms = new QP_SMS(sms_list_model.getValueAt(i, 0).toString(),
											sms_list_model.getValueAt(i, 1).toString(),
											(QP_SMS.MODE) sms_list_model.getValueAt(i, 2));
									CMD.SEND_SMS(sms);
									start_sending_sms.setText("Sending: [" + i + "/" + sms_count + "]");
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
						start_sending_sms.setEnabled(true);
						start_sending_sms.setText("Start");

					}
				}, 0);
			}
		});

		// SEND MULTIPLE SMSs />

		JPanel rdio_button_sms = new JPanel(new BorderLayout());

		JPanel rdio_button_start = new JPanel(new FlowLayout());
		JPanel rdio_button_child = new JPanel(new FlowLayout());
		rdio_button_child.add(sms_all);
		rdio_button_child.add(sms_selected);

		rdio_button_start.add(start_sending_sms);

		rdio_button_sms.add(rdio_button_child, BorderLayout.NORTH);
		rdio_button_sms.add(rdio_button_start, BorderLayout.CENTER);

		// TABLE BUTTONS >

		// ADD
		add_sms.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean valid = false;

				if (number_destination.getText().isEmpty() || number_destination.getText().length() != 10
						|| !number_destination.getText().startsWith("075411") || unit_name.getText().isEmpty()
						|| (!del_mode.isSelected() && !keep_mode.isSelected())) {
					System.err.println(
							"Number not valid! (e.g. 075411XXXX)\nUnit Name cannot be empty!\nAt least one mode has to be selected!");
					return;
				} else
					valid = true;

				if (valid) {
					if (del_mode.isSelected())
						addSMStoList(new QP_SMS(number_destination.getText(), unit_name.getText(),
								QP_SMS.MODE.DELETE_TASKS));
					else
						addSMStoList(
								new QP_SMS(number_destination.getText(), unit_name.getText(), QP_SMS.MODE.KEEP_TASKS));
				}
			}
		});

		// REM
		rem_sms.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				removeSMSFromList(SMS_REMOVE_MODE.SELECTED_ONLY);
			}
		});

		// TABLE BUTTOS />

		sms_list_model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				if (sms_list_model.getColumnName(column).equals("MODE"))
					return false;
				return super.isCellEditable(row, column);
			}
		};
		sms_list = new JTable(sms_list_model);
		JScrollPane sms_scroll = new JScrollPane(sms_list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sms_scroll.setPreferredSize(new Dimension(100, 100));

		sms_list_model.addColumn("Destination");
		sms_list_model.addColumn("Unit Name");
		sms_list_model.addColumn("MODE");

		button_panel_sms.add(add_sms);
		button_panel_sms.add(rem_sms);
		multiple_sms_panel.add(button_panel_sms, BorderLayout.NORTH);
		multiple_sms_panel.add(sms_scroll, BorderLayout.CENTER);
		multiple_sms_panel.add(rdio_button_sms, BorderLayout.SOUTH);

		multiple_sms_panel.setVisible(false);
		main_panel.add(multiple_sms_panel);

		// UNIT NAME
		JLabel unit_name_label = new JLabel("Unit Name:");
		unit_name = new HintTextField("(e.g. ISOLDE)");
		unit_name.setToolTipText("Insert the QP destination Unit Name here (e.g. ISOLDE)");

		main_panel.add(unit_name_label);
		main_panel.add(unit_name);

		// MODE
		JLabel mode = new JLabel("Mode:");
		del_mode = new JRadioButton("Delete Tasks");
		keep_mode = new JRadioButton("Keep Tasks");
		apn_reset = new JRadioButton("Reset APN");
		ftp_reset = new JRadioButton("Reset FTP");

		ButtonGroup group = new ButtonGroup();
		group.add(del_mode);
		group.add(keep_mode);
		group.add(apn_reset);
		group.add(ftp_reset);

		main_panel.add(mode);
		main_panel.add(del_mode);
		main_panel.add(keep_mode);
		main_panel.add(apn_reset);
		main_panel.add(ftp_reset);

		// SEND SMS BUTTON
		JPanel panel = new JPanel(new FlowLayout());
		send_sms = new JButton("Send SMS");
		panel.add(send_sms);
		button_panel.add(panel);

		send_sms.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				boolean valid = false;

				if (number_destination.getText().isEmpty() || number_destination.getText().length() != 10
						|| !number_destination.getText().startsWith("075411") || unit_name.getText().isEmpty()
						|| (!del_mode.isSelected() && !keep_mode.isSelected() && !apn_reset.isSelected()
								&& !ftp_reset.isSelected())) {
					System.err.println(
							"Number not valid! (e.g. 075411XXXX)\nUnit Name cannot be empty!\nAt least one mode has to be selected!\nOne MODE has to be selected.");
					return;
				} else
					valid = true;

				QP_SMS sms = null;
				if (valid) {
					if (del_mode.isSelected())
						sms = new QP_SMS(number_destination.getText(), unit_name.getText(), QP_SMS.MODE.DELETE_TASKS);
					else if (keep_mode.isSelected())
						sms = new QP_SMS(number_destination.getText(), unit_name.getText(), QP_SMS.MODE.KEEP_TASKS);
					else if (apn_reset.isSelected())
						sms = new QP_SMS(number_destination.getText(), unit_name.getText(), QP_SMS.MODE.APN_RESET);
					else if (ftp_reset.isSelected())
						sms = new QP_SMS(number_destination.getText(), unit_name.getText(), QP_SMS.MODE.FTP_RESET);

					if (sms == null) {
						System.err.println("ERROR on sending SMS - SMS is NULL!");
						return;
					}

					CMD.SEND_SMS(sms);
					System.out.println("SMS sent!");
				}
			}
		});

		// CONSOLE
		JTextArea console = new JTextArea();
		JScrollPane scroll_console_panel = new JScrollPane(console);
		scroll_console_panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		console.setBorder(BorderFactory.createBevelBorder(1));
		console.setEditable(false);
		PrintStream printStream = new PrintStream(new CustomOutputStream(console));
		System.setOut(printStream);
		System.setErr(printStream);

		console_panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		console_panel.add(scroll_console_panel);

	}

	private enum SMS_REMOVE_MODE {
		SELECTED_ONLY, FIRST
	}

	private void removeSMSFromList(SMS_REMOVE_MODE mode) {
		if (mode == SMS_REMOVE_MODE.SELECTED_ONLY) {
			int[] selectec_index = sms_list.getSelectedRows();
			for (int i = 0; i < selectec_index.length; i++) {
				if (selectec_index[i] > sms_list.getRowCount())
					sms_list_model.removeRow(sms_list.getRowCount() - 1);
				else
					sms_list_model.removeRow(selectec_index[i]);
			}
		} else if (mode == SMS_REMOVE_MODE.FIRST) {
			int colNum = sms_list_model.getRowCount();
			if (colNum > 0)
				sms_list_model.removeRow(0);
		}
	}

	private void initContainer() {
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
		main_panel.setBorder(new EmptyBorder(10, 10, 10, 10));
	}

}
