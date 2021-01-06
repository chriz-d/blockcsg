package model;

import com.jme3.material.Material;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import controller.Controller;
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
public class Shape {
	
	/** Block this object represents */
	private BlockComponent block;
	/** CSG Mesh */
	private CSGGeometry csg;
	
	private Controller controller;
	
	/**
	 * Generates mesh depending on type of block
	 */
	public Shape(Controller controller, BlockComponent block) {
		this.controller = controller;
		this.block = block;
		csg = new CSGGeometry();
		if(block instanceof PrimShapeBlock) {
			PrimShapeBlock primBlock = (PrimShapeBlock) block;
			csg.setMaterial(new Material(controller.getAssetManager(), 
					"Common/MatDefs/Misc/ShowNormals.j3md"));
			switch (primBlock.primType) {
			case CUBE: csg.addShape(new CSGShape("Cube", new Box(1, 1, 1))); break;
			case CYLINDER: csg.addShape(new CSGShape("Cylinder", new CSGCylinder(20, 20, 0.5f, 2))); break;
			case PYRAMID: csg.addShape(new CSGShape("Sphere", new Sphere(20, 20, 1.3f))); break;
			case SPHERE: csg.addShape(new CSGShape("Sphere", new Sphere(20, 20, 1.3f))); break;
			}
		}
		csg.regenerate();
	}
	
	/**
	 * Recursively computes CSG of all children of block.
	 * (Very slow!)
	 * @return
	 */
	public CSGShape generateCSGMesh() {
		if(block instanceof OperatorBlock) {
			csg = new CSGGeometry();
			OperatorBlock opBlock = (OperatorBlock) block;
			csg.setMaterial(new Material(controller.getAssetManager(), "Common/MatDefs/Misc/ShowNormals.j3md"));
			Shape left = controller.getLeftShape(block);
			Shape right = controller.getRightShape(block);
			if(left != null) {
				CSGShape leftCSG = left.generateCSGMesh();
				if(leftCSG != null) {
					csg.addShape(left.generateCSGMesh());
				}
			}
			if(right != null) {
				CSGShape rightCSG = right.generateCSGMesh();
				if(rightCSG != null) {
					switch(opBlock.opType) {
					case DIFFERENCE: csg.subtractShape(right.generateCSGMesh()); break;
					case INTERSECT: csg.intersectShape(right.generateCSGMesh()); break;
					case UNION: csg.addShape(right.generateCSGMesh()); break;
					}
				}
			}
		}
		csg.regenerate();
		if(csg.getMesh() != null) {
			CSGShape result = new CSGShape("Result", csg.getMesh());
			if(result != null) {
				result.setMaterial(new Material(controller.getAssetManager(), 
						"Common/MatDefs/Misc/ShowNormals.j3md"));
				
			}
			return result;
		}
		return null;
	}
	
	public BlockComponent getBlock() {
		return block;
	}
	
	public CSGGeometry getCSG() {
		return csg;
	}
}
