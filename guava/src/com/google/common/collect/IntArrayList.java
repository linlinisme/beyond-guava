
package com.google.common.collect;




import com.sun.istack.NotNull;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static com.google.common.collect.CollectionUtil.checkElementNotNull;

/**
 * A {@link List} implementation that stores int values with the ability to not have them boxed.
 * it is not allow null element,because it is designed for high performance calculate and null meaningless
 */
public class IntArrayList extends AbstractList<Integer> implements List<Integer>, RandomAccess, Serializable {


    /**
     * Initial capacity to which the array will be sized.
     */
    public static final int INITIAL_CAPACITY = 8;

    /**
     * Maximum capacity to which the array can grow.
     */
    public static final int MAX_CAPACITY = Integer.MAX_VALUE;

    private int size = 0;
    private int[] elements;

    public IntArrayList() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Construct a new list.
     *
     * @param initialCapacity for the backing array.
     */
    public IntArrayList(final int initialCapacity) {

        elements = new int[Math.max(initialCapacity, INITIAL_CAPACITY)];
    }

    /**
     * Create a new list that wraps an existing arrays without copying it.
     *
     * @param initialElements to be wrapped.
     * @param initialSize     of the array to wrap.
     */
    public IntArrayList(
            final int[] initialElements,
            final int initialSize) {
        wrap(initialElements, initialSize);
    }

    /**
     * Wrap an existing array without copying it.
     * <p>
     * The array length must be greater than or equal to {@link #INITIAL_CAPACITY}.
     *
     * @param initialElements to be wrapped.
     * @param initialSize     of the array to wrap.
     * @throws IllegalArgumentException if the initialSize is is less than {@link #INITIAL_CAPACITY} or greater than
     *                                  the length of the initial array.
     */
    public void wrap(
            final int[] initialElements,
            final int initialSize) {
        if (initialSize < 0 || initialSize > initialElements.length) {
            throw new IllegalArgumentException(
                    "illegal initial size " + initialSize + " for array length of " + initialElements.length);
        }

        if (initialElements.length < INITIAL_CAPACITY) {
            throw new IllegalArgumentException(
                    "illegal initial array length " + initialElements.length + ", minimum required is " + INITIAL_CAPACITY);
        }

        elements = initialElements;
        size = initialSize;
    }


    public int size() {
        return this.size;
    }

    public void clear() {
        this.size = 0;
    }

    /**
     * Trim the underlying array to be the current size, or {@link #INITIAL_CAPACITY} if size is less.
     */
    public void trimToSize() {
        if (elements.length != size && elements.length > INITIAL_CAPACITY) {
            elements = Arrays.copyOf(elements, Math.max(INITIAL_CAPACITY, size));
        }
    }

    public Integer get(
            final int index) {
        return  getInt(index);
    }

    /**
     * Get the element at a given index without boxing.
     *
     * @param index to get.
     * @return the unboxed element.
     */
    public int getInt(
             final int index)
    {
        checkIndex(index);

        return elements[index];
    }




    public boolean add(@NotNull final Integer element) {
        checkElementNotNull(element);
        return addInt(element);
    }

    /**
     * Add an element without boxing.
     *
     * @param element to be added.
     * @return true
     */
    public boolean addInt(final int element)
    {
        ensureCapacityPrivate(size + 1);
        elements[size] = element;
        size++;

        return true;
    }

    public void add(
            final int index,
            @NotNull final Integer element) {
        checkElementNotNull(element);
        add(index, element);
    }

    /**
     * Add a element without boxing at a given index.
     *
     * @param index   at which the element should be added.
     * @param element to be added.
     */
    public void addInt(
            final int index,
            final int element) {
        checkIndex(index);

        final int requiredSize = size + 1;
        ensureCapacityPrivate(requiredSize);

        if (index < size) {
            System.arraycopy(elements, index, elements, index + 1, size - index);
        }

        elements[index] = element;
        size++;
    }

    public Integer set(
            final int index,
            @NotNull  final Integer element) {
        checkElementNotNull(element);
        return setInt(index, element);
    }

    /**
     * Set an element at a given index without boxing.
     *
     * @param index   at which to set the element.
     * @param element to be added.
     * @return the previous element at the index.
     */
    public int setInt(
            final int index,
            final int element) {
        checkIndex(index);
        final int previous = elements[index];
        elements[index] = element;

        return previous;
    }

    /**
     * Does the list contain this element value.
     *
     * @param value of the element.
     * @return true if present otherwise false.
     */
    public boolean contains(final int value) {
        return -1 != indexOf(value);
    }

