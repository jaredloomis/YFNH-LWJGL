#version 120

uniform vec4 uniformAmbientLight;
uniform mat3 uniformNormalMatrix;
uniform mat4 uniformModelViewMatrix;
uniform vec3 uniformLightPosition;
uniform float uniformShininess;

// The colour that we passed in through the vertex shader.
varying vec4 varyingColour;
// The normal that we passed in through the vertex shader.
varying vec3 varyingNormal;
// The vertex that we passed in through the vertex shader.
varying vec4 varyingVertex;

void main() {
    gl_FragColor = vec4(1, 0, 0, 0);
/**
    vec3 vertexPosition = (uniformModelViewMatrix * varyingVertex).xyz;
    vec3 surfaceNormal = (uniformNormalMatrix * varyingNormal).xyz;
    gl_FragColor += uniformAmbientLight;
    vec3 lightDirection = normalize(uniformLightPosition - vertexPosition);
    float diffuseLightIntensity = max(0, dot(surfaceNormal, lightDirection));
    gl_FragColor = diffuseLightIntensity * varyingColour;
    vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
    float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
    if (diffuseLightIntensity != 0) {
        float fspecular = pow(specular, gl_FrontMaterial.shininess);
        gl_FragColor += fspecular;
    }**/
}