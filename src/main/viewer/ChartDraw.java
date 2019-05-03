package main.viewer;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;


public class ChartDraw implements ExampleChart<CategoryChart> {
    private int[] array;
    private String name;
    private int[] X = new int[256];

    public void setName(String name) {
        this.name = name;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    @Override
    public CategoryChart getChart() {

        for (int i=0;i<256;i++){
            X[i] = i;
        }
        CategoryChart chart = new CategoryChartBuilder().width(1200).height(600).
                xAxisTitle("Value").yAxisTitle("Count").build();
        chart.getStyler().setOverlapped(false);
        int[]  arrayData;
        arrayData = array;
        chart.addSeries(name,X,arrayData);
        return chart;
    }
}

