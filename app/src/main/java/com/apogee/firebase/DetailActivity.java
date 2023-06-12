package com.apogee.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apogee.firebase.databinding.ActivityDetailBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    Boolean isAllFabsVisible;
    String key="";
    String imageUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            binding.detailTitle.setText(bundle.getString("dataTitle"));
            Glide.with(this).load(bundle.getString("dataImage")).into(binding.detailImage);
            binding.detailLanguage.setText(bundle.getString("dataLang"));
            binding.detailDescription.setText(bundle.getString("dataDesc"));
            key = bundle.getString("key");
            Log.d("TAG", "Detail: key:== "+key);
            imageUrl = bundle.getString("dataImage");
            Toast.makeText(this, "Recived", Toast.LENGTH_SHORT).show();
        }
        isAllFabsVisible = false;

        binding.addFab.shrink();

        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAllFabsVisible){
                    binding.delete.show();
                    binding.delete.setVisibility(View.VISIBLE);
                    binding.update.setVisibility(View.VISIBLE);
                    binding.addFab.extend();
                    isAllFabsVisible = true;
                }else{
                    binding.delete.hide();
                    binding.delete.setVisibility(View.GONE);
                    binding.update.setVisibility(View.GONE);
                    binding.addFab.shrink();
                    isAllFabsVisible = false;
                }
            }
        });

        //Firebase Tutorials is the table name
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Firebase Tutorials");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
                intent.putExtra("dataTitle", binding.detailTitle.getText().toString());
                intent.putExtra("dataLang", binding.detailLanguage.getText().toString());
                intent.putExtra("dataDesc", binding.detailDescription.getText().toString());
                intent.putExtra("dataImage", imageUrl);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });
    }
}