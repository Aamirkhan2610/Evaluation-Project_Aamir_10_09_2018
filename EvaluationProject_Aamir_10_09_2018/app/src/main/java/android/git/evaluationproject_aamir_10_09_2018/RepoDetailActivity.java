package android.git.evaluationproject_aamir_10_09_2018;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RepoDetailActivity extends AppCompatActivity {
    private WebView webRepoDetail;
    private String RemoteURL="";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);
        Init();
    }
    private void Init() {
        progressDialog=new ProgressDialog(RepoDetailActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        RemoteURL="";
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            RemoteURL=bundle.getString("RemoteURL");
        }
        webRepoDetail=findViewById(R.id.web_repodetail);
        webRepoDetail.loadUrl(RemoteURL);
        webRepoDetail.getSettings().setJavaScriptEnabled(true);
        webRepoDetail.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                progressDialog.dismiss();
                super.onReceivedError(view, request, error);
            }
        });
    }

}
