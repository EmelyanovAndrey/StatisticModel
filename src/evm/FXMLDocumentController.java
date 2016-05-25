/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evm;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

/**
 *
 * @author Игорь
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private LineChart<Number, Number> coordX;
    @FXML
    private NumberAxis coordXaxisX;
    @FXML
    private NumberAxis coordXaxisT;
    @FXML
    private LineChart<Number, Number> coordY;
    @FXML
    private NumberAxis coordYaxisX;
    @FXML
    private NumberAxis coordYaxisT;
    @FXML
    private LineChart<Number, Number> coordZ;
    @FXML
    private NumberAxis coordZaxisX;
    @FXML
    private NumberAxis coordZaxisT;
    @FXML
    private LineChart<Number, Number> speedX;
    @FXML
    private NumberAxis speedXaxisX;
    @FXML
    private NumberAxis speedXaxisT;
    @FXML
    private LineChart<Number, Number> speedY;
    @FXML
    private NumberAxis speedYaxisX;
    @FXML
    private NumberAxis speedYaxisT;
    @FXML
    private LineChart<Number, Number> speedZ;
    @FXML
    private NumberAxis speedZaxisX;
    @FXML
    private NumberAxis speedZaxisT;

    @FXML
    private Button startButton;

    private final int experimentCount = 500;
    private final double dt = 0.01;
    private final int stepCount = 1000;

    private ArrayList<Double> coordXresult = new ArrayList<>(experimentCount);
    private ArrayList<Double> coordYresult = new ArrayList<>(experimentCount);
    private ArrayList<Double> coordZresult = new ArrayList<>(experimentCount);
    private ArrayList<Double> speedXresult = new ArrayList<>(experimentCount);
    private ArrayList<Double> speedYresult = new ArrayList<>(experimentCount);
    private ArrayList<Double> speedZresult = new ArrayList<>(experimentCount);

    private ObservableList<XYChart.Data> coordXdatas = null;
    private ObservableList<XYChart.Data> coordYdatas = null;
    private ObservableList<XYChart.Data> coordZdatas = null;
    private ObservableList<XYChart.Data> speedXdatas = null;
    private ObservableList<XYChart.Data> speedYdatas = null;
    private ObservableList<XYChart.Data> speedZdatas = null;
    
    private XYChart.Series coordXseries = null;
    private XYChart.Series coordYseries = null;
    private XYChart.Series coordZseries = null;
    private XYChart.Series speedXseries = null;
    private XYChart.Series speedYseries = null;
    private XYChart.Series speedZseries = null;
    
    
    //узнать почему линии не отрисовываются (одинаковые - нулевые)
    @FXML
    private void handleStartButton() {

        java.util.Random random = new Random();
        random.nextGaussian();
        
        int idev = stepCount/20; // 20 точек на линии
        int jdev = experimentCount/10;//10 линий
        boolean flag = true;
        for (int j = 0; j < experimentCount; j++) {
            double[] Y0 = {random.nextGaussian() * 2, random.nextGaussian() * 2, random.nextGaussian() * 2,
            random.nextGaussian() * 10 / experimentCount, random.nextGaussian() * 10 / experimentCount,
            random.nextGaussian() * 10 / experimentCount};
        SatteliteRK rk = new SatteliteRK(0, Y0);
            if (j%jdev==0)
            {
                flag=true;
                coordXdatas = FXCollections.observableArrayList();
                coordYdatas = FXCollections.observableArrayList();
                coordZdatas = FXCollections.observableArrayList();
                speedXdatas = FXCollections.observableArrayList();
                speedYdatas = FXCollections.observableArrayList();
                speedZdatas = FXCollections.observableArrayList();
                coordXseries = new XYChart.Series();
                coordYseries = new XYChart.Series();
                coordZseries = new XYChart.Series();
                speedXseries = new XYChart.Series();
                speedYseries = new XYChart.Series();
                speedZseries = new XYChart.Series();
            }
            System.out.println("\n\nЭксперимент: " + j + "\n");
            for (int i = 0; i < stepCount; i++) {
                if (flag && (i%idev == 0))
                {
                    coordXdatas.add(new XYChart.Data(dt * i, rk.getY(0)));
                    coordYdatas.add(new XYChart.Data(dt * i, rk.getY(1)));
                    coordZdatas.add(new XYChart.Data(dt * i, rk.getY(2)));
                    speedXdatas.add(new XYChart.Data(dt * i, rk.getY(3)));
                    speedYdatas.add(new XYChart.Data(dt * i, rk.getY(4)));
                    speedZdatas.add(new XYChart.Data(dt * i, rk.getY(5)));
                }
                System.out.println(rk.getY(0));
                rk.NextStep(dt);
            }
            if (flag)
            {
                coordXseries.setData(coordXdatas);
                coordYseries.setData(coordYdatas);
                coordZseries.setData(coordZdatas);
                speedXseries.setData(speedXdatas);
                speedYseries.setData(speedYdatas);
                speedZseries.setData(speedZdatas);

                coordX.getData().add(coordXseries);
                coordY.getData().add(coordYseries);
                coordZ.getData().add(coordZseries);
                speedX.getData().add(speedXseries);
                speedY.getData().add(speedYseries);
                speedZ.getData().add(speedZseries);
                flag = false;  
            } 
            coordXresult.add(j, rk.getY(0));
            coordYresult.add(j, rk.getY(1));
            coordZresult.add(j, rk.getY(2));
            speedXresult.add(j, rk.getY(3));
            speedYresult.add(j, rk.getY(4));
            speedZresult.add(j, rk.getY(5));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}

class SatteliteRK extends RungeKutta {

    public SatteliteRK(double t0, double[] Y0) {
        super(t0, Y0);
    }
    /// p=x'
    /// p=y'
    /// p=z'
    /// p'=-x/r^3     
    /// p'=-y/r^3
    /// p'=-z/r^3
    /// r=sqrt(x^2+y^2+z^2)

    @Override
    public double[] F(double t, double[] Y) {
        FY[0] = Y[3];
        FY[1] = Y[4];
        FY[2] = Y[5];
        FY[3] = -Y[0] / Math.sqrt(Y[0] * Y[0] + Y[1] * Y[1] + Y[2] * Y[2]);
        FY[4] = -Y[1] / Math.sqrt(Y[0] * Y[0] + Y[1] * Y[1] + Y[2] * Y[2]);
        FY[5] = -Y[2] / Math.sqrt(Y[0] * Y[0] + Y[1] * Y[1] + Y[2] * Y[2]);
        return FY;
    }

}
