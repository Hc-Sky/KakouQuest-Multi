package fr.studiokakou.kakouquest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import fr.studiokakou.kakouquest.keybinds.Keybinds;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;

/**
 * La classe GameSpace représente l'application principale du jeu.
 */
public class GameSpace extends Game {
	/**
	 * Le SpriteBatch principal pour dessiner les éléments du jeu.
	 */
	public SpriteBatch batch;
	/**
	 * Le SpriteBatch pour dessiner les éléments de l'HUD (interface utilisateur).
	 */
	public SpriteBatch hudBatch;
	/**
	 * Le temps de démarrage de l'application.
	 */
	public long startTime;

	/**
	 * Méthode appelée lors de la création de l'application.
	 */
	@Override
	public void create() {

		Keybinds.updateKeys();

		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();

		startTime = TimeUtils.millis();


		this.setScreen(new OnlineGameScreen(this));
	}

	/**
	 * Méthode appelée à chaque frame pour afficher le jeu.
	 */
	@Override
	public void render() {
		super.render();
	}

	/**
	 * Méthode appelée lors de la fermeture de l'application pour libérer les ressources.
	 */
	@Override
	public void dispose() {
		batch.dispose();
		hudBatch.dispose();
	}

	/**
	 * Méthode appelée lors du redimensionnement de la fenêtre de l'application.
	 *
	 * @param width  Nouvelle largeur de la fenêtre
	 * @param height Nouvelle hauteur de la fenêtre
	 */
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
