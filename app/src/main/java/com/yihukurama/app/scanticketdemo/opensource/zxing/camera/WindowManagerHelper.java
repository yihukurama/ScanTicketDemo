package com.yihukurama.app.scanticketdemo.opensource.zxing.camera;



import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * 应用窗口管理器
 * 
 * @author 
 */
public class WindowManagerHelper {

	/**
	 * 获取窗口管理器对象
	 * 
	 * @param context
	 *            当前环境
	 * @return 窗口管理对象，如果获取不到返回null
	 */
	public static WindowManager getWindowManager(Context context) {
		Object obj = context.getSystemService("window");
		if (obj instanceof WindowManager) {
			return (WindowManager) obj;
		}
		return null;
	}

	/**
	 * 获取默认显示器,通常用于获取显示器宽高
	 * 
	 * @param context
	 *            当前环境
	 * @return 默认显示器对象,如果获取不到窗口管理器对象则返回null
	 */
	public static Display getDefaultDisplay(Context context) {
		WindowManager windowManager = getWindowManager(context);
		if (windowManager != null) {
			return windowManager.getDefaultDisplay();
		}
		return null;
	}

	/**
	 * 获取显示器宽度
	 * 
	 * @param context
	 *            当前活动窗体
	 */
	@SuppressWarnings("deprecation")
	public static int getDisplayWidth(Context context) {
		Display display = getDefaultDisplay(context);
		if (display != null) {
			return display.getWidth();
		}
		return 0;
	}

	/**
	 * 获显示器高度
	 * 
	 * @param context
	 *            当前活动窗体
	 */
	@SuppressWarnings("deprecation")
	public static int getDisplayHeight(Context context) {
		Display display = getDefaultDisplay(context);
		if (display != null) {
			return display.getHeight();
		}
		return 0;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param activity
	 *            当前活动窗体
	 */
	@Deprecated
	public static int getStatusBarHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (statusHeight == 0) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources().getDimensionPixelSize(i);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	/**
	 * 切换当前界面为全屏窗口
	 * 
	 * @param activity
	 *            当前活动窗体
	 */
	public static void switchToFullScreenWindow(Activity activity) {
		Window window = activity.getWindow();
		if (window != null) {
			window.setFlags(LayoutParams.FLAG_FULLSCREEN,
					LayoutParams.FLAG_FULLSCREEN);
			activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	}
	
	
	
	
}