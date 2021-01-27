package model;

import java.util.concurrent.atomic.AtomicBoolean;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import controller.ICSGModelManager;
import controller.ITreeManager;
import controller.TreeManager;
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
public class CSGModel {
	
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
	
	private AtomicBoolean highlighted;
	
	public CSGModel(ITreeManager controller, ICSGModelManager modelMan, BlockComponent block,
			AssetManager assetMan) {
		this.controller = controller;
		this.block = block;
		this.modelMan = modelMan;
		this.assetMan = assetMan;
		size = new SizeMeasurements();
		csg = new CSGShape("New Element", new Mesh());
		csg.setMaterial(Support.getTransparentMaterial(assetMan));
		highlighted = new AtomicBoolean(false);
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
		csgBlender.setMaterial(Support.getTransparentMaterial(assetMan));
		csgBlender.setQueueBucket(Bucket.Translucent);
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
			if(highlighted.get()) {
				csg.setMaterial(Support.getHighlightMaterial(assetMan));
			} else {
				csg.setMaterial(Support.getTransparentMaterial(assetMan));
			}
			csg.setQueueBucket(Bucket.Translucent);
			return null;
		} else {
			csg = new CSGShape("ReturnVal", csgBlender.getMesh());
			if(highlighted.get()) {
				csg.setMaterial(Support.getHighlightMaterial(assetMan));
			} else {
				csg.setMaterial(Support.getTransparentMaterial(assetMan));
			}
			csg.setQueueBucket(Bucket.Translucent);
		}
		return csg;
	}
	
	public BlockComponent getBlock() {
		return block;
	}
	
	public CSGShape getCSG() {
		return csg;
	}
	
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
		csg = new CSGShape("result", mesh);
		csg.setMaterial(Support.getTransparentMaterial(assetMan));
		csg.setQueueBucket(Bucket.Translucent);
	}
	
	public SizeMeasurements getSize() {
		return size;
	}
	
	public void doHighlight() {
		highlighted.set(true);
		csg.setMaterial(Support.getHighlightMaterial(assetMan));
	}
	
	public void unHighlight() {
		highlighted.set(false);
		csg.setMaterial(Support.getTransparentMaterial(assetMan));
	}
}
