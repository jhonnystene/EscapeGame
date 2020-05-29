package com.asuscomm.johnnystene.infinity;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.DataBuffer;
// fuck you java
import java.awt.image.Raster;
import java.awt.image.SampleModel;

public class FrameBuffer extends Raster {
	
	public int minX = 0;
	public int minY = 0;
	
	protected FrameBuffer(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle aRegion, Point sampleModelTranslate,
			Raster parent) {
		super(sampleModel, dataBuffer, aRegion, sampleModelTranslate, parent);
		minX = 0;
		minY = 0;
	}

	protected FrameBuffer(SampleModel sampleModel, DataBuffer dataBuffer, Point origin) {
		super(sampleModel, dataBuffer, origin);
		minX = 0;
		minY = 0;
	}
	
	protected FrameBuffer(SampleModel sampleModel, Point origin) {
		super(sampleModel, origin);
		minX = 0;
		minY = 0;
	}

}
