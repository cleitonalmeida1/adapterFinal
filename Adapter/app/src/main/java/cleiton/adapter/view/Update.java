package cleiton.adapter.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.Firebase;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cleiton.adapter.R;
import cleiton.adapter.model.Agenda;

/**
 * Created by Cleiton Gonçalves on 16/04/2016.
 */
public class Update extends AppCompatActivity {

    private Firebase firebase;

    @Bind(R.id.imagem)
    ImageView imvImagem;


    @Bind(R.id.telefone)
    EditText telefone;
    @Bind(R.id.nome)
    EditText edtNome;

    @Bind(R.id.fabDel)
    FloatingActionButton fbDell;

    private boolean update = false;

    private Agenda agenda;

    private int id;

    private String localFoto;

    private static final int FOTO = 1;

    private boolean fotoResource = false;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebase = new Firebase("https://cleitonapp.firebaseio.com/");
    }

    @Override
    protected void onStart() {
        super.onStart();
        agenda = new Agenda();
        Bundle b = getIntent().getExtras();

        if (b != null) {
            agenda = (Agenda) b.get("agenda");
            update = true;
            fbDell.setVisibility(View.VISIBLE);
            updateUI();
            localFoto = agenda.getImagem();
        }
    }

    @OnClick(R.id.fabDel)
    public void fabDel(View view) {
        firebase.child("agenda").child(agenda.getId()).removeValue();
        finish();
    }

    @OnClick(R.id.fabCad)
    public void fabcad(View v) {

        agenda.setNome(edtNome.getText().toString());
        agenda.setValor(telefone.getText().toString());
        if (update) {
            agenda.setImagem((String) imvImagem.getTag());

            Map<String,Object> mp = new HashMap<String,Object>();
            mp.put(agenda.getId(), agenda);

            firebase.child("agenda").child(agenda.getId()).updateChildren(mp);

        } else {
            agenda.setNome(edtNome.getText().toString());
            agenda.setValor(telefone.getText().toString());
            agenda.setImagem((String) imvImagem.getTag());


            Firebase posRef = firebase.child("agenda");
            Firebase newPost = posRef.push();
            agenda.setId(newPost.getKey());

            newPost.setValue(agenda);
            Log.i("Teste", (String) imvImagem.getTag());
        }
        finish();
    }

    public void updateUI() {
        if (agenda == null) {
            edtNome.setText(null);
            telefone.setText(null);
        } else {
            edtNome.setText(agenda.getNome());
            setFoto(agenda.getImagem());
            telefone.setText(agenda.getValor());
        }
    }

    public void carregaFoto() {
        fotoResource = true;
        localFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(localFoto)));

        startActivityForResult(intentCamera, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!fotoResource) {
            if (resultCode == -1) {
                InputStream stream = null;

                try {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }

                    stream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    imvImagem.setImageBitmap(bitmap);
                    localFoto = data.getDataString();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            if (requestCode == FOTO) {
                if (resultCode == Activity.RESULT_OK) {
                    setFoto(localFoto);
                } else {
                    this.localFoto = null;
                }
            }
        }

    }

    private void setFoto(String url) {
        if (url != null) {
            Bitmap imagemFoto = BitmapFactory.decodeFile(url);
            imvImagem.setImageBitmap(imagemFoto);
            imvImagem.setTag(url);
        }
    }

    @OnClick(R.id.fabFhoto)
    public void foto(View v) {
        carregaFoto();
    }

}
