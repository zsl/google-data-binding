package android.databinding.testapp;

import android.databinding.testapp.databinding.ConditionalBindingBinding;
import android.databinding.testapp.vo.ConditionalVo;
import android.databinding.testapp.vo.NotBindableVo;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ConditionalBindingTest extends BaseDataBinderTest<ConditionalBindingBinding> {

    public ConditionalBindingTest() {
        super(ConditionalBindingBinding.class);
    }

    @Test
    @UiThreadTest
    public void test1() {
        initBinder();
        testCorrectness(true, true);
    }

    @Test
    @UiThreadTest
    public void testTernary() {
        ConditionalVo obj4 = new ConditionalVo();
        initBinder();
        mBinder.setObj4(obj4);
        mBinder.executePendingBindings();
        assertEquals("hello", mBinder.textView1.getText().toString());
        obj4.setUseHello(true);
        mBinder.executePendingBindings();
        assertEquals("Hello World", mBinder.textView1.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testNullListener() {
        ConditionalVo obj4 = new ConditionalVo();
        initBinder();
        mBinder.setObj4(obj4);
        mBinder.executePendingBindings();
        mBinder.view1.callOnClick();
        assertFalse(obj4.wasClicked);
        mBinder.setCond1(true);
        mBinder.executePendingBindings();
        mBinder.view1.callOnClick();
        assertTrue(obj4.wasClicked);
    }

    private void testCorrectness(boolean cond1, boolean cond2) {
        NotBindableVo o1 = new NotBindableVo("a");
        NotBindableVo o2 = new NotBindableVo("b");
        NotBindableVo o3 = new NotBindableVo("c");
        mBinder.setObj1(o1);
        mBinder.setObj2(o2);
        mBinder.setObj3(o3);
        mBinder.setCond1(cond1);
        mBinder.setCond2(cond2);
        mBinder.executePendingBindings();
        final String text = mBinder.textView.getText().toString();
        assertEquals(cond1 && cond2, "a".equals(text));
        assertEquals(cond1 && !cond2, "b".equals(text));
        assertEquals(!cond1, "c".equals(text));
    }
}
