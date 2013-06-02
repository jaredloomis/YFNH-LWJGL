package net.future.model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;
import net.future.material.Material;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL15.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class OBJLoader 
{
	/**
	 * Loads a model.
	 * Requirements for model: 
	 * <ul>
	 * <li><b>Wavefront</b> .obj format</li>
	 * <li>Triangulated faces</li>
	 * <li>Normals included</li>
	 * <li>One or no image files</li>
	 * </ul>
	 * 
	 * @param f - The path of the .obj file
	 * @return a Model object of the given file
	 */
	public static Model loadModel(File f)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(f));

			Model m = new Model();
			Material cur = null;
			List<Material> mats = new ArrayList<Material>();

			String line;
			while ((line = reader.readLine()) != null)
			{
				//Indicates a vertex
				if (line.startsWith("v ")) 
				{
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					m.verts.add(new Vector3f(x, y, z));
				} 
				//Indicates a vertex normal
				else if (line.startsWith("vn ")) 
				{
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					m.norms.add(new Vector3f(x, y, z));
				} 
				//Indicates a texture coordinate
				else if (line.startsWith("vt ")) 
				{
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					m.textureCoords.add(new Vector2f(x, y));
				} 
				//Indicates a face
				else if (line.startsWith("f ")) 
				{
					//If face is triangulated
					if(line.split(" ").length == 4)
					{			
						Vector3f vertexIndices = new Vector3f(
								Float.valueOf(line.split(" ")[1].split("/")[0]),
								Float.valueOf(line.split(" ")[2].split("/")[0]),
								Float.valueOf(line.split(" ")[3].split("/")[0]));


						//Instantiate as null for scope reasons
						Vector3f textureIndices = null;

						if(!line.split(" ")[1].split("/")[1].equals("")&&!(line.split(" ")[1].split("/")[1].equals(null)))
							textureIndices = new Vector3f(
									Float.valueOf(line.split(" ")[1].split("/")[1]),
									Float.valueOf(line.split(" ")[2].split("/")[1]),
									Float.valueOf(line.split(" ")[3].split("/")[1]));

						Vector3f normalIndices = new Vector3f(
								Float.valueOf(line.split(" ")[1].split("/")[2]),
								Float.valueOf(line.split(" ")[2].split("/")[2]),
								Float.valueOf(line.split(" ")[3].split("/")[2]));

						Face mf = new Face();

						//Instantiate all the arrays
						mf.normals = new Vector3f[3];
						mf.points = new Vector3f[3];

						//// SETUP NORMALS ////
						Vector3f n1 = m.norms.get((int)normalIndices.x - 1);
						mf.normals[0] = n1;
						Vector3f n2 = m.norms.get((int)normalIndices.y - 1);
						mf.normals[1] = n2;
						Vector3f n3 = m.norms.get((int)normalIndices.z - 1);
						mf.normals[2] = n3;

						//// SETUP VERTICIES ////
						Vector3f v1 = m.verts.get((int)vertexIndices.x - 1);
						mf.points[0] = v1;
						Vector3f v2 = m.verts.get((int)vertexIndices.y - 1);
						mf.points[1] = v2;
						Vector3f v3 = m.verts.get((int)vertexIndices.z - 1);
						mf.points[2] = v3;

						//// SETUP TEXTURE COORDS ////
						if(textureIndices!=null)
						{
							mf.textureCoords = new Vector2f[3];

							Vector2f t1 = m.textureCoords.get((int)textureIndices.x - 1);
							mf.textureCoords[0] = t1;
							Vector2f t2 = m.textureCoords.get((int)textureIndices.y - 1);
							mf.textureCoords[1] = t2;
							Vector2f t3 = m.textureCoords.get((int)textureIndices.z - 1);
							mf.textureCoords[2] = t3;
						}

						//Set the face's material to the current material
						if(cur != null)
						{
							mf.material = cur;
							//if(cur.texture!=null)
								//m.texture = cur.texture;
						}

						//Tell face to set up AABB
						mf.setUpAABB();

						m.faces.add(mf);
					}
				}
				//Indicates a reference to an exterior .mtl file
				else if(line.startsWith("mtllib "))
				{
					//The file being referenced by mtllib call
					File lib = new File(f.getParentFile()+File.separator+line.split(" ")[1]);

					//Parse it and add all generated Materials to the mats list
					mats.addAll(parseMTL(lib, m));
				}
				//Telling us to use a material
				else if(line.startsWith("usemtl "))
				{
					String name = line.split(" ")[1];

					if(mats!=null)
						//Find material with correct name and use it
						for(Material material : mats)
							if(material.name.equals(name))
							{
								cur = material;
								break;
							}
				}
			}
			reader.close();

			//Tell model to set up AABB
			m.setUpAABB();
			
			//Remove the first element, because...
			m.faces.remove(0);

			return m;

		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Generates a List of Materials from the materials
	 * in an MTL file
	 * 
	 * @param f - The file to read from
	 * @param m - A model, it's texture will be set to the last texture loaded. If not wanted, set to null.
	 * @return a List< Material > containing all materials defined in the file
	 */
	public static List<Material> parseMTL(File f, Model m)
	{
		List<Material> mats = new ArrayList<Material>();
		int matNum = -1;

		try{
			BufferedReader reader = new BufferedReader(new FileReader(f));
			Material mtl = null;
			String line;

			while((line = reader.readLine())!=null)
			{	
				//Declaring a new material
				if(line.startsWith("newmtl "))
				{
					//If this is not the first material,
					//Add the current material to the mats list
					//before creating a new one
					if(mtl != null)
						mats.add(mtl);

					//Make mtl a new material and set name and id
					mtl = new Material();
					mtl.name = line.split(" ")[1];
					mtl.id = -1;
				}
				//Specifying ambient color of material
				else if(line.startsWith("Ka "))
				{
					//Just split line up and set the ambient
					mtl.ambient = new Vector3f(
							Float.valueOf(line.split(" ")[1]),
							Float.valueOf(line.split(" ")[2]),
							Float.valueOf(line.split(" ")[3])
							);
				}
				//Specifying diffuse color of material
				else if(line.startsWith("Kd "))
				{
					mtl.diffuse = new Vector3f(
							Float.valueOf(line.split(" ")[1]),
							Float.valueOf(line.split(" ")[2]),
							Float.valueOf(line.split(" ")[3])
							);
				}
				//Specifying specular color of material
				else if(line.startsWith("Ks "))
				{
					mtl.specular = new Vector3f(
							Float.valueOf(line.split(" ")[1]),
							Float.valueOf(line.split(" ")[2]),
							Float.valueOf(line.split(" ")[3])
							);
				}
				//Specifying coefficiant of specular color
				//TODO add support for specular coefficiants
				else if(line.startsWith("Ns "))
				{

				}
				//Specifying ambient texture map
				//TODO add support for ambient texture map
				else if(line.startsWith("map_Ka "))
				{
				}
				//Specifying diffuse texture map
				else if(line.startsWith("map_Kd "))
				{
					matNum++;
					//Set the material's texture to specified texture
					mtl.texture = MyTextureLoader.getTexture(f.getParentFile() + File.separator + line.split(" ")[1]);
					m.textures.add(mtl.texture);
					m.texture = mtl.texture;
					mtl.id = matNum;
					m.temp.add(matNum);
				}
				//Specifying specular texture map
				//TODO add support for specular texture map
				else if(line.startsWith("map_Ks "))
				{

				}
				//Specifying highlight component map
				//TODO add support for highlight component map
				else if(line.startsWith("map_Ns "))
				{

				}
			}
			reader.close();

			mats.add(mtl);
		}
		catch(FileNotFoundException e){e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}

		return mats;
	}

	private static FloatBuffer reserveData(int size) {
		return BufferUtils.createFloatBuffer(size);
	}

	private static float[] asFloats(Vector3f v) {
		return new float[]{v.x, v.y, v.z};
	}
	private static float[] asFloats(Vector2f v) {
		return new float[]{v.x, v.y};
	}

	/**
	 * Returns An Object[][]
	 * 
	 * <ul>
	 * 	<li>return[0][0] - vboVertexHandle:int</li>
	 * 	<li>return[0][1] - vboNormalHandle:int</li>
	 * 	<li>return[0][2] - vboTextureHandle:int</li>
	 *  <li>return[0][3] - vboColorHandle:int</li>
	 *  <li>return[0][4] - vboTexIDHandle:int</li>
	 * 	<li>return[1][0] - vertices:FloatBuffer</li>
	 * 	<li>return[1][1] - normals:FloatBuffer</li>
	 * 	<li>return[1][2] - textureCoordinates:FloatBuffer</li>
	 *  <li>return[1][3] - colors:FloatBuffer</li>
	 *  <li>return[1][4] - textureID:FloatBuffer</li>
	 * </ul>
	 */
	public static Object[][] createVBO(Model model) 
	{
		int vboVertexHandle = glGenBuffers();
		int vboNormalHandle = glGenBuffers();
		int vboColorHandle = glGenBuffers();
		int vboTexHandle = glGenBuffers();
		int vboTexIDHandle = glGenBuffers();

		FloatBuffer vertices = reserveData(model.faces.size() * 9);
		FloatBuffer normals = reserveData(model.faces.size() * 9);
		FloatBuffer colors = reserveData(model.faces.size() * 9);
		FloatBuffer textCoords = reserveData(model.faces.size() * 6);
		IntBuffer textID = BufferUtils.createIntBuffer(model.faces.size() * 3);//reserveData(model.faces.size() * 3);

		for (Face face : model.faces)
		{
			vertices.put(asFloats(face.points[0]));
			vertices.put(asFloats(face.points[1]));
			vertices.put(asFloats(face.points[2]));

			normals.put(asFloats(face.normals[0]));
			normals.put(asFloats(face.normals[1]));
			normals.put(asFloats(face.normals[2]));

			if(face.material!=null)
			{
				colors.put(asFloats(face.material.diffuse));
				colors.put(asFloats(face.material.diffuse));
				colors.put(asFloats(face.material.diffuse));
			}
			if(face.textureCoords!=null)
			{
				textCoords.put(asFloats(face.textureCoords[0]));
				textCoords.put(asFloats(face.textureCoords[1]));
				textCoords.put(asFloats(face.textureCoords[2]));
			}
			if(face.material!=null)
			{
				textID.put(face.material.id);
				textID.put(face.material.id);
				textID.put(face.material.id);
			}
		}
		vertices.flip();
		normals.flip();
		colors.flip();
		textCoords.flip();
		textID.flip();

		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glVertexPointer(3, GL_FLOAT, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
		glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
		glNormalPointer(GL_FLOAT, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
		glColorPointer(3, GL_FLOAT, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, vboTexHandle);
		glBufferData(GL_ARRAY_BUFFER, textCoords, GL_STATIC_DRAW);
		glTexCoordPointer(2, GL_FLOAT, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, vboTexIDHandle);
		glBufferData(GL_ARRAY_BUFFER, textID, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		return new Object[][]{
				{vboVertexHandle, vertices}, 
				{vboNormalHandle, normals}, 
				{vboTexHandle, textCoords}, 
				{vboColorHandle, colors},
				{vboTexIDHandle, textID}
		};
	}
}
