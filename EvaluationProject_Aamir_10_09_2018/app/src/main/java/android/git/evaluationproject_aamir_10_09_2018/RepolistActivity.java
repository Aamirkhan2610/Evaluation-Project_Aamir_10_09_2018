package android.git.evaluationproject_aamir_10_09_2018;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import Model.DataModel;
import Retrofit.RetroInterface;
import Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepolistActivity extends AppCompatActivity {
    private RecyclerView recycleRepList;
    private RetroInterface retroInterface;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repolist);
        Init();
    }

    private void Init() {
        recycleRepList=findViewById(R.id.recycle_repolist);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycleRepList.setLayoutManager(llm);
        retroInterface = RetrofitClient.getRetrofitClient("https://api.github.com/").create(RetroInterface.class);
        progressDialog=new ProgressDialog(RepolistActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        getRepoList();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getRepoList(){
        Call <List<DataModel>> call = retroInterface.getRepoList();
        call.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                List<DataModel> dataModelList = response.body();
                DataAdapter dataAdapter=new DataAdapter(dataModelList);
                recycleRepList.setAdapter(dataAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

     class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder>{
        private List<DataModel> dataList;
        public DataAdapter(List<DataModel> dataList) {

            this.dataList = dataList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_repolist,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tvRepoName.setText(dataList.get(position).getName());
            if(dataList.get(position).getDescription()==null){
                holder.tvRepoDetail.setText(dataList.get(position).getHtmlUrl());
            }else{
                holder.tvRepoDetail.setText(dataList.get(position).getDescription());
            }
            holder.linearRepo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(),RepoDetailActivity.class);
                    intent.putExtra("RemoteURL",dataList.get(position).getHtmlUrl());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView tvRepoName;
            public TextView tvRepoDetail;
            public LinearLayout linearRepo;
            public MyViewHolder(View itemView) {
                super(itemView);
                tvRepoName = itemView.findViewById(R.id.tv_reponame);
                tvRepoDetail = itemView.findViewById(R.id.tv_repodetail);
                linearRepo=itemView.findViewById(R.id.linear_repo);
            }
        }
    }
}
