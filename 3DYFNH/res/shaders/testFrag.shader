#version 120

varying vec3 varyingColour;

void main() 
{   
    // Turns the varying color into a 4D color and stores in the built-in output gl_FragColor.
    gl_FragColor = vec4(varyingColour, 1);
}