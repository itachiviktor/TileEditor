package editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.InMemoryDatabase;
import database.queryObject.create.Create;
import database.queryObject.create.CreateMapTest;
import database.queryObject.create.Database;
import database.queryObject.create.Map;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class CreateMapWindow extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField mapName;
	private InMemoryDatabase db;


	/**
	 * Create the dialog.
	 */
	public CreateMapWindow(InMemoryDatabase db) {
		setModal(true);
		this.db = db;
		setTitle("Create Map");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblAddMapName = new JLabel("Add Map name:");
			lblAddMapName.setBounds(53, 83, 128, 15);
			contentPanel.add(lblAddMapName);
		}
		{
			mapName = new JTextField();
			mapName.setBounds(188, 81, 114, 19);
			contentPanel.add(mapName);
			mapName.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(mapName != null){
							Create create = new Create();
							Map d = new Map(mapName.getText(), CreateMapWindow.this.db);
							create.setExec(d);
							create.execute();
							
							CreateMapWindow.this.db.persist();/*itt bele is írjuk a fileba*/
							
							CreateMapWindow.this.setVisible(false);;
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
						CreateMapWindow.this.setVisible(false);;
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
