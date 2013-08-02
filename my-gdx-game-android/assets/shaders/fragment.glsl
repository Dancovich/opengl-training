#ifdef GL_ES 
precision mediump float;
#endif

uniform vec4 diffuseColor;
uniform vec4 ambientColor;
uniform vec4 specularColor;
uniform vec3 u_directional;
uniform vec3 u_cameraPos;

uniform sampler2D u_texture;
uniform sampler2D u_normal;

varying vec2 v_texCoord0;
varying vec3 v_normal;
varying vec3 v_position;
varying float v_shininess;

void main() {

  //Calcula a cor difusa final a partir do ângulo com a luz
  vec4 diffuse = max( 0.0 , dot(v_normal , u_directional) ) * diffuseColor;

  //Calcula vetores da superfície a luz e da reflexão da luz
  vec3 surfaceToLight = normalize(u_directional - v_position);
  vec3 surfaceToCamera = normalize(u_cameraPos - v_position);
  
  //Calcula cor especular
  vec3 incidenceVector = -surfaceToLight;
  vec3 reflectionVector = reflect(incidenceVector, v_normal);
  float cosAngle = max(0.0, dot(surfaceToCamera, reflectionVector));
  float specularCoefficient = pow(cosAngle, v_shininess);
  vec4 specular = specularCoefficient * specularColor;
  
  //Pega cor da textura
  vec4 texColor = texture2D(u_texture , v_texCoord0);

  gl_FragColor = diffuse + texColor + specular;
  //gl_FragColor = vec4(v_normal , 1.0);
}