package br.com.questingsoftware;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class OpenGLTest implements ApplicationListener {

	private PerspectiveCamera camera;
	private Model model;
	private AssetManager assetManager;
	private ShaderProgram shaderProgram;
	private Vector3 directionalLight;

	@Override
	public void create() {
		assetManager = new AssetManager();
		assetManager.load("data/models/cube.g3db", Model.class);
		assetManager.finishLoading();
		model = assetManager.get("data/models/cube.g3db");
		
		/*for (int i=0; i<model.getSubMesh("Cube").material.getNumberOfAttributes(); i++){
			MaterialAttribute attr = model.getSubMesh("Cube").material.getAttribute(i);
			if (attr instanceof TextureAttribute){
				model.getSubMesh("Cube").material.removeAttribute(attr);
			}
		}*/
		
		shaderProgram = new ShaderProgram(
				Gdx.files.internal("data/shaders/color-vs.glsl"),
				Gdx.files.internal("data/shaders/color-fs.glsl"));

		if (!shaderProgram.isCompiled()) {
			String msg = "Failure compiling shader program: "
					+ shaderProgram.getLog();
			Gdx.app.log(OpenGLTest.class.getCanonicalName(), msg);
			throw new RuntimeException(msg);
		}
		
		directionalLight = new Vector3(-1,-1,-1).nor();
		
	}

	@Override
	public void dispose() {
		model.dispose();
	}

	@Override
	public void render() {
		Gdx.graphics.getGL20().glClearColor(1, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		camera.position.z += 1f * Gdx.graphics.getDeltaTime();
		camera.position.x -= 1f * Gdx.graphics.getDeltaTime();
		camera.position.y += 1f * Gdx.graphics.getDeltaTime();
		camera.update();

		shaderProgram.begin();

		shaderProgram.setUniformMatrix("u_projView", camera.combined);
		
		for (Material m : model.materials){
			ColorAttribute  attr = (ColorAttribute)m.get(ColorAttribute.Diffuse);
			shaderProgram.setUniformf("diffuseColor", attr.color);
			
			attr = (ColorAttribute)m.get(ColorAttribute.Specular);
			shaderProgram.setUniformf("specularColor", attr.color);
		}
		
		shaderProgram.setUniformf("v_eye", camera.position.x , camera.position.y , camera.position.z , 1f);
		shaderProgram.setUniformf("v_directLight", directionalLight.x , directionalLight.y , directionalLight.z , 1f);
		
		for (Mesh mesh : model.meshes){
			mesh.render(shaderProgram, GL20.GL_TRIANGLES);
		}
		
		
		shaderProgram.end();
	}

	@Override
	public void resize(int width, int height) {
		camera = new PerspectiveCamera(45, width, height);
		camera.position.z = 6f;
		camera.lookAt(0f, 0f, 0f);
		camera.update();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
