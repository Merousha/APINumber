package com.example.merousha.apinumber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merousha.apinumber.api.GitHubRepo;
import com.example.merousha.apinumber.api.ServiceGenerator;
import com.example.merousha.apinumber.service.GitHubClient;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Realm myRealm;
    private RealmAsyncTask realmAsyncTask;
    private ArrayList<String> githubrepoNames;
    private RealmResults<GitHubRepo> gitHubRepoRealmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.pagination_list);

        Realm.init(this);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("myFirstRealm.realm") // By default the name of db is "default.realm"
                .build();

        Realm.setDefaultConfiguration(configuration);

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                .build());

        myRealm = Realm.getDefaultInstance();


        GitHubClient client = ServiceGenerator.createService();
        Call<List<GitHubRepo>> call = client.reposForUser("fs-opensource");

        call.enqueue(new Callback<List<GitHubRepo>>() { //sends a request asynchronously and receives a response
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                List<GitHubRepo> repos = response.body();

                addToRealmASynchronously(repos);

            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addToRealmASynchronously(final List<GitHubRepo> repos) {

        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                myRealm.copyToRealmOrUpdate(repos);
                Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public ArrayList<String> getRealmData () {

        githubrepoNames = new ArrayList<>();
        gitHubRepoRealmResults = myRealm.where(GitHubRepo.class).findAll();

        for (GitHubRepo gr : gitHubRepoRealmResults) {

            githubrepoNames.add(gr.getMyName());
        }

        return githubrepoNames;
    }

    public void displayData(View view){

        listView.setAdapter(new GitHubRepoAdapter(MainActivity.this, getRealmData()));

    }

    @Override
    protected void onStop() { // if we get a phone call for example, it is our responsibility to cancel the task
        super.onStop();

        if (realmAsyncTask != null && !realmAsyncTask.isCancelled()) {
            realmAsyncTask.cancel();

        }
    }

    @Override
    protected void onDestroy() { // It is our responsibility to close the realm db if we had instantiated it in any activity or fragment therefore we use onDestroy method
        super.onDestroy();
        myRealm.close(); // if we do not close we will leak the memory.

    }

}