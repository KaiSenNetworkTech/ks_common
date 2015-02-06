package com.kaisen.common.xmemcached.transcoders;

import java.nio.charset.StandardCharsets;

import net.rubyeye.xmemcached.transcoders.CachedData;
import net.rubyeye.xmemcached.transcoders.CompressionMode;
import net.rubyeye.xmemcached.transcoders.Transcoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class FastJsonTranscoder implements Transcoder<Object> {
	@Override
	public CachedData encode(Object o) {
		CachedData cachedData = new CachedData();
		cachedData.setCapacity(CachedData.MAX_SIZE);
		cachedData.setData(JSON.toJSONString(o)
				.getBytes(StandardCharsets.UTF_8));
		return cachedData;
	}

	@Override
	public Object decode(CachedData d) {
		Object obj = d.decodedObject;
		if (obj != null) {
			return obj;
		}
		byte[] data = d.getData();
		obj = JSON.parse(data, new Feature[0]);
		d.decodedObject = obj;
		return obj;
	}

	@Override
	public void setPrimitiveAsString(boolean primitiveAsString) {
	}

	@Override
	public void setPackZeros(boolean packZeros) {
	}

	@Override
	public boolean isPrimitiveAsString() {
		return false;
	}

	@Override
	public boolean isPackZeros() {
		return false;
	}

	@Override
	public void setCompressionThreshold(int to) {
	}

	@Override
	public void setCompressionMode(CompressionMode compressMode) {
	}
}
