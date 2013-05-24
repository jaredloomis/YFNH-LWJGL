#version 120

attribute vec4 attributeVertex;
attribute vec3 attributeNormal;

uniform vec4 uniformColour;
uniform mat4 uniformModelViewProjectionMatrix;

// The colour we're going to pass to the fragment shader.
varying vec4 varyingColour;
// The normal we're going to pass to the fragment shader.
varying vec3 varyingNormal;
// The vertex we're going to pass to the fragment shader.
varying vec4 varyingVertex;

void main() {
    // Pass the vertex colour attribute to the fragment shader.
    // This value will be interpolated automatically by OpenGL
    // if GL_SHADE_MODEL is GL_SMOOTH. (that's the default)
    varyingColour = attributeColour;
    // Pass the vertex normal attribute to the fragment shader.
    // This value will be interpolated automatically by OpenGL
    // if GL_SHADE_MODEL is GL_SMOOTH. (that's the default)
    varyingNormal = attributeNormal;
    // Pass the vertex position attribute to the fragment shader.
    // This value will be interpolated automatically by OpenGL
    // if GL_SHADE_MODEL is GL_SMOOTH. (that's the default)
    varyingVertex = attributeVertex;
    // Send the vertex position, modified by glTranslate/glRotate/glScale
    // and glOrtho/glFrustum/gluPerspective to primitive assembly.
    gl_Position = uniformModelViewProjectionMatrix * attributeVertex;
}