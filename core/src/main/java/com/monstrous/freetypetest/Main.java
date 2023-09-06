package com.monstrous.freetypetest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private BitmapFont ftFont;
    private int width;
    private int height;
    private GlyphLayout titleGlyphs;
    private float titleWidth, titleHeight;
    private Viewport viewport;

    static private final String TITLE = "WORD!";


    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new ScreenViewport();
    }

    private void generateFont(int screenWidth) {
        FileHandle fontFile = Gdx.files.internal("font/Halloween Fright.ttf");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        generator.setMaxTextureSize(2048);      // allow for big font sizes

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = screenWidth/8;    // scale the font to the screen width
        parameter.color.set(Color.ORANGE);
        parameter.flip = false;
        parameter.genMipMaps = false;
        parameter.characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!";
        parameter.borderColor.set(Color.RED);
        parameter.borderWidth = 3;
        parameter.borderGamma = 0.5f;


        // this breaks on teavm up to version b6
        parameter.shadowOffsetX = 5;
        parameter.shadowOffsetY = 5;
        parameter.shadowColor = Color.PURPLE;

        try {
            FreeTypeFontGenerator.FreeTypeBitmapFontData fontData = generator.generateData(parameter);
            ftFont = generator.generateFont(parameter);
        }
        catch (Exception e) {
            Gdx.app.log("Font generation", "error");
        }
        generator.dispose();

        // calculate size of title text
        titleGlyphs = new GlyphLayout(); //don't do this every frame! Store it as member
        titleGlyphs.setText(ftFont, TITLE);
        titleWidth = titleGlyphs.width;
        titleHeight = titleGlyphs.height;

        Gdx.app.log("font size", ""+parameter.size);
        Gdx.app.log("titleGlyphs", ""+titleWidth+" x " + titleHeight);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.1f, 0.2f, 0.3f, 1);

        batch.begin();
        batch.setProjectionMatrix( viewport.getCamera().combined );

        float x = ((width - titleWidth)/2f);     // centred horizontally
        float y = (height/2f + titleHeight);        // align bottom of title to mid-height of screen

        ftFont.setColor(Color.WHITE);
        ftFont.draw(batch, titleGlyphs, x, y);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        this.width = width;
        this.height = height;
        viewport.update(width, height, true);
        Gdx.app.log("resize", ""+width+" x " + height);
        generateFont(width);
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        ftFont.dispose();
        batch.dispose();
    }
}
