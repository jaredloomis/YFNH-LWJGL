#version 120

uniform sampler2D[10] textures;

varying vec3 specColor;
varying vec3 diffColor;
varying vec3 ambColor;

varying int texID;

void main() 
{	
	if(texID == -1)
	{	
		gl_FragColor = vec4(ambColor, 1.0) + vec4(diffColor + specColor, 1.0);
	}
	else
	{
		//EDITED
		//gl_FragColor = vec4(ambColor, 1.0) + vec4(diffColor * vec3(texture(textures[texID], gl_TexCoord[texID].st)) + specColor, 1.0);
		gl_FragColor = vec4(ambColor, 1.0) + vec4(diffColor * vec3(texture(textures[texID], gl_TexCoord[0].st)) + specColor, 1.0);
	}
}