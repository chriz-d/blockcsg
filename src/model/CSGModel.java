package model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import controller.ICSGModelManager;
import controller.ITreeManager;
import net.wcomohundro.jme3.csg.CSGGeometry;
import net.wcomohundro.jme3.csg.CSGShape;
import net.wcomohundro.jme3.csg.shape.CSGCylinder;
import support.Support;
import view.block.BlockComponent;
import view.block.OperatorBlock;
import view.block.PrimShapeBlock;
/**
 * Represents a single CSG object of a specific {@link view.block.BlockComponent block}.
 * @author chriz
 *
 */
public class CSGModel implements ICSGModel {
	
	/** Block this object represents. */
	private BlockComponent block;
	
	/** CSG Mesh of the object. */
	private CSGShape csg;
	
	/** Reference to tree manager for generating the CSG object. */
	private ITreeManager controller;
	
	/** Reference to model manager for for generating the CSG object. */
	private ICSGModelManager modelMan;
	
	/** Asset Manager for easy access of different material types. */ 
	private AssetManager assetMan;
	
	/** Current size of the csg mesh */
	private SizeMeasurements size;
	
	public CSGModel(ITreeManager controller, ICSGModelManager modelMan, BlockComponent block,
			AssetManager assetMan) {
		this.controller = controller;
		this.block = block;
		this.modelMan = modelMan;
		this.assetMan = assetMan;
		size = new SizeMeasurements();
		csg = new CSGShape("New Element", new Mesh());
		csg.setMaterial(Support.getTransparentMaterial(assetMan));
		csg.setQueueBucket(Bucket.Translucent);
		if(block instanceof PrimShapeBlock) {
			PrimShapeBlock primBlock = (PrimShapeBlock) block;
			switch (primBlock.primType) {
			case CUBE: {
				size.length = 1;
				size.height = 1;
				size.width = 1;
				csg.setMesh(new Box(size.length, size.height, size.width));
				}; break;
			case CYLINDER: {
				size.length = 2;
				size.radius = 0.5f;
				csg.setMesh(new CSGCylinder(20, 20, size.radius, size.length));
				}; break;
			case SPHERE: {
				size.radius = 1.3f;
				csg.setMesh(new Sphere(20, 20, size.radius));
			}; break;
			}
		}
	}
	
	/**
	 * Recursively computes CSG of all children of block and sets the result as
	 * its current shape.
	 * (Very slow!)
	 * @return Returns the computed CSG shape.
	 */
	@Override
	public CSGShape generateCSGMesh() {
		// Create a new geometry for blending shapes and config it
		CSGGeometry csgBlender = new CSGGeometry();
		csgBlender.setMaterial(Support.getTransparentMaterial(assetMan));
		csgBlender.setQueueBucket(Bucket.Translucent);
		
		// All meshes that need blending need to moved to (0, 0, 0) hence save oldPos
		ICSGModel left = null;
		ICSGModel right = null;
		Vector3f oldPos = csg.getLocalTranslation().clone();
		// If block is a operator recursively compute csg of children
		if(block instanceof OperatorBlock) {
			OperatorBlock opBlock = (OperatorBlock) block;
			left = modelMan.getCSGModel(controller.getLeft(block));
			right = modelMan.getCSGModel(controller.getRight(block));
			if(left != null) {
				CSGShape leftCSG = left.generateCSGMesh();
				if(leftCSG != null) {
					// Move to center and add to blend
					oldPos = leftCSG.getLocalTranslation().clone();
					leftCSG.setLocalTranslation(new Vector3f(0, 0, 0));
					csgBlender.addShape(leftCSG);
				}
			}
			if(right != null) {
				CSGShape rightCSG = right.generateCSGMesh();
				if(rightCSG != null) {
					// Move to center and add to blend
					rightCSG.setLocalTranslation(rightCSG.getLocalTranslation().subtract(oldPos));
					switch(opBlock.opType) {
					case DIFFERENCE: csgBlender.subtractShape(rightCSG); break;
					case INTERSECT: csgBlender.intersectShape(rightCSG); break;
					case UNION: csgBlender.addShape(rightCSG); break;
					}
				}
			}
		} else {
			// Not a operator, add simply to blend and move to center
			csg.setLocalTranslation(new Vector3f(0, 0, 0));
			csgBlender.addShape(csg);
		}
		// Compute CSG
		csgBlender.regenerate();
		
		// Move children back to their old pos
		if(left != null) {
			left.getCSG().setLocalTranslation(oldPos);
		}
		if(right != null) {
			right.getCSG().setLocalTranslation(right.getCSG().getLocalTranslation().add(oldPos));
		}
		
		// Pretty ugly, create a new shape from the csg result, take generated mesh and update pos
		if(csgBlender.getMesh() == null) {
			Material oldMat = csg.getMaterial();
			Bucket oldBucket = csg.getQueueBucket();
			csg = new CSGShape("ReturnVal", new Mesh());
			csg.setLocalTranslation(oldPos);
			csg.setMaterial(oldMat);
			csg.setQueueBucket(oldBucket);
			return null;
		} else {
			Material oldMat = csg.getMaterial();
			Bucket oldBucket = csg.getQueueBucket();
			csg = new CSGShape("ReturnVal", csgBlender.getMesh());
			csg.setLocalTranslation(oldPos);
			csg.setMaterial(oldMat);
			csg.setQueueBucket(oldBucket);
		}
		return csg;
	}
	
	@Override
	public BlockComponent getBlock() {
		return block;
	}
	
	@Override
	public CSGShape getCSG() {
		return csg;
	}
	
	@Override
	public void resizeModel(SizeMeasurements size) {
		this.size = size;
		// Create new mesh with new size and swap it out
		Mesh mesh = new Mesh();
		if(block instanceof PrimShapeBlock) {
			PrimShapeBlock primBlock = (PrimShapeBlock) block;
			switch (primBlock.primType) {
			case CUBE: mesh = new Box(size.length, size.height, size.width); break;
			case CYLINDER: mesh = new CSGCylinder(20, 20, size.radius, size.length); break;
			case SPHERE: mesh = new Sphere(20, 20, size.radius); break;
			}
		}
		csg.setMesh(mesh);
	}
	
	@Override
	public SizeMeasurements getSize() {
		return size;
	}
}
