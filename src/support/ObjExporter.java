package support;

import java.io.FileWriter;
import java.io.IOException;

import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

/**
 * Exporter for exporting meshes to .obj file.
 * @author chriz
 *
 */
public class ObjExporter {
	
	/**
	 * Exports mesh to .obj file
	 * @param path Path to export the .obj file to.
	 * @param mesh Mesh to export.
	 */
	public static void exportMesh(String path, Mesh mesh) {
		try {
			Vector3f[] vertices = new Vector3f[mesh.getTriangleCount() * 3];
			Vector3f[] normals = new Vector3f[mesh.getTriangleCount()];
			// Extract vertices and normals from triangles of given mesh
			for(int i = 0; i < mesh.getTriangleCount(); i++) {
				Triangle triangle = new Triangle();
				mesh.getTriangle(i, triangle);
				vertices[i*3] = triangle.get1();
				vertices[i*3+1] = triangle.get2();
				vertices[i*3+2] = triangle.get3();
				normals[i] = triangle.getNormal();
			}
			
			FileWriter writer = new FileWriter(path);
			
			// Write all vertices to file
			for(int i = 0; i < vertices.length; i++) {
				writer.write(String.format("v %f %f %f\n", vertices[i].x, vertices[i].y, vertices[i].z));
			}
			
			writer.write("\n");
			
			// Write all normals to file
			for(int i = 0; i < mesh.getTriangleCount(); i++) {
				writer.write(String.format("vn %f %f %f\n", normals[i].x, normals[i].y, normals[i].z));
			}
			
			writer.write("\n");
			
			// Write all indices of together belonging triangles to file
			for(int i = 0; i < mesh.getTriangleCount(); i++) {
				writer.write(String.format("f %d//%d %d//%d %d//%d\n", (i*3)+1, i+1, (i*3)+2, i+1, (i*3)+3, i+1));
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
