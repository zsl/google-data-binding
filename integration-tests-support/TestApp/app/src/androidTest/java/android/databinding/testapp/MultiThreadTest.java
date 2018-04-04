package android.databinding.testapp;

import android.databinding.testapp.databinding.MultiThreadLayoutBinding;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MultiThreadTest extends BaseDataBinderTest<MultiThreadLayoutBinding> {
    public MultiThreadTest() {
        super(MultiThreadLayoutBinding.class);
    }

    @Test
    public void testSetOnBackgroundThread() throws InterruptedException {
        initBinder();
        mBinder.setText("a");
        assertEquals("a", mBinder.getText());
        sleep(500);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertEquals("a", mBinder.myTextView.getText().toString());
            }
        });
        mBinder.setText("b");
        sleep(500);
        assertEquals("b", mBinder.getText());
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertEquals("b", mBinder.myTextView.getText().toString());
            }
        });
    }
}
