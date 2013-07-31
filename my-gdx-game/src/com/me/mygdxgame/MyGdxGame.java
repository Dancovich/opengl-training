package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.lights.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

public class MyGdxGame implements ApplicationListener {
	
	//private static final String MODEL_PATH = "data/ship/ship2.obj";
	private static final String MODEL_PATH = "data/sphere.obj";
	
	private PerspectiveCamera camera;
	private AssetManager assetManager;
	
	private Model model;
	private ModelInstance modelInstance;
	
	private Lights lights;
	
	private RenderContext context;
	private CustomShader shader;
	private Renderable renderable;
	
	private float angle = 0;
	
	@Override
	public void create() {
		assetManager = new AssetManager();
		
		assetManager.load(MODEL_PATH, Model.class,new ObjLoader.ObjLoaderParameters(true));
		assetManager.finishLoading();
		
		model = assetManager.get(MODEL_PATH);
		modelInstance = new ModelInstance(model);
		
		PointLight pLight = new PointLight();
		pLight.set(Color.WHITE, 1, 1, 1, 1);
		
		PointLight pLight2 = new PointLight();
		pLight2.set(Color.WHITE, -1, -1, 1, 1);
		
		DirectionalLight dLight = new DirectionalLight();
		dLight.set(Color.WHITE, 1,1,-1);
		
		
		lights = new Lights(Color.WHITE , pLight, pLight2, dLight);
		
		NodePart part = modelInstance.nodes.first().parts.first();
		renderable = new Renderable();
		
		part.setRenderable(renderable);
		
		renderable.lights = lights;
		renderable.worldTransform.idt();
		
		context = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED,1));
		shader = new CustomShader();
		shader.init();
	}

	@Override
	public void dispose() {
		shader.dispose();
		assetManager.dispose();
	}

	@Override
	public void render() {
		Gdx.graphics.getGL20().glClearColor(0f, 0f, 0f, 1f);
		Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		
		angle += 5 * Gdx.graphics.getDeltaTime();
		if (angle>360f){
			angle = 0f;
		}
		
		camera.position.x = (float)(Math.cos(angle) * 5f);
		camera.position.z = (float)(Math.sin(angle) * 5f);
		
		camera.lookAt(0f,0f,0f);
		camera.update();
		
		context.begin();
		shader.begin(camera, context);
		
		shader.render(renderable);
		
		shader.end();
		context.end();
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
