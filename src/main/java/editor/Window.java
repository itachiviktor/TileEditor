package editor;

import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Canvas;
import javax.swing.filechooser.FileNameExtensionFilter;
import database.InMemoryDatabase;
import database.queryObject.IQueryObject;
import database.queryObject.insert.Insert;
import datastructure.Instance;
import datastructure.TileMap;
import parser.Parser;
import java.awt.Color;
import java.awt.TextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Label;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

public class Window extends JFrame {
	private CameraMove camera; /*ebben tároljuk, hogy nyíl gomra kattintáskor jány pixellel vigye arrébb
	a kamerát x és y tengely irányába. Default érték (10,10). */

	private JPanel contentPane;
	
	private Boolean solid = false;
	
	public static boolean show = false;
	
	private List<Rectangle> selectedRectangles;/*select után a képernyőn a kijelölt pályaelemeket bejelöli.*/
	
	private JTextField imageName;
	private JTextField widthParam;
	private JTextField heightParam;
	
	private File choosenFile;
	
	private File choosenDatabaseFilePath;/*itt tárolom az adatbázis jsonhöz és mappához tartozó útvonalat.*/
	
	private BufferedImage loadedImage;
	private int loadedImageWidth;
	private int loadedImageHeight;

	private MapCanvas canvas;
	private boolean buttonHoldClickOnCanvas;

	
	private List<BufferedImage> map;/*itt az éppen aktuálisan megnyitott map képei vannak.*/
	private List<Location> mapPositions;/*ez a fenti képek koordinátáit tárolja*/

	private Canvas loadedImageCanvas;
	
	private String openedDatabase = "-";
	private String openedMap = "-";

	private Label openedDbName;
	private File openedDbNameFileObject;

	
	private JLabel openedMapName;

	private Label openedDbNameValue;

	/*ide írjuk bele az épp megnyitott map nevet*/
	private JLabel openedMapNameValue;
	
	private InMemoryDatabase db;
	
	/*A kiválasztott mappa, aminek az elemei a térképen kivannak mutatva.*/
	public TileMap selectedMap;
	
	/*alapból 0,0-ba rakna az insert, de itt jelezzük, hogy a kamera mennyivel van eltolva*/
	private int x;
	private int y;

	private TextField zlayerTextField;

	private JTextArea queryResultArea;

	private JTextArea queryArea;
	private JLabel cameraXLabel;

	private JLabel cameraYLabel;
	private JLabel lblImageWidth;

	private JLabel lblImageHeight;
	private JToggleButton tglbtnShow;
	private JToggleButton tglbtnCollide;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
					frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Window() {
		this.selectedRectangles = new ArrayList<Rectangle>();
		
		this.camera = new CameraMove(10, 10);
		this.map = new ArrayList<BufferedImage>();
		this.mapPositions = new ArrayList<Location>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1260, 700);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenDatabase = new JMenuItem("Open Database");
		mnFile.add(mntmOpenDatabase);
		mntmOpenDatabase.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON", "json");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(Window.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					Window.this.openedDbNameFileObject = chooser.getSelectedFile();
					Window.this.openedDatabase = openedDbNameFileObject.getName().split("\\.")[0];
					Window.this.openedDbNameValue.setText(Window.this.openedDatabase);
					
					
					String[] path = null;
					if(Window.this.openedDbNameFileObject.getPath().contains("/")){
						path = Window.this.openedDbNameFileObject.getPath().split("/");
					}else if(Window.this.openedDbNameFileObject.getPath().contains("\\")){
						path = Window.this.openedDbNameFileObject.getPath().split("\\");
					}
					
					StringBuilder sb = new StringBuilder();
					for(int i=0;i<path.length - 1;i++){
						if(i < path.length - 2){
							sb.append(path[i] + "/");
						}else{
							sb.append(path[i]);
						}
						
					}

					Window.this.choosenDatabaseFilePath = new File(sb.toString());
					
