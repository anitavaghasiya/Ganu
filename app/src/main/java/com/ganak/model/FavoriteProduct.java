package com.ganak.model;

public class FavoriteProduct {

    private String shape_id, size_id, grade_id, location_id, name_shape, name_grade, name_size, name_location, length, weight, pieces, et_length, et_weight, et_pieces;

    public FavoriteProduct(String shape_id, String size_id, String grade_id, String location_id, String name_shape, String name_grade, String name_size, String name_location, String et_length, String et_weight, String et_pieces) {
        this.shape_id = shape_id;
        this.size_id = size_id;
        this.grade_id = grade_id;
        this.location_id = location_id;
        this.name_shape = name_shape;
        this.name_grade = name_grade;
        this.name_size = name_size;
        this.name_location = name_location;
        this.length = length;
        this.weight = weight;
        this.pieces = pieces;
        this.et_length = et_length;
        this.et_weight = et_weight;
        this.et_pieces = et_pieces;
    }
    /*public FavoriteProduct(String shape_id, String size_id, String grade_id, String location_id, String name_shape, String name_grade, String name_size, String name_location) {
        this.shape_id = shape_id;
        this.size_id = size_id;
        this.grade_id = grade_id;
        this.location_id = location_id;
        this.name_shape = name_shape;
        this.name_grade = name_grade;
        this.name_size = name_size;
        this.name_location = name_location;
    }*/

    public String getShape_id() {
        return shape_id;
    }

    public void setShape_id(String shape_id) {
        this.shape_id = shape_id;
    }

    public String getSize_id() {
        return size_id;
    }

    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(String grade_id) {
        this.grade_id = grade_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getName_shape() {
        return name_shape;
    }

    public void setName_shape(String name_shape) {
        this.name_shape = name_shape;
    }

    public String getName_grade() {
        return name_grade;
    }

    public void setName_grade(String name_grade) {
        this.name_grade = name_grade;
    }

    public String getName_size() {
        return name_size;
    }

    public void setName_size(String name_size) {
        this.name_size = name_size;
    }

    public String getName_location() {
        return name_location;
    }

    public void setName_location(String name_location) {
        this.name_location = name_location;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public String getEt_length() {
        return et_length;
    }

    public void setEt_length(String et_length) {
        this.et_length = et_length;
    }

    public String getEt_weight() {
        return et_weight;
    }

    public void setEt_weight(String et_weight) {
        this.et_weight = et_weight;
    }

    public String getEt_pieces() {
        return et_pieces;
    }

    public void setEt_pieces(String et_pieces) {
        this.et_pieces = et_pieces;
    }
}
