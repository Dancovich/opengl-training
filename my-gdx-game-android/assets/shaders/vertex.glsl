attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
attribute float shininess;

uniform mat4 u_worldTrans;
uniform mat4 u_projTrans;

varying float v_shininess;
varying vec3 v_position;
varying vec3 v_normal;
varying vec2 v_texCoord0;
varying vec3 v_surfaceToLight;
varying vec3 v_surfaceToCamera;

void main() {
  //Passa a normal e as coordenadas UV para o fragment shader
  v_position = a_position;
  v_normal = a_normal;
  v_texCoord0 = a_texCoord0;
  v_shininess = shininess;
  
  gl_Position = u_projTrans * u_worldTrans * vec4(a_position, 1.0);
}