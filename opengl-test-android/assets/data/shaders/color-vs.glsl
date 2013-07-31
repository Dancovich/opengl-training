#ifdef GL_ES
#define LOWP lowp
#define MEDP mediump
#define HIGP highp
#else
#define LOWP
#define MEDP
#define HIGP
#endif

attribute vec4 a_position;
attribute vec4 a_normal;

uniform mat4 u_projView;
varying vec4 v_normal;

void main() {
	v_normal = a_normal;
	gl_Position = u_projView * a_position;
}