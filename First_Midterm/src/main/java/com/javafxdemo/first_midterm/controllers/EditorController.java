package com.javafxdemo.first_midterm.controllers;

import com.javafxdemo.first_midterm.utils.*;

public class EditorController {
    ImageTransformer imageTransformer;
    MainController father;

    public void setImageTransformer(ImageTransformer imgTransformer){
        this.imageTransformer = imgTransformer;
    }

    public void setMainController(MainController father){
        this.father = father;
    }
}
