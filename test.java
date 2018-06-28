import java.awt.FlowLayout;
import javax.swing.*;
import java.awt.*;

class test { //Class with the main method
    public static void main(String args[]) {

        dictionary obj = new dictionary(); //Object is created from the dictionary class
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.getContentPane().setBackground(new Color(187, 200, 253));
        obj.setSize(400, 423);
        obj.setLocationRelativeTo(null); //To open the window from centre of the screen
        obj.setVisible(true);
        obj.setResizable(false);
    }
}