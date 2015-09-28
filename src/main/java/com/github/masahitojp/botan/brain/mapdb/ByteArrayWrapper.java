package com.github.masahitojp.botan.brain.mapdb;

import org.mapdb.Serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public final class ByteArrayWrapper implements Serializable, Serializer<ByteArrayWrapper> {
	private static final long serialVersionUID = 101L;
	private final byte[] data;

	public ByteArrayWrapper(byte[] data) {
		if (data == null) {
			throw new NullPointerException();
		}
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof ByteArrayWrapper && Arrays.equals(data, ((ByteArrayWrapper) other).data);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(data);
	}

	@Override
	public void serialize(final DataOutput out, final ByteArrayWrapper value) throws IOException {
		out.writeInt(value.data.length);
		out.write(value.getData());
	}

	@Override
	public ByteArrayWrapper deserialize(final DataInput in, final int available) throws IOException {
		byte[] ary = new byte[in.readInt()];
		in.readFully(ary);
		return new  ByteArrayWrapper(ary);
	}

	@Override
	public int fixedSize() {
		return this.data.length;
	}


}
