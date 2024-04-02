package org.example.model.Statistics;

public class Statistics {

    private int numberOfItems;
    private int totalValue;
    private int uniqueCustomers;
    private int period;

    public Statistics(int numberOfItems, int totalValue, int uniqueCustomers, int period) {
        this.numberOfItems = numberOfItems;
        this.totalValue = totalValue;
        this.uniqueCustomers = uniqueCustomers;
        this.period = period;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public int getUniqueCustomers() {
        return uniqueCustomers;
    }

    public void setUniqueCustomers(int uniqueCustomers) {
        this.uniqueCustomers = uniqueCustomers;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "The selected cashier sold " +
                 numberOfItems + " items, " +
                "with a total value of " + totalValue + "," +
                "produced by at least " + uniqueCustomers + " unique customers " +
                "in the month number " + period;
    }
}
