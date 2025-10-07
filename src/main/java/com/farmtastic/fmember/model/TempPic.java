package com.farmtastic.fmember.model;

import java.io.Serializable;

public class TempPic implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private byte[] fmemPic;
	private byte[] organicPic;
	private byte[] landPic;
	private byte[] insurPic;
	private byte[] storePic;
	
	public TempPic() {}

	public byte[] getFmemPic() {
		return fmemPic;
	}

	public void setFmemPic(byte[] fmemPic) {
		this.fmemPic = fmemPic;
	}

	public byte[] getOrganicPic() {
		return organicPic;
	}

	public void setOrganicPic(byte[] organicPic) {
		this.organicPic = organicPic;
	}

	public byte[] getLandPic() {
		return landPic;
	}

	public void setLandPic(byte[] landPic) {
		this.landPic = landPic;
	}

	public byte[] getInsurPic() {
		return insurPic;
	}

	public void setInsurPic(byte[] insurPic) {
		this.insurPic = insurPic;
	}

	public byte[] getStorePic() {
		return storePic;
	}

	public void setStorePic(byte[] storePic) {
		this.storePic = storePic;
	}

}
