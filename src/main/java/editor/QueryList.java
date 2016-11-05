package editor;

import java.util.List;

import database.InMemoryDatabase;
import database.queryObject.create.AttributeDescriptor;
import database.queryObject.create.Class;
import database.queryObject.create.Create;
import database.queryObject.insert.Insert;
import database.queryObject.insert.TreeBuilder;
import database.queryObject.insert.TreeNode;
import database.queryObject.insert.Values;
import datastructure.InstanceMaker;

public class QueryList {

	public static void main(String[] args) {
		InMemoryDatabase db = new InMemoryDatabase("Game");
		
		/*Insert insert = new Insert(db, "og");
		
		insert.makeRoot("Oval", "og");
		insert.makeChildren("x", "0");
		insert.makeChildren("y", "0");
		insert.makeChildren("width","100");
		insert.makeChildren("height", "100");*/

		
		/*Create create = new Create();
		
		Class cl = new Class("Stone", db);
		AttributeDescriptor strong = new AttributeDescriptor();
		strong.attrName = "strong";
		strong.attrType = "Number";
		
		AttributeDescriptor color = new AttributeDescriptor();
		color.attrName = "color";
		color.attrType = "Color";
		
		
		cl.getAttributes().add(strong);
		cl.getAttributes().add(color);
		
		create.setExec(cl);
		create.execute();*/
		
		
		
		/*Insert insert = new Insert(db, "og");
		
		insert.makeRoot("Stone", "og");
		insert.makeChildren("strong", "0");
		insert.makeChildren("color");
		insert.makeChildren("rgb","100");

		insert.execute();*/
		
		
		
		
		/*Create create = new Create();
		
		Class cl = new Class("Almaaa", db);
		AttributeDescriptor p = new AttributeDescriptor();
		p.attrName = "p";
		p.attrType = "Number";
		
		AttributeDescriptor palinka = new AttributeDescriptor();
		palinka.attrName = "location";
		palinka.attrType = "Point";
		
		
		AttributeDescriptor lekvar = new AttributeDescriptor();
		lekvar.attrName = "size";
		lekvar.attrType = "Point";
		
		
		cl.getAttributes().add(palinka);
		cl.getAttributes().add(lekvar);
		cl.getAttributes().add(p);
		
		create.setExec(cl);
		create.execute();*/
		
		
		
		
		/*Insert insert = new Insert(db, "og");
		
		insert.makeRoot("Rectangle", "og");
		
		insert.makeChildren("location");
		insert.makeChildren("x", "100");
		insert.makeChildren("y", "200");
		insert.moveToParent();
		insert.makeChildren("size");
		insert.makeChildren("width", "200");
		insert.makeChildren("height", "100");

		insert.execute();*/
		
		
		
		
		db.persist();

	}

}
