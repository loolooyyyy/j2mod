/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.koosha.modbus.util;

import java.io.Serializable;
import java.util.*;


/**
 * Range class is stolen from guava.
 *
 * A range (or "interval") defines the <i>boundaries</i> around a contiguous span of values of some
 * {@code Comparable} type; for example, "integers from 1 to 100 inclusive." Note that it is not
 * possible to <i>iterate</i> over these contained values. To do so, pass this range instance and an
 * appropriate {@link DiscreteDomain} to ContiguousSet#create
 *
 * <h3>Types of ranges</h3>
 *
 * <p>Each end of the range may be bounded or unbounded. If bounded, there is an associated
 * <i>endpoint</i> value, and the range is considered to be either <i>open</i> (does not include the
 * endpoint) or <i>closed</i> (includes the endpoint) on that side. With three possibilities on each
 * side, this yields nine basic types of ranges, enumerated below. (Notation: a square bracket
 * ({@code [ ]}) indicates that the range is closed on that side; a parenthesis ({@code ( )}) means
 * it is either open or unbounded. The construct {@code {x | statement}} is read "the set of all
 * <i>x</i> such that <i>statement</i>.")
 *
 * <blockquote>
 *
 * <table>
 * <caption>Range Types</caption>
 * <tr><th>Notation        <th>Definition               <th>Factory method
 * <tr><td>{@code (a..b)}  <td>{@code {x | a < x < b}}  <td>{@link Range#open open}
 * <tr><td>{@code [a..b]}  <td>{@code {x | a <= x <= b}}<td>{@link Range#closed closed}
 * <tr><td>{@code (a..b]}  <td>{@code {x | a < x <= b}} <td>{@link Range#openClosed openClosed}
 * <tr><td>{@code [a..b)}  <td>{@code {x | a <= x < b}} <td>{@link Range#closedOpen closedOpen}
 * <tr><td>{@code (a..+∞)} <td>{@code {x | x > a}}      <td>{@link Range#greaterThan greaterThan}
 * <tr><td>{@code [a..+∞)} <td>{@code {x | x >= a}}     <td>{@link Range#atLeast atLeast}
 * <tr><td>{@code (-∞..b)} <td>{@code {x | x < b}}      <td>{@link Range#lessThan lessThan}
 * <tr><td>{@code (-∞..b]} <td>{@code {x | x <= b}}     <td>{@link Range#atMost atMost}
 * <tr><td>{@code (-∞..+∞)}<td>{@code {x}}              <td>{@link Range#all all}
 * </table>
 *
 * </blockquote>
 *
 * <p>When both endpoints exist, the upper endpoint may not be less than the lower. The endpoints
 * may be equal only if at least one of the bounds is closed:
 *
 * <ul>
 * <li>{@code [a..a]} : a singleton range
 * <li>{@code [a..a); (a..a]} : {@linkplain #isEmpty empty} ranges; also valid
 * <li>{@code (a..a)} : <b>invalid</b>; an exception will be thrown
 * </ul>
 *
 * <h3>Warnings</h3>
 *
 * <ul>
 * <li>Use immutable value types only, if at all possible. If you must use a mutable type, <b>do
 * not</b> allow the endpoint instances to mutate after the range is created!
 * <li>Your value type's comparison method should be {@linkplain Comparable consistent with
 * equals} if at all possible. Otherwise, be aware that concepts used throughout this
 * documentation such as "equal", "same", "unique" and so on actually refer to whether {@link
 * Comparable#compareTo compareTo} returns zero, not whether {@link Object#equals equals}
 * returns {@code true}.
 * <li>A class which implements {@code Comparable<UnrelatedType>} is very broken, and will cause
 * undefined horrible things to happen in {@code Range}. For now, the Range API does not
 * prevent its use, because this would also rule out all ungenerified (pre-JDK1.5) data types.
 * <b>This may change in the future.</b>
 * </ul>
 *
 * <h3>Other notes</h3>
 *
 * <ul>
 * <li>Instances of this type are obtained using the static factory methods in this class.
 * <li>Ranges are <i>convex</i>: whenever two values are contained, all values in between them
 * must also be contained. More formally, for any {@code c1 <= c2 <= c3} of type {@code C},
 * {@code r.contains(c1) && r.contains(c3)} implies {@code r.contains(c2)}). This means that a
 * {@code Range<Integer>} can never be used to represent, say, "all <i>prime</i> numbers from
 * 1 to 100."
 * <li>When evaluated as a Predicate, a range yields the same result as invoking {@link
 * #contains}.
 * <li>Terminology note: a range {@code a} is said to be the <i>maximal</i> range having property
 * <i>P</i> if, for all ranges {@code b} also having property <i>P</i>, {@code a.encloses(b)}.
 * Likewise, {@code a} is <i>minimal</i> when {@code b.encloses(a)} for all {@code b} having
 * property <i>P</i>. See, for example, the definition of {@link #intersection intersection}.
 * </ul>
 *
 * <h3>Further reading</h3>
 *
 * <p>See the Guava User Guide article on <a
 * href="https://github.com/google/guava/wiki/RangesExplained">{@code Range}</a>.
 *
 * @author Kevin Bourrillion
 * @author Gregory Kick
 * @since 10.0
 */
