package com.leledevelopers.smartirrigation.models;

import java.io.Serializable;

public class ConfigurationFeildFertigationModel implements Serializable {
    private int fieldNo;
    private int wetPeriod;
    private int injectPeriod;
    private int noIterations;

    private int injector1OnPeriod;
    private int injector1OffPeriod;
    private int injector1Cycles;
    private int injector2OnPeriod;
    private int injector2OffPeriod;
    private int injector2Cycles;
    private int injector3OnPeriod;
    private int injector3OffPeriod;
    private int injector3Cycles;
    private int injector4OnPeriod;
    private int injector4OffPeriod;
    private int injector4Cycles;

    private boolean isEnabled = false;
    private boolean isModelEmpty = true;

    public int getFieldNo() {
        return fieldNo;
    }

    public void setFieldNo(int fieldNo) {
        this.fieldNo = fieldNo;
    }

    public int getWetPeriod() {
        return wetPeriod;
    }

    public void setWetPeriod(int wetPeriod) {
        this.wetPeriod = wetPeriod;
    }

    public int getInjectPeriod() {
        return injectPeriod;
    }

    public void setInjectPeriod(int injectPeriod) {
        this.injectPeriod = injectPeriod;
    }

    public int getNoIterations() {
        return noIterations;
    }

    public void setNoIterations(int noIterations) {
        this.noIterations = noIterations;
    }

    public int getInjector1OnPeriod() {
        return injector1OnPeriod;
    }

    public void setInjector1OnPeriod(int injector1OnPeriod) {
        this.injector1OnPeriod = injector1OnPeriod;
    }

    public int getInjector1OffPeriod() {
        return injector1OffPeriod;
    }

    public void setInjector1OffPeriod(int injector1OffPeriod) {
        this.injector1OffPeriod = injector1OffPeriod;
    }

    public int getInjector1Cycles() {
        return injector1Cycles;
    }

    public void setInjector1Cycles(int injector1Cycles) {
        this.injector1Cycles = injector1Cycles;
    }

    public int getInjector2OnPeriod() {
        return injector2OnPeriod;
    }

    public void setInjector2OnPeriod(int injector2OnPeriod) {
        this.injector2OnPeriod = injector2OnPeriod;
    }

    public int getInjector2OffPeriod() {
        return injector2OffPeriod;
    }

    public void setInjector2OffPeriod(int injector2OffPeriod) {
        this.injector2OffPeriod = injector2OffPeriod;
    }

    public int getInjector2Cycles() {
        return injector2Cycles;
    }

    public void setInjector2Cycles(int injector2Cycles) {
        this.injector2Cycles = injector2Cycles;
    }

    public int getInjector3OnPeriod() {
        return injector3OnPeriod;
    }

    public void setInjector3OnPeriod(int injector3OnPeriod) {
        this.injector3OnPeriod = injector3OnPeriod;
    }

    public int getInjector3OffPeriod() {
        return injector3OffPeriod;
    }

    public void setInjector3OffPeriod(int injector3OffPeriod) {
        this.injector3OffPeriod = injector3OffPeriod;
    }

    public int getInjector3Cycles() {
        return injector3Cycles;
    }

    public void setInjector3Cycles(int injector3Cycles) {
        this.injector3Cycles = injector3Cycles;
    }

    public int getInjector4OnPeriod() {
        return injector4OnPeriod;
    }

    public void setInjector4OnPeriod(int injector4OnPeriod) {
        this.injector4OnPeriod = injector4OnPeriod;
    }

    public int getInjector4OffPeriod() {
        return injector4OffPeriod;
    }

    public void setInjector4OffPeriod(int injector4OffPeriod) {
        this.injector4OffPeriod = injector4OffPeriod;
    }

    public int getInjector4Cycles() {
        return injector4Cycles;
    }

    public void setInjector4Cycles(int injector4Cycles) {
        this.injector4Cycles = injector4Cycles;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isModelEmpty() {
        return isModelEmpty;
    }

    public void setModelEmpty(boolean modelEmpty) {
        isModelEmpty = modelEmpty;
    }

    @Override
    public String toString() {
        return "ConfigurationFeildFertigationModel{" +
                "fieldNo=" + fieldNo +
                ", wetPeriod=" + wetPeriod +
                ", injectPeriod=" + injectPeriod +
                ", noIterations=" + noIterations +
                ", injector1OnPeriod=" + injector1OnPeriod +
                ", injector1OffPeriod=" + injector1OffPeriod +
                ", injector1Cycles=" + injector1Cycles +
                ", injector2OnPeriod=" + injector2OnPeriod +
                ", injector2OffPeriod=" + injector2OffPeriod +
                ", injector2Cycles=" + injector2Cycles +
                ", injector3OnPeriod=" + injector3OnPeriod +
                ", injector3OffPeriod=" + injector3OffPeriod +
                ", injector3Cycles=" + injector3Cycles +
                ", injector4OnPeriod=" + injector4OnPeriod +
                ", injector4OffPeriod=" + injector4OffPeriod +
                ", injector4Cycles=" + injector4Cycles +
                ", isEnabled=" + isEnabled +
                ", isModelEmpty=" + isModelEmpty +
                '}';
    }
}
