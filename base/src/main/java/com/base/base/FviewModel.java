package com.base.base;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

public abstract class FviewModel<F extends Fragment> extends ViewModel {
    protected F mView;

    public F getView() {
        return mView;
    }

    public void setView(F mView) {
        this.mView = mView;
    }

    protected abstract void registLifeOwner(LifecycleOwner owner);
}
