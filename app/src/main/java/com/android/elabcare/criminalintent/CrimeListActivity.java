package com.android.elabcare.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by runningwater on 2016/10/27.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
