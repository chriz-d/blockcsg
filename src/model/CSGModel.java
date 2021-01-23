package model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.BufferUtils;

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
 * Represents a single CSG object of a specific {@link view.block.BlockComponent block}.
 * @author chriz
 *
 */
public class CSGModel {
	
	/** Block this object represents. */
	private BlockComponent block;
	
	/** CSG Mesh of the object. */
	private CSGShape csg;
	
	/** Reference to tree manager for generating the CSG object. */
	private TreeManager controller;
	
	/** Reference to model manager for for generating the CSG object. */
	private CSGModelManager modelMan;
	
	/** Asset Manager for easy access of different material types. */ 
	private AssetManager assetMan;
	
	/** Current size of the csg mesh */
	private SizeMeasurements size;
	
	public CSGModel(TreeManager controller, CSGModelManager modelMan, BlockComponent block,
			AssetManager assetMan) {
		this.controller = controller;
		this.block = block;
		this.modelMan = modelMan;
		this.assetMan = assetMan;
		size = new SizeMeasurements();
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
	 * Recursively computes CSG of all children of block and sets the result as
	 * its current shape.
	 * (Very slow!)
	 * @return Returns the computed CSG shape.
	 */
	private CSGShape generateCSGMesh() {
		// Create a new geometry for blending shapes
		CSGGeometry csgBlender = new CSGGeometry();
		csgBlender.setMaterial(new Material(assetMan, "Common/MatDefs/Misc/ShowNormals.j3md"));
		// If block is a operator recursively compute csg of children
		if(block instanceof OperatorBlock) {
			OperatorBlock opBlock = (OperatorBlock) block;
			CSGModel left = modelMan.getCSGModel(controller.getLeft(block));
			CSGModel right = modelMan.getCSGModel(controller.getRight(block));
			if(left != null) {
				CSGShape leftCSG = left.generateCSGMesh();
				if(leftCSG != null) {
					csgBlender.addShape(leftCSG);
				}
			}
			if(right != null) {
				CSGShape rightCSG = right.generateCSGMesh();
				if(rightCSG != null) {
					switch(opBlock.opType) {
					case DIFFERENCE: csgBlender.subtractShape(rightCSG); break;
					case INTERSECT: csgBlender.intersectShape(rightCSG); break;
					case UNION: csgBlender.addShape(rightCSG); break;
					}
				}
			}
		} else {
			csgBlender.addShape(csg);
		}
		csgBlender.regenerate();
		
		if(csgBlender.getMesh() == null) {
			csg = new CSGShape("ReturnVal", new Mesh());
			csg.setMaterial(new Material(assetMan, "Common/MatDefs/Misc/ShowNormals.j3md"));
			return null;
		} else {
			csg = new CSGShape("ReturnVal", csgBlender.getMesh());
		}
		csg.setMaterial(new Material(assetMan, "Common/MatDefs/Misc/ShowNormals.j3md"));
		return csg;
	}
	
	public BlockComponent getBlock() {
		return block;
	}
	
	public CSGShape getCSG() {
		return csg;
	}
	
	public void resizeModel(SizeMeasurements size) {
		Mesh mesh = new Mesh();
		if(block instanceof PrimShapeBlock) {
			PrimShapeBlock primBlock = (PrimShapeBlock) block;
			switch (primBlock.primType) {
			case CUBE: mesh = new Box(size.length, size.height, size.width); break;
			case CYLINDER: mesh = new CSGCylinder(20, 20, size.radius, size.length); break;
			case PYRAMID: mesh = new Sphere(20, 20, 1.3f); break;
			case SPHERE: mesh = new Sphere(20, 20, size.radius); break;
			}
		}
		csg = new CSGShape("result", mesh);
		csg.setMaterial(new Material(assetMan, "Common/MatDefs/Misc/ShowNormals.j3md"));
	}
	
	public SizeMeasurements getSize() {
		return size;
	}
}
