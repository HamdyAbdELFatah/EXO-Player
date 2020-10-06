package com.moallem.moallemtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.moallem.moallemtask.adapter.SubjectsAdapter;
import com.moallem.moallemtask.adapter.VideosAdapter;
import com.moallem.moallemtask.data.SubjectData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("video");
    List<SubjectData> subjectData;
    List<String> videosData;
    ProgressBar progressBar;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.progressBar2);
        setDataSubjects();
        setVideosSubjects();

    }
    void setDataSubjects(){
        subjectData=new ArrayList<>();
        subjectData.add(new SubjectData("Physics", R.drawable.ic_physics));
        subjectData.add(new SubjectData("Biology", R.drawable.ic_biology));
        subjectData.add(new SubjectData("History", R.drawable.ic_history));
        subjectData.add(new SubjectData("Algebra", R.drawable.ic_algebra));

        RecyclerView recyclerViewSubjects = findViewById(R.id.recyclerViewSubjects);
        recyclerViewSubjects.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSubjects.setAdapter(new SubjectsAdapter(this,subjectData ));
    }
    void setVideosSubjects(){
        videosData=new ArrayList<>();
        final RecyclerView recyclerViewVideos = findViewById(R.id.recyclerViewVideos);
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> m = document.getData();
                        videosData.add(m.get("url").toString());
                    }
                    recyclerViewVideos.setAdapter(new VideosAdapter(MainActivity.this, videosData));
                    progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });
    }
}