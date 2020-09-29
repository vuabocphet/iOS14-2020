package com.developer.phimtatnhanh.setuptouch.utilities;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.setuptouch.config.ConfigAll;
import com.developer.phimtatnhanh.setuptouch.dialog.MenuTouchDialog;
import com.developer.phimtatnhanh.setuptouch.gridviewstarapp.AppItem;
import com.developer.phimtatnhanh.setuptouch.gridviewstarapp.CustomStarAppAdapter;
import com.github.florent37.viewanimator.ViewAnimator;

public class StarAppUtil implements ConfigAll {

    private ListUtils listUtils;
    private View csStarAppMenuTouch;
    private View csHomeMenuTouch;
    private GridView gridStarAppMenuTouch;
    private MenuTouchDialog menuTouchDialog;
    private ProgressBar progressBar;
    private int positionAppNone;

    public StarAppUtil setContext(Context context) {
        AppContext.create(context);
        return this;
    }

    public StarAppUtil setListUtils(ListUtils listUtils) {
        this.listUtils = listUtils;
        return this;
    }

    public StarAppUtil setCsStarAppMenuTouch(View csStarAppMenuTouch) {
        this.csStarAppMenuTouch = csStarAppMenuTouch;
        return this;
    }

    public StarAppUtil setCsHomeMenuTouch(View csHomeMenuTouch) {
        this.csHomeMenuTouch = csHomeMenuTouch;
        return this;
    }

    public StarAppUtil setGridStarAppMenuTouch(GridView gridStarAppMenuTouch) {
        this.gridStarAppMenuTouch = gridStarAppMenuTouch;
        return this;
    }

    public StarAppUtil setMenuTouchDialog(MenuTouchDialog menuTouchDialog) {
        this.menuTouchDialog = menuTouchDialog;
        return this;
    }

    public StarAppUtil setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
        return this;
    }

    public static StarAppUtil init() {
        return new StarAppUtil();
    }

    public void show() {
        this.csStarAppMenuTouch.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.listUtils.starGetAppItemList(appItems ->
        {
            this.progressBar.setVisibility(View.GONE);
            CustomStarAppAdapter.init(AppContext.get().getContext(), appItems, this.gridStarAppMenuTouch)
                    .setClickAppItem(new CustomStarAppAdapter.ClickAppItem() {
                        @Override
                        public void onClick(AppItem appItem, int position) {
                            typeClick(appItem,position);
                        }

                        @Override
                        public void onUpdate() {
                           show();
                        }
                    });
        });
        animationG(this.csHomeMenuTouch);
        animationV(this.csStarAppMenuTouch);
    }

    public void showListAppAll() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.gridStarAppMenuTouch.setVisibility(View.GONE);
        this.listUtils.getListAppAll(appItems ->
        {
            this.progressBar.setVisibility(View.GONE);
            animationV(this.gridStarAppMenuTouch);
            CustomStarAppAdapter.init(AppContext.get().getContext(), appItems, this.gridStarAppMenuTouch).setAddApp(true)
                    .setClickAppItem(new CustomStarAppAdapter.ClickAppItem() {
                        @Override
                        public void onClick(AppItem appItem, int position) {
                            addApp(appItem,position);
                        }

                        @Override
                        public void onUpdate() {

                        }
                    });

        });
    }

    private void animationV(View view) {
        ViewAnimator.animate(view)
                .pulse()
                .duration(durationShowHideStarApp).alpha(0f, 1f).onStart(() -> view.setVisibility(View.VISIBLE)).start();
    }

    private void animationG(View view) {
        ViewAnimator.animate(view)
                .pulse()
                .duration(durationShowHideStarApp).alpha(1f, 0f).start().onStop(() -> view.setVisibility(View.GONE));
    }

    private void gone() {
        this.csHomeMenuTouch.setVisibility(View.VISIBLE);
        animationG(this.csStarAppMenuTouch);
        animationV(this.csHomeMenuTouch);
    }

    private void typeClick(AppItem appItem, int position) {
        switch (appItem.typeStar) {
            case APP:
                Intent launchIntent = AppContext.get().getContext().getPackageManager().getLaunchIntentForPackage(appItem.packname);
                if (launchIntent != null) {
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppContext.get().getContext().startActivity(launchIntent);
                    this.menuTouchDialog.cancelMenuTouch(true);
                } else {
                    Toast.makeText(AppContext.get().getContext(), "Package not found", Toast.LENGTH_SHORT).show();
                }
                return;
            case BACK:
                this.gone();
                return;
            case NONE:
                this.positionAppNone = position;
                this.showListAppAll();
        }
    }

    private void addApp(AppItem appItem, int position) {
        PrefUtil.get().postString("a" + this.positionAppNone, appItem.packname);
        this.show();
    }

}
