#version 120

///////////////NOTES////////////////////////////
// Varying is set by the vertex program and read by the fragment
// program.
// Uniform is set by the Java code and read by the vertex and fragment
// program. 
// Attribute is set by the Java code and read by the vertex program. 
// Object Space: Vertices you specify with glVertex(Pointer)
// Eye Space: Vertices transformed by the modelview matrix 
// 							(affected by glTranslate/glRotate)
// Clip Space: Vertices transformed by the projection matrix
//							(affected by gluPerspective/glOrtho)

// Diffuse lighting: light is reflected in every direction evenly
// To calculate the intensity of the reflected lighting we need the cosine 
// of the angle between the light and the surface normal. We could also use 
// diffuse attributes of the material and of the light.
// Specular lighting: 
///////////////END NOTES////////////////////////////

//varying vec3 varyingColour;

varying vec3 specColor;
varying vec3 diffColor;
varying vec3 ambColor;

varying int texID;

attribute int textureID;

void main() 
{	
	//Set position of vertex
	vec3 vertexPosition = (gl_ModelViewMatrix * gl_Vertex).xyz;
	
	//Direction of light, normalized
	vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);
	
	//Normal of vertex
	vec3 surfaceNormal  = (gl_NormalMatrix * gl_Normal).xyz;

	//Light's diffuse intensity
	float diffuseLightIntensity = max(0, dot(surfaceNormal, lightDirection));
	
	//Set the color
	//varyingColour.rgb = diffuseLightIntensity * gl_Color;
	
	diffColor = diffuseLightIntensity * gl_Color;
	
	//varyingColour += gl_LightModel.ambient.rgb;

	ambColor = gl_LightModel.ambient.rgb;
	
	// Calculates the direction of the reflectionDirection by using the method reflect, which takes 
	// the normalized direction from the light source to the surface as the 1st parameter,
	// and the normalized surface normal as the second. Since lightDirection points to
	// the direction of the light and not the surface, we need to negate it in order for
	// the returned vector to be valid. 
	vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
	
	// Stores the dot-product of the surface normal and the direction of the reflection
	// in a scalar. Also checks if the value is negative. If so, the scalar is set to 0.0.
	float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
	
	if (diffuseLightIntensity != 0) 
	{
		// Enhances the specular scalar value by raising it to the exponent of the shininess.
		float fspecular = pow(specular, gl_FrontMaterial.shininess);
		
		specColor = vec3(pow(specular, gl_FrontMaterial.shininess));
		
		// Adds the specular value to the colour.
		//varyingColour.rgb += vec3(fspecular, fspecular, fspecular);
	}

	const vec4[8] multiTexCoords = {gl_MultiTexCoord0, gl_MultiTexCoord1, gl_MultiTexCoord2, gl_MultiTexCoord3, gl_MultiTexCoord4, gl_MultiTexCoord5, gl_MultiTexCoord6, gl_MultiTexCoord7 };
	//gl_TexCoord[0] = gl_MultiTexCoord0;
	//gl_TexCoord[1] = gl_MultiTexCoord1;
	gl_TexCoord[texID] = multiTexCoords[texID];

	// Retrieves the position of the vertex in clip space by multiplying it by the modelview-
	// projection matrix and stores it in the built-in output variable gl_Position.
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    
    texID = textureID;
}