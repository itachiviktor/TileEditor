package editor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import datastructure.TileMap;

public class MapCanvas extends Canvas{
	
	private List<BufferedImage> map;
	private List<Location> mapLocations;
	private Window window;
	private List<Rectangle> selectedRectangles;
	
	public MapCanvas(List<BufferedImage> map, List<Location> mapLocations, Window window, List<Rectangle> selectedRectangles) {
		this.map = map;
		this.mapLocations = mapLocations;
		this.window = window;
		this.selectedRectangles = selectedRectangles;
	}
	



	@Override
	public void paint(Graphics g) {
		for(int i=0;i<this.map.size();i++){
			g.drawImage(this.map.get(i), this.mapLocations.get(i).x + this.window.getX(), this.mapLocations.get(i).y + this.window.getY(), null);
		}
		
		if(window.selectedMap != null){
			g.setColor(Color.black);
			for(int i=0;i<window.selectedMap.size();i++){
				if(window.selectedMap.get(i).className.equals("Rectangle")){
					g.drawRect((Integer)window.selectedMap.get(i).getAttribute("location").getAttribute("x").getValue() + this.window.getX(),
							(Integer)window.selectedMap.get(i).getAttribute("location").getAttribute("y").getValue() + this.window.getY(),
							(Integer)window.selectedMap.get(i).getAttribute("size").getAttribute("width").getValue(),
							(Integer)window.selectedMap.get(i).getAttribute("size").getAttribute("height").getValue());
				}else if(window.selectedMap.get(i).className.equals("Oval")){
					g.drawOval((Integer)window.selectedMap.get(i).getAttribute("x").getValue()+ this.window.getX(),
							(Integer)window.selectedMap.get(i).getAttribute("y").getValue() + this.window.getY(),
							(Integer)window.selectedMap.get(i).getAttribute("width").getValue(),
							(Integer)window.selectedMap.get(i).getAttribute("height").getValue());
				}
			}
		}
		
		
		if(Window.show){
			g.setColor(Color.red);
			for(int i=0;i<this.selectedRectangles.size();i++){
				g.drawRect(this.selectedRectangles.get(i).x + this.window.getX(), this.selectedRectangles.get(i).y + this.window.getY(), this.selectedRectangles.get(i).width, this.selectedRectangles.get(i).height);
			}
			g.setColor(Color.black);
		}
		
	}
}
