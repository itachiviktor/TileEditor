package editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.InMemoryDatabase;
import datastructure.TileMap;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OpenMapName extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private InMemoryDatabase db;
	private static TileMap selectedMap;/*ezt a referenciát kívülről kapjuk, beállítunk rá valamit.*/
	private JLabel openedMapName;
	
	/**
	 * Create the dialog.
	 */
	private OpenMapName(InMemoryDatabase db, TileMap selectedMap, JLabel openedMapName) {
		setModal(true);
		this.selectedMap = selectedMap;
		this.openedMapName = openedMapName;
		this.db = db;
		setTitle("Open Map");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(228, 66, 145, 19);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblDatabaseName = new JLabel("Database Name:");
		lblDatabaseName.setBounds(60, 68, 130, 17);
		contentPanel.add(lblDatabaseName);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						OpenMapName.selectedMap = OpenMapName.this.db.getMapByName(OpenMapName.this.textField.getText());
						OpenMapName.this.openedMapName.setText(OpenMapName.selectedMap.getMapName());
						
						OpenMapName.this.setVisible(false);
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
						OpenMapName.this.setVisible(false);;
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setVisible(true);
		
	}
	
	public static TileMap showOpenMapName(InMemoryDatabase db, TileMap selectedMap, JLabel openedMapName){
		OpenMapName map = new OpenMapName(db, selectedMap, openedMapName);
		//System.out.println(OpenMapName.selectedMap);
		return OpenMapName.selectedMap;
	}
}
