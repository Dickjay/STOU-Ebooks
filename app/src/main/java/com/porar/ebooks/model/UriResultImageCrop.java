package com.porar.ebooks.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;

public class UriResultImageCrop {
	private static Uri uri, urisave;
	private static Bitmap bmp;
	private static Matrix matrix1;
	private static Matrix matrix2;
	private static float delta = 1F;

	/**
	 * @return the uri
	 */
	public static Uri getUri() {
		return uri;
	}
	public static Uri getUriSave() {
		return urisave;
	}
	/**
	 * @param uri
	 *            the uri to set
	 */
	public static void setUri(Uri Uri) {
		uri = Uri;
	}

	public static void setUriSave(Uri Urisave) {
		urisave = Urisave;
	}

	/**
	 * @return the bitmap
	 */
	public static Bitmap getBitmap() {
		return bmp;
	}

	/**
	 * @param bitmap
	 *            the bitmap to set
	 */
	public static void setBitmap(Bitmap bitmap) {
		bmp = bitmap;
	}

	/**
	 * @return the matrix
	 */
	public static Matrix getMatrix1() {
		return matrix1;
	}

	public static Matrix getMatrix2() {
		return matrix2;
	}

	/**
	 * @param matrix
	 *            the matrix to set
	 */
	public static void setMatrix(Matrix Matrix1, Matrix Matrix2) {
		UriResultImageCrop.matrix1 = Matrix1;
		UriResultImageCrop.matrix2 = Matrix2;
	}

	/**
	 * @return the delta
	 */
	public static float getDelta() {
		return delta;
	}

	/**
	 * @param delta
	 *            the delta to set
	 */
	public static void setDelta(float delta) {
		UriResultImageCrop.delta = delta;
	}

	/**
	 * @return the deltaScale
	 */

}
