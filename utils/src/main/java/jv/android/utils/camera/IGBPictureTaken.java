package jv.android.utils.camera;

public interface IGBPictureTaken {

	public void onPictureTaken(String fileName);
	public void onPictureError(String message);
}
