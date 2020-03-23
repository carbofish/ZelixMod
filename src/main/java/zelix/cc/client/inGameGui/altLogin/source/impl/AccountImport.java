package zelix.cc.client.inGameGui.altLogin.source.impl;

import zelix.cc.client.Zelix;
import zelix.cc.client.inGameGui.altLogin.source.account.Account;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AccountImport extends JPanel implements ActionListener {

	public JButton openButton;
	private JFileChooser fc;

	public AccountImport() {
		fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
		openButton = new JButton("Open a File...");
		openButton.addActionListener(this);
		add(openButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(AccountImport.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fc.getSelectedFile()))) {
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						final String[] arguments = line.split(":");

						for (int i = 0; i < 2; i++)
							arguments[i].replace(" ", "");

						Zelix.getInstance().getAltManager().getAccounts()
								.add(new Account(arguments[0], arguments[1], ""));
					}
					Zelix.getInstance().getAltManager().getAltSaving().saveFile();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "An error happened.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

}
