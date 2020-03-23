package zelix.cc.client.security.Menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.EventListener;

public interface ActionListener extends EventListener {
    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e, JTextField jTextField, JPasswordField jPasswordField);
}