@SuppressWarnings({"rawtypes", "WeakerAccess", "Duplicates"})
public final class Range<C extends Comparable> implements Serializable {

    private static <T> T checkNotNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    private static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
        return new Range<C>(lowerBound, upperBound);
    }

    /**
     * Returns a range that contains all values strictly greater than {@code lower} and strictly less
     * than {@code upper}.
     *
     * @throws IllegalArgumentException if {@code lower} is greater than <i>or equal to</i> {@code
     *                                  upper}
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> open(C lower, C upper) {
        return create(Cut.aboveValue(lower), Cut.belowValue(upper));
    }

    /**
     * Returns a range that contains all values greater than or equal to {@code lower} and less than
     * or equal to {@code upper}.
     *
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
        return create(Cut.belowValue(lower), Cut.aboveValue(upper));
    }

    /**
     * Returns a range that contains all values greater than or equal to {@code lower} and strictly
     * less than {@code upper}.
     *
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) {
        return create(Cut.belowValue(lower), Cut.belowValue(upper));
    }

    /**
     * Returns a range that contains all values strictly greater than {@code lower} and less than or
     * equal to {@code upper}.
     *
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) {
        return create(Cut.aboveValue(lower), Cut.aboveValue(upper));
    }

    /**
     * Returns a range that contains any value from {@code lower} to {@code upper}, where each
     * endpoint may be either inclusive (closed) or exclusive (open).
     *
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> range(
            C lower, BoundType lowerType, C upper, BoundType upperType) {
        checkNotNull(lowerType);
        checkNotNull(upperType);

        Cut<C> lowerBound =
                (lowerType == BoundType.OPEN) ? Cut.aboveValue(lower) : Cut.belowValue(lower);
        Cut<C> upperBound =
                (upperType == BoundType.OPEN) ? Cut.belowValue(upper) : Cut.aboveValue(upper);
        return create(lowerBound, upperBound);
    }

    /**
     * Returns a range that contains all values strictly less than {@code endpoint}.
     *
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
        return create(Cut.<C>belowAll(), Cut.belowValue(endpoint));
    }

    /**
     * Returns a range that contains all values less than or equal to {@code endpoint}.
     *
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
        return create(Cut.<C>belowAll(), Cut.aboveValue(endpoint));
    }

    /**
     * Returns a range with no lower bound up to the given endpoint, which may be either inclusive
     * (closed) or exclusive (open).
     *
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
        switch (boundType) {
            case OPEN:
                return lessThan(endpoint);
            case CLOSED:
                return atMost(endpoint);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a range that contains all values strictly greater than {@code endpoint}.
     *
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
        return create(Cut.aboveValue(endpoint), Cut.<C>aboveAll());
    }

    /**
     * Returns a range that contains all values greater than or equal to {@code endpoint}.
     *
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
        return create(Cut.belowValue(endpoint), Cut.<C>aboveAll());
    }

    /**
     * Returns a range from the given endpoint, which may be either inclusive (closed) or exclusive
     * (open), with no upper bound.
     *
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
        switch (boundType) {
            case OPEN:
                return greaterThan(endpoint);
            case CLOSED:
                return atLeast(endpoint);
            default:
                throw new AssertionError();
        }
    }

    private static final Range<Comparable> ALL = new Range<Comparable>(Cut.belowAll(), Cut.aboveAll());

    /**
     * Returns a range that contains every value of type {@code C}.
     *
     * @since 14.0
     */
    @SuppressWarnings("unchecked")
    public static <C extends Comparable<?>> Range<C> all() {
        return (Range) ALL;
    }

    /**
     * Returns a range that {@linkplain Range#contains(Comparable) contains} only the given value. The
     * returned range is {@linkplain BoundType#CLOSED closed} on both ends.
     *
     * @since 14.0
     */
    public static <C extends Comparable<?>> Range<C> singleton(C value) {
        return closed(value, value);
    }

    /**
     * Returns the minimal range that {@linkplain Range#contains(Comparable) contains} all of the
     * given values. The returned range is {@linkplain BoundType#CLOSED closed} on both ends.
     *
     * @throws ClassCastException     if the parameters are not <i>mutually comparable</i>
     * @throws NoSuchElementException if {@code values} is empty
     * @throws NullPointerException   if any of {@code values} is null
     * @since 14.0
     */
    @SuppressWarnings("unchecked")
    public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
        checkNotNull(values);
        if (values instanceof SortedSet) {
            SortedSet<? extends C> set = cast(values);
            Comparator<?> comparator = set.comparator();
            if (comparator == null) {
                return closed(set.first(), set.last());
            }
        }
        Iterator<C> valueIterator = values.iterator();
        C min = checkNotNull(valueIterator.next());
        C max = min;
        while (valueIterator.hasNext()) {
            C value = checkNotNull(valueIterator.next());
            min = (((Comparable) min).compareTo(value) <= 0) ? min : value;
            max = (((Comparable) max).compareTo(value) >= 0) ? max : value;
        }
        return closed(min, max);
    }

    private final Cut<C> lowerBound;
    private final Cut<C> upperBound;

    private Range(Cut<C> lowerBound, Cut<C> upperBound) {
        this.lowerBound = checkNotNull(lowerBound);
        this.upperBound = checkNotNull(upperBound);
        if (lowerBound.compareTo(upperBound) > 0
                || lowerBound == Cut.<C>aboveAll()
                || upperBound == Cut.<C>belowAll()) {
            throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
        }
    }

    /**
     * Returns {@code true} if this range has a lower endpoint.
     */
    public boolean hasLowerBound() {
        return lowerBound != Cut.belowAll();
    }

    /**
     * Returns the lower endpoint of this range.
     *
     * @throws IllegalStateException if this range is unbounded below (that is, {@link
     *                               #hasLowerBound()} returns {@code false})
     */
    public C lowerEndpoint() {
        return lowerBound.endpoint();
    }

    /**
     * Returns the type of this range's lower bound: {@link BoundType#CLOSED} if the range includes
     * its lower endpoint, {@link BoundType#OPEN} if it does not.
     *
     * @throws IllegalStateException if this range is unbounded below (that is, {@link
     *                               #hasLowerBound()} returns {@code false})
     */
    public BoundType lowerBoundType() {
        return lowerBound.typeAsLowerBound();
    }

    /**
     * Returns {@code true} if this range has an upper endpoint.
     */
    public boolean hasUpperBound() {
        return upperBound != Cut.aboveAll();
    }

    /**
     * Returns the upper endpoint of this range.
     *
     * @throws IllegalStateException if this range is unbounded above (that is, {@link
     *                               #hasUpperBound()} returns {@code false})
     */
    public C upperEndpoint() {
        return upperBound.endpoint();
    }

    /**
     * Returns the type of this range's upper bound: {@link BoundType#CLOSED} if the range includes
     * its upper endpoint, {@link BoundType#OPEN} if it does not.
     *
     * @throws IllegalStateException if this range is unbounded above (that is, {@link
     *                               #hasUpperBound()} returns {@code false})
     */
    public BoundType upperBoundType() {
        return upperBound.typeAsUpperBound();
    }

    /**
     * Returns {@code true} if this range is of the form {@code [v..v)} or {@code (v..v]}. (This does
     * not encompass ranges of the form {@code (v..v)}, because such ranges are <i>invalid</i> and
     * can't be constructed at all.)
     *
     * <p>Note that certain discrete ranges such as the integer range {@code (3..4)} are <b>not</b>
     * considered empty, even though they contain no actual values. In these cases, it may be helpful
     * to preprocess ranges with canonical(DiscreteDomain
     */
    public boolean isEmpty() {
        return lowerBound.equals(upperBound);
    }

    /**
     * Returns {@code true} if {@code value} is within the bounds of this range. For example, on the
     * range {@code [0..2)}, {@code contains(1)} returns {@code true}, while {@code contains(2)}
     * returns {@code false}.
     */
    public boolean contains(C value) {
        checkNotNull(value);
        // let this throw CCE if there is some trickery going on
        return lowerBound.isLessThan(value) && !upperBound.isLessThan(value);
    }

    /**
     * Returns {@code true} if every element in {@code values} is {@linkplain #contains contained} in
     * this range.
     */
    public boolean containsAll(Iterable<? extends C> values) {
        if (values instanceof Collection && ((Collection<?>) values).isEmpty()
                || !values.iterator().hasNext())
            return true;

        // this optimizes testing equality of two range-backed sets
        if (values instanceof SortedSet) {
            SortedSet<? extends C> set = cast(values);
            Comparator<?> comparator = set.comparator();
            if (comparator == null) {
                return contains(set.first()) && contains(set.last());
            }
        }

        for (C value : values) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if the bounds of {@code other} do not extend outside the bounds of this
     * range. Examples:
     *
     * <ul>
     * <li>{@code [3..6]} encloses {@code [4..5]}
     * <li>{@code (3..6)} encloses {@code (3..6)}
     * <li>{@code [3..6]} encloses {@code [4..4)} (even though the latter is empty)
     * <li>{@code (3..6]} does not enclose {@code [3..6]}
     * <li>{@code [4..5]} does not enclose {@code (3..6)} (even though it contains every value
     * contained by the latter range)
     * <li>{@code [3..6]} does not enclose {@code (1..1]} (even though it contains every value
     * contained by the latter range)
     * </ul>
     *
     * <p>Note that if {@code a.encloses(b)}, then {@code b.contains(v)} implies {@code
     * a.contains(v)}, but as the last two examples illustrate, the converse is not always true.
     *
     * <p>Being reflexive, antisymmetric and transitive, the {@code encloses} relation defines a
     * <i>partial order</i> over ranges. There exists a unique {@linkplain Range#all maximal} range
     * according to this relation, and also numerous {@linkplain #isEmpty minimal} ranges. Enclosure
     * also implies {@linkplain #isConnected connectedness}.
     */
    public boolean encloses(Range<C> other) {
        return lowerBound.compareTo(other.lowerBound) <= 0
                && upperBound.compareTo(other.upperBound) >= 0;
    }

    /**
     * Returns {@code true} if there exists a (possibly empty) range which is {@linkplain #encloses
     * enclosed} by both this range and {@code other}.
     *
     * <p>For example,
     *
     * <ul>
     * <li>{@code [2, 4)} and {@code [5, 7)} are not connected
     * <li>{@code [2, 4)} and {@code [3, 5)} are connected, because both enclose {@code [3, 4)}
     * <li>{@code [2, 4)} and {@code [4, 6)} are connected, because both enclose the empty range
     * {@code [4, 4)}
     * </ul>
     *
     * <p>Note that this range and {@code other} have a well-defined {@linkplain #span union} and
     * {@linkplain #intersection intersection} (as a single, possibly-empty range) if and only if this
     * method returns {@code true}.
     *
     * <p>The connectedness relation is both reflexive and symmetric, but does not form an
     * Equivalence equivalence relation as it is not transitive.
     *
     * <p>Note that certain discrete ranges are not considered connected, even though there are no
     * elements "between them." For example, {@code [3, 5]} is not considered connected to {@code [6,
     * 10]}. In these cases, it may be desirable for both input ranges to be preprocessed with {@link
     * #canonical(DiscreteDomain)} before testing for connectedness.
     */
    public boolean isConnected(Range<C> other) {
        return lowerBound.compareTo(other.upperBound) <= 0
                && other.lowerBound.compareTo(upperBound) <= 0;
    }

    /**
     * Returns the maximal range {@linkplain #encloses enclosed} by both this range and {@code
     * connectedRange}, if such a range exists.
     *
     * <p>For example, the intersection of {@code [1..5]} and {@code (3..7)} is {@code (3..5]}. The
     * resulting range may be empty; for example, {@code [1..5)} intersected with {@code [5..7)}
     * yields the empty range {@code [5..5)}.
     *
     * <p>The intersection exists if and only if the two ranges are {@linkplain #isConnected
     * connected}.
     *
     * <p>The intersection operation is commutative, associative and idempotent, and its identity
     * element is {@link Range#all}).
     *
     * @throws IllegalArgumentException if {@code isConnected(connectedRange)} is {@code false}
     */
    public Range<C> intersection(Range<C> connectedRange) {
        int lowerCmp = lowerBound.compareTo(connectedRange.lowerBound);
        int upperCmp = upperBound.compareTo(connectedRange.upperBound);
        if (lowerCmp >= 0 && upperCmp <= 0) {
            return this;
        }
        else if (lowerCmp <= 0 && upperCmp >= 0) {
            return connectedRange;
        }
        else {
            Cut<C> newLower = (lowerCmp >= 0) ? lowerBound : connectedRange.lowerBound;
            Cut<C> newUpper = (upperCmp <= 0) ? upperBound : connectedRange.upperBound;
            return create(newLower, newUpper);
        }
    }

    /**
     * Returns the minimal range that {@linkplain #encloses encloses} both this range and {@code
     * other}. For example, the span of {@code [1..3]} and {@code (5..7)} is {@code [1..7)}.
     *
     * <p><i>If</i> the input ranges are {@linkplain #isConnected connected}, the returned range can
     * also be called their <i>union</i>. If they are not, note that the span might contain values
     * that are not contained in either input range.
     *
     * <p>Like {@link #intersection(Range) intersection}, this operation is commutative, associative
     * and idempotent. Unlike it, it is always well-defined for any two input ranges.
     */
    public Range<C> span(Range<C> other) {
        int lowerCmp = lowerBound.compareTo(other.lowerBound);
        int upperCmp = upperBound.compareTo(other.upperBound);
        if (lowerCmp <= 0 && upperCmp >= 0) {
            return this;
        }
        else if (lowerCmp >= 0 && upperCmp <= 0) {
            return other;
        }
        else {
            Cut<C> newLower = (lowerCmp <= 0) ? lowerBound : other.lowerBound;
            Cut<C> newUpper = (upperCmp >= 0) ? upperBound : other.upperBound;
            return create(newLower, newUpper);
        }
    }

    /**
     * Returns the canonical form of this range in the given domain. The canonical form has the
     * following properties:
     *
     * <ul>
     * <li>equivalence: {@code a.canonical().contains(v) == a.contains(v)} for all {@code v} (in
     * other words, {@code ContiguousSet.create(a.canonical(domain), domain).equals(
     * ContiguousSet.create(a, domain))}
     * <li>uniqueness: unless {@code a.isEmpty()}, {@code ContiguousSet.create(a,
     * domain).equals(ContiguousSet.create(b, domain))} implies {@code
     * a.canonical(domain).equals(b.canonical(domain))}
     * <li>idempotence: {@code a.canonical(domain).canonical(domain).equals(a.canonical(domain))}
     * </ul>
     *
     * <p>Furthermore, this method guarantees that the range returned will be one of the following
     * canonical forms:
     *
     * <ul>
     * <li>[start..end)
     * <li>[start..+∞)
     * <li>(-∞..end) (only if type {@code C} is unbounded below)
     * <li>(-∞..+∞) (only if type {@code C} is unbounded below)
     * </ul>
     */
    public Range<C> canonical(DiscreteDomain<C> domain) {
        checkNotNull(domain);
        Cut<C> lower = lowerBound.canonical(domain);
        Cut<C> upper = upperBound.canonical(domain);
        return (lower == lowerBound && upper == upperBound) ? this : create(lower, upper);
    }

    /**
     * Returns {@code true} if {@code object} is a range having the same endpoints and bound types as
     * this range. Note that discrete ranges such as {@code (1..4)} and {@code [2..3]} are <b>not</b>
     * equal to one another, despite the fact that they each contain precisely the same set of values.
     * Similarly, empty ranges are not equal unless they have exactly the same representation, so
     * {@code [3..3)}, {@code (3..3]}, {@code (4..4]} are all unequal.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Range) {
            Range<?> other = (Range<?>) object;
            return lowerBound.equals(other.lowerBound) && upperBound.equals(other.upperBound);
        }
        return false;
    }

    /**
     * Returns a hash code for this range.
     */
    @Override
    public int hashCode() {
        return lowerBound.hashCode() * 31 + upperBound.hashCode();
    }

    /**
     * Returns a string representation of this range, such as {@code "[3..5)"} (other examples are
     * listed in the class documentation).
     */
    @Override
    public String toString() {
        return toString(lowerBound, upperBound);
    }

    private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
        StringBuilder sb = new StringBuilder(16);
        lowerBound.describeAsLowerBound(sb);
        sb.append("..");
        upperBound.describeAsUpperBound(sb);
        return sb.toString();
    }

    /**
     * Used to avoid http://bugs.sun.com/view_bug.do?bug_id=6558557
     */
    private static <T> SortedSet<T> cast(Iterable<T> iterable) {
        return (SortedSet<T>) iterable;
    }

    private Object readResolve() {
        if (this.equals(ALL)) {
            return all();
        }
        else {
            return this;
        }
    }

    @SuppressWarnings("unchecked") // this method may throw CCE
    static int compareOrThrow(Comparable left, Comparable right) {
        return left.compareTo(right);
    }

    /**
     * Implementation detail for the internal structure of {@link Range} instances. Represents a unique
     * way of "cutting" a "number line" (actually of instances of type {@code C}, not necessarily
     * "numbers") into two sections; this can be done below a certain value, above a certain value,
     * below all values or above all values. With this object defined in this way, an interval can
     * always be represented by a pair of {@code Cut} instances.
     *
     * @author Kevin Bourrillion
     */
    private static abstract class Cut<C extends Comparable> implements Comparable<Cut<C>>, Serializable {
        final C endpoint;

        private Cut(C endpoint) {
            this.endpoint = endpoint;
        }

        abstract boolean isLessThan(C value);

        abstract BoundType typeAsLowerBound();

        abstract BoundType typeAsUpperBound();

        abstract void describeAsLowerBound(StringBuilder sb);

        abstract void describeAsUpperBound(StringBuilder sb);

        /*
         * The canonical form is a BelowValue cut whenever possible, otherwise ABOVE_ALL, or
         * (only in the case of types that are unbounded below) BELOW_ALL.
         */
        Cut<C> canonical(DiscreteDomain<C> domain) {
            return this;
        }

        // note: overridden by {BELOW,ABOVE}_ALL
        @Override
        public int compareTo(Cut<C> that) {
            if (that == belowAll()) {
                return 1;
            }
            if (that == aboveAll()) {
                return -1;
            }
            int result = Range.compareOrThrow(endpoint, that.endpoint);
            if (result != 0) {
                return result;
            }

            // same value. below comes before above
            boolean a = this instanceof Cut.AboveValue;
            boolean b = that instanceof Cut.AboveValue;
            return (a == b) ? 0 : (a ? 1 : -1);
        }

        C endpoint() {
            return endpoint;
        }

        @SuppressWarnings("unchecked") // catching CCE
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Cut) {
                // It might not really be a Cut<C>, but we'll catch a CCE if it's not
                Cut<C> that = (Cut<C>) obj;
                try {
                    int compareResult = compareTo(that);
                    return compareResult == 0;
                }
                catch (ClassCastException ignored) {
                }
            }
            return false;
        }

        // Prevent "missing hashCode" warning by explicitly forcing subclasses implement it
        @Override
        public abstract int hashCode();

        /*
         * The implementation neither produces nor consumes any non-null instance of type C, so
         * casting the type parameter is safe.
         */
        @SuppressWarnings("unchecked")
        static <C extends Comparable> Cut<C> belowAll() {
            return (Cut<C>) BelowAll.INSTANCE;
        }

        private static final long serialVersionUID = 0;

        private static final class BelowAll extends Cut<Comparable<?>> {
            private static final BelowAll INSTANCE = new BelowAll();

            private BelowAll() {
                super(null);
            }

            @Override
            Comparable<?> endpoint() {
                throw new IllegalStateException("range unbounded on this side");
            }

            @Override
            boolean isLessThan(Comparable<?> value) {
                return true;
            }

            @Override
            BoundType typeAsLowerBound() {
                throw new IllegalStateException();
            }

            @Override
            BoundType typeAsUpperBound() {
                throw new AssertionError("this statement should be unreachable");
            }

            @Override
            void describeAsLowerBound(StringBuilder sb) {
                sb.append("(-\u221e");
            }

            @Override
            void describeAsUpperBound(StringBuilder sb) {
                throw new AssertionError();
            }

            @Override
            Cut<Comparable<?>> canonical(DiscreteDomain<Comparable<?>> domain) {
                try {
                    return Cut.<Comparable<?>>belowValue(domain.minValue());
                }
                catch (NoSuchElementException e) {
                    return this;
                }
            }

            @Override
            public int compareTo(Cut<Comparable<?>> o) {
                return (o == this) ? 0 : -1;
            }

            @Override
            public int hashCode() {
                return System.identityHashCode(this);
            }

            @Override
            public String toString() {
                return "-\u221e";
            }

            private Object readResolve() {
                return INSTANCE;
            }

            private static final long serialVersionUID = 0;
        }

        /*
         * The implementation neither produces nor consumes any non-null instance of
         * type C, so casting the type parameter is safe.
         */
        @SuppressWarnings("unchecked")
        static <C extends Comparable> Cut<C> aboveAll() {
            return (Cut<C>) AboveAll.INSTANCE;
        }

        private static final class AboveAll extends Cut<Comparable<?>> {
            private static final AboveAll INSTANCE = new AboveAll();

            private AboveAll() {
                super(null);
            }

            @Override
            Comparable<?> endpoint() {
                throw new IllegalStateException("range unbounded on this side");
            }

            @Override
            boolean isLessThan(Comparable<?> value) {
                return false;
            }

            @Override
            BoundType typeAsLowerBound() {
                throw new AssertionError("this statement should be unreachable");
            }

            @Override
            BoundType typeAsUpperBound() {
                throw new IllegalStateException();
            }

            @Override
            void describeAsLowerBound(StringBuilder sb) {
                throw new AssertionError();
            }

            @Override
            void describeAsUpperBound(StringBuilder sb) {
                sb.append("+\u221e)");
            }

            @Override
            public int compareTo(Cut<Comparable<?>> o) {
                return (o == this) ? 0 : 1;
            }

            @Override
            public int hashCode() {
                return System.identityHashCode(this);
            }

            @Override
            public String toString() {
                return "+\u221e";
            }

            private Object readResolve() {
                return INSTANCE;
            }

            private static final long serialVersionUID = 0;
        }

        static <C extends Comparable> Cut<C> belowValue(C endpoint) {
            return new BelowValue<C>(endpoint);
        }

        private static final class BelowValue<C extends Comparable> extends Cut<C> {
            BelowValue(C endpoint) {
                super(checkNotNull(endpoint));
            }

            @Override
            boolean isLessThan(C value) {
                return Range.compareOrThrow(endpoint, value) <= 0;
            }

            @Override
            BoundType typeAsLowerBound() {
                return BoundType.CLOSED;
            }

            @Override
            BoundType typeAsUpperBound() {
                return BoundType.OPEN;
            }

            @Override
            void describeAsLowerBound(StringBuilder sb) {
                sb.append('[').append(endpoint);
            }

            @Override
            void describeAsUpperBound(StringBuilder sb) {
                sb.append(endpoint).append(')');
            }

            @Override
            public int hashCode() {
                return endpoint.hashCode();
            }

            @Override
            public String toString() {
                return "\\" + endpoint + "/";
            }

            private static final long serialVersionUID = 0;
        }

        static <C extends Comparable> Cut<C> aboveValue(C endpoint) {
            return new AboveValue<C>(endpoint);
        }

        private static final class AboveValue<C extends Comparable> extends Cut<C> {
            AboveValue(C endpoint) {
                super(checkNotNull(endpoint));
            }

            @Override
            boolean isLessThan(C value) {
                return Range.compareOrThrow(endpoint, value) < 0;
            }

            @Override
            BoundType typeAsLowerBound() {
                return BoundType.OPEN;
            }

            @Override
            BoundType typeAsUpperBound() {
                return BoundType.CLOSED;
            }

            @Override
            void describeAsLowerBound(StringBuilder sb) {
                sb.append('(').append(endpoint);
            }

            @Override
            void describeAsUpperBound(StringBuilder sb) {
                sb.append(endpoint).append(']');
            }

            C leastValueAbove(DiscreteDomain<C> domain) {
                return domain.next(endpoint);
            }

            @Override
            Cut<C> canonical(DiscreteDomain<C> domain) {
                C next = leastValueAbove(domain);
                return (next != null) ? belowValue(next) : Cut.<C>aboveAll();
            }

            @Override
            public int hashCode() {
                return ~endpoint.hashCode();
            }

            @Override
            public String toString() {
                return "/" + endpoint + "\\";
            }

            private static final long serialVersionUID = 0;
        }

    }

    /**
     * A descriptor for a <i>discrete</i> {@code Comparable} domain such as all {@link Integer}
     * instances. A discrete domain is one that supports the three basic operations: {@link #next},
     * {@link #previous} and #distance, according to their specifications. The methods {@link
     * #minValue} and {@link #maxValue} should also be overridden for bounded types.
     *
     * <p>A discrete domain always represents the <i>entire</i> set of values of its type; it cannot
     * represent partial domains such as "prime integers" or "strings of length 5."
     *
     * <p>See the Guava User Guide section on <a href=
     * "https://github.com/google/guava/wiki/RangesExplained#discrete-domains"> {@code
     * DiscreteDomain}</a>.
     *
     * @author Kevin Bourrillion
     * @since 10.0
     */
    private static abstract class DiscreteDomain<C extends Comparable> {

        final boolean supportsFastOffset;

        /**
         * Private constructor for built-in DiscreteDomains supporting fast offset.
         */
        private DiscreteDomain(boolean supportsFastOffset) {
            this.supportsFastOffset = supportsFastOffset;
        }

        /**
         * Returns the unique least value of type {@code C} that is greater than {@code value}, or {@code
         * null} if none exists. Inverse operation to {@link #previous}.
         *
         * @param value any value of type {@code C}
         * @return the least value greater than {@code value}, or {@code null} if {@code value} is {@code
         * maxValue()}
         */
        public abstract C next(C value);

        /**
         * Returns the unique greatest value of type {@code C} that is less than {@code value}, or {@code
         * null} if none exists. Inverse operation to {@link #next}.
         *
         * @param value any value of type {@code C}
         * @return the greatest value less than {@code value}, or {@code null} if {@code value} is {@code
         * minValue()}
         */
        public abstract C previous(C value);

        /**
         * Returns the minimum value of type {@code C}, if it has one. The minimum value is the unique
         * value for which {@link Comparable#compareTo(Object)} never returns a positive value for any
         * input of type {@code C}.
         *
         * <p>The default implementation throws {@code NoSuchElementException}.
         *
         * @return the minimum value of type {@code C}; never null
         * @throws NoSuchElementException if the type has no (practical) minimum value; for example,
         *                                {@link java.math.BigInteger}
         */
        public C minValue() {
            throw new NoSuchElementException();
        }

        /**
         * Returns the maximum value of type {@code C}, if it has one. The maximum value is the unique
         * value for which {@link Comparable#compareTo(Object)} never returns a negative value for any
         * input of type {@code C}.
         *
         * <p>The default implementation throws {@code NoSuchElementException}.
         *
         * @return the maximum value of type {@code C}; never null
         * @throws NoSuchElementException if the type has no (practical) maximum value; for example,
         *                                {@link java.math.BigInteger}
         */
        public C maxValue() {
            throw new NoSuchElementException();
        }
    }

    /**
     * Indicates whether an endpoint of some range is contained in the range itself ("closed") or not
     * ("open"). If a range is unbounded on a side, it is neither open nor closed on that side; the
     * bound simply does not exist.
     *
     * @since 10.0
     */
    public static enum BoundType {
        /**
         * The endpoint value <i>is not</i> considered part of the set ("exclusive").
         */
        OPEN(false),
        CLOSED(true);

        final boolean inclusive;

        BoundType(boolean inclusive) {
            this.inclusive = inclusive;
        }

        /**
         * Returns the bound type corresponding to a boolean value for inclusivity.
         */
        static BoundType forBoolean(boolean inclusive) {
            return inclusive ? CLOSED : OPEN;
        }

        BoundType flip() {
            return forBoolean(!inclusive);
        }
    }

    private static final long serialVersionUID = 0;

}
