package android.databinding.testapp;

import android.databinding.testapp.databinding.CustomNsAdapterBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CustomNamespaceAdapterTest extends BaseDataBinderTest<CustomNsAdapterBinding> {
    public CustomNamespaceAdapterTest() {
        super(CustomNsAdapterBinding.class);
    }

    @Test
    @UiThreadTest
    public void testAndroidNs() {
        initBinder();
        mBinder.setStr1("a");
        mBinder.setStr2("b");
        mBinder.executePendingBindings();
        assertEquals("a", mBinder.textView1.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testCustomNs() {
        initBinder();
        mBinder.setStr1("a");
        mBinder.setStr2("b");
        mBinder.executePendingBindings();
        assertEquals("b", mBinder.textView2.getText().toString());
    }
}
