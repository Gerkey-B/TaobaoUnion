package com.bzq.taobaounion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bzq.taobaounion.R;
import com.bzq.taobaounion.base.BaseActivity;
import com.bzq.taobaounion.model.DTO.TicketResult;
import com.bzq.taobaounion.presenter.ITicketPresenter;
import com.bzq.taobaounion.utils.LogUtils;
import com.bzq.taobaounion.utils.PresenterManager;
import com.bzq.taobaounion.utils.StringUtils;
import com.bzq.taobaounion.utils.ToastUtils;
import com.bzq.taobaounion.utils.UrlUtils;
import com.bzq.taobaounion.view.ITicketPageCallback;

import butterknife.BindView;

/**
 * @author Gerkey
 * Created on 2021/7/22
 */
public class TicketActivity extends BaseActivity implements ITicketPageCallback {

    private ITicketPresenter mTicketPresenter;
    private boolean mHasTaoBaoApp = false;

    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_or_open_btn)
    public TextView mOpenOrCopyBtn;

    @BindView(R.id.ticket_back_press)
    public View backPress;

    @BindView(R.id.ticket_cover_loading)
    public View loadingView;

    @BindView(R.id.ticket_load_retry)
    public TextView retryLoadText;

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
        handleTaoBao();
    }

    @Override
    protected void initEvent() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mOpenOrCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击后，应该复制到粘贴板，而不是单纯的获取到 et 的内容。
                String ticketCode = mTicketCode.getText().toString().trim();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 复制到粘贴板
                ClipData clipData = ClipData.newPlainText("my_taoBao_ticket_code", ticketCode);
                cm.setPrimaryClip(clipData);
                // 判断有没有安装淘宝,有就打开,没有就提示复制成功。
                if (mHasTaoBaoApp) {
                    Intent taoBaoIntent = new Intent();
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taoBaoIntent.setComponent(componentName);
                    startActivity(taoBaoIntent);
                } else {
                    ToastUtils.showToast("已经复制，粘贴分享，或打开淘宝");
                }
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }

    private void handleTaoBao() {
        // 判断是否安装淘宝
        // 首先是需要获取到淘宝的包名，才能进行判断，要获取包名，肯定不能猜一个，要用专业的 ===== adb 操作。
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaoBaoApp = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaoBaoApp = false;
        }
        mOpenOrCopyBtn.setText(mHasTaoBaoApp ? "打开淘宝领券" : "复制淘口令");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        if (mCover != null && !TextUtils.isEmpty(cover)) {
            String targetPath = UrlUtils.getCoverPath(cover);
            LogUtils.d(this, "targetPath ---- > " + targetPath);
            Glide.with(this).load(targetPath).into(mCover);
        }
        if (TextUtils.isEmpty(cover)) {
            mCover.setImageResource(R.mipmap.no_image);
        }
        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            String model = result.getData().getTbk_tpwd_create_response().getData().getModel();
            String subModel = StringUtils.getSubModel(model);
            mTicketCode.setText(subModel);
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEmpty() {

    }
}

