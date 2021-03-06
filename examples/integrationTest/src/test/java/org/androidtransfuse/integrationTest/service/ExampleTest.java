package org.androidtransfuse.integrationTest.service;

import android.content.Intent;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ExampleTest {

    private Example example;
    private ExampleService exampleService;

    @Before
    public void setup() {
        exampleService = new ExampleService();
        exampleService.onCreate();

        example = DelegateUtil.getDelegate(exampleService, Example.class);
    }

    @Test
    public void testOnCreate() {
        Assert.assertTrue(example.isOnCreateCalled());
    }

    @Test
    public void testOnStartCommand() {
        Intent intent = new Intent();
        assertFalse(example.isOnStartCommandCalled());
        exampleService.onStartCommand(intent, 0, 1);
        assertTrue(example.isOnStartCommandCalled());
    }

    @Test
    public void testOnDestroy() {
        assertFalse(example.isOnDestroyCalled());
        exampleService.onDestroy();
        assertTrue(example.isOnDestroyCalled());
    }

    @Test
    public void testOnLowMemory() {
        assertFalse(example.isOnLowMemoryCalled());
        exampleService.onLowMemory();
        assertTrue(example.isOnLowMemoryCalled());
    }

    @Test
    public void testOnRebind() {
        Intent intent = new Intent();
        assertFalse(example.isOnRebindCalled());
        exampleService.onRebind(intent);
        assertTrue(example.isOnRebindCalled());
    }

    @Test
    @Ignore
    public void testOnTrimMemory(){
        assertFalse(example.isOnTrimMemoryCalled());
        //todo:exampleService.onTrimMemory();
        assertTrue(example.isOnTrimMemoryCalled());
    }

    @Test
    @Ignore
    public void testOnTaskRemoved(){
        assertFalse(example.isOnTaskRemoved());
        //todo:exampleService.onTaskRemoved();
        assertTrue(example.isOnTaskRemoved());
    }


    @Test
    public void testOnConfigurationChanged(){
        assertFalse(example.isOnConfigurationChangedCalled());
        exampleService.onConfigurationChanged(null);
        assertTrue(example.isOnConfigurationChangedCalled());
    }

    @Test
    public void testOnUnbind(){
        assertFalse(example.isOnUnbindCalled());
        exampleService.onUnbind(null);
        assertTrue(example.isOnUnbindCalled());
    }
}
