package model;

import com.jme3.asset.AssetManager;
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
	private CSGShape csg;
	
	private TreeManager controller;
	
	private CSGModelManager modelMan;
	
	private AssetManager assetMan;
	
	/**
	 * Generates mesh depending on type of block
	 */
	public CSGModel(TreeManager controller, CSGModelManager modelMan, BlockComponent block,
			AssetManager assetMan) {
		this.controller = controller;
		this.block = block;
		this.modelMan = modelMan;
		this.assetMan = assetMan;
		csg = new CSGShape("New Element", new Mesh());
		csg.setMaterial(new Material(assetMan, "Common/MatDefs/Misc/ShowNormals.j3md"));
		if(block instanceof PrimShapeBlock) {
			PrimShapeBlock primBlock = (PrimShapeBlock) block;
			switch (primBlock.primType) {
			case CUBE: csg.setMesh(new Box(1, 1, 1)); break;
			case CYLINDER: csg.setMesh(new CSGCylinder(20, 20, 0.5f, 2)); break;
			case PYRAMID: csg.setMesh(new Sphere(20, 20, 1.3f)); break;
			case SPHERE: csg.setMesh(new Sphere(20, 20, 1.3f)); break;
			}
		}
	}
	
	public void startCSGGeneration() {
		generateCSGMesh();
	}
	
	/**
	 * Recursively computes CSG of all children of block.
	 * (Very slow!)
	 * @return
	 */
	private CSGShape generateCSGMesh() {
		CSGGeometry csgBlender = new CSGGeometry();
		csgBlender.setMaterial(new Material(assetMan, "Common/MatDefs/Misc/ShowNormals.j3md"));
		if(block instanceof OperatorBlock) {
			OperatorBlock opBlock = (OperatorBlock) block;
			CSGModel left = modelMan.getCSGModel(controller.getLeft(block));
			CSGModel right = modelMan.getCSGModel(controller.getRight(block));
			if(left != null) {
				CSGShape leftCSG = left.generateCSGMesh();
				csgBlender.addShape(leftCSG);
			}
			if(right != null) {
				CSGShape rightCSG = right.generateCSGMesh();
				switch(opBlock.opType) {
				case DIFFERENCE: csgBlender.subtractShape(rightCSG); break;
				case INTERSECT: csgBlender.intersectShape(rightCSG); break;
				case UNION: csgBlender.addShape(rightCSG); break;
				}
			}
		} else {
			csgBlender.addShape(csg);
		}
		csgBlender.regenerate();
		csg = new CSGShape("ReturnVal", csgBlender.getMesh());
		csg.setMaterial(new Material(assetMan, "Common/MatDefs/Misc/ShowNormals.j3md"));
		return csg;
	}
	
	public BlockComponent getBlock() {
		return block;
	}
	
	public CSGShape getCSG() {
		return csg;
	}
}
