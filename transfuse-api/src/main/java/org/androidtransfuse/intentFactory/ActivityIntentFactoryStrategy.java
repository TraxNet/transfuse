package org.androidtransfuse.intentFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author John Ericksen
 */
public class ActivityIntentFactoryStrategy extends AbstractIntentFactoryStrategy {

    protected ActivityIntentFactoryStrategy(Class<? extends Context> targetContext, Bundle bundle) {
        super(targetContext, bundle);
    }

    public void start(Context context, Intent intent) {
        context.startActivity(intent);
    }
}
