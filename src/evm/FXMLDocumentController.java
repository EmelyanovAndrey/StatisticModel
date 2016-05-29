/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evm;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author Игорь
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private LineChart<Number, Number> coordX;
    @FXML
    private LineChart<Number, Number> coordY;
    @FXML
    private LineChart<Number, Number> coordZ;

    @FXML
    private BarChart<Number, Number> coordXGist;
    @FXML
    private BarChart<Number, Number> coordYGist;
    @FXML
    private BarChart<Number, Number> coordZGist;

    @FXML
    private Button startButton;

    public static final int experimentCount = 1000;
    private final double dt = 0.01;
    private final int stepCount = 1000;

    private ArrayList<Double> K1result = new ArrayList<>(experimentCount);
    private ArrayList<Double> K2result = new ArrayList<>(experimentCount);
    private ArrayList<Double> K3result = new ArrayList<>(experimentCount);

    private ArrayList<Double> a1 = new ArrayList<>(experimentCount);
    private ArrayList<Double> a2 = new ArrayList<>(experimentCount);
    private ArrayList<Double> a3 = new ArrayList<>(experimentCount);
    private ArrayList<Double> b1 = new ArrayList<>(experimentCount);
    private ArrayList<Double> b2 = new ArrayList<>(experimentCount);
    private ArrayList<Double> b3 = new ArrayList<>(experimentCount);

    private ObservableList<XYChart.Data> K1datas = null;
    private ObservableList<XYChart.Data> K2datas = null;
    private ObservableList<XYChart.Data> K3datas = null;

    private XYChart.Series K1series = null;
    private XYChart.Series K2series = null;
    private XYChart.Series K3series = null;

    @FXML
    private Label LineK1Label;
    @FXML
    private Label LineFK1Label;
    @FXML
    private Label LineK2Label;
    @FXML
    private Label LineFK2Label;
    @FXML
    private Label LineFK3Label;
    @FXML
    private Label LineK3Label;
    @FXML
    private Label LineFLabel;
    @FXML
    private Label LineLabel;

    @FXML
    private Label NLineLabel;
    @FXML
    private Label NLineFK1Label;
    @FXML
    private Label NLineK1Label;
    @FXML
    private Label NLineFLabel;
    @FXML
    private Label NLineK3Label;
    @FXML
    private Label NLineFK3Label;
    @FXML
    private Label NLineFK2Label;
    @FXML
    private Label NLineK2Label;

    @FXML
    private Label MeanXLabel;
    @FXML
    private Label StdXLabel;
    @FXML
    private Label DispXLabel;
    @FXML
    private Label DispYLabel;
    @FXML
    private Label StdYLabel;
    @FXML
    private Label MeanYLabel;
    @FXML
    private Label DispZLabel;
    @FXML
    private Label StdZLabel;
    @FXML
    private Label MeanZLabel;
    @FXML
    private LineChart<Number, Number> ThXChart;
    @FXML
    private NumberAxis coordXaxisX;
    @FXML
    private NumberAxis coordXaxisT;
    @FXML
    private NumberAxis coordXaxisX1;
    @FXML
    private NumberAxis coordXaxisT1;
    @FXML
    private Label PirsonXLabel;
    @FXML
    private Label PirsonYLabel;
    @FXML
    private Label PirsonZLabel;
    @FXML
    private Label Coef1aLabel;
    @FXML
    private Label Coef1bLabel;
    @FXML
    private Label Coef2aLabel;
    @FXML
    private Label Coef2bLabel;
    @FXML
    private Label Coef3aLabel;
    @FXML
    private Label Coef3bLabel;




    @FXML
    private void handleStartButton() {
        java.util.Random random = new Random();
        int idev = stepCount / 20; // 20 точек на линии
        int jdev = experimentCount / 10;//10 линий
        boolean flag = true;
        for (int j = 0; j < experimentCount; j++) {

            a1.add(j, random.nextDouble());
            a2.add(j, random.nextDouble());
            a3.add(j, random.nextDouble());
            b1.add(j, random.nextDouble());
            b2.add(j, random.nextDouble());
            b3.add(j, random.nextDouble());
            double[] Y0 = {1, 2, 10,
                a1.get(j), b1.get(j),
                a2.get(j), b2.get(j),
                a3.get(j), b3.get(j)};
            Economic rk = new Economic(0, Y0);
            if (j % jdev == 0) {
                flag = true;
                K1datas = FXCollections.observableArrayList();
                K2datas = FXCollections.observableArrayList();
                K3datas = FXCollections.observableArrayList();
                K1series = new XYChart.Series();
                K2series = new XYChart.Series();
                K3series = new XYChart.Series();
            }
            System.out.println("\n\nЭксперимент: " + j + "\n");
            for (int i = 0; i < stepCount; i++) {
                if (flag && (i % idev == 0)) {
                    K1datas.add(new XYChart.Data(dt * i, rk.getY(0)));
                    K2datas.add(new XYChart.Data(dt * i, rk.getY(1)));
                    K3datas.add(new XYChart.Data(dt * i, rk.getY(2)));
                }
                rk.NextStep(dt);
            }
            if (flag) {
                K1series.setData(K1datas);
                K2series.setData(K2datas);
                K3series.setData(K3datas);

                coordX.getData().add(K1series);
                coordY.getData().add(K2series);
                coordZ.getData().add(K3series);
                flag = false;
            }
            K1result.add(j, rk.getY(0));
            K2result.add(j, rk.getY(1));
            K3result.add(j, rk.getY(2));
        }
        drawDiagrams();
        linearRegression();
        nonLinearRegression();
        sleep();
    }
    
    private double[] K1coef;
    private double[] K2coef;
    private double[] K3coef;
    
    private double[] F_K1;
    private double[] F_K2;
    private double[] F_K3;
    
    private void linearRegression(){
        double[][] body1 = new double[experimentCount][3];
        double[][] body2 = new double[experimentCount][3];
        double[][] body3 = new double[experimentCount][3];
        for (int i = 0; i < experimentCount; i++) {
            body1[i][0] = 1;
            body1[i][1] = a1.get(i);
            body1[i][2] = b1.get(i);
            body2[i][0] = 1;
            body2[i][1] = a2.get(i);
            body2[i][2] = b2.get(i);
            body3[i][0] = 1;
            body3[i][1] = a3.get(i);
            body3[i][2] = b3.get(i);
        }
        double[][] K1arr = new double[experimentCount][1];
        double[][] K2arr = new double[experimentCount][1];
        double[][] K3arr = new double[experimentCount][1];
        for (int i = 0; i < experimentCount; i++) {
            K1arr[i][0] = K1result.get(i);
            K2arr[i][0] = K2result.get(i);
            K3arr[i][0] = K3result.get(i);
        }
        K1coef = coef(body1, K1arr);
        K2coef = coef(body2, K2arr);
        K3coef = coef(body3, K3arr);
        //------------------------------------------//
        F_K1 = new double[experimentCount];
        F_K2 = new double[experimentCount];
        F_K3 = new double[experimentCount];
        for (int i = 0; i < experimentCount; i++) {
            F_K1[i] = K1coef[0] + K1coef[1] * a1.get(i) + K1coef[2] * b1.get(i);
            F_K2[i] = K2coef[0] + K2coef[1] * a2.get(i) + K2coef[2] * b2.get(i);
            F_K3[i] = K3coef[0] + K3coef[1] * a3.get(i) + K3coef[2] * b3.get(i);
        }
        double S1 = 0, S2 = 0, S3 = 0;
        double avg1 = 0, avg2 = 0, avg3 = 0;
        for (int i = 0; i < experimentCount; i++) {
            S1 += Math.pow(F_K1[i] - K1result.get(i), 2);
            S2 += Math.pow(F_K2[i] - K2result.get(i), 2);
            S3 += Math.pow(F_K3[i] - K3result.get(i), 2);  
            avg1 += K1result.get(i);
            avg2 += K2result.get(i);
            avg3 += K3result.get(i);
        }
        S1 /= experimentCount - 3;
        S2 /= experimentCount - 3;
        S3 /= experimentCount - 3;
        avg1 /= experimentCount;
        avg2 /= experimentCount;
        avg3 /= experimentCount;
        double d1 = 0, d2 = 0, d3 =0;
        for (int i = 0; i < experimentCount; i++) {
            d1 += Math.pow(K1result.get(i) - avg1, 2);
            d2 += Math.pow(K2result.get(i) - avg2, 2);
            d3 += Math.pow(K3result.get(i) - avg3, 2);
        }
        d1 /= experimentCount - 1;
        d2 /= experimentCount - 1;
        d3 /= experimentCount - 1;
        double F1 = S1 / d1;
        double F2 = S2 / d2;
        double F3 = S3 / d3;
        FDistribution FDistr = new FDistribution(experimentCount - 3, experimentCount - 1);
        double F = FDistr.getNumericalMean();
        
        LineLabel.setVisible(true);
        LineK1Label.setVisible(true);
        LineK1Label.setText("K1 = " + K1coef[0] + " + a("+K1coef[1]+") + b("+K1coef[2]+")");
        LineFK1Label.setVisible(true);
        LineFK1Label.setText("FK1 = " + F1);
        LineK2Label.setVisible(true);
        LineK2Label.setText("K2 = " + K2coef[0] + " + a("+K2coef[1]+") + b("+K2coef[2]+")");
        LineFK2Label.setVisible(true);
        LineFK2Label.setText("FK2 = " + F2);
        LineK3Label.setVisible(true);
        LineK3Label.setText("K3 = " + K3coef[0] + " + a("+K3coef[1]+") + b("+K3coef[2]+")");
        LineFK3Label.setVisible(true);
        LineFK3Label.setText("FK3 = " + F3);
        LineFLabel.setVisible(true);
        LineFLabel.setText("F = " + F);
    }
    
    private void nonLinearRegression(){
        double[][] body1 = new double[experimentCount][6];
        double[][] body2 = new double[experimentCount][6];
        double[][] body3 = new double[experimentCount][6];
        for (int i = 0; i < experimentCount; i++) {
            body1[i][0] = 1;
            body1[i][1] = a1.get(i);
            body1[i][2] = b1.get(i);
            body1[i][3] = a1.get(i)*b1.get(i);
            body1[i][4] = a1.get(i)*a1.get(i);
            body1[i][5] = b1.get(i)*b1.get(i);
            body2[i][0] = 1;
            body2[i][1] = a2.get(i);
            body2[i][2] = b2.get(i);
            body2[i][3] = a2.get(i)*b2.get(i);
            body2[i][4] = a2.get(i)*a2.get(i);
            body2[i][5] = b2.get(i)*b2.get(i);
            body3[i][0] = 1;
            body3[i][1] = a3.get(i);
            body3[i][2] = b3.get(i);
            body3[i][3] = a3.get(i)*b3.get(i);
            body3[i][4] = a3.get(i)*a3.get(i);
            body3[i][5] = b3.get(i)*b3.get(i);
        }
        double[][] K1arr = new double[experimentCount][1];
        double[][] K2arr = new double[experimentCount][1];
        double[][] K3arr = new double[experimentCount][1];
        for (int i = 0; i < experimentCount; i++) {
            K1arr[i][0] = K1result.get(i);
            K2arr[i][0] = K2result.get(i);
            K3arr[i][0] = K3result.get(i);
        }
        double[] K1coef = coef(body1, K1arr);
        double[] K2coef = coef(body2, K2arr);
        double[] K3coef = coef(body3, K3arr);
        //------------------------------------------//
        double[] F_K1 = new double[experimentCount];
        double[] F_K2 = new double[experimentCount];
        double[] F_K3 = new double[experimentCount];
        for (int i = 0; i < experimentCount; i++) {
            F_K1[i] = K1coef[0] + K1coef[1] * a1.get(i) + K1coef[2] * b1.get(i) +
                    K1coef[3] * a1.get(i) * b1.get(i) +
                    K1coef[4] * a1.get(i) * a1.get(i) +
                    K1coef[5] * b1.get(i) * b1.get(i);
            F_K2[i] = K2coef[0] + K2coef[1] * a2.get(i) + K2coef[2] * b2.get(i) +
                    K2coef[3] * a2.get(i) * b2.get(i) +
                    K2coef[4] * a2.get(i) * a2.get(i) +
                    K2coef[5] * b2.get(i) * b2.get(i);
            F_K3[i] = K3coef[0] + K3coef[1] * a3.get(i) + K3coef[2] * b3.get(i) +
                    K3coef[3] * a3.get(i) * b3.get(i) +
                    K3coef[4] * a3.get(i) * a3.get(i) +
                    K3coef[5] * b3.get(i) * b3.get(i);
        }
        double S1 = 0, S2 = 0, S3 = 0;
        double avg1 = 0, avg2 = 0, avg3 = 0;
        for (int i = 0; i < experimentCount; i++) {
            S1 += Math.pow(F_K1[i] - K1result.get(i), 2);
            S2 += Math.pow(F_K2[i] - K2result.get(i), 2);
            S3 += Math.pow(F_K3[i] - K3result.get(i), 2);  
            avg1 += K1result.get(i);
            avg2 += K2result.get(i);
            avg3 += K3result.get(i);
        }
        S1 /= experimentCount - 3;
        S2 /= experimentCount - 3;
        S3 /= experimentCount - 3;
        avg1 /= experimentCount;
        avg2 /= experimentCount;
        avg3 /= experimentCount;
        double d1 = 0, d2 = 0, d3 =0;
        for (int i = 0; i < experimentCount; i++) {
            d1 += Math.pow(K1result.get(i) - avg1, 2);
            d2 += Math.pow(K2result.get(i) - avg2, 2);
            d3 += Math.pow(K3result.get(i) - avg3, 2);
        }
        d1 /= experimentCount - 1;
        d2 /= experimentCount - 1;
        d3 /= experimentCount - 1;
        double F1 = S1 / d1;
        double F2 = S2 / d2;
        double F3 = S3 / d3;
        FDistribution FDistr = new FDistribution(experimentCount - 3, experimentCount - 1);
        double F = FDistr.getNumericalMean();
        
        NLineLabel.setVisible(true);
        NLineK1Label.setVisible(true);
        NLineK1Label.setText("K1 = " + K1coef[0] + " + a("+K1coef[1]+") + b("+K1coef[2]+") + ab("+
                K1coef[3]+")+\n+ a^2("+K1coef[4]+") + b^2("+K1coef[5]+")");
        NLineFK1Label.setVisible(true);
        NLineFK1Label.setText("FK1 = " + F1);
        NLineK2Label.setVisible(true);
        NLineK2Label.setText("K2 = " + K2coef[0] + " + a("+K2coef[1]+") + b("+K2coef[2]+") + ab("+
                K2coef[3]+")+\n+ a^2("+K2coef[4]+") + b^2("+K2coef[5]+")");
        NLineFK2Label.setVisible(true);
        NLineFK2Label.setText("FK2 = " + F2);
        NLineK3Label.setVisible(true);
        NLineK3Label.setText("K3 = " + K3coef[0] + " + a("+K3coef[1]+") + b("+K3coef[2]+") + ab("+
                K3coef[3]+")+\n+ a^2("+K3coef[4]+") + b^2("+K3coef[5]+")");
        NLineFK3Label.setVisible(true);
        NLineFK3Label.setText("FK3 = " + F3);
        NLineFLabel.setVisible(true);
        NLineFLabel.setText("F = " + F);
    }
    
    private double[] coef(double[][] coef, double[][] result) {
        RealMatrix Y = new Array2DRowRealMatrix(result);
        RealMatrix X = new Array2DRowRealMatrix(coef);
        RealMatrix Xt = X.transpose();
        RealMatrix XtX = Xt.multiply(X);
        RealMatrix XtX1 = MatrixUtils.inverse(XtX);
        RealMatrix XtY = Xt.multiply(Y);
        RealMatrix B = XtX1.multiply(XtY);
        double[] out = new double[coef[0].length];
        for (int i = 0; i < B.getRowDimension(); i++) {
            out[i] = B.getEntry(i, 0);
        }
        return out;
    }

    
    private ArrayList<Double> dataGistX =new ArrayList();
    private ArrayList<Double> dataGistY =new ArrayList();
    private ArrayList<Double> dataGistZ =new ArrayList();
    
    private void drawDiagrams() {
        ArrayList<Double> arl = null;
        double min, max, step, cur, counter;
        int i,k=0;
        XYChart.Series series;
        for (int j = 1; j <= 3; j++) {
            k=0;
            switch (j) {
                case 1:
                    arl = K1result;                    
                    break;
                case 2:
                    arl = K2result;                 
                    break;
                case 3:
                    arl = K3result;
                    break;
            }
            Collections.sort(arl);
            min = arl.get(0);
            max = arl.get(arl.size() - 1);
            step = (max - min) / 20;///////////////// 20 столбцов в диаграмме
            cur = min;
            counter = 0;
            i = 0;
            while (((cur + step) < max)) {
                while ((arl.get(i) < cur + step)) {
                    counter++;
                    i++;
                }
                series = new XYChart.Series();
                switch (j) {
                    case 1:
                        dataGistX.add(k,counter);
                        k++;
                        coordXGist.getData().add(series);
                        series.setName(cur + " < X < " + (cur + step));
                        series.getData().add(new XYChart.Data("X", counter));
                        break;
                    case 2:
                        dataGistY.add(k,counter);
                        k++;
                        coordYGist.getData().add(series);
                        series.setName(cur + " < Y < " + (cur + step));
                        series.getData().add(new XYChart.Data("Y", counter));
                        break;
                    case 3:
                        dataGistZ.add(k,counter);
                        k++;
                        coordZGist.getData().add(series);
                        series.setName(cur + " < Z < " + (cur + step));
                        series.getData().add(new XYChart.Data("Z", counter));
                        break;
                }
                counter = 0;
                cur += step;
                
            }
        }
        paintStatisticData();
    }
    
    private void paintStatisticData()
    {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double el : K1result)
        {
            stats.addValue(el);
        }
        double mean = stats.getMean();
        double std = stats.getStandardDeviation();
        double disp = stats.getVariance();
        MeanXLabel.setVisible(true);
        MeanXLabel.setText("M = " + mean);
        DispXLabel.setVisible(true);
        DispXLabel.setText("D = " + disp);
        StdXLabel.setVisible(true);
        StdXLabel.setText("S = " + std);
        stats = new DescriptiveStatistics();
        for (double el : K2result)
        {
            stats.addValue(el);
        }
        mean = stats.getMean();
        std = stats.getStandardDeviation();
        disp = stats.getVariance();
        MeanYLabel.setVisible(true);
        MeanYLabel.setText("M = " + mean);
        DispYLabel.setVisible(true);
        DispYLabel.setText("D = " + disp);
        StdYLabel.setVisible(true);
        StdYLabel.setText("S = " + std);
        stats = new DescriptiveStatistics();
        for (double el : K3result)
        {
            stats.addValue(el);
        }
        mean = stats.getMean();
        std = stats.getStandardDeviation();
        disp = stats.getVariance();
        MeanZLabel.setVisible(true);
        MeanZLabel.setText("M = " + mean);
        DispZLabel.setVisible(true);
        DispZLabel.setText("D = " + disp);
        StdZLabel.setVisible(true);
        StdZLabel.setText("S = " + std);

        double dt2 = stepCount * dt / 1000;
        double y, x = 0;
        Series series = new XYChart.Series();
        for (int i = 0; i < 3000; i++)
        {
            x = dt2 * i;
            y = (FastMath.pow(1 / 2, 1 / 2) / FastMath.sqrt(FastMath.PI)) * FastMath.pow(x, -1 / 2) * FastMath.exp(-x / 2);
            series.getData().add(new XYChart.Data(x, y));
        }
        ThXChart.getData().add(series);

        dt2 = stepCount * dt / 20;
        y = 0;
        x = 0;
        double[] teoretical = new double[20];
        double[] shortteoretical = new double[19];
        for (int i = 0; i < dataGistX.size() - 1; i++)
        {
            x = dt2 * i;
            y = (FastMath.pow(1 / 2, 1 / 2) / FastMath.sqrt(FastMath.PI)) * FastMath.pow(x, -1 / 2) * FastMath.exp(-x / 2);
            teoretical[i] = y;
            shortteoretical[i] = y;
        }
        x = dt2 * dataGistX.size() - 1;
        y = (FastMath.pow(1 / 2, 1 / 2) / FastMath.sqrt(FastMath.PI)) * FastMath.pow(x, -1 / 2) * FastMath.exp(-x / 2);
        teoretical[dataGistX.size() - 1] = y;

        double[] k = new double[dataGistX.size()];
        ArrayList<Double> al = dataGistX;
        Collections.sort(al);
        //System.out.println("X "+" k.length = "+k.length+" al.size = "+al.size()+" teoretical "+teoretical.length);
        for (int i = 0; i < k.length; i++)
        {
            k[i] = al.get(i);
        }
        PirsonXLabel.setVisible(true);
        if (k.length != teoretical.length)
        {
            PirsonXLabel.setText("R = " + new PearsonsCorrelation().correlation(shortteoretical, k));
        }
        else
            PirsonXLabel.setText("R = " + new PearsonsCorrelation().correlation(teoretical, k));

        k = new double[dataGistY.size()];
        al = dataGistY;
        Collections.sort(al);
        //System.out.println("Y "+" k.length = "+k.length+" al.size = "+al.size()+" teoretical "+teoretical.length);
        for (int i = 0; i < k.length; i++)
        {
            k[i] = al.get(i);
        }
        PirsonYLabel.setVisible(true);
        if (k.length != teoretical.length)
        {
            PirsonYLabel.setText("R = " + new PearsonsCorrelation().correlation(shortteoretical, k));
        }
        else
            PirsonYLabel.setText("R = " + new PearsonsCorrelation().correlation(teoretical, k));

        k = new double[dataGistZ.size()];
        al = dataGistZ;
        Collections.sort(al);
        //System.out.println("Z "+" k.length = "+k.length+" al.size = "+al.size()+" teoretical "+teoretical.length);
        for (int i = 0; i < k.length; i++)
        {
            k[i] = al.get(i);
        }
        PirsonZLabel.setVisible(true);
        if (k.length != teoretical.length)
        {
            PirsonZLabel.setText("R = " + new PearsonsCorrelation().correlation(shortteoretical, k));
        }
        else
            PirsonZLabel.setText("R = " + new PearsonsCorrelation().correlation(teoretical, k));
    }
    
    private void sleep()
    {
        DescriptiveStatistics statsA = new DescriptiveStatistics();
        DescriptiveStatistics statsB = new DescriptiveStatistics();
        DescriptiveStatistics statsK = new DescriptiveStatistics();
        for (int i = 0; i < experimentCount; i++)
        {
            statsA.addValue(a1.get(i));
            statsB.addValue(b1.get(i));
            statsK.addValue(F_K1[i]);
        }
        double A1 = Math.pow(K1coef[1], 2) * statsA.getVariance() / statsK.getVariance();
        double B1 = Math.pow(K1coef[2], 2) * statsB.getVariance() / statsK.getVariance();
        System.out.println(A1 + ":" + B1);
        Coef1aLabel.setVisible(true);
        Coef1aLabel.setText("Влияние a = "+A1);
        Coef1bLabel.setVisible(true);
        Coef1bLabel.setText("Влияние b = "+B1);
        
        statsA = new DescriptiveStatistics();
        statsB = new DescriptiveStatistics();
        statsK = new DescriptiveStatistics();
        for (int i = 0; i < experimentCount; i++)
        {
            statsA.addValue(a2.get(i));
            statsB.addValue(b2.get(i));
            statsK.addValue(F_K2[i]);
        }
        A1 = Math.pow(K2coef[1], 2) * statsA.getVariance() / statsK.getVariance();
        B1 = Math.pow(K2coef[2], 2) * statsB.getVariance() / statsK.getVariance();
        System.out.println(A1 + ":" + B1);
        Coef2aLabel.setVisible(true);
        Coef2aLabel.setText("Влияние a = "+A1);
        Coef2bLabel.setVisible(true);
        Coef2bLabel.setText("Влияние b = "+B1);
        
        statsA = new DescriptiveStatistics();
        statsB = new DescriptiveStatistics();
        statsK = new DescriptiveStatistics();
        for (int i = 0; i < experimentCount; i++)
        {
            statsA.addValue(a3.get(i));
            statsB.addValue(b3.get(i));
            statsK.addValue(F_K3[i]);
        }
        A1 = Math.pow(K3coef[1], 2) * statsA.getVariance() / statsK.getVariance();
        B1 = Math.pow(K3coef[2], 2) * statsB.getVariance() / statsK.getVariance();
        System.out.println(A1 + ":" + B1);
        Coef3aLabel.setVisible(true);
        Coef3aLabel.setText("Влияние a = "+A1);
        Coef3bLabel.setVisible(true);
        Coef3bLabel.setText("Влияние b = "+B1);
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    class Economic extends RungeKutta {

        public Economic(double t0, double[] Y0) {
            super(t0, Y0);
        }

        ///K'=A*a0*K^a1*L^a2-BK
        @Override
        public double[] F(double t, double[] Y) {
            FY[0] = Y[3] * 5 * Math.pow(Y[0], 0.4) - Y[6] * Y[0];
            FY[1] = Y[4] * 5 * Math.pow(Y[1], 0.4) - Y[7] * Y[1];
            FY[2] = Y[5] * 5 * Math.pow(Y[2], 0.4) - Y[8] * Y[2];
            return FY;
        }

    }

}

