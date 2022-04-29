package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import javax.swing.AbstractListModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;

//Create PizzaSystem JFrame class
public class PizzaSystem extends JFrame {

	// Define all controls or widgets for all the program
	private JPanel contentPane;
	private ButtonGroup options;
	private JRadioButton takeOutRdbtn;
	private JRadioButton dineInRdbtn;
	private JList<Pizza> menuList;
	private JList<Pizza> orderList;
	private JButton checkOutBtn;
	private JButton addBtn;
	private JLabel nameLbl;
	private JTextField nameTxbox;

	// Array Lists and other variables and constants
	DefaultListModel<Pizza> lmodel1;
	DefaultListModel<Pizza> lmodel2;
	private ArrayList<Pizza> pizzaList = new ArrayList<Pizza>();
	private ArrayList<Pizza> selectedList = new ArrayList<Pizza>();
	private double totalCost;
	private final double PIZZA_RATE_TAXE = 0.05;

	// Define variables of connection, statement and resultSet for all the program
	private static Connection connection;
	private static Statement statement;
	private static ResultSet resultSet;

	// Define the driver as a static constant
	private static final String JDBC_DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
	// Define the DB Location as a static constant
	private static final String MSA_FILE = "./menu.accdb";
	// Define the Protocol MS ACCESS as a static constant
	private static final String DBA_URL = "jdbc:ucanaccess://" + MSA_FILE;

	/**
	 * Method for launching the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PizzaSystem frame = new PizzaSystem();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Method for create the frame.
	 */
	public PizzaSystem() {
		// Call the method to set up all the controls and their characteristics
		setComponents();
		// Call the method to execute all the event-handler methods.
		setEvents();
	}

