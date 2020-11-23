package br.usjt.ucsist.minhasviagens;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText mTitleEt, mDescriptionEt;
    Button mSaveBtn, mListBtn;

    ProgressDialog pd;

    FirebaseFirestore db;

    String pId, pTitle, pDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Data");

        mTitleEt = findViewById(R.id.titleEt);
        mDescriptionEt = findViewById(R.id.descriptionEt);
        mSaveBtn = findViewById(R.id.saveBtn);
        mListBtn = findViewById(R.id.listBtn);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null ){
            // Update data
            actionBar.setTitle("Update Data");
            mSaveBtn.setText("Update");
            //get data
            pId = bundle.getString("pId");
            pTitle = bundle.getString("pTitle");
            pDescription = bundle.getString("pDescription");
            //set data
            mTitleEt.setText(pTitle);
            mDescriptionEt.setText(pDescription);
        }
        else {
            //newdata
            actionBar.setTitle("Add Data");
            mSaveBtn.setText("Save");
        }

        pd = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();




        //Botao para subir os dados
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if (bundle!= null){
                    //updating
                    String id = pId;
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();
                    //function call to update data
                    updateData(id, title, description);
                }
                else {
                    //adding new
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();

                    uploadData(title, description);
                }



            }
        });

        //Botao para startar a ListActivity
        mListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();
            }
        });

    }

    private void updateData(String id, String title, String description) {
        pd.setTitle("Atualizando dados...");
        pd.show();

        db.collection("Documents").document(id)
                .update("title", title, "description", description)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //called when updated sucessfully
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //called when there is any error
                        pd.dismiss();
                        //get and show error message
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadData(String title, String description) {
        pd.setTitle("Adicionando dados no Firestore");
        pd.show();
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("title", title);
        doc.put("description", description);

        db.collection("Documents").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Salvando...", Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        pd.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }
}
