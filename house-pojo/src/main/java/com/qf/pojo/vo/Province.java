package com.qf.pojo.vo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "province")
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String province;
}
