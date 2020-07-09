package com.autodeploy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "linux_host")
public class LinuxHost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;


}
