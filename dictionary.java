import java.awt.FlowLayout;
import java.awt.event.ActionListener; //For event handling
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent; //To hide default text when clicked
import java.awt.event.MouseAdapter;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.Border; //To add a border to JTextArea

public class dictionary extends JFrame { //Class dictionary will inherit all the methods of JFrame class

    private JButton clear, search, add, remove; //Butons
    private static JTextField item1, item2;     //Text fields
    private JTextArea definitions, meaning;     //Text Areas

    public dictionary() {
        super("Dictionary"); //calls the constructor of JFrame class
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); //To custormize buttons
        } catch (Exception e) {
            System.out.println(e);
        }
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1); //Borders will be applied to JTextAreas
        setLayout(new FlowLayout());
        item1 = new JTextField("Search", 24);
        item1.setEditable(true);
        add(item1);
        item1.addMouseListener(new MouseAdapter() {    //To hide default text when clicked
            public void mouseClicked(MouseEvent e) {
                item1.setText("");
            }
        });
        Icon a = new ImageIcon(getClass().getResource("a.png")); //Adding an image to Search JButton
        search = new JButton("", a);                             //Buttons are added to the GUI
        search.setPreferredSize(new Dimension(20, 20));
        add(search);
        clear = new JButton("CLEAR");
        clear.setForeground(new Color(46, 50, 144));
        clear.setFont(new Font("Serif", Font.BOLD, 13));
        clear.setPreferredSize(new Dimension(75, 20));
        add(clear);
        definitions = new JTextArea(10, 31);
        definitions.setFont(new Font("Serif", Font.ITALIC, 16));
        definitions.setBorder(border);
        definitions.setLineWrap(true);
        definitions.setWrapStyleWord(true);
        definitions.setEditable(false);
        add(definitions);
        item2 = new JTextField("Enter a word to Add / Remove", 18);
        item2.setEditable(true);
        add(item2);
        item2.addMouseListener(new MouseAdapter() {          //To hide default text when clicked
            public void mouseClicked(MouseEvent e) {
                item2.setText("");
            }
        });
        add = new JButton("ADD");
        add.setPreferredSize(new Dimension(75, 20));
        add.setForeground(new Color(46, 50, 144));
        add.setFont(new Font("Serif", Font.BOLD, 13));
        add(add);
        remove = new JButton("REMOVE");
        remove.setPreferredSize(new Dimension(90, 20));
        remove.setForeground(new Color(200, 18, 18));
        remove.setFont(new Font("Serif", Font.BOLD, 13));
        add(remove);
        meaning = new JTextArea("Enter the meaning of the new word", 4, 31);
        meaning.setFont(new Font("Serif", Font.ITALIC, 16));
        meaning.setForeground(Color.BLUE);
        meaning.setLineWrap(true);
        meaning.setWrapStyleWord(true);
        meaning.setEditable(true);
        meaning.setBorder(border);
        add(meaning);
        meaning.addMouseListener(new MouseAdapter() {       //To hide default text when clicked
            public void mouseClicked(MouseEvent e) {
                meaning.setText("");
            }
        });
        HandlerClass handler = new HandlerClass();
        clear.addActionListener(handler);
        search.addActionListener(handler);
        add.addActionListener(handler);
        remove.addActionListener(handler);
        item1.addActionListener(handler);
    }

    public static String similarWords(String input2) { //Method to find words with same meaning
        try {                                          //Similar words are matched using same definition
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line;
            String simWords = " ";
            String wholeLine[] = new String[2];
            while ((line = reader.readLine()) != null) {
                wholeLine = line.split(" - ", 2);
                if (!(item1.getText().equalsIgnoreCase(wholeLine[0])) && input2.equalsIgnoreCase(wholeLine[1])) {
                    simWords += "  " + wholeLine[0];
                    continue;
                }
            }
            reader.close();
            return simWords;

        } catch (Exception e) {
            System.out.print(e);
        }
        return " ";       //If no similar words found method will return an empty string
    }

    public class HandlerClass implements ActionListener {
        public void actionPerformed(ActionEvent event) {  //Event handling whenever a button is clicked
            BSTree obj2 = new BSTree();                   //BST object is created
            try {                  //Whenever a object is created all the lines in the input.txt are added to the BST.
                BufferedReader br = new BufferedReader(new FileReader("input.txt"));
                String line;
                while ((line = br.readLine()) != null) {
                    obj2.insertNode(line); //Whole line along with the definitions is added as a node to BST
                }
            } catch (Exception e) {
                System.out.print(e);
            }
            if (event.getSource() == clear) {
                item1.setText("");
                definitions.setText("");
                item2.setText("");
                meaning.setText("");
            } else if (event.getSource() == search || event.getSource() == item1) {
                String word = item1.getText();
                try {
                    meaning.setText("");
                    item2.setText("");
                    definitions.setText("Definition:\n    " + obj2.searchNode1(word) + "\nSimilar Words:\n    " +
                        similarWords(obj2.searchNode1(word)));
                } catch (Exception e) {
                    definitions.setText("Cannot find a definition.");
                }
            } else if (event.getSource() == add) {
                if (item2.getText().equals("") || meaning.getText().equals("")) {
                    meaning.setText("Error: There are empty fields you need to fill");
                } else {
                    String word = item2.getText();
                    try {
                        obj2.searchNode1(word);
                        item1.setText("");
                        definitions.setText("");
                        meaning.setText("Word is already in the Dictionary. Check again!");
                    } catch (Exception e) {
                        String means = meaning.getText();
                        obj2.insertNode(word + " - " + means);
                        try {
                            PrintWriter writer = new PrintWriter("input.txt"); //To clean-up input.txt file
                            writer.print("");
                            writer.close();
                        } catch (Exception f) {
                            System.out.println(f);
                        }
                        obj2.updateTextFile(obj2.getRoot());
                        meaning.setText(item2.getText() + " added to the Dictionary");
                        item1.setText("");
                        definitions.setText("");
                    }
                }
            } else if (event.getSource() == remove) {
                try {
                    obj2.deleteNode(obj2.searchNode2(item2.getText() + " - " + obj2.searchNode1(item2.getText())));
                    PrintWriter writer = new PrintWriter("input.txt"); //To clean-up input.txt file
                    writer.print("");
                    writer.close();
                    obj2.updateTextFile(obj2.getRoot());
                    meaning.setText(item2.getText() + " deleted from the Dictionary");
                    item2.setText("");
                    item1.setText("");
                    definitions.setText("");
                } catch (Exception e) {
                    meaning.setText("No such word to delete. Check again!");
                }
            }
        }
    }
}