	// Method to load the driver, create and set up connection
	public void initDB() {
		try {
			Class.forName(JDBC_DRIVER);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			connection = DriverManager.getConnection(DBA_URL);
			statement = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Method to close every step to access the DB
	public void closeDB() {

		try {
			if (resultSet != null) {
				resultSet.close();
			} else if (statement != null) {
				statement.close();
			} else if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Method for reading all records from the menu database
	// and add each Pizza object to a pizza list
	public void readDB() {
		// Every time this method is called, clear the pizza list
		pizzaList.clear();
		// Call the method to set up connection
		initDB();
		String readQuery = "SELECT * FROM MENU";
		try {
			// Execute the query for reading purposes
			resultSet = statement.executeQuery(readQuery);
			// Set up a loop for reading purposes and add each record to the pizza list
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String pizza = resultSet.getString(2);
				double price = resultSet.getDouble(3);
				Pizza aPizza = new Pizza(pizza, price);
				pizzaList.add(aPizza);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Call the method to close the connection
		closeDB();
	}

	// Method to set up all the controls and their characteristics
	public void setComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 512, 363);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// Create label control for the name
		nameLbl = new JLabel("Name:");

		// Create label text box for the name
		nameTxbox = new JTextField();
		nameTxbox.setColumns(10);

		// Call the method to read every record from the DB and populate the pizza list
		// with all the records
		readDB();
		// Create a list to hold the pizza menu
		menuList = new JList<Pizza>();
		menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		menuList.setFont(new Font("Tahoma", Font.BOLD, 10));

		// Create the model as pizza type
		lmodel1 = new DefaultListModel<Pizza>();

		// Add each pizza object from the pizza list to the model
		for (int i = 0; i < pizzaList.size(); i++) {
			lmodel1.addElement(pizzaList.get(i));
		}

		// Add the model to the menu list
		menuList.setModel(lmodel1);

		// Create a list to hold what the user order from the menu list
		orderList = new JList<Pizza>();
		orderList.setFont(new Font("Tahoma", Font.BOLD, 10));

		// Create the check out and add buttons
		checkOutBtn = new JButton("Check Out");
		checkOutBtn.setForeground(Color.BLACK);
		checkOutBtn.setBackground(Color.WHITE);
		checkOutBtn.setFont(new Font("Tahoma", Font.BOLD, 10));
		addBtn = new JButton("Add");
		addBtn.setFont(new Font("Tahoma", Font.BOLD, 10));

		// Create the button group to allow the take out and dine in buttons to be
		// mutually
		// exclusive (user can only select one of them)
		options = new ButtonGroup();
		takeOutRdbtn = new JRadioButton("Take Out");
		takeOutRdbtn.setBackground(Color.GRAY);
		options.add(takeOutRdbtn);
		dineInRdbtn = new JRadioButton("Dine In");
		dineInRdbtn.setBackground(Color.GRAY);
		options.add(dineInRdbtn);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(10)
						.addComponent(nameLbl, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE).addGap(4)
						.addComponent(nameTxbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(21).addComponent(takeOutRdbtn,
						GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(21)
						.addComponent(dineInRdbtn, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
						.addGap(108).addComponent(addBtn))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(21)
						.addComponent(menuList, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE).addGap(18)
						.addComponent(orderList, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(10).addComponent(checkOutBtn)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(10)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup().addGap(3).addComponent(nameLbl))
								.addComponent(nameTxbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(18).addComponent(takeOutRdbtn).addGap(2)
						.addGroup(gl_contentPane
								.createParallelGroup(Alignment.LEADING).addComponent(dineInRdbtn).addComponent(addBtn))
						.addGap(18)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(menuList, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
								.addComponent(orderList, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
						.addGap(16).addComponent(checkOutBtn)));
		contentPane.setLayout(gl_contentPane);
	}

	// Method to set up and execute all the event-handler methods.
	public void setEvents() {
		// Add a event handler method to deny user select the take out button if he/she
		// has not enter their name before
		takeOutRdbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (nameTxbox.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "You must enter a name!");
					// if name is empty, clear any previous selection by the user
					options.clearSelection();
					return;
				}
			}
		});
		// Add a event handler method to deny user select the dine in button if he/she
		// has not enter their name before
		dineInRdbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (nameTxbox.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "You must enter a name!");
					// if name is empty, clear any previous selection by the user
					options.clearSelection();
					return;
				}

			}
		});
		// Add a event handler method to allow the user add the type of pizza he/she
		// wants to order to the order list
		// the user can select a pizza one at a time from the menu list and add them to
		// the order list.
		// User can select more than 1 pizza and can repeat any pizza
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// If the user does not select any option and press the add button, display
				// an error message to tell user he/she must select an option
				if (menuList.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null, "You must select an option!");
					return;
				}

				// Add each selected option to a selected list each time the user press the add
				// button
				selectedList.add(menuList.getSelectedValue());

				// Create the model for the order list
				lmodel2 = new DefaultListModel<Pizza>();
				// Add each option added previously in the selected list to the model
				for (int i = 0; i < selectedList.size(); i++) {
					lmodel2.addElement(selectedList.get(i));
				}
				// Add the model to the order list.
				orderList.setModel(lmodel2);
			}
		});

		// Add a event handler method to allow the user check out what he/she previously
		// added to the order list
		checkOutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Assign the value of 0 to the total cost.
				totalCost = 0;

				// If the user does not select any option like take our or dine in
				// before it press the check out button, a message will be displayed to indicate
				// the user he/she must select Dine In or Take Out.
				if (!takeOutRdbtn.isSelected() && !dineInRdbtn.isSelected()) {
					JOptionPane.showMessageDialog(null, "Please select Dine In or Take Out.");
					return;
				}

				// Generate a loop to sum the price of the options selected by
				// the user, based on the options added to the selected list.

				for (int i = 0; i < selectedList.size(); i++) {
					if (selectedList.get(i).toString().contains("Hawaian")) {
						totalCost += 11.99;
					} else if (selectedList.get(i).toString().contains("Pepperoni")) {
						totalCost += 11.49;
					} else if (selectedList.get(i).toString().contains("BBQ")) {
						totalCost += 13.99;
					} else if (selectedList.get(i).toString().contains("Tropical")) {
						totalCost += 12.99;
					} else if (selectedList.get(i).toString().contains("VegeWorld")) {
						totalCost += 10.99;
					} else {
						totalCost += 14.99;
					}
				}

				// Display the correct summarize message including if he/she selected Dine In
				// or Take Out, as well as
				// considering the options he/she order previously. Also, add the final price
				// including taxes
				if (takeOutRdbtn.isSelected()) {
					JOptionPane.showMessageDialog(null,
							"Hi! " + nameTxbox.getText() + "!" + "\n" + "You ordered " + selectedList.size()
									+ " item(s) for " + takeOutRdbtn.getText() + ". The total cost is $"
									+ String.format("%.2f", totalCost * (1 + PIZZA_RATE_TAXE)));
				} else {
					JOptionPane.showMessageDialog(null,
							"Hi! " + nameTxbox.getText() + "!" + "\n" + "You ordered " + selectedList.size()
									+ " item(s) for " + dineInRdbtn.getText() + ". The total cost is $"
									+ String.format("%.2f", totalCost * (1 + PIZZA_RATE_TAXE)));
				}

			}
		});
	}
}
