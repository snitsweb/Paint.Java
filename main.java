import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.*;
import javax.swing.JDialog;

/** Główna klasa programu */

public class main extends JFrame{

    /** Kostruktor klasy main */
    public main() {
        initUI();
    }

    /** Funkcja tworżąca wszystkie UI dla użytkownika */
    private void initUI() {
        DrawingPanel dp = new DrawingPanel();
        JMenuBar menuBar = new JMenuBar();
        JMenuItem save = new JMenuItem("Zapisz");
        JMenuItem load = new JMenuItem("Wczytaj");
        JMenuItem rectangle = new JMenuItem("Rectangle");
        JMenuItem circle = new JMenuItem("Circle");
        JMenuItem triangle = new JMenuItem("Triangle");
        JMenuItem edit = new JMenuItem("Edit");

        menuBar.add(rectangle);
        menuBar.add(circle);
        menuBar.add(triangle);
        menuBar.add(edit);
        menuBar.add(save);
        menuBar.add(load);
        setJMenuBar(menuBar);

        circle.addActionListener(dp);
        rectangle.addActionListener(dp);
        triangle.addActionListener(dp);
        edit.addActionListener(dp);
        save.addActionListener(dp);
        load.addActionListener(dp);

        add(dp);
        setTitle("Main");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /** Klasa main */
    public static void main(String[] args) {

        main main = new main();
        main.setVisible(true);
    }
}