					db = new InMemoryDatabase(Window.this.openedDbNameFileObject);
					
					
					/*Itt nyitom meg az adatbázist*/
				}
			}
		});
		
		JMenuItem mntmCreateDatabase = new JMenuItem("Create Database");
		mnFile.add(mntmCreateDatabase);
		mntmCreateDatabase.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				/*Itt kell majd filet létrehozni, méghozzá JSON filet.*/
				CreateDatabaseWindow dbWindow = new CreateDatabaseWindow();
				dbWindow.setVisible(true);;
				
				
			}
		});
		
		JMenuItem createMap = new JMenuItem("Create Map");
		mnFile.add(createMap);
		createMap.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				/*Itt kell majd filet létrehozni, méghozzá JSON filet.*/
				CreateMapWindow dbWindow = new CreateMapWindow(Window.this.db);
				dbWindow.setVisible(true);;
			}
		});
		
		JMenuItem openMap = new JMenuItem("Open Map");
		mnFile.add(openMap);
		openMap.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Window.this.selectedMap = OpenMapName.showOpenMapName(Window.this.db, Window.this.selectedMap, Window.this.openedMapNameValue);

				map.clear();
				mapPositions.clear();

				for(int i=0;i<Window.this.selectedMap.size();i++){
					if(Window.this.selectedMap.get(i).hasThisAttribute("image")){
						try {
		
							
							File newImage = new File(Window.this.choosenDatabaseFilePath.getPath()+ "/" + Window.this.openedDatabase + "/" +
									(String)Window.this.selectedMap.get(i).getAttribute("image").getValue());
							
							map.add(ImageIO.read(newImage));
							mapPositions.add(new Location((Integer)Window.this.selectedMap.get(i).getAttribute("x").getValue(), 
									(Integer)Window.this.selectedMap.get(i).getAttribute("y").getValue()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Window.this.db.persist();
			}
		});
		mnFile.add(mntmSave);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JMenuItem mntmCameraSettings = new JMenuItem("Camera Settings");
		mntmCameraSettings.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Window.this.camera = CameraSettingsWindow.showCameraSettings();
				
			}
		});
		mnSettings.add(mntmCameraSettings);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAboutTheEditor = new JMenuItem("About the Editor");
		mntmAboutTheEditor.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				EditorInformationWindow win = new EditorInformationWindow();
			}
		});
		mnHelp.add(mntmAboutTheEditor);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		canvas = new MapCanvas(this.map, this.mapPositions, this, this.selectedRectangles);
		

		canvas.setBackground(Color.LIGHT_GRAY);
		canvas.setBounds(10, 10, 900, 515);
		
		canvas.addKeyListener(new KeyListener() {
			
		
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP){
					Window.this.y += camera.getY();
					Window.this.cameraYLabel.setText("y: " + -Window.this.y);
						
					Window.this.canvas.repaint();
				}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
					Window.this.y -= camera.getY();
					Window.this.cameraYLabel.setText("y: " + -Window.this.y);
					
					Window.this.canvas.repaint();
				}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
					Window.this.x += camera.getX();
					Window.this.cameraXLabel.setText("x: " + -Window.this.x);
					
					Window.this.canvas.repaint();
				}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					Window.this.x -= camera.getX();
					Window.this.cameraXLabel.setText("x: " + -Window.this.x);
				
					Window.this.canvas.repaint();
				}
				
			}
		});
		
		contentPane.add(canvas);
		
		zlayerTextField = new TextField();
		zlayerTextField.setBounds(1053, 24, 144, 19);
		contentPane.add(zlayerTextField);
		
		Label label = new Label("Zlayer:");
		label.setBounds(934, 22, 68, 21);
		contentPane.add(label);
		
		openedDbName = new Label("Database opened:");
		openedDbName.setBounds(934, 493, 125, 21);
		contentPane.add(openedDbName);
		
		openedMapName = new JLabel("Map opened:");
		openedMapName.setBounds(932, 518, 100, 21);
		contentPane.add(openedMapName);
		
		openedDbNameValue = new Label(this.openedDatabase);
		openedDbNameValue.setBounds(1100, 493, 125, 21);
		contentPane.add(openedDbNameValue);
		
		openedMapNameValue = new JLabel(this.openedMap);
		openedMapNameValue.setBounds(1100, 518, 100, 21);
		contentPane.add(openedMapNameValue);
		
		loadedImageCanvas = new Canvas(){
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				
				if(Window.this.loadedImage != null){
					g.drawImage(Window.this.loadedImage, 0, 0, null);
				}
				
			}
		};
		loadedImageCanvas.setBackground(Color.LIGHT_GRAY);
		loadedImageCanvas.setBounds(930, 100, 236, 266);
		contentPane.add(loadedImageCanvas);
		
		loadedImageCanvas.repaint();
		
		widthParam = new JTextField();
		widthParam.setBounds(1200, 100, 50, 25);
		contentPane.add(widthParam);
	    
		heightParam = new JTextField();
		heightParam.setBounds(1200, 150, 50, 25);
		contentPane.add(heightParam);
		
		JButton applyWidthHeight = new JButton("resize");
		applyWidthHeight.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(!widthParam.getText().equals("") && !heightParam.getText().equals("")
						&& widthParam.getText() != null && heightParam.getText() != null){
					
					Window.this.loadedImage = ImageResizer.createThumb(Window.this.loadedImage, 
							Integer.parseInt(widthParam.getText()), Integer.parseInt(heightParam.getText()));
					
					
					Window.this.lblImageWidth.setText("Image width: " + Window.this.loadedImage.getWidth());
					Window.this.lblImageHeight.setText("Image height: " + Window.this.loadedImage.getHeight());
					
					Window.this.loadedImageCanvas.repaint();
				}else{
					JOptionPane.showMessageDialog(Window.this, "Mindkét adatot(szélesség, magasság) meg kell adni!");
				}
				
			}
		});
		applyWidthHeight.setBounds(1170, 200, 85, 25);
		contentPane.add(applyWidthHeight);
		
		
		JButton btnLoadImage = new JButton("Load Image");
		btnLoadImage.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG&PNG", "jpg", "png");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(Window.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					choosenFile = chooser.getSelectedFile();

					try {
						Window.this.loadedImage = ImageIO.read(choosenFile);
						Window.this.loadedImageWidth = Window.this.loadedImage.getWidth();
						Window.this.loadedImageHeight = Window.this.loadedImage.getHeight();
						
						Window.this.lblImageWidth.setText("Image width: " + Window.this.loadedImageWidth);
						Window.this.lblImageHeight.setText("Image height: " + Window.this.loadedImageHeight);
						
						Window.this.loadedImageCanvas.repaint();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnLoadImage.setBounds(930, 372, 117, 25);
		contentPane.add(btnLoadImage);
		
		JButton btnMovetomap = new JButton("Move to map");
		btnMovetomap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Window.this.imageName.getText() != null && !Window.this.imageName.getText().equals("")){
					try {
						Window.this.map.add(Window.this.loadedImage);
						Window.this.mapPositions.add(new Location(-Window.this.x, -Window.this.y));
						
						Insert insert = new Insert(Window.this.db, Window.this.openedMapNameValue.getText());
						
						//InstanceMaker maker = Window.this.db.getMapByName(Window.this.openedMapNameValue.getText()).getMaker();
						
						//TreeBuilder builder = new TreeBuilder(db, maker);
						insert.makeRoot("Entity", Window.this.openedMapNameValue.getText());
						insert.makeChildren("x", String.valueOf(-Window.this.x));
						insert.makeChildren("y", String.valueOf(-Window.this.y));
						insert.makeChildren("width", String.valueOf(Window.this.loadedImage.getWidth()));
						insert.makeChildren("height", String.valueOf(Window.this.loadedImage.getHeight()));
						insert.makeChildren("image", Window.this.imageName.getText());
						insert.makeChildren("solid", Window.this.solid.toString());
						
						
						
						/*Ha írunk valamit a zlayer szövegdobozba akkor azt be kell állítanunknk, ha nem, akkor 
						 autómatikusan a 0 zlayerre pakol.*/

						if(zlayerTextField.getText() != null && !zlayerTextField.getText().equals("")){
							/*Instance inst = Window.this.db.getMapByName(Window.this.openedMapNameValue.getText()).get
									(Window.this.db.getMapByName(Window.this.openedMapNameValue.getText()).size() - 1);*/
							insert.setLayer(Integer.parseInt(zlayerTextField.getText()));
						}
						
						insert.execute();
						
						Window.this.canvas.repaint();
						ImageIO.write(Window.this.loadedImage, "png",new File(openedDbNameValue.getText() + "/" + imageName.getText()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(Window.this, "Nincs megadva Image name attribútum!");
				}
			}
		});
		btnMovetomap.setBounds(930, 407, 137, 25);
		contentPane.add(btnMovetomap);
		
		/*Itt a kép nevét kell megadni.*/
		imageName = new JTextField();
		imageName.setBounds(1029, 449, 137, 25);
		contentPane.add(imageName);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 562, 424, 78);
		contentPane.add(scrollPane);
		
		queryArea = new JTextArea();
		scrollPane.setViewportView(queryArea);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(494, 562, 416, 78);
		contentPane.add(scrollPane_1);
		
		queryResultArea = new JTextArea();
		scrollPane_1.setViewportView(queryResultArea);
		
		JLabel lblNewLabel = new JLabel("Query:");
		lblNewLabel.setBounds(10, 533, 100, 25);
		contentPane.add(lblNewLabel);
		
		JLabel lblQueryResult = new JLabel("Query Result:");
		lblQueryResult.setBounds(488, 537, 117, 15);
		contentPane.add(lblQueryResult);
		
		JButton btnExecuteQuery = new JButton("Execute query");
		btnExecuteQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				/*Itt kell végrehajtani a lekérdezést....*/

				String query = Window.this.queryArea.getText();
				Parser parser = new Parser();
				
				List<Instance> ini = null;
				
				StringBuilder sb = new StringBuilder();
				try{
					IQueryObject object = parser.parse(Window.this.db, query);
					ini = object.execute();
					
					Window.this.selectedRectangles.clear();
					
					if(ini != null){
						for(int i=0;i<ini.size();i++){
							if(ini.get(i).hasThisAttribute("x") && ini.get(i).hasThisAttribute("y") && 
									ini.get(i).hasThisAttribute("width") && ini.get(i).hasThisAttribute("height") && 
									ini.get(i).hasThisAttribute("image")){
								Window.this.selectedRectangles.add(new Rectangle(
										(Integer)ini.get(i).getAttribute("x").getValue(), 
										(Integer)ini.get(i).getAttribute("y").getValue(), 
										(Integer)ini.get(i).getAttribute("width").getValue(), 
										(Integer)ini.get(i).getAttribute("height").getValue()));
							}
							sb.append(ini.get(i).toString());
							sb.append(System.lineSeparator());
						}
					}else{
						sb.append("Lefutott a lekérdezés");
					}
				}catch (Exception ex) {
					sb.append(ex.getMessage());
				}
				
				
				/*Ez a rész azért kell, ha updatenél elmozdul valamelyik*/
				map.clear();
				mapPositions.clear();

				for(int i=0;i<Window.this.selectedMap.size();i++){
					if(Window.this.selectedMap.get(i).hasThisAttribute("image")){
						try {
		
							
							File newImage = new File(Window.this.choosenDatabaseFilePath.getPath()+ "/" + Window.this.openedDatabase + "/" +
									(String)Window.this.selectedMap.get(i).getAttribute("image").getValue());
							
							
							map.add(ImageIO.read(newImage));
							mapPositions.add(new Location((Integer)Window.this.selectedMap.get(i).getAttribute("x").getValue(), 
									(Integer)Window.this.selectedMap.get(i).getAttribute("y").getValue()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				
				
				Window.this.queryResultArea.setText(sb.toString());
				Window.this.canvas.repaint();
				
			}
		});
		btnExecuteQuery.setBounds(934, 584, 157, 25);
		contentPane.add(btnExecuteQuery);
		
		JLabel lblImageName = new JLabel("Image name:");
		lblImageName.setBounds(928, 454, 97, 15);
		contentPane.add(lblImageName);
		
		JLabel lblW = new JLabel("w:");
		lblW.setBounds(1172, 103, 25, 19);
		contentPane.add(lblW);
		
		JLabel lblH = new JLabel("h:");
		lblH.setBounds(1172, 155, 25, 15);
		contentPane.add(lblH);
		
		cameraXLabel = new JLabel("x:0");
		cameraXLabel.setBounds(713, 537, 70, 15);
		contentPane.add(cameraXLabel);
		
		cameraYLabel = new JLabel("y:0");
		cameraYLabel.setBounds(795, 535, 70, 15);
		contentPane.add(cameraYLabel);
		
		lblImageWidth = new JLabel("Image width: -");
		lblImageWidth.setBounds(1100, 377, 150, 15);
		contentPane.add(lblImageWidth);
		
		lblImageHeight = new JLabel("Image height: -");
		lblImageHeight.setBounds(1096, 407, 148, 15);
		contentPane.add(lblImageHeight);
		
		tglbtnShow = new JToggleButton("Show");
		tglbtnShow.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if(Window.this.tglbtnShow.isSelected()){
					Window.show = true;
					/*enged*/
				}else{
					/*tiltva van*/
					Window.show = false;
				}
				
				Window.this.canvas.repaint();
			}
		});
		tglbtnShow.setBounds(1117, 584, 108, 25);
		contentPane.add(tglbtnShow);
		
		tglbtnCollide = new JToggleButton("solid");
		tglbtnCollide.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(Window.this.tglbtnCollide.isSelected()){
					Window.this.solid = true;
					/*enged*/
				}else{
					/*tiltva van*/
					Window.this.solid = false;
				}
				
			}
		});
		tglbtnCollide.setBounds(1169, 449, 81, 25);
		contentPane.add(tglbtnCollide);
		
		JButton btnFill = new JButton("Filling");
		btnFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(Window.this.db != null && Window.this.openedMapNameValue.getText() != null &&
						Window.this.loadedImage != null &&
						Window.this.solid.toString() != null && Window.this.imageName.getText() != null &&
						!Window.this.imageName.getText().equals("")){
					
					FillingDialog.showFillingDialog(Window.this.db, Window.this.openedMapNameValue.getText(),
							Window.this.loadedImage.getWidth(), Window.this.loadedImage.getHeight(),
							Window.this.solid.toString(), Window.this.imageName.getText(),
							Window.this.loadedImage, openedDbNameValue.getText(), Window.this.map,
							Window.this.mapPositions,x, y);
					
					Window.this.canvas.repaint();
				}else{
					JOptionPane.showMessageDialog(Window.this, "Nincs minden adat kitöltve!");
				}
			}
		});
		btnFill.setBounds(1172, 324, 78, 25);
		contentPane.add(btnFill);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}