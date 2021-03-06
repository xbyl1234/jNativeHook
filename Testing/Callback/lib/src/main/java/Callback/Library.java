/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Callback;

public class Library {
	static {
		System.loadLibrary("Test");
	}

	private static native void basicNative();
	private static native int retuningIntNative();
	private static native void paramsNative(int a, double b);
	private static native int allTogetherNow(int a, float b);

    public void callBasicNative() {
        basicNative();
    }
	public int callReturningIntNative() {
		return retuningIntNative();
	}
	public void callParamsNative(int a, double b) {
		paramsNative(a, b);
	}
	public int callAllTogetherNow(int a, float b) {
		return allTogetherNow(a, b);
	}

	public static void callMeNatively() {
		System.out.println("Yay! I was successfully called!");
	}
}
