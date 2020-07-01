/*******************************************************************************
 * Copyright 2020 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.core.collections.concurrent;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.lock.ReadWriteLock;
import org.mini2Dx.gdx.utils.Bits;

public class ConcurrentBits extends Bits implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    public ConcurrentBits() {
    }

    /**
     * Creates a bit set whose initial size is large enough to explicitly represent bits with indices in the range 0 through
     * nbits-1.
     *
     * @param nbits the initial size of the bit set
     */
    public ConcurrentBits(int nbits) {
        super(nbits);
    }

    /**
     * @param index the index of the bit
     * @return whether the bit is set
     * @throws ArrayIndexOutOfBoundsException if index is less than 0
     */
    @Override
    public boolean get(int index) {
        lock.lockRead();
        boolean b = super.get(index);
        lock.unlockRead();
        return b;
    }

    /**
     * Returns the bit at the given index and clears it in one go.
     *
     * @param index the index of the bit
     * @return whether the bit was set before invocation
     * @throws ArrayIndexOutOfBoundsException if index is less than 0
     */
    @Override
    public boolean getAndClear(int index) {
        lock.lockWrite();
        boolean b = super.getAndClear(index);
        lock.unlockWrite();
        return b;
    }

    /**
     * Returns the bit at the given index and sets it in one go.
     *
     * @param index the index of the bit
     * @return whether the bit was set before invocation
     * @throws ArrayIndexOutOfBoundsException if index is less than 0
     */
    @Override
    public boolean getAndSet(int index) {
        lock.lockWrite();
        boolean b = super.getAndSet(index);
        lock.unlockWrite();
        return b;
    }

    /**
     * @param index the index of the bit to set
     * @throws ArrayIndexOutOfBoundsException if index is less than 0
     */
    @Override
    public void set(int index) {
        lock.lockWrite();
        super.set(index);
        lock.unlockWrite();
    }

    /**
     * @param index the index of the bit to flip
     */
    @Override
    public void flip(int index) {
        lock.lockWrite();
        super.flip(index);
        lock.unlockWrite();
    }

    /**
     * @param index the index of the bit to clear
     * @throws ArrayIndexOutOfBoundsException if index is less than 0
     */
    @Override
    public void clear(int index) {
        lock.lockWrite();
        super.clear(index);
        lock.unlockWrite();
    }

    /**
     * Clears the entire bitset
     */
    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    /**
     * @return the number of bits currently stored, <b>not</b> the highset set bit!
     */
    @Override
    public int numBits() {
        lock.lockRead();
        int n = super.numBits();
        lock.unlockRead();
        return n;
    }

    /**
     * Returns the "logical size" of this bitset: the index of the highest set bit in the bitset plus one. Returns zero if the
     * bitset contains no set bits.
     *
     * @return the logical size of this bitset
     */
    @Override
    public int length() {
        lock.lockRead();
        int l = super.length();
        lock.unlockRead();
        return l;
    }

    /**
     * @return true if this bitset contains no bits that are set to true
     */
    @Override
    public boolean isEmpty() {
        lock.lockRead();
        boolean b = super.isEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns the index of the first bit that is set to true that occurs on or after the specified starting index. If no such bit
     * exists then -1 is returned.
     *
     * @param fromIndex
     */
    @Override
    public int nextSetBit(int fromIndex) {
        lock.unlockRead();
        int i = super.nextSetBit(fromIndex);
        lock.unlockRead();
        return i;
    }

    /**
     * Returns the index of the first bit that is set to false that occurs on or after the specified starting index.
     *
     * @param fromIndex
     */
    @Override
    public int nextClearBit(int fromIndex) {
        lock.lockRead();
        int i = super.nextClearBit(fromIndex);
        lock.unlockRead();
        return i;
    }

    /**
     * Performs a logical <b>AND</b> of this target bit set with the argument bit set. This bit set is modified so that each bit in
     * it has the value true if and only if it both initially had the value true and the corresponding bit in the bit set argument
     * also had the value true.
     *
     * @param other a bit set
     */
    @Override
    public void and(Bits other) {
        boolean isOtherConcurrent = other instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().lockRead();
        }
        lock.lockWrite();
        super.and(other);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().unlockRead();
        }
    }

    /**
     * Clears all of the bits in this bit set whose corresponding bit is set in the specified bit set.
     *
     * @param other a bit set
     */
    @Override
    public void andNot(Bits other) {
        boolean isOtherConcurrent = other instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().lockRead();
        }
        lock.lockWrite();
        super.andNot(other);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().unlockRead();
        }
    }

    /**
     * Performs a logical <b>OR</b> of this bit set with the bit set argument. This bit set is modified so that a bit in it has the
     * value true if and only if it either already had the value true or the corresponding bit in the bit set argument has the
     * value true.
     *
     * @param other a bit set
     */
    @Override
    public void or(Bits other) {
        boolean isOtherConcurrent = other instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().lockRead();
        }
        lock.lockWrite();
        super.or(other);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().unlockRead();
        }
    }

    /**
     * Performs a logical <b>XOR</b> of this bit set with the bit set argument. This bit set is modified so that a bit in it has
     * the value true if and only if one of the following statements holds:
     * <ul>
     * <li>The bit initially has the value true, and the corresponding bit in the argument has the value false.</li>
     * <li>The bit initially has the value false, and the corresponding bit in the argument has the value true.</li>
     * </ul>
     *
     * @param other
     */
    @Override
    public void xor(Bits other) {
        boolean isOtherConcurrent = other instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().lockRead();
        }
        lock.lockWrite();
        super.xor(other);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().unlockRead();
        }
    }

    /**
     * Returns true if the specified BitSet has any bits set to true that are also set to true in this BitSet.
     *
     * @param other a bit set
     * @return boolean indicating whether this bit set intersects the specified bit set
     */
    @Override
    public boolean intersects(Bits other) {
        boolean isOtherConcurrent = other instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().lockRead();
        }
        lock.lockRead();
        boolean b = super.intersects(other);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().unlockRead();
        }
        return b;
    }

    /**
     * Returns true if this bit set is a super set of the specified set, i.e. it has all bits set to true that are also set to true
     * in the specified BitSet.
     *
     * @param other a bit set
     * @return boolean indicating whether this bit set is a super set of the specified set
     */
    @Override
    public boolean containsAll(Bits other) {
        boolean isOtherConcurrent = other instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().lockRead();
        }
        lock.lockRead();
        boolean b = super.containsAll(other);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection) other).getLock().unlockRead();
        }
        return b;
    }

    @Override
    public int hashCode() {
        lock.lockRead();
        int hc = super.hashCode();
        lock.unlockRead();
        return hc;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isOtherConcurrent = obj instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) obj).getLock().lockRead();
        }
        lock.lockRead();
        boolean b = super.equals(obj);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection) obj).getLock().unlockRead();
        }
        return b;
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
