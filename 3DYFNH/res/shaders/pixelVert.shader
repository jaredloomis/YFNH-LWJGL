#version 120

// The colour we're going to pass to the fragment shader.
varying vec4 varyingColour;

// The normal we're going to pass to the fragment shader.
varying vec3 varyingNormal;

// The vertex we're going to pass to the fragment shader.
varying vec4 varyingVertex;

void main()
{
    // Pass the vertex colour attribute to the fragment shader.
    varyingColour = gl_Color;
    
    // Pass the vertex normal attribute to the fragment shader.
    varyingNormal = gl_Normal;
    
    // Pass the vertex position attribute to the fragment shader.
    varyingVertex = gl_Vertex;
    
    // Send the vertex position, modified by glTranslate/glRotate/glScale
    // and glOrtho/glFrustum/gluPerspective to primitive assembly.
    //gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}