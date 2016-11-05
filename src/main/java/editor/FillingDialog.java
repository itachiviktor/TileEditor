package editor;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.FlowLayout;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.omg.PortableServer.ServantRetentionPolicyValue;
import database.InMemoryDatabase;
import database.queryObject.insert.Insert;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class FillingDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField xField;
	private JTextField yField;
	private JTextField widthField;
	private JTextField heightField;
	private JTextField layerField;
	
	public static InMemoryDatabase db;
	public static String openedMapName;
	public static int loadedImageWidth;
	public static int loadedImageHeight;
	public static String imageName;
	public static String solid;
	public static BufferedImage loadedImage; 
	public static String openedDBName;
	
	public static List<BufferedImage> map;
	public static List<Location> mapPositions;

	public static int cameraX;
	public static int cameraY;
	
	private FillingDialog() {
		setTitle("Filling Settings");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		xField = new JTextField();
		xField.setBounds(89, 12, 114, 19);
		contentPanel.add(xField);
		xField.setColumns(10);
		
		yField = new JTextField();
		yField.setBounds(89, 43, 114, 19);
		contentPanel.add(yField);
		yField.setColumns(10);
		
		widthField = new JTextField();
		widthField.setBounds(89, 74, 114, 19);
		contentPanel.add(widthField);
		widthField.setColumns(10);
		
		heightField = new JTextField();
		heightField.setBounds(89, 105, 114, 19);
		contentPanel.add(heightField);
		heightField.setColumns(10);
		
		JLabel lblX = new JLabel("x:");
		lblX.setBounds(12, 14, 70, 15);
		contentPanel.add(lblX);
		
		JLabel lblY = new JLabel("y:");
		lblY.setBounds(12, 45, 70, 15);
		contentPanel.add(lblY);
		
		JLabel lblWidth = new JLabel("width:");
		lblWidth.setBounds(12, 76, 70, 15);
		contentPanel.add(lblWidth);
		
		JLabel lblHeight = new JLabel("height:");
		lblHeight.setBounds(12, 107, 70, 15);
		contentPanel.add(lblHeight);
		
		JLabel lblZlayer = new JLabel("zlayer:");
		lblZlayer.setBounds(12, 137, 70, 15);
		contentPanel.add(lblZlayer);
		
		layerField = new JTextField();
		layerField.setBounds(89, 136, 114, 19);
		contentPanel.add(layerField);
		layerField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						if(FillingDialog.this.xField.getText() != null && !FillingDialog.this.xField.getText().equals("") &&
								FillingDialog.this.yField.getText() != null && !FillingDialog.this.yField.getText().equals("") &&
								FillingDialog.this.widthField.getText() != null && !FillingDialog.this.widthField.getText().equals("") &&
								FillingDialog.this.heightField.getText() != null && !FillingDialog.this.heightField.getText().equals("")){
							
							int zlayer = -1;
							if(FillingDialog.this.layerField.getText() != null && !FillingDialog.this.layerField.getText().equals("")){
								zlayer = Integer.parseInt(FillingDialog.this.layerField.getText());
							}
							
							int x = Integer.parseInt(FillingDialog.this.xField.getText());
							int y = Integer.parseInt(FillingDialog.this.yField.getText());	
							int width = Integer.parseInt(FillingDialog.this.widthField.getText());	
							int height = Integer.parseInt(FillingDialog.this.heightField.getText());	
							
							boolean drawAtLeastOne = false;
							
							for(int i=x;i<width + x;i += FillingDialog.loadedImageWidth){
								for(int j=y;j<height + y;j += FillingDialog.loadedImageHeight){
									
									if(i + FillingDialog.loadedImageWidth < width + x && 
											j + FillingDialog.loadedImageHeight < height + y){
									
										
										drawAtLeastOne = true;
										
										FillingDialog.map.add(FillingDialog.loadedImage);
										FillingDialog.mapPositions.add(new Location(i, j));
										
										Insert insert = new Insert(FillingDialog.db, FillingDialog.openedMapName);
										
										insert.makeRoot("Entity", FillingDialog.openedMapName);
										insert.makeChildren("x", String.valueOf(i));
										insert.makeChildren("y", String.valueOf(j));
										insert.makeChildren("width", String.valueOf(FillingDialog.loadedImageWidth));
										insert.makeChildren("height", String.valueOf(FillingDialog.loadedImageHeight));
										insert.makeChildren("image", FillingDialog.imageName);
										insert.makeChildren("solid", FillingDialog.solid);
										
										if(zlayer > -1){
											insert.setLayer(zlayer);
										}
										insert.execute();
									}
								}
							}
							
							if(drawAtLeastOne){
								try {
									ImageIO.write(FillingDialog.loadedImage, "png",new File(FillingDialog.openedDBName + "/" + FillingDialog.imageName));
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
						setVisible(false);
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
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setVisible(true);
	}
	
	public static void showFillingDialog(InMemoryDatabase db, String openedMapName,
			int loadedImageWidth, int loadedImageHeight, String solid, String imageName,
			BufferedImage loadedImage, String openedDBName, List<BufferedImage> map,
			List<Location> mapPositions, int cameraX, int cameraY){
		
		FillingDialog dialog = new FillingDialog();
		FillingDialog.db = db;
		FillingDialog.openedMapName = openedMapName;
		FillingDialog.loadedImageWidth = loadedImageWidth;
		FillingDialog.loadedImageHeight = loadedImageHeight;
		FillingDialog.solid = solid;
		FillingDialog.imageName = imageName;
		FillingDialog.loadedImage = loadedImage;
		FillingDialog.openedDBName = openedDBName;
		FillingDialog.map = map;
		FillingDialog.mapPositions = mapPositions;
		FillingDialog.cameraX = cameraX;
		FillingDialog.cameraY = cameraY;
	}
}
