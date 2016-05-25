/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evm;

import java.io.IOException;

/**
 *
 * @author Игорь
 */
public abstract class RungeKutta {

    public double t;
    public double[] Y;
    double[] YY, K1, K2, K3, K4;
    protected double[] FY;

    public RungeKutta(double t0, double[] Y0) {
        t = t0;
        Init((int) Y0.length);
        System.arraycopy(Y0, 0, Y, 0, Y.length);
    }

    protected void Init(int N) {
        Y = new double[N];
        YY = new double[N];
        K1 = new double[N];
        K2 = new double[N];
        K3 = new double[N];
        K4 = new double[N];
        FY = new double[N];
    }

    public void SetInit(double t0, double[] Y0) {
        t = t0;
        Init((int) Y0.length);
        System.arraycopy(Y0, 0, Y, 0, Y.length);
    }

    abstract public double[] F(double t, double[] Y);

    public void NextStep(double dt) {
        int i;

        if (dt < 0) {
            return;
        }
        K1 = F(t, Y);
        for (i = 0; i < Y.length; i++) {
            YY[i] = Y[i] + K1[i] * (dt / 2.0);
        }
        K2 = F(t + dt / 2.0, YY);
        for (i = 0; i < Y.length; i++) {
            YY[i] = Y[i] + K2[i] * (dt / 2.0);
        }
        K3 = F(t + dt / 2.0, YY);
        for (i = 0; i < Y.length; i++) {
            YY[i] = Y[i] + K3[i] * dt;
        }
        K4 = F(t + dt, YY);
        for (i = 0; i < Y.length; i++) {
            Y[i] = Y[i] + dt / 6.0 * (K1[i] + 2.0 * K2[i] + 2.0 * K3[i] + K4[i]);
        }
        t = t + dt;
    }

    public double getY(int index) {
        return Y[index];
    }
}
