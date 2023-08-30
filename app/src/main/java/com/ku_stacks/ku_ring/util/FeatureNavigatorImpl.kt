package com.ku_stacks.ku_ring.util

import android.app.Activity
import com.ku_stacks.common.FeatureNavigator
import com.ku_stacks.testfeature.TestFeatureActivity
import javax.inject.Inject

class FeatureNavigatorImpl @Inject constructor() : FeatureNavigator {
    override fun navigateToTestFeature(activity: Activity) {
        TestFeatureActivity.start(activity)
    }
}
