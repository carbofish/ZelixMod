package zelix.cc.client.security;

import zelix.cc.client.security.Menu.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AUTHCATIONMANAGER {

    @InvokeAnnotation
    public void auth()
    {
        new MainMenu();
    }
}