    /**
     * Index of the first element with this value.
     *
     * @param value for the element.
     * @return the index if found otherwise -1.
     */
    public int indexOf(
            final int value) {
        for (int i = 0; i < size; i++) {
            if (value == elements[i]) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Index of the last element with this value.
     *
     * @param value for the element.
     * @return the index if found otherwise -1.
     */
    public int lastIndexOf(
            final int value) {
        for (int i = size - 1; i >= 0; i--) {
            if (value == elements[i]) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Remove at a given index.
     *
     * @param index of the element to be removed.
     * @return the existing value at this index.
     */
    public Integer remove(
            final int index) {
        checkIndex(index);

        final int value = elements[index];

        final int moveCount = size - index - 1;
        if (moveCount > 0) {
            System.arraycopy(elements, index + 1, elements, index, moveCount);
        }

        size--;

        return value;
    }

    /**
     * Removes element at index, but instead of copying all elements to the left,
     * it replaces the item in the slot with the last item in the list. This avoids the copy
     * costs at the expense of preserving list order. If index is the last element it is just removed.
     *
     * @param index of the element to be removed.
     * @return the existing value at this index.
     * @throws IndexOutOfBoundsException if index is out of bounds.
     */
    public int fastUnorderedRemove(
            final int index) {
        checkIndex(index);

        final int value = elements[index];
        elements[index] = elements[--size];

        return value;
    }

    /**
     * Remove the first instance of a value if found in the list.
     *
     * @param value to be removed.
     * @return true if successful otherwise false.
     */
    public boolean removeInt(final int value) {
        final int index = indexOf(value);
        if (-1 != index) {
            remove(index);

            return true;
        }

        return false;
    }

    /**
     * Remove the first instance of a value if found in the list and replaces it with the last item
     * in the list. This saves a copy down of all items at the expense of not preserving list order.
     *
     * @param value to be removed.
     * @return true if successful otherwise false.
     */
    public boolean fastUnorderedRemoveInt(final int value) {
        final int index = indexOf(value);
        if (-1 != index) {
            elements[index] = elements[--size];

            return true;
        }

        return false;
    }

    /**
     * Push an element onto the end of the array like a stack.
     *
     * @param element to be pushed onto the end of the array.
     */
    public void pushInt(final int element) {
        ensureCapacityPrivate(size + 1);

        elements[size] = element;
        size++;
    }

    /**
     * Pop a value off the end of the array as a stack operation.
     *
     * @return the value at the end of the array.
     * @throws NoSuchElementException if the array is empty.
     */
    public int popInt() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return elements[--size];
    }

    /**
     * For each element in order provide the int value to a {@link IntConsumer}.
     *
     * @param consumer for each element.
     */
    public void forEachOrderedInt(final IntConsumer consumer) {
        for (int i = 0; i < size; i++) {
            consumer.accept(elements[i]);
        }
    }

    /**
     * Create a {@link IntStream} over the elements of underlying array.
     *
     * @return a {@link IntStream} over the elements of underlying array.
     */
    public IntStream intStream() {
        return Arrays.stream(elements, 0, size);
    }

    /**
     * Create a new array that is a copy of the elements.
     *
     * @return a copy of the elements.
     */
    public int[] toIntArray() {
        return Arrays.copyOf(elements, size);
    }

    /**
     * Create a new array that is a copy of the elements.
     *
     * @param dst destination array for the copy if it is the correct size.
     * @return a copy of the elements.
     */
    public int[] toIntArray(final int[] dst) {
        if (dst.length == size) {
            System.arraycopy(elements, 0, dst, 0, dst.length);
            return dst;
        } else {
            return Arrays.copyOf(elements, size);
        }
    }

    /**
     * Ensure the backing array has a required capacity.
     *
     * @param requiredCapacity for the backing array.
     */
    public void ensureCapacity(final int requiredCapacity) {
        ensureCapacityPrivate(Math.max(requiredCapacity, INITIAL_CAPACITY));
    }

    /**
     * compare is the same array
     * @param that
     * @return
     */
    public boolean equals(final IntArrayList that) {

        if (that == this) {
            return true;
        }
        if (this.size() != that.size()) {
            return false;
        }

        for (int i = 0; i < this.size(); i++) {
            if (this.getInt(i) != that.getInt(i)) {
                return false;
            }
        }

        return true;


    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }

        boolean isEqual = false;

        if (other instanceof IntArrayList) {
            return equals((IntArrayList) other);
        } else if (other instanceof List) {
            final List<?> that = (List<?>) other;

            if (this.size == ((List) other).size()) {
                isEqual = true;
                int i = 0;

                for (final Object o : that) {
                    if (o == null || o instanceof Integer) {
                        final Integer thisValue = get(i++);
                        final Integer thatValue = (Integer) o;

                        if (Objects.equals(thisValue, thatValue)) {
                            continue;
                        }
                    }

                    isEqual = false;
                    break;
                }
            }
        }

        return isEqual;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < size; i++) {
            final int value = elements[i];
            hashCode = 31 * hashCode +  Hashing.hash(value);
        }

        return hashCode;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            final int value = elements[i];

                sb.append(value);
                sb.append(", ");

        }

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }

        sb.append(']');

        return sb.toString();
    }

    private void ensureCapacityPrivate(final int requiredCapacity) {
        final int currentCapacity = elements.length;
        if (requiredCapacity > currentCapacity) {
            int newCapacity = currentCapacity + (currentCapacity >> 1);

            if (newCapacity < 0 || newCapacity > MAX_CAPACITY) {
                if (currentCapacity == MAX_CAPACITY) {
                    throw new IllegalStateException("max capacity reached: " + MAX_CAPACITY);
                }

                newCapacity = MAX_CAPACITY;
            }

            final int[] newElements = new int[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, currentCapacity);
            elements = newElements;
        }
    }

    private void checkIndex(final int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("index=" + index + " size=" + size);
        }
    }

    /**
     * Checks if the given index is in range.  If not, throws an appropriate
     * runtime exception.  This method does *not* check if the index is
     * negative: It is always used immediately prior to an array access,
     * which throws an ArrayIndexOutOfBoundsException if index is negative.
     */
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Constructs an IndexOutOfBoundsException detail message.
     * Of the many possible refactorings of the error handling code,
     * this "outlining" performs best with both server and client VMs.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }








}
