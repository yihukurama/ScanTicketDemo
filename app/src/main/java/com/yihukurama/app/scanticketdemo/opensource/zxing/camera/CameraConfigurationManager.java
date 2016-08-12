/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yihukurama.app.scanticketdemo.opensource.zxing.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

final class CameraConfigurationManager {

  private static final String TAG = CameraConfigurationManager.class.getSimpleName();

  private static final int TEN_DESIRED_ZOOM = 27;
  private static final int DESIRED_SHARPNESS = 30;

  private static final Pattern COMMA_PATTERN = Pattern.compile(",");

  private final Context context;
  private Point screenResolution;
  private Point cameraResolution;
  private int previewFormat;
  private String previewFormatString;

  CameraConfigurationManager(Context context) {
    this.context = context;
  }

  /**
   * Reads, one time, values from the camera that are needed by the app.
   */
  void initFromCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    previewFormat = parameters.getPreviewFormat();
    previewFormatString = parameters.get("preview-format");
    Log.d(TAG, "Default preview format: " + previewFormat + '/' + previewFormatString);






    screenResolution = new Point(WindowManagerHelper.getDisplayWidth(context), WindowManagerHelper.getDisplayHeight(context));
    Log.d(TAG, "Screen resolution: " + screenResolution);
//    cameraResolution = getCameraResolution(parameters, screenResolution);
    cameraResolution = findBestPreviewSizeValue(parameters);

