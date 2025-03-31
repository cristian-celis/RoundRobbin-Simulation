module uptc.so.rr.procesosroundrobin {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.context;
    requires spring.beans;

    opens uptc.so.rr.procesosroundrobin to javafx.fxml;
    opens uptc.so.rr.procesosroundrobin.configs to spring.beans;
    opens uptc.so.rr.procesosroundrobin.controllers to javafx.fxml;
    opens uptc.so.rr.procesosroundrobin.view to javafx.fxml;
    
    exports uptc.so.rr.procesosroundrobin;
    exports uptc.so.rr.procesosroundrobin.controllers;
    exports uptc.so.rr.procesosroundrobin.domain;
    exports uptc.so.rr.procesosroundrobin.domain.model;
    exports uptc.so.rr.procesosroundrobin.view;
    exports uptc.so.rr.procesosroundrobin.configs;
}