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

uniform vec4 diffuseColor;
uniform vec4 specularColor;
uniform float shininess;

varying vec4 v_normal;

void main() {
	gl_FragColor = diffuseColor;
}