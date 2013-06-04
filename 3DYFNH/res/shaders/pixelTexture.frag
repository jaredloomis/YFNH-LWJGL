#version 120

uniform sampler2D[10] textures;

varying vec4 varyingColour;

varying vec3 varyingNormal;

varying vec4 varyingVertex;

varying int texID;

void main()
{
	//Position of vertex in modelview space
    vec3 vertexPosition = (gl_ModelViewMatrix * varyingVertex).xyz;
    
    //Surface normal of current vertex
    vec3 surfaceNormal = normalize((gl_NormalMatrix * varyingNormal).xyz);
    
    //Direction light has traveled to get to vertexPosition
    vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);
    
    //Basically how much light is hitting the vertex
    float diffuseLightIntensity = max(0.0, dot(surfaceNormal, lightDirection));
    
    //"Main color" of vertex
    vec3 diffColor = diffuseLightIntensity * varyingColour.rgb;
    
    //Lowest light level possible
    vec3 ambColor = gl_LightModel.ambient;
    
    //Instantiate specColor for scope
    vec3 specColor = vec3(0.0, 0.0, 0.0);
    
    if(gl_FrontMaterial.shininess != 0.0)
    {
    	//Direction light is reflected off of surface normal
    	vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
    
    	//The intensity of reflection (specular)
    	float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
    	
    	//Raise specular to exponent of shininess
        float fspecular = pow(specular, gl_FrontMaterial.shininess);
        specColor = fspecular;
    }
    
    if(texID == -1)
	{	
		//Does not have a texture, just use diffuse, specular, and ambient colors
		gl_FragColor = vec4(ambColor, 1.0) + vec4(diffColor + specColor, 1.0);
	}
	else 
	{
		//Fragment has texture, use the texture's color, and diffuse, specular, and ambient colors
		gl_FragColor = vec4(ambColor, 1.0) + vec4(diffColor * vec3(texture(textures[texID], gl_TexCoord[0].st)) + specColor, 1.0);
	}
}