    Log.d(TAG, "Camera resolution: " + screenResolution);
  }

  private Point findBestPreviewSizeValue(final Camera.Parameters parameters) {

    final List<Camera.Size> rawSupportedSizes = parameters
            .getSupportedPreviewSizes();
    if (rawSupportedSizes == null) {
      final Camera.Size defaultSize = parameters.getPreviewSize();
      return new Point(defaultSize.width, defaultSize.height);
    }

    // Sort by size, descending
    final List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(
            rawSupportedSizes);
    Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
      @Override
      public int compare(final Camera.Size a, final Camera.Size b) {
        final int aPixels = a.height * a.width;
        final int bPixels = b.height * b.width;
        if (bPixels < aPixels) {
          return -1;
        }
        if (bPixels > aPixels) {
          return 1;
        }
        return 0;
      }
    });

    final StringBuilder previewSizesString = new StringBuilder();
    for (final Camera.Size supportedPreviewSize : supportedPreviewSizes) {
      previewSizesString.append(supportedPreviewSize.width).append('x')
              .append(supportedPreviewSize.height).append(' ');
    }

    if (!supportedPreviewSizes.isEmpty()) {
      final Camera.Size largestPreview = supportedPreviewSizes.get(0);
      final Point largestSize = new Point(largestPreview.width,
              largestPreview.height);
      return largestSize;
    }

    // If there is nothing at all suitable, return current preview size
    final Camera.Size defaultPreview = parameters.getPreviewSize();
    final Point defaultSize = new Point(defaultPreview.width,
            defaultPreview.height);
    return defaultSize;
  }
  /**
   * Sets the camera up to take preview images which are used for both preview and decoding.
   * We detect the preview format here so that buildLuminanceSource() can build an appropriate
   * LuminanceSource subclass. In the future we may want to force YUV420SP as it's the smallest,
   * and the planar Y can be used for barcode scanning without a copy in some cases.
   */
  void setDesiredCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    Log.d(TAG, "Setting preview size: " + cameraResolution);

    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
    setFlash(parameters);
    setZoom(parameters);
    //setSharpness(parameters);
    //modify here
    camera.setDisplayOrientation(90);
    camera.setParameters(parameters);



  }

  Point getCameraResolution() {
    return cameraResolution;
  }

  Point getScreenResolution() {
    return screenResolution;
  }

  int getPreviewFormat() {
    return previewFormat;
  }

  String getPreviewFormatString() {
    return previewFormatString;
  }

  private static Point getCameraResolution(Camera.Parameters parameters, Point screenResolution) {

    String previewSizeValueString = parameters.get("preview-size-values");
    // saw this on Xperia
    if (previewSizeValueString == null) {
      previewSizeValueString = parameters.get("preview-size-value");
    }

    Point cameraResolution = null;

    if (previewSizeValueString != null) {
      Log.d(TAG, "preview-size-values parameter: " + previewSizeValueString);
      cameraResolution = findBestPreviewSizeValue(previewSizeValueString, screenResolution);
    }

    if (cameraResolution == null) {
      // Ensure that the camera resolution is a multiple of 8, as the screen may not be.
      cameraResolution = new Point(
          (screenResolution.x >> 3) << 3,
          (screenResolution.y >> 3) << 3);
    }

    return cameraResolution;
  }

  private static Point findBestPreviewSizeValue(CharSequence previewSizeValueString, Point screenResolution) {
    int bestX = 0;
    int bestY = 0;
    int diff = Integer.MAX_VALUE;
    for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {

      previewSize = previewSize.trim();
      int dimPosition = previewSize.indexOf('x');
      if (dimPosition < 0) {
        Log.w(TAG, "Bad preview-size: " + previewSize);
        continue;
      }

      int newX;
      int newY;
      try {
        newX = Integer.parseInt(previewSize.substring(0, dimPosition));
        newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
      } catch (NumberFormatException nfe) {
        Log.w(TAG, "Bad preview-size: " + previewSize);
        continue;
      }

      int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
      if (newDiff == 0) {
        bestX = newX;
        bestY = newY;
        break;
      } else if (newDiff < diff) {
        bestX = newX;
        bestY = newY;
        diff = newDiff;
      }

    }

    if (bestX > 0 && bestY > 0) {
      return new Point(bestX, bestY);
    }
    return null;
  }

  private static int findBestMotZoomValue(CharSequence stringValues, int tenDesiredZoom) {
    int tenBestValue = 0;
    for (String stringValue : COMMA_PATTERN.split(stringValues)) {
      stringValue = stringValue.trim();
      double value;
      try {
        value = Double.parseDouble(stringValue);
      } catch (NumberFormatException nfe) {
        return tenDesiredZoom;
      }
      int tenValue = (int) (10.0 * value);
      if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom - tenBestValue)) {
        tenBestValue = tenValue;
      }
    }
    return tenBestValue;
  }

  private void setFlash(Camera.Parameters parameters) {
    // FIXME: This is a hack to turn the flash off on the Samsung Galaxy.
    // And this is a hack-hack to work around a different value on the Behold II
    // Restrict Behold II check to Cupcake, per Samsung's advice
    //if (Build.MODEL.contains("Behold II") &&
    //    CameraManager.SDK_INT == Build.VERSION_CODES.CUPCAKE) {
    if (Build.MODEL.contains("Behold II") && CameraManager.SDK_INT == 3) { // 3 = Cupcake
      parameters.set("flash-value", 1);
    } else {
      parameters.set("flash-value", 2);
    }
    // This is the standard setting to turn the flash off that all devices should honor.
    parameters.set("flash-mode", "off");
  }

  private void setZoom(Camera.Parameters parameters) {

    String zoomSupportedString = parameters.get("zoom-supported");
    if (zoomSupportedString != null && !Boolean.parseBoolean(zoomSupportedString)) {
      return;
    }

    int tenDesiredZoom = TEN_DESIRED_ZOOM;

    String maxZoomString = parameters.get("max-zoom");
    if (maxZoomString != null) {
      try {
        int tenMaxZoom = (int) (10.0 * Double.parseDouble(maxZoomString));
        if (tenDesiredZoom > tenMaxZoom) {
          tenDesiredZoom = tenMaxZoom;
        }
      } catch (NumberFormatException nfe) {
        Log.w(TAG, "Bad max-zoom: " + maxZoomString);
      }
    }

    String takingPictureZoomMaxString = parameters.get("taking-picture-zoom-max");
    if (takingPictureZoomMaxString != null) {
      try {
        int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
        if (tenDesiredZoom > tenMaxZoom) {
          tenDesiredZoom = tenMaxZoom;
        }
      } catch (NumberFormatException nfe) {
        Log.w(TAG, "Bad taking-picture-zoom-max: " + takingPictureZoomMaxString);
      }
    }

    String motZoomValuesString = parameters.get("mot-zoom-values");
    if (motZoomValuesString != null) {
      tenDesiredZoom = findBestMotZoomValue(motZoomValuesString, tenDesiredZoom);
    }

    String motZoomStepString = parameters.get("mot-zoom-step");
    if (motZoomStepString != null) {
      try {
        double motZoomStep = Double.parseDouble(motZoomStepString.trim());
        int tenZoomStep = (int) (10.0 * motZoomStep);
        if (tenZoomStep > 1) {
          tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
        }
      } catch (NumberFormatException nfe) {
        // continue
      }
    }

    // Set zoom. This helps encourage the user to pull back.
    // Some devices like the Behold have a zoom parameter
    if (maxZoomString != null || motZoomValuesString != null) {
      parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
    }

    // Most devices, like the Hero, appear to expose this zoom parameter.
    // It takes on values like "27" which appears to mean 2.7x zoom
    if (takingPictureZoomMaxString != null) {
      parameters.set("taking-picture-zoom", tenDesiredZoom);
    }
  }

	public static int getDesiredSharpness() {
		return DESIRED_SHARPNESS;
	}

}
