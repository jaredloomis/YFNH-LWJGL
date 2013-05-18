#version 120

// Per-vertex Phong lighting model

uniform sampler2D texture1;

varying vec3 varyingColour;

void main() 
{
    gl_FragColor = texture2D(texture1, gl_TexCoord[0].st);
    
    // Turns the varying color into a 4D color and stores in the built-in output gl_FragColor.
    //gl_FragColor += vec4(varyingColour, 1);
    //float amb = 0.1;
    //gl_FragColor += vec4(amb, amb, amb, 1);
}

//gl_FragColor = texture2D(texture1, gl_TexCoord[0].st);