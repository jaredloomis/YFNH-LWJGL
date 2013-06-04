#version 120

varying vec3 specColor;
varying vec3 diffColor;
varying vec3 ambColor;

varying int texID;

attribute int textureID;

void main() 
{	
	//Position of vertex
	vec3 vertexPosition = (gl_ModelViewMatrix * gl_Vertex).xyz;
	
	//Direction of light, normalized
	vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);
	
	//Normal of vertex
	vec3 surfaceNormal  = (gl_NormalMatrix * gl_Normal).xyz;

	//Light's diffuse intensity
	float diffuseLightIntensity = max(0, dot(surfaceNormal, lightDirection));
	
	//Main color of vertex
	diffColor = diffuseLightIntensity * gl_Color;

	//Ambient color
	ambColor = gl_LightModel.ambient.rgb;
	
	//Direction the light reflects
	vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
	
	//Base specular intensity
	float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
	
	if (diffuseLightIntensity != 0) 
	{
		// Enhances the specular scalar value by raising it to the exponent of the shininess.
		float fspecular = pow(specular, gl_FrontMaterial.shininess);
		
		if(gl_FrontMaterial.shininess == 0.0)
		{
			fspecular = vec3(0.0, 0.0, 0.0);
		}
		
		//specular = pow(specular, gl_FrontMaterial.shininess);
		
		specColor = fspecular;
		//specColor = fspecular;
	}
	

	//!!!!EDITED!~!!!!
	//const vec4[8] multiTexCoords = {gl_MultiTexCoord0, gl_MultiTexCoord1, gl_MultiTexCoord2, gl_MultiTexCoord3, gl_MultiTexCoord4, gl_MultiTexCoord5, gl_MultiTexCoord6, gl_MultiTexCoord7 };
	//gl_TexCoord[texID] = multiTexCoords[texID];
	gl_TexCoord[0] = gl_MultiTexCoord0;

	// Retrieves the position of the vertex in clip space
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    
    texID = textureID;
}