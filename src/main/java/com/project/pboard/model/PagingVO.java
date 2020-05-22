package com.project.pboard.model;


import lombok.Data;


@Data
public class PagingVO {
    private int totalPages;
    private int number;
    private int FP;

    public  PagingVO(){
        this.FP = 1;
    }
    
    public void setPaging(int totalPages, int number){
        this.totalPages = totalPages;
        this.number = number;
    }

    
}