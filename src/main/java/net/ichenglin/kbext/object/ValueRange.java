package net.ichenglin.kbext.object;

public class ValueRange implements Comparable<ValueRange> {

    private int range_minimum;
    private int range_maximum;

    public ValueRange(int range_minimum, int range_maximum) {
        if (range_minimum > range_maximum) throw new IllegalArgumentException("Minimum greater than maximum number");
        this.range_minimum = range_minimum;
        this.range_maximum = range_maximum;
    }

    public void set_minimum(int range_minimum) {
        if (range_minimum > this.range_maximum) throw new IllegalArgumentException("Minimum greater than maximum number");
        this.range_minimum = range_minimum;
    }

    public void set_maximum(int range_maximum) {
        if (this.range_minimum > range_maximum) throw new IllegalArgumentException("Maximum less than minimum number");
        this.range_maximum = range_maximum;
    }

    public boolean check_within(int candidate) {
        return (this.range_minimum <= candidate) && (candidate <= this.range_maximum);
    }

    public ValueRange get_copy() {
        return new ValueRange(this.range_minimum, this.range_maximum);
    }

    public Integer[] get_array() {
        int       range_minimum = this.get_minimum();
        int       range_length  = this.get_length();
        Integer[] range_array   = new Integer[range_length];
        for (int number_index = 0; number_index < range_length; number_index++) {
            range_array[number_index] = range_minimum + number_index;
        }
        return range_array;
    }

    public double get_mean() {
        return ((this.range_minimum + this.range_maximum) / 2f);
    }

    public int get_length() {
        return (this.range_maximum - this.range_minimum + 1);
    }

    public int get_minimum() {
        return this.range_minimum;
    }

    public int get_maximum() {
        return this.range_maximum;
    }

    @Override
    public int compareTo(ValueRange object) {
        return (this.get_length() - object.get_length());
    }

    @Override
    public String toString() {
        return "ValueRange (" + this.get_minimum() + " ~ " + this.get_maximum() + ")";
    }
}
