package model;

import com.jme3.material.Material;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import controller.TreeManager;
import controller.CSGModelManager;
import controller.JME;
import net.wcomohundro.jme3.csg.CSGGeometry;
import net.wcomohundro.jme3.csg.CSGShape;
import net.wcomohundro.jme3.csg.shape.CSGCylinder;
import view.block.BlockComponent;
import view.block.OperatorBlock;
import view.block.PrimShapeBlock;
/**
 * Organizes CSG classes.
 * @author chriz
 *
 */
public class CSGModel {
	
	/** Block this object represents */
	private BlockComponent block;
	/** CSG Mesh */
	private CSGGeometry csg;
	
	private TreeManager controller;
	
	private CSGModelManager modelMan;
	
	/**
	 * Generates mesh depending on type of block
	 */
	public CSGModel(TreeManager controller, CSGModelManager modelMan, BlockComponent block) {
		this.controller = controller;
		this.block = block;
		this.modelMan = modelMan;
		csg = new CSGGeometry("New Element", new Mesh());
		csg.setMaterial(new Material(JME.getInstance().getAssetManager(), 
				"Common/MatDefs/Misc/ShowNormals.j3md"));
		if(block instanceof PrimShapeBlock) {
			PrimShapeBlock primBlock = (PrimShapeBlock) block;
			switch (primBlock.primType) {
			case CUBE: csg.addShape(new CSGShape("Cube", new Box(1, 1, 1))); break;
			case CYLINDER: csg.addShape(new CSGShape("Cylinder", new CSGCylinder(20, 20, 0.5f, 2))); break;
			case PYRAMID: csg.addShape(new CSGShape("Sphere", new Sphere(20, 20, 1.3f))); break;
			case SPHERE: csg.addShape(new CSGShape("Sphere", new Sphere(20, 20, 1.3f))); break;
			}
		}
		csg.regenerate();
	}
	
	public void startCSGGeneration() {
		csg.addShape(generateCSGMesh());
	}
	
	/**
	 * Recursively computes CSG of all children of block.
	 * (Very slow!)
	 * @return
	 */
	private CSGShape generateCSGMesh() {
		if(block instanceof OperatorBlock) {
			csg.removeAllShapes();
			OperatorBlock opBlock = (OperatorBlock) block;
			CSGModel left = modelMan.getCSG(controller.getLeft(block));
			CSGModel right = modelMan.getCSG(controller.getRight(block));
			if(left != null) {
				CSGShape leftCSG = left.generateCSGMesh();
				csg.addShape(leftCSG);
			}
			if(right != null) {
				CSGShape rightCSG = right.generateCSGMesh();
				switch(opBlock.opType) {
				case DIFFERENCE: csg.subtractShape(rightCSG); break;
				case INTERSECT: csg.intersectShape(rightCSG); break;
				case UNION: csg.addShape(rightCSG); break;
				}
			}
		}
		CSGShape newShape = csg.regenerate();
		return newShape;
	}
	
	public BlockComponent getBlock() {
		return block;
	}
	
	public CSGGeometry getCSG() {
		return csg;
	}
}
