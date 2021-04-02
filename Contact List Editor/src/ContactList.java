import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Vector;

public class ContactList extends JFrame implements ActionListener, ListSelectionListener {

	JMenuItem newMI, openMI, saveMI, saveAsMI, exitMI;
	JMenuItem searchMI, deleteMI, updateMI, newEntryMI, sortMI;
    JTextField lastName, firstName, phoneNumber;
    JList<String> listView;
	DefaultListModel<String> nameList = new DefaultListModel<String>();
	Vector<String> numberList = new Vector<String>();
	File currentFile = null;

    /**
     * Constructor
     */
	public ContactList() {
		super("Phone Contacts");          // set frame title
		setLayout(new BorderLayout());    // set layout

		// create menu bar
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		// create file menu
		JMenu fileMenu = new JMenu("File");
		menubar.add(fileMenu);
		newMI = fileMenu.add(new JMenuItem("New"));
		newMI.addActionListener(this);
		openMI = fileMenu.add(new JMenuItem("Open"));
		openMI.addActionListener(this);
		fileMenu.addSeparator();
		saveMI = fileMenu.add(new JMenuItem("Save"));
		saveAsMI = fileMenu.add(new JMenuItem("Save As ..."));
		fileMenu.addSeparator();
		exitMI = fileMenu.add(new JMenuItem("Exit"));
		exitMI.addActionListener(this);

		// create edit menu
		JMenu editMenu = new JMenu("Edit");
		menubar.add(editMenu);
		updateMI = editMenu.add(new JMenuItem("Update"));
		updateMI.addActionListener(this);
		newEntryMI = editMenu.add(new JMenuItem("New Entry"));
		newEntryMI.addActionListener(this);
		deleteMI = editMenu.add(new JMenuItem("Delete"));
		deleteMI.addActionListener(this);  // Added Action Listener for delete option
		editMenu.addSeparator();
		searchMI = editMenu.add(new JMenuItem("Search"));
		searchMI.addActionListener(this);	// Add
		sortMI = editMenu.add(new JMenuItem("Sort"));
		sortMI.addActionListener(this);	   // Added Action Listener for sort option

        // create phone list and controls
        JPanel listPanel = new JPanel(new BorderLayout());
        add(listPanel, BorderLayout.CENTER);
        JLabel label = new JLabel("Name List", JLabel.LEFT);
        listPanel.add(label, BorderLayout.NORTH);
        listPanel.setBackground(Color.LIGHT_GRAY);
        
        listView = new JList<String>(nameList);
        listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listView.addListSelectionListener(this);
        JScrollPane listScroller = new JScrollPane(listView);
        listPanel.add(listScroller, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
	    add(panel, BorderLayout.WEST);
	    JPanel editPanel = new JPanel(new GridLayout(6, 1));
	    panel.add(editPanel, BorderLayout.NORTH);
	    label = new JLabel("Last Name", Label.LEFT);
	    editPanel.add(label);
	    lastName = new JTextField();
	    editPanel.add(lastName);
	    label = new JLabel("First Name", Label.LEFT);
	    editPanel.add(label);
	    firstName = new JTextField();
	    editPanel.add(firstName);
	    label = new JLabel("Phone Number", Label.LEFT);
	    editPanel.add(label);
	    phoneNumber = new JTextField();
	    editPanel.add(phoneNumber);
	}

    // implementing ActionListener
	public void actionPerformed(ActionEvent event) {
	    Object source = event.getSource();
	    if(source == newMI) {
    		nameList.clear();
    		numberList.clear();
	    	currentFile = null;
	    	display(-1);
		    setTitle("Phone Contacts");   // reset frame title
		}
		else if(source == openMI) {
			doOpen();
		}
	    else if(source == exitMI) {
	        System.exit(0);
	    }
		else if(source == updateMI) {
		    int index = listView.getSelectedIndex();
		    String name = lastName.getText().trim() + " " + firstName.getText().trim();
		    String number = phoneNumber.getText().trim();
		    if(index < 0) {  // add a new entry
		        nameList.addElement(name);
		        numberList.addElement(number);
		        index = nameList.getSize()-1;
		    }
		    else {  // update an existing entry
		        nameList.set(index, name);
		        numberList.set(index, number);        
		    }
		    listView.setSelectedIndex(index);
		    listView.ensureIndexIsVisible(index);
		}
		else if(source == newEntryMI) {
		    listView.clearSelection();
		    display(-1);
		}
		else if(source == searchMI) {
			// Prompts dialog box and ask's user for input
			// input saved as searchName
		    String searchName = JOptionPane.showInputDialog(this,
		                        "Please enter a name, Last First: ");
		    
		    boolean found = false;
		    // iterates through nameList, if searchName and nameList match
		    // the name is selected within the list and text box 
		    for(int i = 0; i < nameList.size(); i++) {
		    	if (nameList.get(i).equals(searchName)) {
		    		 listView.setSelectedIndex(i);
		    		 listView.ensureIndexIsVisible(i);
		    		 found = true;
		    	}
		    }
		    // If not found, dialog box prompts message
		    if(found == false){
		    	JOptionPane.showMessageDialog(this, "The name " + searchName + 
		    			" is not in the Contact List");
		    }
		}
		else if(source == deleteMI) {
			// gets the index of the selected name
			int index = listView.getSelectedIndex();
			nameList.remove(index);  // Removes the name from nameList
			numberList.remove(index); // removes the phone number associated with the name
			
			if(index == nameList.getSize()) {
				listView.setSelectedIndex(nameList.indexOf(nameList.lastElement()));
				listView.ensureIndexIsVisible(nameList.indexOf(nameList.lastElement()));
			}
			else if(index == nameList.indexOf(nameList.firstElement())) {
				listView.setSelectedIndex(nameList.indexOf(nameList.firstElement()));
				listView.ensureIndexIsVisible(nameList.indexOf(nameList.firstElement()));
			}
			else {
				listView.setSelectedIndex(index);
				listView.setSelectedIndex(index);
			}

		}
		else if( source == sortMI) {
		    for(int i = 0; i < nameList.size() - 1; i++) 
		    { 
		    	// Finding the minimum element in the unsorted list
		        int min_index = i; 
		        String minStr = nameList.get(i);
		        for(int j = i + 1; j < nameList.size(); j++)  
		        { 
		              
		            if(nameList.get(j).compareTo(minStr) < 0)  
		            {  
		                minStr = nameList.get(j); 
		                min_index = j; 
		            } 
		        }	  
		    // Swapping the minimum element  
		    // found with the first element. 
		    if(min_index != i){ 
		    	String temp = nameList.getElementAt(min_index);
		    	nameList.set(min_index, nameList.get(i));
		    	nameList.remove(min_index);
		    	nameList.add(i, temp);
		    	
		    	String temp2 = numberList.get(min_index);
		    	numberList.set(min_index, numberList.get(i));
		    	numberList.remove(min_index);
		    	numberList.add(i, temp2);
		    	}
		      }
		    listView.setSelectedIndex(0);
			listView.ensureIndexIsVisible(0);
		  }
	}
	
    /**
     * Implementing ListSelectionListener to display the selected entry
     */
    public void valueChanged(ListSelectionEvent e) {
        display(listView.getSelectedIndex());
    }

	/**
	 * method to specify and open a file
	 */
	private void doOpen() {
	    // display file selection dialog
		JFileChooser fChooser = new JFileChooser(new File("."));
		if(fChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			// Get the file name chosen by the user
			currentFile = fChooser.getSelectedFile();
		}
		else {	// If user canceled file selection, return without doing anything.
			return;
		}

		// Try to create a file reader from the chosen file.
		FileReader reader;
        try {
			reader = new FileReader(currentFile);
	    } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "File Not Found: " + currentFile.getPath(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
            doOpen();
            return;
	    }
		BufferedReader bReader = new BufferedReader(reader);
        // remove items from before if any
   		nameList.clear();
        numberList.clear();
		// Try to read from the input file one line at a time.
		try {
		    int index;
		    String name, number;
		    String textLine = bReader.readLine();
			while (textLine != null) {
			    index = textLine.indexOf((int) ',');
			    if(index > 0) {
			        name = textLine.substring(0, index);
			        number = textLine.substring(index+1);
			        nameList.addElement(name.trim());
			        numberList.addElement(number.trim());
			    }
				textLine = bReader.readLine();
			}
			bReader.close();
			reader.close();
		} catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ioe.toString(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
		}
		setTitle("Phone Contacts: " + currentFile.getPath());   // reset frame title
		listView.setSelectedIndex(0);
        display(0);
	}

    /**
     * method to display the current entry
     */
	private void display(int index) {
	    if(index < 0) {
	        lastName.setText("");
	        firstName.setText("");
	        phoneNumber.setText("");
	    }
	    else {
	        String name = nameList.elementAt(index);
	        int space = name.indexOf((int) ' ');
	        lastName.setText(name.substring(0, space));
	        firstName.setText(name.substring(space+1));
	        phoneNumber.setText(numberList.elementAt(index));
	    }
	}

    /**
     * the main method
     */
    public static void main(String[] argv) {
        // create frame
        ContactList jframe = new ContactList();
	    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        jframe.setSize(size.width/2, size.height-150);
        jframe.setLocation(100, 100);

        // set to terminate application on window closing
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // show the frame
        jframe.setVisible(true);
    }
}