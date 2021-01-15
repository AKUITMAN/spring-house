package com.qf.pojo.vo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "province")
public class Provincejpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    private String province;
}
