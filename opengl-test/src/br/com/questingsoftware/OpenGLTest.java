package br.com.questingsoftware;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;

public class OpenGLTest implements ApplicationListener {

	private PerspectiveCamera camera;
	private Model model;
	private ModelInstance modelInstance;
	private AssetManager assetManager;
	private ModelBatch modelBatch;
	private Lights lights;

	@Override
	public void create() {
		assetManager = new AssetManager();
		assetManager.load("data/models/cube.g3db", Model.class);
		assetManager.finishLoading();
		model = assetManager.get("data/models/cube.g3db");
		
		modelInstance = new ModelInstance(model);
		
		
		/*for (int i=0; i<model.getSubMesh("Cube").material.getNumberOfAttributes(); i++){
			MaterialAttribute attr = model.getSubMesh("Cube").material.getAttribute(i);
			if (attr instanceof TextureAttribute){
				model.getSubMesh("Cube").material.removeAttribute(attr);
			}
		}*/
		
		/*modelBatch = new ModelBatch(Gdx.files.internal("data/shaders/color-vs.glsl")
				, Gdx.files.internal("data/shaders/color-fs.glsl"));*/
		
		modelBatch = new ModelBatch();
		
		DirectionalLight dLight = new DirectionalLight();
		dLight.set(Color.WHITE, -2f, -0.5f, -1f);
		lights = new Lights(Color.WHITE
				, dLight);
		
		/*if (!shaderProgram.isCompiled()) {
			String msg = "Failure compiling shader program: "
					+ shaderProgram.getLog();
			Gdx.app.log(OpenGLTest.class.getCanonicalName(), msg);
			throw new RuntimeException(msg);
		}
		
		directionalLight = new Vector3(-1,-1,-1).nor();*/
		
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		modelBatch.dispose();
	}

	@Override
	public void render() {
		Gdx.graphics.getGL20().glClearColor(0f, 0f, 0f, 1f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		camera.position.z += 1f * Gdx.graphics.getDeltaTime();
		camera.position.x -= 1f * Gdx.graphics.getDeltaTime();
		camera.position.y += 1f * Gdx.graphics.getDeltaTime();
		camera.update();

		modelBatch.begin(camera);
		
		modelBatch.render(modelInstance,lights);
		
		modelBatch.end();
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
