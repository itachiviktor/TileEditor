package editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CameraSettingsWindow extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField xText;
	private JTextField yText;
	private static CameraMove camera;

	/**
	 * Create the dialog.
	 */
	private CameraSettingsWindow() {
		camera = new CameraMove();
		setModal(true);
		setTitle("Setting Camera");
		setBounds(100, 100, 356, 234);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		xText = new JTextField();
		xText.setBounds(56, 12, 114, 19);
		contentPanel.add(xText);
		xText.setColumns(10);
		
		yText = new JTextField();
		yText.setBounds(56, 61, 114, 19);
		contentPanel.add(yText);
		yText.setColumns(10);
		
		JLabel lblX = new JLabel("x:");
		lblX.setBounds(31, 14, 70, 15);
		contentPanel.add(lblX);
		
		JLabel lblY = new JLabel("y:");
		lblY.setBounds(31, 63, 70, 15);
		contentPanel.add(lblY);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						CameraSettingsWindow.camera.setX(Integer.parseInt(xText.getText()));
						CameraSettingsWindow.camera.setY(Integer.parseInt(yText.getText()));
						
						CameraSettingsWindow.this.setVisible(false);
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
						CameraSettingsWindow.this.setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setVisible(true);
	}
	
	public static CameraMove showCameraSettings(){
		CameraSettingsWindow window = new CameraSettingsWindow();
		
		return CameraSettingsWindow.camera;
	}
}
