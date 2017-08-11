package android.databinding.testlibrary2;

public class CrossLibraryBRAccess {
    private int a = BR.cat;
    private int b = android.databinding.testlibrary1.BR.foo;
}
