package com.emu.java.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.emu.java.core.JarLoader;
import com.emu.java.core.KeyMapper;

public class MainActivity extends Activity {
    private J2CanvasView canvasView;
    private GamePadView gamePadView;
    private static final int PICK_JAR_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        canvasView = new J2CanvasView(this, null);
        gamePadView = new GamePadView(this, null);

        LinearLayout.LayoutParams canvasParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 2.0f);
        LinearLayout.LayoutParams padParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);

        layout.addView(canvasView, canvasParams);
        layout.addView(gamePadView, padParams);
        setContentView(layout);

        // Lanzar selector de archivos al iniciar
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/java-archive"); // Filtro para .jar
        startActivityForResult(Intent.createChooser(intent, "Selecciona tu juego Java"), PICK_JAR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_JAR_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    JarLoader loader = new JarLoader();
                    loader.loadJar(uri.getPath());
                    Toast.makeText(this, "Cargando: " + loader.getAppName(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error cargando archivo", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
