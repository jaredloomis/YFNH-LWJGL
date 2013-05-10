package net.future.math;
import java.util.List;

import net.future.gameobject.GameObject;
import net.future.model.AABB;
import net.future.model.Face;
import net.future.model.Model;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

@SuppressWarnings("unused")
public class GeometryHelper 
{
	/**
	 * Determines the slope of specified face
	 * 
	 * @param f - The face
	 * 
	 * @return A Vector3f with the change of the x, y, and z axes
	 */
	public static Vector3f getSlope(Face f)
	{
		Vector3f p1 = f.points[0];
		Vector3f p2 = f.points[1];
		Vector3f p3 = f.points[2];

		//Vectors from point1 to point2 and from
		//point1 to point3
		Vector3f a = new Vector3f(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
		Vector3f b = new Vector3f(p3.x - p1.x, p3.y - p1.y, p3.z - p1.y);

		//Vector perpindiculat to the plane
		Vector3f E = Vector3f.cross(a, b, null);

		float A = E.x;
		float B = E.y;
		float C = E.z;
		float x = p1.x;
		float y = p1.y;
		float z = p1.z;
		//float Ax = E.x*p1.x;
		//float By = E.y*p1.y;
		//float Cz = E.z*p1.z;

		//plane equation
		float PlaneEquation = A*x + B*y + C*z;

		//X value of point intersecting
		//The yz coordinate plane
		float yzIntercept = B*y + C*z;

		Vector3f yzInt = new Vector3f(yzIntercept, 0, 0);

		float deltaX = x - yzInt.x;
		float deltaY = y - yzInt.y;
		float deltaZ = z - yzInt.z;


		return new Vector3f(deltaX, deltaY, deltaZ);
	}

	/**
	 * Determines whether the two objects would
	 * be intersecting if the first object was moved to
	 * the specified position.
	 * 
	 * @param mover - The GameObject that will be moved
	 * @param collide - The GameObject to test intersection against
	 * @param newPos - The new position to place mover
	 * @return <b>true</b> if the two GameObjects will intersect
	 */
	public static boolean willIntersect(GameObject mover, GameObject collide, Vector3f newPos)
	{	
		System.out.println(mover.getName());
		//GameObject newOne = new ObjectCube(mover.world);
		GameObject newOne = new GameObject(mover);
		//newOne.model = mover.model;
		newOne.position = newPos;

		return intersecting(newOne, collide);
	}

	public static boolean faceCollide(Face f1, Vector3f pos1, float length1, float width1, float height1, Face f2, Vector3f pos2, float length2, float width2, float height2) 
	{
		if (Math.abs(pos1.x - pos2.x) > (length1 + length2)) return false;
		if (Math.abs(pos1.y - pos2.y) > (width1 + width2)) return false;
		if (Math.abs(pos1.z - pos2.z) > (height1 + height2)) return false;
		return true;
	}

	public static boolean intersecting(GameObject obj1, GameObject obj2)
	{
		if(true)
		{
			Model m1 = obj1.model;
			Model m2 = obj2.model;
			AABB ab1 = m1.boundingBox;
			AABB ab2 = m2.boundingBox;
			
			AABB adjAB1 = new AABB(Vector3f.add(ab1.min, obj1.position, null),
								   Vector3f.add(ab1.max, obj1.position, null));
			
			AABB adjAB2 = new AABB(Vector3f.add(ab2.min, obj2.position, null), 
								   Vector3f.add(ab2.max, obj2.position, null));
			
			if(adjAB1.intersecting(adjAB2)) return true;
			
			return false;
		}
		else if(obj1.model != null && obj2.model != null)
		{
			//System.out.println("asdf");
			Model mod2=obj2.model;
			Model mod1=obj1.model;

			//Loop through the faces of the first object's model
			for (int i = 0; i < mod1.faces.size(); i++)
			{
				Face f1 = mod1.faces.get(i);
				AABB ab1 = f1.boundingBox;
				Vector3f adjMin1 = Vector3f.add(obj1.position, ab1.min, null);
				Vector3f adjMax1 = Vector3f.add(obj1.position, ab1.max, null);
				AABB adjAB1 = new AABB(adjMin1, adjMax1);

				//Loop through the faces of the second object's model
				for(int j = 0; j < mod2.faces.size(); j++)
				{
					Face f2 = mod2.faces.get(j);
					AABB ab2 = f2.boundingBox;
					Vector3f adjMin2 = Vector3f.add(obj2.position, ab2.min, null);
					Vector3f adjMax2 = Vector3f.add(obj2.position, ab2.max, null);
					AABB adjAB2 = new AABB(adjMin2, adjMax2);

					float length1 = Math.abs(adjMax1.x-adjMin1.x);
					float width1 = Math.abs(adjMax1.y-adjMin1.y);
					float height1 = Math.abs(adjMax1.z-adjMin1.z);

					float length2 = Math.abs(adjMax2.x-adjMin2.x);
					float width2 = Math.abs(adjMax2.y-adjMin2.y);
					float height2 = Math.abs(adjMax2.z-adjMin2.z);

					if(faceCollide(f1, obj1.position, length1, width1, height1, f2, obj2.position, length2, width2, height2))
						return true;

					/*
					if(adjAB1.intersecting(adjAB2))
					{
						return true;
					}*/
				}
			}
		}
		else if(obj2.model != null)
		{
			Model mod2=obj2.model;

			AABB ab1 = new AABB(new Vector3f(0, 0, 0), new Vector3f(0.3f, 0.3f, 0.3f));
			Vector3f adjMin1 = Vector3f.add(obj1.position, ab1.min, null);
			Vector3f adjMax1 = Vector3f.add(obj1.position, ab1.max, null);
			AABB adjAB1 = new AABB(adjMin1, adjMax1);

			//Loop through the faces of the second object's model
			for(int j = 0; j < mod2.faces.size(); j++)
			{
				//AABB ab1 = new AABB(f1);
				Face f2 = mod2.faces.get(j);
				AABB ab2 = f2.boundingBox;
				Vector3f adjMin2 = Vector3f.add(obj1.position, ab2.min, null);
				Vector3f adjMax2 = Vector3f.add(obj1.position, ab2.max, null);
				AABB adjAB2 = new AABB(adjMin2, adjMax2);

				if(adjAB1.intersecting(adjAB2))
				{
					return true;
				}
			}
		}
		else if(obj1.model != null)
		{
			Model mod1=obj1.model;

			AABB ab2 = new AABB(new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f));
			Vector3f adjMin2 = Vector3f.add(obj1.position, ab2.min, null);
			Vector3f adjMax2 = Vector3f.add(obj1.position, ab2.max, null);
			AABB adjAB2 = new AABB(adjMin2, adjMax2);

			//Loop through the faces of the second object's model
			for(int j = 0; j < mod1.faces.size(); j++)
			{
				//AABB ab1 = new AABB(f1);
				Face f1 = mod1.faces.get(j);
				AABB ab1 = f1.boundingBox;
				Vector3f adjMin1 = Vector3f.add(obj1.position, ab2.min, null);
				Vector3f adjMax1 = Vector3f.add(obj1.position, ab2.max, null);
				AABB adjAB1 = new AABB(adjMin1, adjMax1);

				if(adjAB1.intersecting(adjAB2))
				{
					return true;
				}
			}
		}


		return false;

	}

