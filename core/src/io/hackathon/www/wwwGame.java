package io.hackathon.www;
 import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
 import com.badlogic.gdx.graphics.g2d.Sprite;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
 import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
 import org.w3c.dom.css.Rect;


public class wwwGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	private int isfirst;
	private long lastLevelTime;
	private int speed;
	private OrthographicCamera camera;
	Texture img;
	private Rectangle lastDead;
	private Texture dropImage;
	private Texture bucketImage;
	private Texture deadImage;
	private Texture advancedImage;
	private Music rainMusic;
	private Sound dropSound;
	private Rectangle bucket;
	private Random rand;
	private GameLogic gameLogic;
	private Texture levelImage;
	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	private Rectangle mytrack;
	private ShapeRenderer shapeRenderer;
	private Array<Long> levelupevents;
	private Array<Rectangle> advancedMosquitos;
	private Array<Rectangle> raindrops;
	private Array<Rectangle> dieMosquitos;
	private Array<Texture> tracks;
	private int randomflag;
	private long lastDropTime;
	private final ScreenActivity screens;

	public wwwGame(ScreenActivity screens){
		this.screens = screens;
		gameLogic = new GameLogic("小房子");
	}

	@Override
	public void create () {
		font = new BitmapFont();
		tracks = new Array<Texture>();
		Texture track1 = new Texture("track1.png");
		Texture track2 = new Texture("track2.png");
		tracks.add(track1);
		tracks.add(track2);
		speed = 1;

		randomflag = 0;
		lastDead = new Rectangle();
		lastDead.x = 0;
		lastDead.y = 0;
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		img = new Texture("badlogic.jpg");
		dropImage = new Texture("Mosquito.png");
		bucketImage = new Texture("bucket.png");
		deadImage = new Texture("dieMosquito.png");
		advancedImage = new Texture("advancedMos.png");
		levelImage = new Texture("levelup.png");
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("bgm.mp3"));
		dropSound = Gdx.audio.newSound(Gdx.files.internal(("slap.mp3")));
		isfirst = 1;
		rainMusic.setLooping(true);
		rainMusic.setVolume((float)0.5);
		rainMusic.play();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		bucket = new Rectangle();
		bucket.x = 0;
		bucket.y = 0;
		bucket.width = 36;
		bucket.height = 36;
		rand = new Random();

		backgroundTexture = new Texture("background.jpg");
		backgroundSprite =new Sprite(backgroundTexture);

		raindrops = new Array<Rectangle>();
		dieMosquitos = new Array<Rectangle>();
		advancedMosquitos = new Array<Rectangle>();
		levelupevents =new Array<Long>();
		screens.pushMessage(1);
		spawnRaindrop();
	}

	public Rectangle randomPath(Rectangle rect) {
		Random boolrant = new Random();
		int value1 = boolrant.nextInt(1);
		int value2 = boolrant.nextInt(1);
		rect.width = rect.width * Gdx.graphics.getDeltaTime() * (float) 2.5;
		rect.height = rect.height * Gdx.graphics.getDeltaTime() * (float) 2.5;
		rect.x += Math.pow(-1, (double)value1) * 200 * Gdx.graphics.getDeltaTime() - 64/2;
		rect.y += Math.pow(-1, (double)value2) * 200 * Gdx.graphics.getDeltaTime() - 64/2;
		return rect;
	}

	private void spawnRaindrop() {
		for (int i = 0; i < 4; i++) {
			Rectangle raindrop = new Rectangle();
			raindrop.x = MathUtils.random(0, 800 - 64);
			raindrop.y = MathUtils.random(0, 480 - 64);
			raindrop.width = 32;
			raindrop.height = 32;
			raindrops.add(raindrop);
		}
		lastDropTime = TimeUtils.nanoTime();
	}

	private void spawnAdvanced() {
		Rectangle mos = new Rectangle();
		mos.x = MathUtils.random(0, 800 - 64);
		mos.y = MathUtils.random(0, 480);
		mos.width = 64;
		mos.height = 64;
		advancedMosquitos.add(mos);
	}

	public void levelup() {
		for (Long lastTime: levelupevents) {
			if(Math.abs(lastTime - TimeUtils.nanoTime()) < 2000000000) {
				batch.draw(levelImage, 350, 240, 100, 60);
			} else {
				levelupevents.pop();
			};
		}
	}

	@Override
	public void render () {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		// Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (gameLogic.getHealth() < 0) {
			// todo sth
			screens.finish(gameLogic.getScore());
		}

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops

		batch.begin();
		backgroundSprite.draw(batch);

		if (dieMosquitos.size > 0) {
			Rectangle justdead = dieMosquitos.get(dieMosquitos.size - 1);
			if (lastDead.x != justdead.x || lastDead.y != justdead.y) {
				lastDead.x = justdead.x;
				lastDead.y = justdead.y;
				randomflag = rand.nextInt(2);
			}
			batch.draw(tracks.get(randomflag), justdead.x - 20, justdead.y - 20, justdead.width, justdead.height);
		}
		for (Rectangle dead: dieMosquitos) {
			batch.draw(deadImage, dead.x, dead.y, dead.width, dead.height);
		}
		for (Rectangle raindrop: raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y, raindrop.width, raindrop.height);
		}
		for (Rectangle mosquito: advancedMosquitos) {
			batch.draw(advancedImage, mosquito.x, mosquito.y, mosquito.width, mosquito.height);
		}

		font.draw(batch, "LV: 8" + "   Score:" + String.valueOf(gameLogic.getScore()), 20, 80, 200, 0, true);

		if (gameLogic.getScore() > 100 && isfirst > 0) {
			isfirst = 0;
			levelupevents.add(TimeUtils.nanoTime());
		}
		levelup();
		batch.end();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0xa4 / 255, 0xd8 / 255, 0xc7 / 255, 1);
		shapeRenderer.rect(50, 20, 50 + 200, 28);
		shapeRenderer.setColor((float)0x0d/255, (float)0x74/255, (float)0x59/255, 0xff/255);
		shapeRenderer.rect(50, 20, 50 + (int) (200 * (float)gameLogic.getHealth() / 2333), 28);
		shapeRenderer.end();

		// process user input
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
			bucket.y = touchPos.y - 64 / 2;
		}

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
			spawnRaindrop();
			spawnAdvanced();
		}

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we play back
		// a sound effect as well.
		Iterator<Rectangle> iter = raindrops.iterator();
		while(iter.hasNext()) {
			Rectangle rect = iter.next();
			int randValue = rand.nextInt(10);
			int yrandValue =rand.nextInt(10);
			rect.x -= (int) Math.pow(-1, randValue) * 200 * Gdx.graphics.getDeltaTime();
			rect.y -= (int) Math.pow(-1, yrandValue) * 200 * Gdx.graphics.getDeltaTime();
			rect.width = rect.width + rect.width * (float) 1 * Gdx.graphics.getDeltaTime();
			rect.height = rect.height + rect.height * (float) 1 * Gdx.graphics.getDeltaTime();
			if(rect.y + 64 < 0
					|| rect.y - 64 > 480
					|| rect.x < 0
					|| rect.x > 800
					|| rect.width > 128
					|| rect.height > 128) {
				iter.remove();
				gameLogic.skipmosq(1);
			}
			else if(rect.contains(bucket)) {
				dropSound.play((float)1.0);
				dieMosquitos.add(rect);
				gameLogic.killmosq(1);
				if (dieMosquitos.size >= 10) {
					dieMosquitos.removeIndex(0);
				}
				iter.remove();
			}
		}
		iter = advancedMosquitos.iterator();
		while(iter.hasNext()) {
			Rectangle rect = iter.next();
			int randValue = rand.nextInt();
			int yrandValue =rand.nextInt();
			rect.x -= Math.pow(-1, randValue) * 500 * Gdx.graphics.getDeltaTime();
			rect.y -= Math.pow(-1, yrandValue) * 500 * Gdx.graphics.getDeltaTime();
			rect.width = rect.width + rect.width * (float) 2 * Gdx.graphics.getDeltaTime();
			rect.height = rect.height + rect.height * (float) 2 * Gdx.graphics.getDeltaTime();
			if(rect.y + 64 < 0
					|| rect.y - 64 > 480
					|| rect.x < 0
					|| rect.x > 800
					|| rect.width > 128
					|| rect.height > 128) {
				gameLogic.skipmosq(2);
				iter.remove();
			}
			else if(rect.contains(bucket)) {
				dropSound.play((float)1.0);
				dieMosquitos.add(rect);
				gameLogic.killmosq(2);
				if (dieMosquitos.size >= 10) {
					dieMosquitos.removeIndex(0);
				}
				iter.remove();
			}
		}
		bucket.x = 0;
		bucket.y = 0;
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		font.dispose();
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
