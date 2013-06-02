#version 120

uniform sampler2D[10] textures;
uniform sampler2D texture1;

varying vec3 varyingColour;

varying vec3 specColor;
varying vec3 diffColor;
varying vec3 ambColor;

varying int texID;

void main() 
{	
	if(texID < 0)
	{	
		gl_FragColor = vec4(ambColor, 1.0) + vec4(diffColor + specColor, 1.0);
	}
	else
	{
		vec3 textureColor = vec3(texture(textures[texID], gl_TexCoord[texID].st));
		gl_FragColor = vec4(ambColor, 1.0) + vec4(diffColor * textureColor + specColor, 1.0);
	}
}