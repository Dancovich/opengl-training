package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Esse Shader customizado é usado ao invés do padrão do libGDX
 * 
 * 
 *
 */
public class CustomShader implements Shader {
	




	private ShaderProgram program;

	private static final String U_WORLD_TRANSFORM = "u_worldTrans";
	private static final String U_PROJECTION_MATRIX = "u_projTrans";
	private static final String U_DIRECTIONAL = "u_directional";
	private static final String U_CAMERA_POS = "u_cameraPos";
	private static final String U_TEXTURE = "u_texture";
	
	private Camera camera;
	private RenderContext context;
	
	/*
	 * Armazena como um inteiro o apontador para essas variáveis no shader GLSL  
	 */
	private int uWorldTransformLocation;
	private int uProjectionMatrixLocation;
	private int diffuseColorLocation;
	private int specularColorLocation;
	private int directionalLightLocation;
	private int cameraPosLocation;
	private int textureLocation;

	@Override
	public void dispose() {
		program.dispose();
	}

	@Override
	public void init() {
		FileHandle vertexFile = Gdx.files.internal("shaders/vertex.glsl");
		FileHandle fragmentFile = Gdx.files.internal("shaders/fragment.glsl");

		program = new ShaderProgram(vertexFile, fragmentFile);

		if (!program.isCompiled())
			throw new GdxRuntimeException(program.getLog());
		
		//Faz apenas uma vez o lookup do nome dos uniformes no shader para acelerar o acesso 
		uWorldTransformLocation = program.getUniformLocation(U_WORLD_TRANSFORM);
		uProjectionMatrixLocation = program.getUniformLocation(U_PROJECTION_MATRIX);
		diffuseColorLocation = program.getUniformLocation(ColorAttribute.DiffuseAlias);
		specularColorLocation = program.getUniformLocation(ColorAttribute.SpecularAlias);
		directionalLightLocation = program.getUniformLocation(U_DIRECTIONAL);
		cameraPosLocation = program.getUniformLocation(U_CAMERA_POS);
		textureLocation = program.getUniformLocation(U_TEXTURE);
	}

	@Override
	public int compareTo(Shader other) {
		return 0;
	}

	@Override
	public boolean canRender(Renderable instance) {
		//É necessário haver pelo menos uma cor difusa para renderizar essa parte do modelo
		return instance.material.has(ColorAttribute.Diffuse);
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		this.camera = camera;
		this.context = context;

		program.begin();
		
		//A matriz de projeção não muda durante os passos do render, definimos aqui para evitar repetição
		program.setUniformMatrix(uProjectionMatrixLocation, this.camera.combined);
		
		//Precisamos da posição da câmera para calcular a luz especular
		program.setUniformf(cameraPosLocation,this.camera.position);
		
		//Definimos também algumas propriedades globais do OpenGL
		this.context.setDepthTest(true, GL20.GL_LEQUAL);
		this.context.setCullFace(GL20.GL_BACK);
	}

	@Override
	public void render(Renderable renderable) {
		
		//Matriz de transformação que mapeia o plano cartesiano local do objeto para o global do OpenGL
		program.setUniformMatrix(uWorldTransformLocation, renderable.worldTransform);
		
		//Definimos a cor dessa geometria. Opcionalmente definimos a cor difusa se houver
		ColorAttribute diffuseColor = (ColorAttribute) renderable.material.get(ColorAttribute.Diffuse);
		program.setUniformf(diffuseColorLocation, diffuseColor.color);
		
		if (renderable.material.has(ColorAttribute.Specular)){
			ColorAttribute specColor = (ColorAttribute) renderable.material.get(ColorAttribute.Specular);
			program.setUniformf(specularColorLocation, specColor.color);
		}
		
		//Se foram definidas luzes para a cena, usa essas luzes
		if (renderable.lights!=null){
			program.setUniformf(directionalLightLocation, renderable.lights.directionalLights.first().direction);
		}
		
		//Aplica a textura
		if (renderable.material.has(TextureAttribute.Diffuse)){
			TextureAttribute texAttr = (TextureAttribute)renderable.material.get(TextureAttribute.Diffuse);
			texAttr.textureDescription.texture.bind(0);
			program.setUniformi(textureLocation, 0);
		}
		
		/*if (diffuseColor.color.a!=1f){
			this.context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}
		else{
			this.context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}*/
		
		//Renderizamos a geometria
		renderable.mesh.render(program
				, renderable.primitiveType
				, renderable.meshPartOffset
				, renderable.meshPartSize);
	}

	@Override
	public void end() {
		program.end();
	}

}
