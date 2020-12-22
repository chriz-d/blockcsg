package model;

import com.jme3.material.Material;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import controller.Controller;
import net.wcomohundro.jme3.csg.CSGGeometry;
import net.wcomohundro.jme3.csg.CSGShape;
import net.wcomohundro.jme3.csg.shape.CSGCylinder;
import view.BlockComponent;
import view.OperatorBlock;
import view.PrimShapeBlock;

public class Shape {
	
	private BlockComponent block;
	private CSGGeometry csg;
	
	public Shape(BlockComponent block) {
		this.block = block;
		csg = new CSGGeometry();
		if(block instanceof PrimShapeBlock) {
			PrimShapeBlock primBlock = (PrimShapeBlock) block;
			csg.setMaterial(new Material(Controller.getInstance().getAssetManager(), 
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
	
	public CSGShape generateCSGMesh() {
		if(block instanceof OperatorBlock) {
			csg = new CSGGeometry();
			OperatorBlock opBlock = (OperatorBlock) block;
			Controller controller = Controller.getInstance();
			csg.setMaterial(new Material(controller.getAssetManager(), "Common/MatDefs/Misc/ShowNormals.j3md"));
			Shape left = controller.getLeftShape(block);
			Shape right = controller.getRightShape(block);
			switch(opBlock.opType) {
			case DIFFERENCE: {
				if(left != null) {
					csg.addShape(left.generateCSGMesh());
				}
				if(right != null) {
					csg.subtractShape(right.generateCSGMesh());
				}
			} break;
			case INTERSECT: {
				if(left != null) {
					csg.addShape(left.generateCSGMesh());
				}
				if(right != null) {
					csg.intersectShape(right.generateCSGMesh());
				}
			} break;
			case UNION: {
				if(left != null) {
					csg.addShape(left.generateCSGMesh());
				}
				if(right != null) {
					csg.addShape(right.generateCSGMesh());
				}
			} break;
			}
		} else {
			System.out.println("Not an operator block!");
		}
		csg.regenerate();
		if(csg.getMesh() != null) {
			CSGShape result = new CSGShape("Result", csg.getMesh());
			if(result != null) {
				result.setMaterial(new Material(Controller.getInstance().getAssetManager(), 
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Shape)) {
			return false;
		}
		Shape other = (Shape) obj;
		if(this.getBlock().equals(other.getBlock())) {
			return true;
		}
		return false;
	}
}
