package editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.queryObject.create.Create;
import database.queryObject.create.Database;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class CreateDatabaseWindow extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField databaseName;
	

	/**
	 * Create the dialog.
	 */
	public CreateDatabaseWindow() {
		setModal(true);
		setTitle("Create Database");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		databaseName = new JTextField();
		databaseName.setBounds(239, 31, 114, 19);
		contentPanel.add(databaseName);
		databaseName.setColumns(10);
		
		JLabel lblDatabasename = new JLabel("DatabaseName:");
		lblDatabasename.setBounds(72, 33, 150, 15);
		contentPanel.add(lblDatabasename);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(databaseName != null){
							Create create = new Create();
							Database d = new Database(databaseName.getText());
							create.setExec(d);
							create.execute();
							
							File theDir = new File(databaseName.getText());
							theDir.mkdir();/*Ezzel itt létrehozzuk mellé a directoryt, amibe a képek kerülnek.*/
							
							
							CreateDatabaseWindow.this.setVisible(false);;
							//CreateDatabaseWindow.this.dispose();
							/*Itt is be kell zárni az ablakot.*/
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						CreateDatabaseWindow.this.setVisible(false);;
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
