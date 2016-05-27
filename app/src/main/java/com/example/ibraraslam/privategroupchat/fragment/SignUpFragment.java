package com.example.ibraraslam.privategroupchat.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ibraraslam.privategroupchat.R;
import com.example.ibraraslam.privategroupchat.activity.HomeActivity;
import com.example.ibraraslam.privategroupchat.constant.FirebasePath;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    String name;

    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!= null){
                    Log.d("TAG Sign up","State Change");
                    FirebaseDatabase rootRef = FirebaseDatabase.getInstance();
                    DatabaseReference userDataNode = rootRef.getReference(FirebasePath.getUserDataNode());
                    Log.d("TAG Sign up",user.getUid());
                    userDataNode.child(user.getUid()).setValue(name).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Log.d("Tag",task.getException().toString());
                            }
                        }
                    });
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("userID",user.getUid());
                    startActivity(intent);
                    mListener.isSignupComplete();
                }
            }
        };
    }
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_sign_up, container, false);
        final EditText nameEdt = (EditText) rootView.findViewById(R.id.Edit_name);
        final EditText emailEdt = (EditText) rootView.findViewById(R.id.Edt_email);
        final EditText passwordEdt = (EditText) rootView.findViewById(R.id.Edt_password);
        Button signUp = (Button) rootView.findViewById(R.id.Btn_sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEdt.getText().toString().isEmpty()){
                    nameEdt.setError("Required.");
                }else if(emailEdt.getText().toString().isEmpty()){
                    emailEdt.setError("Required.");
                }else if(passwordEdt.getText().toString().isEmpty()){
                    passwordEdt.setError("Required.");
                }
                else {
                    name = nameEdt.getText().toString();
                    mAuth.createUserWithEmailAndPassword(emailEdt.getText().toString(),passwordEdt.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                                Log.d("TAG",task.getException().toString());

                            }
                            else {
                                AuthResult result = task.getResult();
                                Log.d("TAG",result.getUser().getUid());
                                DatabaseReference userDataNode = FirebaseDatabase.getInstance().getReference(FirebasePath.getUserDataNode());
                                userDataNode.child(result.getUser().getUid()).setValue(nameEdt.getText().toString());
                            }
                        }
                    });
                }
            }
        });
        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void isSignupComplete();
    }
}
