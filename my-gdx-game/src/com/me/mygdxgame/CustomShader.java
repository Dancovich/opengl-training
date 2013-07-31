package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class CustomShader implements Shader {

	private ShaderProgram program;

	private static final String U_WORLD_TRANSFORM = "u_worldTrans";
	private static final String U_PROJECTION_MATRIX = "u_projTrans";
	
	private Camera camera;
	private RenderContext context;
	
	private int uWorldTransformLocation;
	private int uProjectionMatrixLocation;

	@Override
	public void dispose() {
		program.dispose();
	}

	@Override
	public void init() {
		FileHandle vertexFile = Gdx.files.internal("shaders/vertex.glsl");
		FileHandle fragmentFile = Gdx.files
				.internal("shaders/fragment.glsl");

		program = new ShaderProgram(vertexFile, fragmentFile);

		if (!program.isCompiled())
			throw new GdxRuntimeException(program.getLog());
		
		uWorldTransformLocation = program.getUniformLocation(U_WORLD_TRANSFORM);
		uProjectionMatrixLocation = program.getUniformLocation(U_PROJECTION_MATRIX);
	}

	@Override
	public int compareTo(Shader other) {
		return 0;
	}

	@Override
	public boolean canRender(Renderable instance) {
		return true;
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		this.camera = camera;
		this.context = context;

		program.begin();
		program.setUniformMatrix(uProjectionMatrixLocation, this.camera.combined);
		this.context.setDepthTest(true, GL20.GL_LEQUAL);
		this.context.setCullFace(GL20.GL_BACK);
	}

	@Override
	public void render(Renderable renderable) {
		program.setUniformMatrix(uWorldTransformLocation, renderable.worldTransform);
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
