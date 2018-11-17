package gui;

import controller.Controller;
import model.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;


import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class MainFrame extends JFrame {

    private TextPanel textPanel;
    private FormPanel formPanel;
    private Toolbar toolbar;
    private JFileChooser fileChooser;
    private Controller controller;
    private TablePanel tablePanel;

    public MainFrame(){
        super("My Desktop Program");


        setLayout(new BorderLayout());


        textPanel = new TextPanel();
        toolbar = new Toolbar();
        formPanel = new FormPanel();
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new PersonFileFilter());

        tablePanel = new TablePanel();

        controller = new Controller();
        tablePanel.setData(controller.getPeople());

        tablePanel.setPersonTableListener(new PersonTableListener() {
            public void rowDeleted(int row) {
               controller.removePerson(row);
            }
        });

        setJMenuBar(createMenuBar());



        toolbar.setStringListener(new StringListener() {
            @Override
            public void textEmited(String text) {
                System.out.println(text);
                textPanel.appendText(text);
            }
        });


        formPanel.setFormListener(new FormListener() {

            public void formEventOccured(FormEvent e){

                controller.addPerson(e);
                tablePanel.refresh();
            }
        });


        add(tablePanel, BorderLayout.CENTER);

        add(toolbar, BorderLayout.NORTH);

        add(formPanel, BorderLayout.WEST);


        setMinimumSize(new Dimension(500,400));
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);



    }

    private JMenuBar createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem saveDataItem = new JMenuItem("Save File");
        JMenuItem openDataItem = new JMenuItem("Open File");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(saveDataItem);
        fileMenu.add(openDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu windowMenu = new JMenu("Window");

        JMenu showMenu = new JMenu("Show");
        JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem("Person Form");
        showFormItem.setSelected(true);

        openDataItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
                   try {
                       controller.loadFromFile(fileChooser.getSelectedFile());
                       tablePanel.refresh();
                   } catch (IOException e1) {
                       JOptionPane.showMessageDialog(MainFrame.this, "Could not load data from file.",
                               "Error", ERROR_MESSAGE);
                   } catch (ClassNotFoundException e1) {
                       e1.printStackTrace();
                   }
                   System.out.println(fileChooser.getSelectedFile());
               }
            }
        });

        saveDataItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){

                    try {
                        controller.saveToFile(fileChooser.getSelectedFile());
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(MainFrame.this,"Could not save data to file.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        showFormItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)ev.getSource();

                formPanel.setVisible(menuItem.isSelected());
            }
        });

        fileMenu.setMnemonic(KeyEvent.VK_F);
        exitItem.setMnemonic(KeyEvent.VK_X);

        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

        openDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

        saveDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                int action = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Do You Really Want to Close this Application?",
                        "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);

                if (action == JOptionPane.OK_OPTION){
                    System.exit(0);
                }

            }
        });

        showMenu.add(showFormItem);
        windowMenu.add(showMenu);


        JMenu viewMenu = new JMenu("View");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");



        menuBar.add(fileMenu);
        menuBar.add(windowMenu);
        menuBar.add(viewMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);



        return menuBar;
    }





}
