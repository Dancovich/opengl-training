#ifdef GL_ES
#define LOWP lowp
#define MEDP mediump
#define HIGP highp
precision lowp float;
#else
#define LOWP
#define MEDP
#define HIGP
#endif

varying vec2 v_texCoords;

uniform vec4 diffuseColor;
uniform vec4 specularColor;
uniform vec4 v_eye;
uniform vec4 v_directLight;

void main() {
	gl_FragColor = diffuseColor + (specularColor * normalize(v_eye * v_directLight));
}