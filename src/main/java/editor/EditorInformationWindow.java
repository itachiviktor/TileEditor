package editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditorInformationWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public EditorInformationWindow() {
		setModal(true);
		setTitle("About the Editor");
		setBounds(100, 100, 669, 478);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblSdfsfd = new JLabel("");
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>");
		sb.append("<h1>Készítette: Piller Imre és Kiss Viktor</h1><br>");
		sb.append("Az alkalmazás célja, hogy admin felületként működjön az adatbázishoz, amely"
				+ "implementálásra került.<br><br>");
		sb.append("Használati útmutató:<br>");
		sb.append("<ul>");
		sb.append("<li>Show gomb: A térkép debug módját lehet vele állítani.</li>");
		sb.append("<li>Load Image gomb: Ezzel lehet egy képet betölteni a memóriába, majd szerkesztés után beszúrni a térkpre.</li>");
		sb.append("<li>MoveToMap gomb: Ezzel a gombbal a memóriába betöltött képet lehet ráhelyezni a térképre, fontos, hogy az"
				+ "alatta lévő Image Name szövegmezőbe adjuk meg, hogy milyen néven szeretnénk ezt a képet beszúrni.</li>");
		
		sb.append("<li>Execute query gomb: Ezzel a Query szövegdobozba írt utasítást hajthatjuk végre,"
				+ "melynek eredményét a Query Result szövegdobozban láthatjuk, illetve, ha"
				+ "be van kapcsolva a térkép debug módja, akkor ha a lekérdezés eredménye a"
				+ "térképen látható, akkor ott be is keretezi pirossal. </li>");
		sb.append("<li>Settings -> Camera Settings menu: Ezzel azt állíthathuk be, hogy a shift alatti"
				+ "nyilak használata esetén, hány pixellel mozgassuk a kamerát.</li>");
		
		sb.append("<li>File -> Save menu: Ezzel a változtatásokat menthetjük le a json fileba. Ha ezt "
				+ "a gombot nem nyomjuk meg, akkor minden változtatás csak a memóriában történik meg.</li>");
		
		sb.append("</ul>");
		sb.append("</html>");
		
		lblSdfsfd.setText(sb.toString());
		lblSdfsfd.setBounds(12, 12, 624, 392);
		contentPanel.add(lblSdfsfd);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						EditorInformationWindow.this.setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		setVisible(true);
	}
}
