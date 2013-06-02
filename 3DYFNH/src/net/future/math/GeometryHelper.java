package net.future.math;
import net.future.gameobject.GameObject;
import net.future.model.AABB;
import net.future.model.Face;
import net.future.model.Model;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GeometryHelper 
{
	/**
	 * @deprecated Not necessary to use <br/><br/>
	 * Determines the slope of specified face
	 * 
	 * @param f - The face
	 * 
	 * @return A Vector3f with the change of the x, y, and z axes
	 */
	@Deprecated
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

		float B = E.y;
		float C = E.z;
		float x = p1.x;
		float y = p1.y;
		float z = p1.z;
		//float Ax = E.x*p1.x;
		//float By = E.y*p1.y;
		//float Cz = E.z*p1.z;

		//plane equation
		//float PlaneEquation = A*x + B*y + C*z;

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
		GameObject newOne = new GameObject(mover);
		
		newOne.position = newPos;

		return intersecting(newOne, collide);
	}

	/**
	 * Checks if the AABBs of 2 GameObjects are intersecting
	 */
	public static boolean intersecting(GameObject obj1, GameObject obj2)
	{
		Model m2=obj2.model;
		Model m1=obj1.model;
		
		//Whether to just check for intersection of each model, or to check
		//for intersection of each face on each model
		boolean specificCollision = true;

		if(!specificCollision)
		{
			AABB adjAB1 = new AABB(obj1.model.boundingBox, obj1.position);
			AABB adjAB2 = new AABB(obj2.model.boundingBox, obj2.position);
			
			if(adjAB1.intersecting(adjAB2))
			{
				return true;
			}
		}
		else
		{
			AABB adjMAB1 = new AABB(m1.boundingBox, obj1.position);
			AABB adjMAB2 = new AABB(m2.boundingBox, obj2.position);
			
			//If the bounding boxes of the 2 models are not colliding,then 
			//none of the faces are, so return false
			if(!adjMAB1.intersecting(adjMAB2))
				return false;
			
			//Loop through the faces of the first object's model
			for (int i = 0; i < m1.faces.size(); i++)
			{
				Face f1 = m1.faces.get(i);
				AABB adjAB1 = new AABB(f1.boundingBox, obj1.position);

				//Loop through the faces of the second object's model
				for(int j = 0; j < m2.faces.size(); j++)
				{
					Face f2 = m2.faces.get(j);
					AABB adjAB2 = new AABB(f2.boundingBox, obj2.position);

					//for some reason the 
					if(adjAB1.intersecting(adjAB2)&&j!=0)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Returns the distance between two 3D points
	 * @param v1 - The first vector
	 * @param v2 - The second vector
	 */
	public static float distance(Vector3f v1, Vector3f v2)
	{
		return (float)Math.abs(Math.sqrt(
				Math.pow((v2.x-v1.x), 2) +
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
		return (float)Math.abs(Math.sqrt(
				Math.pow((v2.x-v1.x), 2) +
				Math.pow((v2.y-v1.y), 2)));
	}
}