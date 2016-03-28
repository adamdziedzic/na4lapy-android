package pl.kodujdlapolski.na4lapy.ui.animals_list;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.kodujdlapolski.na4lapy.R;
import pl.kodujdlapolski.na4lapy.ui.AbstractDrawerActivity;
import pl.kodujdlapolski.na4lapy.ui.DrawerActivityHandler;

public class AnimalsListActivity extends AbstractDrawerActivity {

    private boolean isFavList = false;
    private boolean isSingleBrowse = false;
    private AnimalsListPresenter animalsListPresenter;
    private boolean isAlive = true;
    @Bind(R.id.container)
    ViewPager mViewPager;
    @Bind(R.id.error_container)
    LinearLayout errorContainer;
    @Bind(R.id.animals_list_progress)
    ProgressBar progressBar;
    @Bind(R.id.tabs)
    TabLayout tabs;


    @SuppressWarnings("unused")
    @OnClick(R.id.try_again_btn)
    public void onTryAgainClick(){
        errorContainer.setVisibility(View.GONE);
        animalsListPresenter.startDownloadingData();
    }

    public static final String EXTRA_IS_FAV_LIST = "extraIsFavList";
    public static final String EXTRA_IS_SINGLE_ELEMENT_BROWSE = "extraIsSingleElementBrowse";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        callSuperOnCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_list);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        handler = new DrawerActivityHandler(this);
        handler.setDrawer();

        ButterKnife.bind(this);

        isFavList = getIntent().getBooleanExtra(EXTRA_IS_FAV_LIST, false);
        if (!isFavList && savedInstanceState != null) {
            isFavList = savedInstanceState.getBoolean(EXTRA_IS_FAV_LIST);
        }
        isSingleBrowse = getIntent().getBooleanExtra(EXTRA_IS_SINGLE_ELEMENT_BROWSE, false);
        if (!isSingleBrowse && savedInstanceState != null) {
            isSingleBrowse = savedInstanceState.getBoolean(EXTRA_IS_SINGLE_ELEMENT_BROWSE);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(isFavList ? R.string.fav_animals_list_activity : R.string.animals_list_activity);
        }
        animalsListPresenter = new AnimalsListPresenter(this, isFavList, isSingleBrowse);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mViewPager.setAdapter(animalsListPresenter.getAdapter());
        ((TabLayout) findViewById(R.id.tabs)).setupWithViewPager(mViewPager);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            isAlive = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        animalsListPresenter.onActivityStart();
    }

    @Override
    protected void onStop() {
        animalsListPresenter.onActivityStop();
        super.onStop();
    }

    public void showError() {
        progressBar.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
    }


    public void showProgressHideContent(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mViewPager.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_IS_FAV_LIST, isFavList);
        outState.putSerializable(EXTRA_IS_SINGLE_ELEMENT_BROWSE, isSingleBrowse);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.onResume();
    }

    public void removeTabs() {
        tabs.setVisibility(View.GONE);
    }
}