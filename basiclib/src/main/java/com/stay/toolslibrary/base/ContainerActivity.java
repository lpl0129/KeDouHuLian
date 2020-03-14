package com.stay.toolslibrary.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import static android.view.View.generateViewId;
import com.stay.toolslibrary.library.swaipLayout.SwipeBackActivity;

import java.lang.ref.WeakReference;



/**
 * 盛装Fragment的一个容器(代理)Activity
 * 普通界面只需要编写Fragment,使用此Activity盛装,这样就不需要每个界面都在AndroidManifest中注册一遍
 */
public class ContainerActivity extends SwipeBackActivity {
    public static final String FRAGMENT = "fragment";
    public static final String BUNDLE = "bundle";
    protected WeakReference<Fragment> mFragment;
    private ViewGroup mianLayout;

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public static void startContainerActivity(Context context, String canonicalName, Bundle bundle) {
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public  static void startContainerActivity(Context context, String canonicalName) {
        startContainerActivity(context, canonicalName, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        processLogic();
    }


    protected void processLogic() {
        mianLayout = new LinearLayout(this);
        mianLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //generateViewId()生成不重复的id
        mianLayout.setId(generateViewId());
        setContentView(mianLayout);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(mianLayout.getId());
        if (fragment == null) {
            initFromIntent(getIntent());
        }
    }

    protected void initFromIntent(Intent data) {
        if (data == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }
        try {
            String fragmentName = data.getStringExtra(FRAGMENT);
            if (fragmentName == null || "".equals(fragmentName)) {
                throw new IllegalArgumentException("can not find page fragmentName");
            }
            Class<?> fragmentClass = Class.forName(fragmentName);
            BaseFragment fragment = (BaseFragment) fragmentClass.newInstance();

            Bundle args = data.getBundleExtra(BUNDLE);
            if (args != null) {
                fragment.setArguments(args);
            }
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(mianLayout.getId(), fragment);
            trans.commitAllowingStateLoss();
            mFragment = new WeakReference<Fragment>(fragment);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(mianLayout.getId());
        if (fragment instanceof BaseFragment) {
            if (!((BaseFragment) fragment).onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
//        if (mFragment != null && mFragment.get() != null
//                && mFragment.get() instanceof BaseFragment) {
//            BaseFragment bf = (BaseFragment) mFragment.get();
//            if (!bf.onBackPressed()) {
//                super.onBackPressed();
//            }
//        } else {
//            super.onBackPressed();
//        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && mFragment.get() instanceof BaseFragment) {
//            ((BaseFragment) mFragment.get()).onBackPressed();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