	/**
	 * Currently uses an algorithm that creates a "bounding cube"
	 * around each triangulated face (using the minimum x, y, and z value
	 * and the maximum x, y, and z value)and tests if any points from
	 * other faces are inside that "bounding cube"
	 * 
	 * @param obj0 - The first GameObject
	 * @param obj1 - The second GameObject
	 * @return <b>true</b> if the two GameObjects are intersecting
	 */
	public static boolean intersectingOLD(GameObject obj0, GameObject obj1)
	{
		Model mod1=obj1.model;

		if(obj0.model != null)
		{
			Model mod0=obj0.model;

			//Loop through the faces of the first object's model
			for (int i = 0; i < mod0.faces.size(); i++)
			{
				Face f1 = mod0.faces.get(i);
				Vector3f mMod0;
				mMod0 = getSlope(f1);

				//Loop through the faces of the second object's model
				for(int j = 0; j < mod1.faces.size(); j++)
				{
					Face f2 = mod1.faces.get(j);
					Vector3f mMod1;
					mMod1 = getSlope(f2);

					//If the slope of the 2 faces are the same, they dont intersect
					if(mMod0.x == mMod1.x && mMod0.y == mMod1.y && mMod0.z == mMod1.z)
					{
						;
					}
					//The slope of the faces are different,
					//so it is still possible that they intersect
					else
					{
						Vector3f pa1 = f1.points[0];
						Vector3f pa2 = f1.points[1];
						Vector3f pa3 = f1.points[2];

						Vector3f pb1 = f2.points[0];
						Vector3f pb2 = f2.points[1];
						Vector3f pb3 = f2.points[2];

						//Mins and maxs of each face added to the object's current
						//position, to be used in main part of algorithm
						float minXA = Math.min(Math.min(pa1.x, pa2.x), pa3.x)+obj0.position.x;
						float minYA = Math.min(Math.min(pa1.y, pa2.y), pa3.y)+obj0.position.y;
						float minZA = Math.min(Math.min(pa1.z, pa2.z), pa3.z)+obj0.position.z;
						float maxXA = Math.max(Math.max(pa1.x, pa2.x), pa3.x)+obj0.position.x;
						float maxYA = Math.max(Math.max(pa1.y, pa2.y), pa3.y)+obj0.position.y;
						float maxZA = Math.max(Math.max(pa1.z, pa2.z), pa3.z)+obj0.position.z;

						float minXB = Math.min(Math.min(pb1.x, pb2.x), pb3.x)+obj1.position.x;
						float minYB = Math.min(Math.min(pb1.y, pb2.y), pb3.y)+obj1.position.y;
						float minZB = Math.min(Math.min(pb1.z, pb2.z), pb3.z)+obj1.position.z;
						float maxXB = Math.max(Math.max(pb1.x, pb2.x), pb3.x)+obj1.position.x;
						float maxYB = Math.max(Math.max(pb1.y, pb2.y), pb3.y)+obj1.position.y;
						float maxZB = Math.max(Math.max(pb1.z, pb2.z), pb3.z)+obj1.position.z;


						//The positions of the points of the current faces
						//(point position + object position)
						Vector3f a1 = Vector3f.add(pa1, obj0.position, null);
						Vector3f a2 = Vector3f.add(pa2, obj0.position, null);
						Vector3f a3 = Vector3f.add(pa3, obj0.position, null);

						Vector3f b1 = Vector3f.add(pb1, obj1.position, null);
						Vector3f b2 = Vector3f.add(pb2, obj1.position, null);
						Vector3f b3 = Vector3f.add(pb3, obj1.position, null);

						/*System.out.println("minX: " + minXB);
					System.out.println("maxX: " + maxXB);
					System.out.println("minY: " + minYB);
					System.out.println("maxY: " + maxYB);
					System.out.println("minZ: " + minXB);
					System.out.println("maxZ: " + maxZB);*/


						//TODO make new algorithm
						///TEMPORARY BAD ALGORITHM///

						//If any of the points from face2
						//are inside the "bounding box" of
						//face1, @return true
						if(b1.x >= minXA && b1.x <= maxXA){
							if(b1.y >= minYA && b1.y <= maxXA){
								if(b1.z >= minZA && b1.z <= maxZA)
								{
									return true;
								}
							}
						}
						if(b2.x >= minXA && b2.x <= maxXA){
							if(b2.y >= minYA && b2.y <= maxXA){
								if(b2.z >= minZA && b2.z <= maxZA)
								{
									return true;
								}
							}
						}
						if(b3.x >= minXA && b3.x <= maxXA){
							if(b3.y >= minYA && b3.y <= maxXA){
								if(b3.z >= minZA && b3.z <= maxZA)
								{
									return true;
								}
							}
						}

						//TODO make new algorithm
						///TEMPORARY BAD ALGORITHM///

						//If any of the points from face1
						//are inside the "bounding box" of
						//face2, @return true
						if(a1.x >= minXB && a1.x <= maxXB){
							if(a1.y >= minYB && a1.y <= maxYB){
								if(a1.z >= minZB && a1.z <= maxZB)
								{
									return true;
								}
							}
						}
						if(a2.x >= minXB && a2.x <= maxXB){
							if(a2.y >= minYB && a2.y <= maxYB){
								if(a2.z >= minZB && a2.z <= maxZB)
								{
									return true;
								}
							}
						}
						if(a3.x >= minXB && a3.x <= maxXB){
							if(a3.y >= minYB && a3.y <= maxYB){
								if(a3.z >= minZB && a3.z <= maxZB)
								{
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}
		else
		{
			Vector3f mMod0 = new Vector3f(-1, -1, -1);

			//Loop through the faces of the second object's model
			for(int j = 0; j < mod1.faces.size(); j++)
			{
				Face f2 = mod1.faces.get(j);
				Vector3f mMod1;
				mMod1 = getSlope(f2);

				//If the slope of the 2 faces are the same, they dont intersect
				if(mMod0.x == mMod1.x && mMod0.y == mMod1.y && mMod0.z == mMod1.z)
					;

				//The slope of the faces are different,
				//so it is still possible that they intersect
				else
				{
					Vector3f pa1 = obj1.position;

					Vector3f pb1 = f2.points[0];
					Vector3f pb2 = f2.points[1];
					Vector3f pb3 = f2.points[2];

					//Mins and maxs of each face added to the object's current
					//position, to be used in main part of algorithm
					float minXA = obj0.position.x;
					float minYA = obj0.position.y;
					float minZA = obj0.position.z;
					float maxXA = obj0.position.x;
					float maxYA = obj0.position.y;
					float maxZA = obj0.position.z;

					float minXB = Math.min(Math.min(pb1.x, pb2.x), pb3.x)+obj1.position.x;
					float minYB = Math.min(Math.min(pb1.y, pb2.y), pb3.y)+obj1.position.y;
					float minZB = Math.min(Math.min(pb1.z, pb2.z), pb3.z)+obj1.position.z;
					float maxXB = Math.max(Math.max(pb1.x, pb2.x), pb3.x)+obj1.position.x;
					float maxYB = Math.max(Math.max(pb1.y, pb2.y), pb3.y)+obj1.position.y;
					float maxZB = Math.max(Math.max(pb1.z, pb2.z), pb3.z)+obj1.position.z;


					//The positions of the points of the current faces
					//(point position + object position)
					Vector3f a1 = Vector3f.add(pa1, obj0.position, null);

					Vector3f b1 = Vector3f.add(pb1, obj1.position, null);
					Vector3f b2 = Vector3f.add(pb2, obj1.position, null);
					Vector3f b3 = Vector3f.add(pb3, obj1.position, null);

					/*System.out.println("minX: " + minXB);
					System.out.println("maxX: " + maxXB);
					System.out.println("minY: " + minYB);
					System.out.println("maxY: " + maxYB);
					System.out.println("minZ: " + minXB);
					System.out.println("maxZ: " + maxZB);*/


					//TODO make new algorithm
					///TEMPORARY BAD ALGORITHM///

					//If any of the points from face2
					//are inside the "bounding box" of
					//face1, @return true
					if(b1.x >= minXA && b1.x <= maxXA){
						if(b1.y >= minYA && b1.y <= maxXA){
							if(b1.z >= minZA && b1.z <= maxZA)
							{
								return true;
							}
						}
					}
					if(b2.x >= minXA && b2.x <= maxXA){
						if(b2.y >= minYA && b2.y <= maxXA){
							if(b2.z >= minZA && b2.z <= maxZA)
							{
								return true;
							}
						}
					}
					if(b3.x >= minXA && b3.x <= maxXA){
						if(b3.y >= minYA && b3.y <= maxXA){
							if(b3.z >= minZA && b3.z <= maxZA)
							{
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}



	/**
	 * Tells whether the Face f would be rendered if the 
	 * GameObject cam was a camera
	 * 
	 * @param cam - "camera"
	 * @param f - the face
	 */
	public static boolean beingRendered(GameObject cam, Face f)
	{
		Vector3f[] normals = f.normals;
		Vector3f deltaNormal = new Vector3f(normals[0]);


		return false;
	}

	/**
	 * Returns an array of the coordinates that should
	 * be used when applying a texture to
	 * the given list of points.
	 * 
	 * @param points - The points of the triangulated face
	 */
	public static Vector2f[] triangleTextureCoordinates(Vector3f... points)
	{
		Vector3f v1 = points[0];
		Vector3f v2 = points[1];
		Vector3f v3 = points[2];

		//Get the length of each side
		float sideLength1 = distance(v1, v2);
		float sideLength2 = distance(v2, v3);
		float sideLength3 = distance(v1, v3);

		//Shortest and longest side
		float min = Math.min(Math.min(sideLength1, sideLength2), sideLength3);
		float max = Math.max(Math.max(sideLength1, sideLength2), sideLength3);

		//Normalize the length of each side to be
		//between 1 and 0
		float textLength1 = (sideLength1 - min)/(max-min);
		float textLength2 = (sideLength2 - min)/(max-min);
		float textLength3 = (sideLength3 - min)/(max-min);

		Vector2f tp1 = new Vector2f(0, 0);
		Vector2f tp2 = new Vector2f(textLength1, 0);
		Vector2f tp3 = getP3(tp1, textLength3, tp2, textLength2);

		//Make sure getP3() returned the correct positive
		//value
		if(tp3 != null)
		{
			tp3.x = Math.abs(tp3.x);
			tp3.y = Math.abs(tp3.y);
		}
		else
		{
			tp3 = new Vector2f(0, 0);
		}

		return new Vector2f[]{tp1, tp2, tp3};
	}

	/**
	 * Given 2 points on a triangle and the distances
	 * from each of those points to the last point,
	 * return the last point's location.
	 * <br/><br/>
	 * <b>THERE ARE ALWAYS 2 POSSIBILITIES, THIS RETURNS ONE. IT IS NOT GUARUNTEED TO BE THE CORRECT ONE!</b>
	 * <br/>
	 * @param p1 - The first point in the triangle
	 * @param distanceFromP1 - Distance from p1 to the new point
	 * @param p2 - The second point in the triangle
	 * @param distanceFromP2 - Distance from p2 to the new point
	 */
	public static Vector2f getP3(Vector2f p1, double distanceFromP1, Vector2f p2, double distanceFromP2) 
	{
		double d = distance(p1, p2);

		/*if(d > (distanceFromP1 + distanceFromP2) || p1.equals(p2) || d < Math.abs(distanceFromP1 - distanceFromP2)) 
		{
			// there does not exist a 3rd point, or there are an infinite amount of them
			return null;
		}*/

		double a = (distanceFromP1*distanceFromP1 - distanceFromP2*distanceFromP2 + d*d) / (2*d);
		if(a != Double.POSITIVE_INFINITY && a != (-1)*Double.POSITIVE_INFINITY)
		{
			double h = Math.sqrt(distanceFromP1*distanceFromP1 - a*a);

			Vector2f temp = new Vector2f((float)(p1.x + a*(p2.x - p1.x) / d), (float)(p1.y + a*(p2.y - p1.y) / d));

			return new Vector2f((float)(temp.x + h * (p2.y - p1.y) / d), (float)(temp.y - h * (p2.x - p1.x) / d));
		}
		return null;
	}

	/**
	 * Returns the distance between two 3D points
	 * @param v1 - The first vector
	 * @param v2 - The second vector
	 */
	public static float distance(Vector3f v1, Vector3f v2)
	{
		return (float)Math.abs(Math.sqrt(Math.pow((v2.x-v1.x), 2) +
				Math.pow((v2.y-v1.y), 2) +
				Math.pow((v2.z-v1.z), 2)));
	}

	/**
	 * Returns the distance between two 2D points
	 * @param v1 - The first vector
	 * @param v2 - The second vector
	 */
	public static float distance(Vector2f v1, Vector2f v2)
	{
		return (float)Math.abs(Math.sqrt(Math.pow((v2.x-v1.x), 2) +
				Math.pow((v2.y-v1.y), 2)));
	}
}
