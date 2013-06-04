#version 120

varying vec4 varyingColour;

varying vec3 varyingNormal;

varying vec4 varyingVertex;

varying int texID;

attribute int textureID;

void main()
{
	texID = textureID;
	
    // Pass the vertex colour attribute to the fragment shader.
    varyingColour = gl_Color;
    
    // Pass the vertex normal attribute to the fragment shader.
    varyingNormal =  gl_Normal;
    
    // Pass the vertex position attribute to the fragment shader.
    varyingVertex = gl_Vertex;
    
    //Set gl_Position
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    
    //Set texture coords
    gl_TexCoord[0] = gl_MultiTexCoord0;
}