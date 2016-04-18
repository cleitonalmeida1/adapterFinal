package cleiton.agenda.view;

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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.Firebase;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cleiton.agenda.R;
import cleiton.agenda.model.Agenda;
import cleiton.agenda.util.FireBaseUtil;

/**
 * Created by Cleiton Gonçalves on 18/04/2016.
 */
public class Update extends AppCompatActivity {
    private Firebase firebase;

    @Bind(R.id.imagem)
    ImageView imvImagem;


    @Bind(R.id.edtTelefone)
    EditText edtTelefone;
    @Bind(R.id.edtNome)
    EditText edtNome;

    @Bind(R.id.fabDel)
    FloatingActionButton fbDell;

    private boolean update = false;

    private Agenda agenda;

    private String localFoto;

    private static final int FOTO = 1;

    private boolean fotoResource = false;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebase = FireBaseUtil.getFirebase();
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        agenda = new Agenda();
        Bundle b = getIntent().getExtras();

        /**
         * verefica se foi enviado
         */
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
        /**
         * pego o link do objeto e o id de onde ele esta e removo o no
         */
        firebase.child("agenda").child(agenda.getId()).removeValue();
        finish();
    }


    @OnClick(R.id.fabCad)
    public void fabcad(View v) {
        Firebase posRef = firebase.child("agenda");

        Firebase newPost ;
        agenda.setNome(edtNome.getText().toString());
        agenda.setTelefone(edtTelefone.getText().toString());

        agenda.setImagem((String) imvImagem.getTag());

        if (update) {
            /**
             * pego o link do objeto e o id de onde ele esta e atualizo o no
             */
            newPost = posRef.child(agenda.getId());
        } else {
            newPost = posRef.push();
            agenda.setId(newPost.getKey());
        }
        newPost.setValue(agenda);

        finish();
    }


    public void updateUI() {
        if (agenda == null) {
            edtNome.setText(null);
            edtTelefone.setText(null);
        } else {
            edtNome.setText(agenda.getNome());
            setFoto(agenda.getImagem());
            edtTelefone.setText(agenda.getTelefone());
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
        if (requestCode == FOTO) {
            if (resultCode == Activity.RESULT_OK) {
                setFoto(localFoto);
            } else {
                this.localFoto = null;
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
