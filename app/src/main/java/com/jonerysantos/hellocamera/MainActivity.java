package com.jonerysantos.hellocamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

import livroandroid.lib.utils.ImageResizeUtils;
import livroandroid.lib.utils.SDCardUtils;

public class MainActivity extends AppCompatActivity {
    //Caminho para salvar o arquivo
    private File file;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = (ImageView) findViewById(R.id.imagem);
        ImageButton b = (ImageButton) findViewById(R.id.btAbrirCamera);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(*1) Cria o caminho do arquivo no SD card
                file = SDCardUtils.getPrivateFile(getBaseContext(), "foto.jpg", Environment.DIRECTORY_PICTURES);
                //Chama a intent informando o arquivo para salvar a foto
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(i, 0);
            }
        });
        if(savedInstanceState != null){
            //(*2) Se girou a tela, recupera o estado
            file = (File) savedInstanceState.getSerializable("file");
            showImage(file);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //(*3) Salvar o estado caso gire a tela
        outState.putSerializable("file", file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && file != null){
            //(*4) Se a câmera retornou, vamos mostrar o arquivo da foto
            showImage(file);
        }
    }

    //Atualiza a imagem na tela
    private void showImage(File file) {
        if(file != null && file.exists()){
            Log.d("foto", file.getAbsolutePath());
            int w = imgView.getWidth();
            int h = imgView.getHeight();
            //(*5) Redimensiona a imagem para o tamanho do ImageView
            Bitmap bitmap = ImageResizeUtils.getResizedImage(Uri.fromFile(file), w, h, false);
            imgView.setImageBitmap(bitmap);
        }
    }
}
