package lfrcheats;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;

public class LFRCheats {

    static JFrame frame = new JFrame("LFRCheats");
    static JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    static List<Robot> robots;
    static Robot robot;
    static boolean flag;

    public static void main(String[] args) {
        robots = new ArrayList<>();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(panel);
        SetUp();
        mainFnc();
    }

    static void SetUp() {
        panel.removeAll();
        JButton selectButton = new JButton("select");
        flag = true;
        
        robots.add(new Robot("Micro blitz", 10, 10, 1, 2, 6, 1000, 8, "FSM_PD", 20, 83, 255));
        robots.add(new Robot("Rabbit bro", 10, 10, 1, 2, 6, 1, 8, "P", 20, 83, 255));//30000 rotates / 6 volts / 1 min         83 rotates / 1 volts / 1 sec

        String[] robotNames = new String[robots.size()];
        for (int i = 0; i < robots.size(); i++) {
            robotNames[i] = robots.get(i).name;
        }
        JComboBox<String> robotSelector = createList(robotNames);
        //add elems
        panel.add(robotSelector);
        panel.add(selectButton);
        frame.pack();

        selectButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flag = false;
            }
        });
        while (flag);
        robot = robots.get(robotSelector.getSelectedIndex());
    }

    static void mainFnc() {
        panel.removeAll();
        flag = true;
        
        JTextField speedField = new JTextField(2);
        JButton genKBtn = new JButton("Generate");
        JPanel speedPanel = createInputPanel("Speed:", speedField);
        
        genKBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {//160   0.06
                
                //System.out.println(robot.getP(2500, 3000, robot.getTargetDelta(2500, 3000, (float) (0.005 / (Integer.parseInt(speedField.getText()) / 160.0)))));
            }
        });
        
        panel.add(new JLabel(robot.name));
        panel.add(speedPanel);
        panel.add(genKBtn);
        
        frame.pack();
        while (flag) {
            
        }
    }

    static JPanel createInputPanel(String text, JTextField in) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(text));
        panel.add(in);
        return panel;
    }

    static JComboBox createList(String[] text) {
        JComboBox<String> list = new JComboBox<>();
        list.setModel(new DefaultComboBoxModel<>(text));
        return list;
    }
}
