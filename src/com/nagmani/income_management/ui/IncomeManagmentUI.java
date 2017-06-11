package com.nagmani.income_management.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.AbstractListModel;

import com.nagmani.income_management.dao.IncomeManagementDao;
import com.toedter.calendar.JDateChooser;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.JScrollPane;


public class IncomeManagmentUI {

	private JFrame frame;
	private JTextField textTipAmount;
	private JTextField txtHomeIncomeAmount;
	int tip_Amount;
	String tip_Date;
	int home_income_amount;
	String home_Work_Date;
	private JTable table;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					IncomeManagmentUI window = new IncomeManagmentUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IncomeManagmentUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 548, 365);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblIncomeManagement = new JLabel("INCOME MANAGEMENT");
		lblIncomeManagement.setFont(new Font("Sitka Subheading", Font.BOLD, 20));
		lblIncomeManagement.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblIncomeManagement, BorderLayout.NORTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel tip_entry = new JPanel();
		tabbedPane.addTab("TIP_Entry", null, tip_entry, null);
		tip_entry.setLayout(null);
		
		JLabel lblDate = new JLabel("Date:");
		lblDate.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDate.setBounds(10, 30, 92, 19);
		tip_entry.add(lblDate);
		
		JLabel label = new JLabel("Amount:");
		label.setFont(new Font("Tahoma", Font.BOLD, 20));
		label.setBounds(10, 87, 92, 19);
		tip_entry.add(label);
		
		
		
		textTipAmount = new JTextField();
		textTipAmount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c= e.getKeyChar();
				
				if (!(Character.isDigit(c))||(c==KeyEvent.VK_BACK_SPACE) ||(c==KeyEvent.VK_DELETE)){
					e.consume();
					
				}
			}
		});
		textTipAmount.setBounds(132, 89, 119, 23);
		tip_entry.add(textTipAmount);
		textTipAmount.setColumns(10);
		
		JDateChooser dateChooserOfWork = new JDateChooser();
		dateChooserOfWork.setBounds(132, 30, 128, 23);
		dateChooserOfWork.setMaxSelectableDate(new Date());
		tip_entry.add(dateChooserOfWork);

		JLabel lblMessage = new JLabel("");
		lblMessage.setBounds(29, 123, 402, 30);
		tip_entry.add(lblMessage);
		
		JButton btnTipSubmit = new JButton("Submit");
		btnTipSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String enteredTipAmount=textTipAmount.getText();
				String selected_tip_Date= ((JTextField)dateChooserOfWork.getDateEditor().getUiComponent()).getText();
				
				/**
				 * perform null check
				 */
				if ((selected_tip_Date.equals(""))){
					
					lblMessage.setText("Please enter Tip Date");
					
					
					
				}else if (enteredTipAmount.equals("")) {
					lblMessage.setText("Please enter tip amount");

				} else{
				
				
				tip_Amount =Integer.parseInt(enteredTipAmount);
				
				/**
				 * Move these date conversion code to utility class
				 */
				
				DateFormat OriginalFormat= new SimpleDateFormat("MMMM dd,yyyy");
				DateFormat targetFormat= new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date= OriginalFormat.parse(selected_tip_Date);
					tip_Date= targetFormat.format(date);

				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					IncomeManagementDao dao= new IncomeManagementDao();
					dao.insertIntoDateTable(tip_Amount, tip_Date);
					dao.closeConnection();
				} catch (IOException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally{
				}
				
				lblMessage.setText("You have Successfully submitted your Tip." + "$" +tip_Amount +" "+ "for"+ tip_Date);
			}
			}
		});
		btnTipSubmit.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnTipSubmit.setBounds(10, 173, 89, 23);
		tip_entry.add(btnTipSubmit);
		
		JButton btnTotalTip = new JButton("Total Tip");
		btnTotalTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					IncomeManagementDao dao= new IncomeManagementDao();
					lblMessage.setText("So far you earned $"+ dao.getTotalTipAmount() +" "+ "as Tip" );
					dao.closeConnection();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnTotalTip.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnTotalTip.setBounds(121, 175, 114, 21);
		tip_entry.add(btnTotalTip);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textTipAmount.setText(null);
				((JTextField)dateChooserOfWork.getDateEditor().getUiComponent()).setText(null);
				lblMessage.setText(null);
				
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnClear.setBounds(260, 175, 89, 23);
		tip_entry.add(btnClear);
		
		JPanel home_entry = new JPanel();
		tabbedPane.addTab("Home_Entry", null, home_entry, null);
		home_entry.setLayout(null);
		
		JLabel lblDate_1 = new JLabel("Date:");
		lblDate_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDate_1.setBounds(10, 11, 97, 25);
		home_entry.add(lblDate_1);
		
		JLabel label_1 = new JLabel("Work Type");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		label_1.setBounds(10, 63, 122, 25);
		home_entry.add(label_1);
		
		JLabel label_2 = new JLabel("Amount");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		label_2.setBounds(10, 138, 122, 25);
		home_entry.add(label_2);
		
		
		
		txtHomeIncomeAmount = new JTextField();
		txtHomeIncomeAmount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c= e.getKeyChar();
				
				if (!(Character.isDigit(c))||(c==KeyEvent.VK_BACK_SPACE) ||(c==KeyEvent.VK_DELETE)){
					e.consume();
					
				}
			}
		});
		txtHomeIncomeAmount.setBounds(159, 142, 143, 25);
		home_entry.add(txtHomeIncomeAmount);
		txtHomeIncomeAmount.setColumns(10);
		
		JList listProductMenu = new JList();
		listProductMenu.setModel(new AbstractListModel() {
			String[] values = new String[] {"-Select", "EyeBrow", "Facial", "Mehendi"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listProductMenu.setBounds(159, 63, 143, 68);
		home_entry.add(listProductMenu);
		listProductMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listProductMenu.setSelectedIndex(0);
		
		
		JDateChooser dateChooserForHomeIncome = new JDateChooser();
		dateChooserForHomeIncome.setBounds(153, 16, 129, 20);
		home_entry.add(dateChooserForHomeIncome);
		dateChooserForHomeIncome.setMaxSelectableDate(new Date());
		
		
		JLabel lblHome_IncomeMessage = new JLabel("");
		lblHome_IncomeMessage.setBounds(20, 174, 402, 30);
		home_entry.add(lblHome_IncomeMessage);
		
		JButton btnHomeIncomeSubmit = new JButton("Submit");
		btnHomeIncomeSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String enteredIncomeAmount=txtHomeIncomeAmount.getText();
				String selectedIncomeDate= ((JTextField)dateChooserForHomeIncome.getDateEditor().getUiComponent()).getText();
				String selectProductType=listProductMenu.getSelectedValue().toString();
				
				/**
				 * perfrom null check 
				 */
				
				if (selectedIncomeDate.equals("")){
					lblHome_IncomeMessage.setText("Please eneter Home Income Date");
				} else if (selectProductType.equals("-Select")){
					lblHome_IncomeMessage.setText("Please eneter Product type");

				}else if (enteredIncomeAmount.equals("")){
					lblHome_IncomeMessage.setText("Please eneter home income");
				} else{
					
					
					home_income_amount =Integer.parseInt(enteredIncomeAmount);
					
					/**
					 * Move these date conversion code to utility class
					 */
					
					DateFormat OriginalFormat= new SimpleDateFormat("MMMM dd,yyyy");
					DateFormat targetFormat= new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date date= OriginalFormat.parse(selectedIncomeDate);
						home_Work_Date= targetFormat.format(date);

					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
					try {
						IncomeManagementDao dao= new IncomeManagementDao();
						dao.insertIntoHomeIncomeTable(home_income_amount, selectProductType, home_Work_Date);;
						dao.closeConnection();
					} catch (IOException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}finally{
					}
					
					lblHome_IncomeMessage.setText("You have Successfully submitted your home income." + "$" +home_income_amount +" "+ "for"+ home_Work_Date);
				}
			}
		});
		
		btnHomeIncomeSubmit.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnHomeIncomeSubmit.setBounds(10, 211, 89, 23);
		home_entry.add(btnHomeIncomeSubmit);
		
		JButton btnTotalHomeIncome = new JButton("Total Income");
		btnTotalHomeIncome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					IncomeManagementDao dao= new IncomeManagementDao();
					lblHome_IncomeMessage.setText("So far you earned $"+ dao.getTotalHomeIncomeAmount() +" "+ "as home income" );
					dao.closeConnection();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnTotalHomeIncome.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnTotalHomeIncome.setBounds(109, 210, 160, 24);
		home_entry.add(btnTotalHomeIncome);
		
		JButton btnHomeIncomeClear = new JButton("Clear");
		btnHomeIncomeClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtHomeIncomeAmount.setText(null);
				((JTextField)dateChooserForHomeIncome.getDateEditor().getUiComponent()).setText(null);
				listProductMenu.setSelectedIndex(0);
				lblHome_IncomeMessage.setText(null);
				
			}
		});
		btnHomeIncomeClear.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnHomeIncomeClear.setBounds(279, 210, 129, 24);
		home_entry.add(btnHomeIncomeClear);
		
		JPanel total_income = new JPanel();
		tabbedPane.addTab("Total_Income", null, total_income, null);
		total_income.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(54, 96, 326, 14);
		total_income.add(lblNewLabel);
		tip_entry.setLayout(null);
		
		
		JButton button_5 = new JButton("Total Income");
		button_5.setBounds(81, 27, 243, 37);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					IncomeManagementDao dao= new IncomeManagementDao();
					JScrollPane incomeTable = new JScrollPane();
					incomeTable.setBounds(30, 121, 430, 43);
					total_income.add(incomeTable);
					
					
					Object []columnNames= {"Tip Income","Home Income","Total Income"};
					Object [][] data= {{new Integer(dao.getTotalTipAmount()),new Integer(dao.getTotalHomeIncomeAmount()),new Integer(dao.getTotalHomeIncomeAmount()+dao.getTotalTipAmount())}};
					 table = new JTable(data, columnNames);
					incomeTable.setViewportView(table);
					dao.closeConnection();
				} catch (IOException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally{
					
				}
				
				
				
				
				
			}
		});
		button_5.setFont(new Font("Tahoma", Font.BOLD, 16));
		total_income.add(button_5);
		
		JButton btnClear_1 = new JButton("Clear");
		btnClear_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				table.setVisible(false);
				
			}
		});
		btnClear_1.setFont(new Font("Sitka Text", Font.BOLD, 16));
		btnClear_1.setBounds(353, 27, 89, 32);
		total_income.add(btnClear_1);
		
		
		
		
		
		
		
	}
}
