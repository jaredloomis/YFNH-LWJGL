#version 120

uniform sampler2D texture1;

varying vec3 varyingColour;

varying int texID;

void main() 
{	
	gl_FragColor = vec4(1, 1, 1, 1);
	if(texID == 0)
	{
    	gl_FragColor = texture2D(texture1, gl_TexCoord[0].st);
    	//gl_FragColor = vec4(varyingColour * vec3(texture2D(texture1, gl_TexCoord[0].st)), 1.0);	
    }
    gl_FragColor *= vec4(varyingColour, 1);
}