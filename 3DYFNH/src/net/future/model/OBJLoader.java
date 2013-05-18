package net.future.model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
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
	 * </ul>
	 * 
	 * @param f - The path of the .obj file
	 * @return a Model object of the given file
	 */
	public static Model loadModel(File f)
	{
		try{
			BufferedReader reader = new BufferedReader(new FileReader(f));
			Model m = new Model();
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
				//Indicates a vertex texture
				else if (line.startsWith("vt ")) {} 

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
						Vector3f normalIndices = new Vector3f(
								Float.valueOf(line.split(" ")[1].split("/")[2]),
								Float.valueOf(line.split(" ")[2].split("/")[2]),
								Float.valueOf(line.split(" ")[3].split("/")[2]));
						Face mf = new Face(vertexIndices, normalIndices);
						m.faces.add(mf);

						//Instantiate all the arrays
						mf.normals = new Vector3f[3];
						mf.points = new Vector3f[3];
						mf.textureCoords = new Vector2f[3];

						Vector3f n1 = m.norms.get((int) mf.normal[0] - 1);
						mf.normals[0] = n1;
						Vector3f n2 = m.norms.get((int) mf.normal[1] - 1);
						mf.normals[1] = n2;
						Vector3f n3 = m.norms.get((int) mf.normal[2] - 1);
						mf.normals[2] = n3;

						Vector3f v1 = m.verts.get((int) mf.vertex[0] - 1);
						mf.points[0] = v1;
						Vector3f v2 = m.verts.get((int) mf.vertex[1] - 1);
						mf.points[1] = v2;
						Vector3f v3 = m.verts.get((int) mf.vertex[2] - 1);
						mf.points[2] = v3;
						
						Vector2f t1 = new Vector2f(0, 0);
						mf.textureCoords[0] = t1;
						Vector2f t2 = new Vector2f(1, 0);
						mf.textureCoords[1] = t2;
						Vector2f t3 = new Vector2f(0, 1);
						mf.textureCoords[2] = t3;
						
						mf.setUpAABB();
					}
				}
			}
			reader.close();

			m.setUpAABB();

			return m;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Creates the list that will be called to
	 * draw the object
	 */
	public static int createDisplayList(Model m) 
	{
		int displayList = glGenLists(1);
		glNewList(displayList, GL_COMPILE);
		{
			//Make model shiny according to the
			//Model.shininess variable
			glMaterialf(GL_FRONT, GL_SHININESS, m.shininess);

			//Use the model's shader
			glUseProgram(m.shader);

			//If the faces are triangulated
			if(m.faces.get(0).vertex.length == 3)
			{
				glBegin(GL_TRIANGLES);

				//If the model does not have a texture
				if(m.texture == null)
				{
					for (Face face : m.faces) 
					{
						//Make the array of normals and points
						//as large as is needed
						//face.normals = new Vector3f[3];
						//face.points = new Vector3f[3];

						//1. Set the normal and give it to OpenGL
						Vector3f n1 = m.norms.get((int) face.normal[0] - 1);
						glNormal3f(n1.x, n1.y, n1.z);

						//2. Add it to the face's normals array
						//face.normals[0] = n1;		

						//3. Set the vertex and give it to OpenGL
						Vector3f v1 = m.verts.get((int) face.vertex[0] - 1);
						glVertex3f(v1.x, v1.y, v1.z);

						//4. Add it to the face's vertices array
						//face.points[0] = v1;

						Vector3f n2 = m.norms.get((int) face.normal[1] - 1);
						glNormal3f(n2.x, n2.y, n2.z);
						//face.normals[1] = n2;
						Vector3f v2 = m.verts.get((int) face.vertex[1] - 1);
						glVertex3f(v2.x, v2.y, v2.z);
						//face.points[1] = v2;

						Vector3f n3 = m.norms.get((int) face.normal[2] - 1);
						glNormal3f(n3.x, n3.y, n3.z);
						//face.normals[2] = n3;
						Vector3f v3 = m.verts.get((int) face.vertex[2] - 1);
						glVertex3f(v3.x, v3.y, v3.z);
						//face.points[2] = v3;
					}
				}
				//If the model has a texture
				else
				{
					m.texture.bind();

					for (Face face : m.faces) 
					{
						//face.normals = new Vector3f[3];
						//face.points = new Vector3f[3];
						Vector2f[] textureCoords = new Vector2f[]{
								new Vector2f(0, 0),
								new Vector2f(1, 0),
								new Vector2f(0, 1)
						};

						glTexCoord2f(textureCoords[0].x, textureCoords[0].y);
						
						Vector3f n1 = m.norms.get((int) face.normal[0] - 1);
						glNormal3f(n1.x, n1.y, n1.z);
						
						Vector3f v1 = m.verts.get((int) face.vertex[0] - 1);
						glVertex3f(v1.x, v1.y, v1.z);
						

						glTexCoord2f(textureCoords[1].x, textureCoords[1].y);
						Vector3f n2 = m.norms.get((int) face.normal[1] - 1);
						glNormal3f(n2.x, n2.y, n2.z);
						//face.normals[1] = n2;
						Vector3f v2 = m.verts.get((int) face.vertex[1] - 1);
						glVertex3f(v2.x, v2.y, v2.z);
						//face.points[1] = v2;

						glTexCoord2f(textureCoords[2].x, textureCoords[2].y);
						Vector3f n3 = m.norms.get((int) face.normal[2] - 1);
						glNormal3f(n3.x, n3.y, n3.z);
						//face.normals[2] = n3;
						Vector3f v3 = m.verts.get((int) face.vertex[2] - 1);
						glVertex3f(v3.x, v3.y, v3.z);
						//face.points[2] = v3;
					}

					m.texture.release();
				}
				glEnd();
			}
		}
		glEndList();
		return displayList;
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
	 * 	<li>return[1][0] - vertices:FloatBuffer</li>
	 * 	<li>return[1][1] - normals:FloatBuffer</li>
	 * 	<li>return[1][2] - textureCoordinates:FloatBuffer</li>
	 * </ul>
	 */
	public static Object[][] createVBO(Model model) 
	{
		int vboVertexHandle = glGenBuffers();
		int vboNormalHandle = glGenBuffers();
		int vboTexHandle = glGenBuffers();
		
		FloatBuffer vertices = reserveData(model.faces.size() * 9);
		FloatBuffer normals = reserveData(model.faces.size() * 9);
		FloatBuffer textCoords = reserveData(model.faces.size() * 9);
		
		for (Face face : model.faces)
		{
			vertices.put(asFloats(face.points[0]));
			vertices.put(asFloats(face.points[1]));
			vertices.put(asFloats(face.points[2]));
			
			normals.put(asFloats(face.normals[0]));
			normals.put(asFloats(face.normals[1]));
			normals.put(asFloats(face.normals[2]));
			
			textCoords.put(asFloats(face.textureCoords[0]));
			textCoords.put(asFloats(face.textureCoords[1]));
			textCoords.put(asFloats(face.textureCoords[2]));
		}
		vertices.flip();
		normals.flip();
		textCoords.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glVertexPointer(3, GL_FLOAT, 0, 0L);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
		glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
		glNormalPointer(GL_FLOAT, 0, 0L);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboTexHandle);
		glBufferData(GL_ARRAY_BUFFER, textCoords, GL_STATIC_DRAW);
		glTexCoordPointer(2, GL_FLOAT, 0, 0L);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return new Object[][]{{vboVertexHandle, vertices}, {vboNormalHandle, normals}, {vboTexHandle, textCoords}};
	}
}
