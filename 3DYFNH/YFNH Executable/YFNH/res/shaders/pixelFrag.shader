#version 120

// The colour that we passed in through the vertex shader.
varying vec4 varyingColour;
// The normal that we passed in through the vertex shader.
varying vec3 varyingNormal;
// The vertex that we passed in through the vertex shader.
varying vec4 varyingVertex;

void main()
{
    vec3 vertexPosition = (gl_ModelViewMatrix * varyingVertex).xyz;
    //vec3 vertexPosition = (gl_ProjectionMatrix * varyingVertex).xyz;
    vec3 surfaceNormal = normalize((gl_NormalMatrix * varyingNormal).xyz);
    vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);
    float diffuseLightIntensity = max(0, dot(surfaceNormal, lightDirection));
    gl_FragColor.rgb = diffuseLightIntensity * varyingColour.rgb;
    gl_FragColor += gl_LightModel.ambient;
    vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
    float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
    
    if (diffuseLightIntensity != 0)
    {
        float fspecular = pow(specular, gl_FrontMaterial.shininess);
        gl_FragColor += fspecular;
    }
}