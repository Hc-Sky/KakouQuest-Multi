package fr.studiokakou.kakouquest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.studiokakou.kakouquest.GameSpace;

public class UsernameScreen implements Screen {

    public static String username = "guest";

    GameSpace game;

    SpriteBatch hudBatch;

    // Police avec échelle ajustée
    BitmapFont scaledFont;

    public UsernameScreen(GameSpace game) {
        this.game = game;
        hudBatch = game.hudBatch;

        // Ajuster l'échelle de la police
        scaledFont = new BitmapFont(); // Utilisez votre police actuelle ici si elle est différente
        scaledFont.getData().setScale(3.0f); // Agrandir la police par un facteur de 3
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ENTER && username.length() > 3) {
                    game.setScreen(new ServerAdressScreen(game));
                } else if (keycode == Input.Keys.BACKSPACE) {
                    if (UsernameScreen.username.length() > 0) {
                        UsernameScreen.username = UsernameScreen.username.substring(0, UsernameScreen.username.length() - 1);
                    }
                } else if (keycode == Input.Keys.SPACE) {
                    UsernameScreen.username += " ";
                } else if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                } else if (username.length() < 20) {
                    String character = getAzertyCharacter(keycode);
                    if (character != null) {
                        UsernameScreen.username += character;
                    }
                }
                return true;
            }
        });
    }

    private String getAzertyCharacter(int keycode) {
        switch (keycode) {
            case Input.Keys.A: return "q";
            case Input.Keys.Q: return "a";
            case Input.Keys.W: return "z";
            case Input.Keys.Z: return "w";
            case Input.Keys.M: return ",";
            case Input.Keys.SEMICOLON: return "m";
            case Input.Keys.LEFT_BRACKET: return "^";
            case Input.Keys.RIGHT_BRACKET: return "$";
            case Input.Keys.BACKSLASH: return "*";
            case Input.Keys.EQUALS: return "=";
            case Input.Keys.SHIFT_LEFT: return "";
            case Input.Keys.SHIFT_RIGHT: return "";
            case Input.Keys.CONTROL_LEFT: return "";
            case Input.Keys.CONTROL_RIGHT: return "";
            case Input.Keys.TAB: return "";
            case Input.Keys.ALT_LEFT: return "";
            case Input.Keys.ALT_RIGHT: return "";
            case Input.Keys.NUMPAD_0: return "0";
            case Input.Keys.NUMPAD_1: return "1";
            case Input.Keys.NUMPAD_2: return "2";
            case Input.Keys.NUMPAD_3: return "3";
            case Input.Keys.NUMPAD_4: return "4";
            case Input.Keys.NUMPAD_5: return "5";
            case Input.Keys.NUMPAD_6: return "6";
            case Input.Keys.NUMPAD_7: return "7";
            case Input.Keys.NUMPAD_8: return "8";
            case Input.Keys.NUMPAD_9: return "9";
            case Input.Keys.UP: return "";
            case Input.Keys.DOWN: return "";
            case Input.Keys.LEFT: return "";
            case Input.Keys.RIGHT: return "";
            case Input.Keys.FORWARD_DEL: return "";
            case Input.Keys.INSERT: return "";
            case Input.Keys.END: return "";
            case Input.Keys.NUMPAD_ADD: return "+";
            case Input.Keys.NUMPAD_SUBTRACT: return "-";
            case Input.Keys.NUMPAD_MULTIPLY: return "*";
            case Input.Keys.NUMPAD_DIVIDE: return "/";
            case Input.Keys.CAPS_LOCK: return "";
            case Input.Keys.PAGE_DOWN: return "";
            case Input.Keys.PAGE_UP: return "";
            case Input.Keys.HOME: return "";
            case Input.Keys.SYM: return "CLIQUEZ PAS LA!";
            case Input.Keys.PRINT_SCREEN: return "";
            case Input.Keys.PAUSE: return "";

            default:
                return Input.Keys.toString(keycode).toLowerCase();
        }
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(34 / 255f, 34 / 255f, 34 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        hudBatch.begin();

        scaledFont.draw(hudBatch, "Entrez votre nom d'utilisateur", (float) Gdx.graphics.getWidth() / 2 - 240, (float) Gdx.graphics.getHeight() /2);
        scaledFont.draw(hudBatch, "Username : " + UsernameScreen.username + "|", (float) Gdx.graphics.getWidth() / 2 - 125, (float) Gdx.graphics.getHeight() / 2 - 50);
        scaledFont.draw(hudBatch, "Le nom d'utilisateur doit faire au moins 3 caractères", (float) Gdx.graphics.getWidth() / 2 - 450, (float) Gdx.graphics.getHeight() / 2 - 100);

        hudBatch.end();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        scaledFont.dispose(); // Disposez de la nouvelle police lors de la destruction de l'écran
    }
}