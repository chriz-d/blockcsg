package model;

import net.wcomohundro.jme3.csg.CSGShape;
import view.block.BlockComponent;

public interface ICSGModel {

	/**
	 * Recursively computes CSG of all children of block and sets the result as
	 * its current shape.
	 * (Very slow!)
	 * @return Returns the computed CSG shape.
	 */
	CSGShape generateCSGMesh();

	BlockComponent getBlock();

	CSGShape getCSG();

	void resizeModel(SizeMeasurements size);

	SizeMeasurements getSize();

}