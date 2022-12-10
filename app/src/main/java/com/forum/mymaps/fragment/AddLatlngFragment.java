package com.forum.mymaps.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.forum.mymaps.Model;
import com.forum.mymaps.R;
import com.forum.mymaps.databinding.FragmentAddLatlngBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class AddLatlngFragment extends Fragment {

    FragmentAddLatlngBinding binding;
    FirebaseDatabase firebaseDB;
    ProgressDialog progressDialog;

    public AddLatlngFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDB = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddLatlngBinding.inflate(inflater, container, false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        Bundle bundle = this.getArguments();
        String latValue = bundle.getString("latitude");
        String lngValue = bundle.getString("longitude");

        binding.latitude2.setText(latValue);
        binding.longitude2.setText(lngValue);

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                Model post = new Model();
                post.setLatitude(Double.parseDouble(binding.latitude2.getText().toString().trim()));
                post.setLongitude(Double.parseDouble(binding.longitude2.getText().toString().trim()));
                post.setAddress(binding.address.getText().toString().trim());
                post.setAddress2(binding.address2.getText().toString().trim());
                post.setHeadLine(binding.headLine.getText().toString().trim());
                post.setDescription(binding.description.getText().toString().trim());

                firebaseDB.getReference().child("post")
                        .push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Location Added!", Toast.LENGTH_SHORT).show();
                                Fragment fragment = new HomeFragment();
                                FragmentTransaction transaction = getActivity()
                                        .getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.container, fragment).commit();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "ERROR" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        });

            }
        });

        return binding.getRoot();
    }
}