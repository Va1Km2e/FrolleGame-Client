package ee.taltech.fruits.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class NameScreen extends ApplicationAdapter implements Input.TextInputListener{
    String text;

    @Override
    public void render() {
        if(Gdx.input.justTouched()){
            Gdx.input.getTextInput(this, "Title", "Default text", "");
        } else {
            Gdx.app.log("Text", text);
        }

    }


    @Override
    public void input(String s) {
        this.text = text;
    }

    @Override
    public void canceled() {
        text = "Cancelled";
    }
}