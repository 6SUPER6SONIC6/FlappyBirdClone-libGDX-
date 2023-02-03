package com.supersonic.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlappyBirdClone extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;

	Texture[] bird;
	int birdStateFlag = 0;
	float flyHeight;
	float fallingSpeed = 0;
	int gameStateFlag = 0;
	Texture topTube;
	Texture bottomTube;
	int spaceBetweenTubes = 500;
	Random random;

	int tubeSpeed = 5;
	int tubesNumber = 5;
	float tubeX[] = new	float[tubesNumber];
	float tubeShift[] = new	float[tubesNumber];
	float distanceBetweenTubes;

	Circle birdHitBox;
	Rectangle[] tobTubeHitBoxes;
	Rectangle[] bottomTubeHitBoxes;

	int gameScore = 0;
	int passedTubeIndex = 0;

	BitmapFont scoreFont;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");


		birdHitBox = new Circle();
		tobTubeHitBoxes = new Rectangle[tubesNumber];
		bottomTubeHitBoxes = new Rectangle[tubesNumber];

		bird = new Texture[2];
		bird[0] = new Texture("bird_wings_up.png");
		bird[1] = new Texture("bird_wings_down.png");
		flyHeight = Gdx.graphics.getHeight() / 2 - bird[0].getHeight() / 2;

		topTube = new Texture("top_tube.png");
		bottomTube = new Texture("bottom_tube.png");

		random = new Random();

		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.BLACK);
		scoreFont.getData().setScale(10);

		distanceBetweenTubes = Gdx.graphics.getWidth() / 2;

		for (int i = 0; i < tubesNumber; i++) {
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() /2 + i * distanceBetweenTubes;
			tubeShift[i] = (random.nextFloat() - 0.5f) *
					(Gdx.graphics.getHeight() - spaceBetweenTubes - 700);

			tobTubeHitBoxes[i] = new Rectangle();
			bottomTubeHitBoxes[i] = new Rectangle();

		}


	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(Gdx.input.justTouched()){
			Gdx.app.log("Tap", "Success");
			gameStateFlag = 1;
		}

		if (gameStateFlag == 1){

			Gdx.app.log("gameScore", String.valueOf(gameScore));

			if (tubeX[passedTubeIndex] < Gdx.graphics.getWidth() / 2){
				gameScore++;

				if (passedTubeIndex < tubesNumber - 1){
					passedTubeIndex++;
				} else {
					passedTubeIndex = 0;
				}
			}

			if(Gdx.input.justTouched()){
				fallingSpeed = -30;
			}

			if (flyHeight > 0 || fallingSpeed < 0){
				fallingSpeed++;
				flyHeight -= fallingSpeed;
			}

		} else {
			if(Gdx.input.justTouched()){
				Gdx.app.log("Tap", "Success");
				gameStateFlag = 1;
			}
		}

		for (int i = 0; i < tubesNumber; i++) {

			if (tubeX[i] < -topTube.getWidth()){
				tubeX[i] = tubesNumber * distanceBetweenTubes;
			} else {
				tubeX[i] -= tubeSpeed;
			}


			batch.draw(topTube, tubeX[i],
					Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i]);
			batch.draw(bottomTube, tubeX[i],
					Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i]);

			tobTubeHitBoxes[i] = new Rectangle(tubeX[i],
					Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i],
					topTube.getWidth(), topTube.getHeight());

			bottomTubeHitBoxes[i] = new Rectangle(tubeX[i],
					Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i],
					bottomTube.getWidth(), bottomTube.getHeight());
		}


		if (birdStateFlag == 0){
			birdStateFlag = 1;
		} else {
			birdStateFlag = 0;
		}


		batch.draw(bird[birdStateFlag], Gdx.graphics.getWidth() / 2 - bird[birdStateFlag].getWidth() / 2,
				flyHeight);

		scoreFont.draw(batch, String.valueOf(gameScore), 100, 200);

		batch.end();

		birdHitBox.set(Gdx.graphics.getWidth() / 2, flyHeight + bird[birdStateFlag].getHeight() / 2,
				bird[birdStateFlag].getWidth() / 2);

		for (int i = 0; i < tubesNumber; i++) {

			if (Intersector.overlaps(birdHitBox, tobTubeHitBoxes[i]) || Intersector.overlaps(birdHitBox, bottomTubeHitBoxes[i])){
				Gdx.app.log("Intersected", "Bump");
			}
		}
	}
	
	@Override
	public void dispose () {

	}
}
