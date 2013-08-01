#ifdef GL_ES 
precision mediump float;
#endif

uniform vec4 diffuseColor;
uniform vec4 specularColor;
uniform vec4 ambientColor;
uniform vec3 u_directional;

varying vec2 v_texCoord0;
varying vec3 v_normal;

void main() {
  gl_FragColor = diffuseColor * max( 0.0 , dot(v_normal , u_directional) );
  //gl_FragColor = vec4(v_normal , 1.0);
}