package jv.android.utils;

public class CellID {
	private int _cellID = 0;
	private int _lac = 0;
	private int _mnc = 0;
	private int _mcc = 0;
	
	public int getCellID() {
		return _cellID;
	}
	public void setCellID(int _cellID) {
		this._cellID = _cellID;
	}
	public int getLac() {
		return _lac;
	}
	public void setLac(int _lac) {
		this._lac = _lac;
	}
	public int getMnc() {
		return _mnc;
	}
	public void setMnc(int _mnc) {
		this._mnc = _mnc;
	}
	public int getMcc() {
		return _mcc;
	}
	public void setMcc(int _mcc) {
		this._mcc = _mcc;
	}
}
