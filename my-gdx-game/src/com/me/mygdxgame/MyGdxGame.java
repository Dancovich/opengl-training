package com.me.mygdxgame;

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
import com.badlogic.gdx.graphics.g3d.lights.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;

public class MyGdxGame implements ApplicationListener {
	
	//private static final String MODEL_PATH = "data/ship/ship2.obj";
	private static final String MODEL_PATH = "data/sphere.obj";
	
	private PerspectiveCamera camera;
	private AssetManager assetManager;
	
	private Model model;
	private ModelInstance modelInstance;
	private ModelBatch modelBatch;
	
	private Lights lights;
	
	private CustomShader shader;
	
	private float angle = 0;
	
	@Override
	public void create() {
		assetManager = new AssetManager();
		
		assetManager.load(MODEL_PATH, Model.class,new ObjLoader.ObjLoaderParameters(true));
		assetManager.finishLoading();
		
		modelBatch = new ModelBatch();
		
		model = assetManager.get(MODEL_PATH);
		modelInstance = new ModelInstance(model);
		
		PointLight pLight = new PointLight();
		pLight.set(Color.WHITE, 1, 1, 1, 1);
		
		PointLight pLight2 = new PointLight();
		pLight2.set(Color.WHITE, -1, -1, 1, 1);
		
		DirectionalLight dLight = new DirectionalLight();
		dLight.set(Color.WHITE, 1,1,-1);
		
		
		lights = new Lights(Color.WHITE , pLight, pLight2, dLight);
		
		shader = new CustomShader();
		shader.init();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		shader.dispose();
		assetManager.dispose();
	}

	@Override
	public void render() {
		Gdx.graphics.getGL20().glClearColor(0f, 0f, 0f, 1f);
		Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		
		angle += 0.5 * Gdx.graphics.getDeltaTime();
		if (angle>360f){
			angle = 0f;
		}
		
		camera.position.x = (float)(Math.cos(angle) * 5f);
		camera.position.z = (float)(Math.sin(angle) * 5f);
		
		/*camera.position.x = 0f;
		camera.position.z = -5f;*/
		
		camera.lookAt(0f,0f,0f);
		camera.update();
		
		modelBatch.begin(camera);
		
		//modelBatch.render(modelInstance, lights, shader);
		modelBatch.render(modelInstance, lights);
		
		modelBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		camera = new PerspectiveCamera(62, width, height);
		camera.position.z = 5f;
		camera.lookAt(0f,0f,0f);
		camera.update();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
