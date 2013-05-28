#version 120

uniform sampler2D texture1;

varying vec3 varyingColour;

varying vec3 specColor;
varying vec3 diffColor;
varying vec3 ambColor;

varying float texID;

void main() 
{	
	gl_FragColor = vec4(varyingColour, 1);
	
	if(texID == 0)
	{
		gl_FragColor = vec4(ambColor, 1.0) + vec4(diffColor * vec3(texture2D(texture1, gl_TexCoord[0].st)) + specColor, 1.0);
    	//gl_FragColor += texture2D(texture1, gl_TexCoord[0].st);
	}
}