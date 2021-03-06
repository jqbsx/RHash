/*
 * This file is a part of Java Bindings for Librhash
 *
 * Copyright (c) 2011, Sergey Basalaev <sbasalaev@gmail.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE  INCLUDING ALL IMPLIED WARRANTIES OF  MERCHANTABILITY
 * AND FITNESS.  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT,  OR CONSEQUENTIAL DAMAGES  OR ANY DAMAGES WHATSOEVER RESULTING FROM
 * LOSS OF USE,  DATA OR PROFITS,  WHETHER IN AN ACTION OF CONTRACT,  NEGLIGENCE
 * OR OTHER TORTIOUS ACTION,  ARISING OUT OF  OR IN CONNECTION  WITH THE USE  OR
 * PERFORMANCE OF THIS SOFTWARE.
 */

package org.sf.rhash;

/**
 * Message digest.
 */
public final class Digest {

	static final int RAW = 0x1;
	static final int HEX = 0x2;
	static final int BASE32 = 0x3;
	static final int BASE64 = 0x4;
	static final int UPPERCASE = 0x8;
	static final int REVERSE   = 0x10;

	private final HashType type;

	/** Pointer to native structure. */
	private final long digest_ptr;

	/**
	 * Creates new <code>Digest</code>.
	 * @param ptr   pointer to the native object
	 * @param type  hash type
	 */
	Digest(long ptr, HashType type) {
		this.digest_ptr = ptr;
		this.type = type;
	}

	/**
	 * Returns type of hashing algorithm that produced
	 * this digest.
	 *
	 * @return type of hashing algorithm
	 */
	public HashType hashType() {
		return type;
	}

	/**
	 * Returns value of this digest as raw bytes.
	 * This method allocates new byte array, modifying it
	 * has no effect on this <code>Digest</code>.
	 *
	 * @return  value of this digest as raw bytes
	 * @see #hex()
	 * @see #base32()
	 * @see #base64()
	 */
	public byte[] raw() {
		return Bindings.rhash_print_bytes(digest_ptr, RAW);
	}

	/**
	 * Returns value of this digest as hexadecimal string.
	 *
	 * @return value of the digest as hexadecimal string
	 * @see #raw()
	 * @see #base32()
	 * @see #base64()
	 */
	public String hex() {
		return new String(Bindings.rhash_print_bytes(digest_ptr, HEX));
	}

	/**
	 * Returns value of this digest as base32 string.
	 *
	 * @return value of the digest as base32 string
	 * @see #raw()
	 * @see #hex()
	 * @see #base64()
	 */
	public String base32() {
		return new String(Bindings.rhash_print_bytes(digest_ptr, BASE32));
	}

	/**
	 * Returns value of this digest as base64 string.
	 *
	 * @return value of the digest as base64 string
	 * @see #raw()
	 * @see #hex()
	 * @see #base32()
	 */
	public String base64() {
		return new String(Bindings.rhash_print_bytes(digest_ptr, BASE64));
	}

	/**
	 * Called by garbage collector to free native resources.
	 */
	@Override
	protected void finalize() {
		Bindings.freeDigest(digest_ptr);
	}

	/**
	 * Returns string representation of this object.
	 * If default output for hashing algorithm is base32 then
	 * returned value is the same as if <code>base32()</code>
	 * method was called; otherwise value is the same as returned
	 * by <code>hex()</code> method.
	 *
	 * @return string representation of this object
	 * @see #base32()
	 * @see #hex()
	 */
	@Override
	public String toString() {
		return (Bindings.rhash_is_base32(type.hashId())) ? base32() : hex();
	}

	/**
	 * Tests whether this object equals to another one
	 * @param  obj  object to compare to
	 * @return
	 *   <code>true</code> if <code>obj</code> is <code>Digest</code>
	 *   instance with the same <code>HashType</code> and value;
	 *   otherwise <code>false</code>
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Digest)) return false;
		final Digest other = (Digest)obj;
		if (!this.hashType().equals(other.hashType())) return false;
		if (this.digest_ptr == other.digest_ptr) return true;
		return Bindings.compareDigests(this.digest_ptr, other.digest_ptr);
	}

	/**
	 * Returns hash code for this object.
	 * @return hash code for the object
	 */
	@Override
	public int hashCode() {
		return Bindings.hashcodeForDigest(digest_ptr);
	}
}
