package zelix.cc.client.security.Menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class MainMenu extends JFrame {

    public MainMenu (){
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("Zelix Login");
        setSize(350, 130);
        setLocationRelativeTo(null);
        setResizable(false);
        FlowLayout flow = new FlowLayout();
        setLayout(flow);
        JLabel jLabel = new JLabel("EMail:", SwingConstants.CENTER);
        add(jLabel);
        JTextField user = new JTextField();
        Dimension dm = new Dimension(280,25);
        user.setPreferredSize(dm);
        add(user);
        JLabel jLabelx = new JLabel("Password:", SwingConstants.CENTER);
        add(jLabelx);
        JPasswordField userx = new JPasswordField();
        Dimension dmx = new Dimension(280,25);
        userx.setPreferredSize(dmx);
        add(userx);

        JButton jb = new JButton("Login");
        Dimension dm3=new Dimension(80,30);
        jb.setPreferredSize(dm3);
        add(jb);

        JButton jb2 = new JButton("Exit");
        Dimension dm32=new Dimension(120,30);
        jb2.setPreferredSize(dm32);
        add(jb2);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }

}
